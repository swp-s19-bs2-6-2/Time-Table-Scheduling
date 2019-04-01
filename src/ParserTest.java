import org.junit.Rule;
import org.junit.rules.TemporaryFolder;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class ParserTest{
    @Rule
    public final TemporaryFolder tmp = new TemporaryFolder();
/*
    @Test
    public void testValidation() throws Exception{
        tmp.create();
        Parser p = new Parser();
        for(String f: Parser.FILES)
            tmp.newFile(f);
        p.validate(tmp.getRoot());
        tmp.delete();

        tmp.create();
        boolean et = false;
        try {
            p.validate(tmp.getRoot());
        }catch (FileNotFoundException e){
            et = true;
        }
        assertTrue("Files not found exception was not thrown", et);

        et = false;
        try {
            p.validate(tmp.newFile());
        }catch (NotDirectoryException e){
            et = true;
        }
        assertTrue("Directory exception was not thrown", et);
    }

    @Test
    public void testGroups() throws Exception{
        Parser p = new Parser();

        assertTrue(p.parseGroups(toIS("")).isEmpty());
        boolean et = false;
        try {
            List<StudentsGroup> l = p.parseGroups(toIS("1, 2, 3\naa"));
        }catch (Parser.IncorrectFileStructureException e){
            et = true;
        }
        assertTrue("Didn't throw Exception on wrong input", et);

    }
*/

    private InputStream toIS(String s){
        return new ByteArrayInputStream(s.getBytes(StandardCharsets.UTF_8));
    }
}
