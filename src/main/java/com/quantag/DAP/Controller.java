package com.quantag.DAP;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletResponse;
//import lombok.extern.slf4j.Slf4j;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Base64;

import com.quantag.DAP.model.*;

@RestController
//@Slf4j
public class Controller {

    @PostMapping("/submitFiles")
    public void getSubmitFiles(@RequestBody GetSubmitFilesRequest requestData,
                                HttpServletResponse response) {

        //log.info("storeContact: " + requestData.getName() +", "+ requestData.getEmail());

//        System.out.println("AL-> "+ requestData.getSessionId() +"  "+ requestData.getPath());
//        System.out.println("AL-> "+ requestData.getSource());

        if(requestData.getSessionId() != null && requestData.getPath() != null && requestData.getSource() != null) {
            byte[] decodedSource = Base64.getDecoder().decode( requestData.getSource() );
            try (OutputStream stream = new FileOutputStream( requestData.getPath() )) {
                stream.write(decodedSource);
                response.setStatus(HttpStatus.OK.value());
            }
            catch (Exception ex) {
//                System.out.println("AL-> ERROR: "+ ex);
                response.setStatus(HttpStatus.CONFLICT.value());
            }
        }
        else {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
        }


    }

}