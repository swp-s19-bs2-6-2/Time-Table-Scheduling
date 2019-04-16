import algorithms.TimeTable;
import io.Parser;

import java.io.File;


public class Main {
    private final static String DIR = "data";

    private static void test(){
        try {
            Parser p = new Parser();
            Parser.TableResult r = p.parseAll(new File(DIR));
            new TimeTable(r.getTimeSlots(), r.getWorkingDays(), r.getLessons()).printTimeTable();
        }catch (Exception e){
            e.printStackTrace();
            System.out.println(e.getMessage());
            System.exit(1);
        }
    }

    public static void main(String[] args){
        test();
    }
}