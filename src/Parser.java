import com.opencsv.CSVReader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class Parser
{
    private InputStream is;

    public Parser(InputStream file){
        is = file;
    }

    public List<String[]> parse(){
        try(CSVReader cr = new CSVReader(new BufferedReader(new InputStreamReader(is)))) {
            return cr.readAll()
                    .stream()
                    .map( s -> Stream.of(s)
                            .map(String::trim)
                            .toArray(String[]::new) )
                    .collect(Collectors.toList());

        }catch (IOException e){
            e.printStackTrace();
            System.exit(1);
            return null;
        }
    }

    static class IncorrectFileStructureException extends Exception{
        IncorrectFileStructureException(String message){super(message);}
        IncorrectFileStructureException(int line, String message){
            super("Incorrect file format: "+message+"\nline: "+line);
        }
    }

    public static List<Classroom> getClassRooms(Parser p) throws IncorrectFileStructureException{
        List<String[]> lines = p.parse();
        List<Classroom> classes = new ArrayList<>();

        int l = 0;//Line number for the exception
        for (String[] line: lines){
            l++;

            //ToDo: line[1::-1] - equipment
            Classroom c;
            try {
                c = new Classroom(Integer.parseInt(line[0]));
            }catch(NumberFormatException e){
                throw new IncorrectFileStructureException(l, "Expected integers, got: "+Arrays.toString(line));
            }
            classes.add(c);
        }
        return classes;
    }

    public static List<TimeSlot> getTimeSlots(Parser p, List<Classroom> availableClassrooms) throws IncorrectFileStructureException {
        List<String[]> lines = p.parse();
        List<TimeSlot> slots = new ArrayList<>();

        int l = 0;//line of error
        for(String[] line: lines){
            if (line.length != 4) throw new IncorrectFileStructureException(l, "Expected be 4 elements, got: "+line.length);
            l++;

            List<Classroom> cp = availableClassrooms
                    .stream()
                    .map( c -> new Classroom(c.getCapacity(), c.getEquipment()) )
                    .collect(Collectors.toList());

            try {
                slots.add(new TimeSlot(Integer.parseInt(line[0]), Integer.parseInt(line[1]), Integer.parseInt(line[2]), Integer.parseInt(line[3]), cp));
            }catch(NumberFormatException e){
                throw new IncorrectFileStructureException(l, "Expected integers, got: "+Arrays.toString(line));
            }
        }
        return slots;
    }

    public static List<Lesson> getLessons(Parser p) throws IncorrectFileStructureException {
        List<String[]> lines = p.parse();
        List<Lesson> lessons = new ArrayList<>();
        for (String[] line: lines){
            Course c = new Course(line[0]); // Course name
            for (int i = 1; i < line.length; i++){
                //ToDo: line[i] - course type which is now not very clear within internal structure
                Lesson l = new Lesson(c);
                lessons.add(l);
            }
        }
        return lessons;
    }
}