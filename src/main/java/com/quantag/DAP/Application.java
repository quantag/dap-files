package com.quantag.DAP;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import lombok.extern.slf4j.Slf4j;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

@SpringBootApplication
@Slf4j
public class Application {
    public static String mainFolder = null;
    public static String imageFolder = "/var/dap/images/";

    public static void main(String[] args) {
        try {
            Properties prop = new Properties();
            InputStream stream = Files.newInputStream(Paths.get("dap-files.properties"));
            if(stream != null) {
                prop.load(stream);

                mainFolder = prop.getProperty("folder", "./");
                imageFolder = prop.getProperty("imageFolder", "/var/dap/images/");
            }
            else {
                log.error("ERROR loading dap-files.properties file");
            }
        }
        catch (NullPointerException nullPointerException) {
            log.error("ERROR loading dap-files.properties file: "+ nullPointerException.getMessage());
        }
        catch (IllegalArgumentException illegalArgumentException) {
            log.error("ERROR loading dap-files.properties file: "+ illegalArgumentException.getMessage());
        }
        catch (IOException ioException) {
            log.error("ERROR loading dap-files.properties file: "+ ioException.getMessage());
        }

        SpringApplication.run(Application.class, args);
    }
}
