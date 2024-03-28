package com.quantag.DAP.model;
import lombok.Getter;
import lombok.Setter;

public class SubmitFileResponse {
    public static int OK = 0;
    public static int BAD_REQUEST = 1;

    @Setter @Getter
    int status;

    public SubmitFileResponse(int status) {
        this.status = status;
    }
}
