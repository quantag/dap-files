package com.quantag.DAP.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class GetFileRequest {
    String file;

    public String toString() {
        return "[" + file + "]";
    }
}
