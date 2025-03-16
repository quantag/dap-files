package com.quantag.DAP.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SubmitFileRequest {
    String sessionId;

    String path;

    String source;

    public String toString() {
        return "[" + sessionId + "] " + path;
    }

}
