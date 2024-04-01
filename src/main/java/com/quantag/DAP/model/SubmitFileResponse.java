package com.quantag.DAP.model;
import lombok.Getter;
import lombok.Setter;
import java.util.Vector;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class SubmitFileResponse {
    public static int OK = 0;
    public static int BAD_REQUEST = 1;

    @Setter @Getter
    int status;

    @Setter @Getter
    int files;

    @Setter @Getter
    Vector<String> paths;

    @Setter @Getter
    String current;

    public SubmitFileResponse(int status) {
        this.status = status;
        this.files = 0;
    }
    public SubmitFileResponse(int status, int files) {
        this.status = status;
        this.files = files;
    }
    public SubmitFileResponse(int status, int files, Vector<String> paths) {
        this.status = status;
        this.files = files;
        this.paths = paths;

        LocalDateTime now = LocalDateTime.now();

        // Define the desired date and time format
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss.SSS");

        // Format the current date and time using the formatter
        this.current= now.format(formatter);
    }

}
