package com.quantag.DAP;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.io.FilenameUtils;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Base64;
import java.util.List;

import com.quantag.DAP.model.*;
import static com.quantag.DAP.Application.mainFolder;

@RestController
@Slf4j
public class Controller {

    @PostMapping("/submitFiles")
    public void getSubmitFiles(@RequestBody GetSubmitFilesRequest requestData, HttpServletResponse response) {
        //log request data
        String fileDataRequest = "";
        if(requestData.getFiles() == null) {
            fileDataRequest = "null";
        }
        else {
            List<FileData> fileDataList = requestData.getFiles();
            for(FileData fileData : fileDataList) {
                fileDataRequest += "{" +
                        "\"path\": " + fileData.getPath() + ", " +
                        "\"source\": \"" + fileData.getSource() + "\"" +
                        "}";
            }
        }
        String newRequest = "{" +
                "\"sessionId\": \"" + requestData.getSessionId() + "\", " +
                "\"files\": [" + fileDataRequest + "]" +
                "}";
        log.info("request: " + newRequest);

        if(requestData.getSessionId() != null && requestData.getFiles() != null) {
            List<FileData> fileDataList = requestData.getFiles();
            for(FileData fileData : fileDataList) {
                //gets file name from path
                String fileName = FilenameUtils.getName( fileData.getPath() );
                //decode source
                byte[] decodedSource = Base64.getDecoder().decode( fileData.getSource() );

                String pathToStore = (mainFolder == null) ? fileName : mainFolder + fileName;
                //String pathToStore = fileName;

                try (OutputStream stream = new FileOutputStream(pathToStore)) {
                    stream.write(decodedSource);
                    log.info("Decoded to "+ pathToStore);
                    response.setStatus(HttpStatus.OK.value());
                }
                catch (Exception ex) {
                    log.error(ex.getMessage());
                    response.setStatus(HttpStatus.CONFLICT.value());
                }
            }
        }
        else {
            log.error("BAD_REQUEST: NULL in request data");
            response.setStatus(HttpStatus.BAD_REQUEST.value());
        }

    }

    @PostMapping("/submitFile")
    public void getSubmitFile(@RequestBody GetSubmitFileRequest requestData, HttpServletResponse response) {
        //log request data
        String newRequest = "{" +
                "\"sessionId\": \"" + requestData.getSessionId() + "\", " +
                "\"path\": " + requestData.getPath() + ", " +
                "\"source\": \"" + requestData.getSource() + "\"" +
                "}";
        log.info("request: "+ newRequest);

        if(requestData.getSessionId() != null && requestData.getPath() != null && requestData.getSource() != null) {
            //gets file name from path
            String fileName = FilenameUtils.getName( requestData.getPath() );
            //decode source
            byte[] decodedSource = Base64.getDecoder().decode( requestData.getSource() );

            String pathToStore = (mainFolder == null) ? fileName : mainFolder + fileName;

            try (OutputStream stream = new FileOutputStream(pathToStore)) {
                stream.write(decodedSource);
                log.info("Decoded to "+ pathToStore);
                response.setStatus(HttpStatus.OK.value());
            }
            catch (Exception ex) {
                log.error(ex.getMessage());
                response.setStatus(HttpStatus.CONFLICT.value());
            }
        }
        else {
            log.error("BAD_REQUEST: NULL in request data");
            response.setStatus(HttpStatus.BAD_REQUEST.value());
        }

    }

}