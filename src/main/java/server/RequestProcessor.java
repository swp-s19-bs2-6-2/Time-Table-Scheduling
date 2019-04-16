package server;

import algorithms.TimeTable;
import com.fasterxml.jackson.annotation.JsonAlias;
import io.JSONWrapper;
import io.Parser;
import org.apache.tomcat.util.http.fileupload.FileUtils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.locks.ReentrantLock;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class RequestProcessor {

//    USE LinkedBlockingQueue to wait until anything to process
//    Also the nice idea is to put to the hashmap of conditional variables when adding to the queue to notify response
//    method that everything is ready

    private static LinkedBlockingQueue <Long> queue = new LinkedBlockingQueue <Long>();
    private static ConcurrentHashMap <Long, File> IDtoFile = new ConcurrentHashMap<>();
    private static ConcurrentHashMap <Long, CountDownLatch> IDtoLock = new ConcurrentHashMap<>();
    private static ConcurrentHashMap <Long, String> IDtoResult= new ConcurrentHashMap<>();
    private static Path tempDirectory = null;
    private static AlgorithmExecutionThread algorithmThread = null;

    private static void generatingLoop() throws InterruptedException, FileNotFoundException, IOException, CloneNotSupportedException {
        while (true) {
            Long currentID = RequestProcessor.queue.take();
            File tempZipFile = RequestProcessor.IDtoFile.get(currentID);

            // Unzipping archive
            List <File> csvFiles = unzipArchive(tempZipFile);
            // Parsing files
            try {
                Parser parser = new Parser();
                Parser.TableResult tableResult = parser.parseAll(tempDirectory.toFile());
                TimeTable generatedTimeTable = new TimeTable(tableResult.getTimeSlots(), 5, tableResult.getLessons());
                // TODO : convert to json
                String timeTablejson = "It works";
                timeTablejson = new JSONWrapper().wrap(generatedTimeTable);
                IDtoResult.put(currentID, timeTablejson);
            } catch (Parser.IncorrectFileStructureException ex){
                ex.printStackTrace();
            }
            // Cleaning temporary folder
            FileUtils.cleanDirectory(tempDirectory.toFile());   // TODO : The package is strange, not sure if it is good to use it
            // Notifying waiting processes about results
            notifyResultReady(currentID);
        }
    }

    public static void startAlgorithm(){
        algorithmThread = new AlgorithmExecutionThread();
        algorithmThread.start();
    }

    private static List<File> unzipArchive(File archive) throws FileNotFoundException, IOException{
        List <File> result = new ArrayList<>();
        if (tempDirectory == null) {
            tempDirectory = Files.createTempDirectory("temp");
        }
        ZipInputStream zipInput = new ZipInputStream(new FileInputStream(archive));
        ZipEntry zipEntry = zipInput.getNextEntry();
        byte[] buffer = new byte[1024];
        while (zipEntry != null){
            File newFile = new File(tempDirectory.toFile(), zipEntry.getName());
            result.add(newFile);
            FileOutputStream fos = new FileOutputStream(newFile);
            int len;
            while ((len = zipInput.read(buffer)) > 0) {
                fos.write(buffer, 0, len);
            }
            fos.close();
            zipEntry = zipInput.getNextEntry();
        }
        return result;
    }

    public static long addToQueue(File file) throws InterruptedException {
        // Finding unused ID
        Long currentID;
        do {
            currentID = ThreadLocalRandom.current().nextLong();
        } while (IDtoFile.containsKey(currentID));
        // Associating file with this ID
        IDtoFile.put(currentID, file);
        // Associating lock with this ID (locked until result is ready)
        CountDownLatch resultLatch = new CountDownLatch(1);
        IDtoLock.put(currentID, resultLatch);
        // Adding to the queue
        queue.put(currentID);
        return currentID;
    }

    private static void notifyResultReady(Long id){
        IDtoLock.get(id).countDown();
    }

    private static void removeID(Long id){
        IDtoFile.remove(id);
        IDtoLock.remove(id);
        IDtoResult.remove(id);
    }

    public static String waitResultReady(Long id) throws InterruptedException{
        String res;
        IDtoLock.get(id).await();
        res = IDtoResult.get(id);
        removeID(id);
        return res;
    }

    private static class AlgorithmExecutionThread extends Thread
    {
        public void run()
        {
            try
            {
                RequestProcessor.generatingLoop();
            }
            catch (Exception e)
            {
                e.printStackTrace();
//                System.out.println ("Exception is caught");
            }
        }
    }
}
