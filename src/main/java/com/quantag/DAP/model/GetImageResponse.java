package com.quantag.DAP.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Base64;


@Setter
@Getter
public class GetImageResponse {
    int status;

    String data;

    public GetImageResponse(int status) {
        this.status = status;
        this.data = "";
    }

    public GetImageResponse(int status, byte[] data) {
        this.status = status;
        if(data!=null) {
            byte[] encoded = Base64.getEncoder().encode(data);
            this.data = new String(encoded);
        } else {
            this.data = "";
        }
    }
}
