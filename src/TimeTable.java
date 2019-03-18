import com.opencsv.CSVReader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class TimeTable extends Parser {
    List<List<TimeSlot>> availableTimeSlots;

    TimeTable(InputStream csv) throws IOException {
        super(csv);
    }



    @Override
    protected void parse(BufferedReader csv) throws IOException {
        int line = 1;
        ArrayList<TimeSlot> ts = new ArrayList<>();

        for (String[] time: new CSVReader(csv)){
            if (time.length != 4)
                throw new Parser.IncorrectCSVFileException(line, 0, "start hour, start minute, end hour, end minute");
            int[] t = Arrays.stream(time).mapToInt(Integer::parseInt).toArray();
            ts.add(new TimeSlot(t[0], t[1], t[2], t[3]));

            line++;
        }

        availableTimeSlots = new ArrayList<>(7);

        for(int i = 0; i < 7; i++)
            availableTimeSlots.add(i, (ArrayList<TimeSlot>)ts.clone());
    }
}
