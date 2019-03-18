import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


abstract class Parser
{
    /**
     * Takes csv and passes it to be parsed
     * @param csv stream to read csv from
     * @throws IOException if some of IO's throws IOException
     */
    Parser(InputStream csv) throws IOException
    {
        parse(new BufferedReader(new InputStreamReader(csv)));
    }

    /**
     * Should parse *.csv file and saves data into the object itself
     * Will be called at constructor
     * @param csv stream to be parsed
     * @throws IncorrectCSVFileException if file is not well formatted
     * @throws IOException if IO exception was thrown during reading
     */
    protected abstract void parse(BufferedReader csv) throws IOException;


    static class IncorrectCSVFileException extends IOException{
        IncorrectCSVFileException(int line, int carr, String message){
            super("Incorrect file format: "+message+"\nline: "+line+" character: "+carr);
        }
    }
}