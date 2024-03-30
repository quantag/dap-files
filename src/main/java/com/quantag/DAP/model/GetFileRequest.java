package com.quantag.DAP.model;

import lombok.Getter;
import lombok.Setter;

public class GetFileRequest {
    @Setter
    @Getter

    String file;

    public String toString() {
        return "[" + file + "]";
    }
}
