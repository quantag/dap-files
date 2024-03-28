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
    public SubmitFileResponse submitFiles(@RequestBody SubmitFilesRequest requestData, HttpServletResponse response) {
        //log request data
        if(requestData==null)
            return new SubmitFileResponse(SubmitFileResponse.BAD_REQUEST);
        
        log.info("\nsubmitFiles request: " + requestData + "\n");

        if(!requestData.validate()) {
            log.error("BAD_REQUEST: NULL in request data");
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            return new SubmitFileResponse(SubmitFileResponse.BAD_REQUEST);
        }

        String sessionPath = mainFolder + "/" + requestData.getSessionId();
        log.info("session path: " + sessionPath);
        Utils.clearFolder(sessionPath);

        List<FileData> fileDataList = requestData.getFiles();
        for(FileData fileData : fileDataList) {
            String relativePath = Utils.replace( requestData.getRelativePath(fileData.getPath()) , '\\', '/' );
            log.info( " relative path: [" + relativePath + "]");

            byte[] decodedSource = Base64.getDecoder().decode( fileData.getSource() );
            String pathToStore = sessionPath + "/";
            pathToStore += relativePath;
            log.info( "pathToStore [" + pathToStore + "]");

            Utils.saveFile(pathToStore, decodedSource);
        }
        response.setStatus(HttpStatus.OK.value());
        return new SubmitFileResponse(SubmitFileResponse.OK);
    }

    @PostMapping("/submitFile")
    public SubmitFileResponse submitFile(@RequestBody SubmitFileRequest requestData, HttpServletResponse response) {
        //log request data
        log.info("\nsubmitFile request: "+ requestData);

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
        return new SubmitFileResponse(SubmitFileResponse.OK);
    }

}