package server;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@RestController
public class APIController {

    private final String pathPrefix = "/api/v1/";

    @GetMapping("/test")
    APIResponses.MSGResponse testRequest(){
        return new APIResponses.MSGResponse(0, "123");
    }

    @CrossOrigin
    @PostMapping(pathPrefix + "csv")
    APIResponses.IDResponse uploadCsv(@RequestParam("file") MultipartFile recvdFile) throws IOException, InterruptedException {
        long new_id = 0;

        File file = File.createTempFile("TTSTempFile", ".zip");
        recvdFile.transferTo(file);

        new_id = RequestProcessor.addToQueue(file);

//        file.delete();    // TODO : Delete temp files!!!

        return new APIResponses.IDResponse(Long.toString(new_id));
    }

    @CrossOrigin
    @GetMapping(pathPrefix + "timetable")
    APIResponses.TableResponse getTimeTable(@RequestParam("id") String id) throws InterruptedException{
        Long converted_id = Long.parseLong(id);
        String result = RequestProcessor.waitResultReady(converted_id);
        return new APIResponses.TableResponse(APIResponses.successCode, result);
    }
}
