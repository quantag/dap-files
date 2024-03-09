package com.quantag.DAP.model;

import lombok.Getter;
import lombok.Setter;

public class SubmitFileRequest {
    @Setter @Getter
    String sessionId;

    @Setter @Getter
    String path;

    @Setter @Getter
    String source;

    public String toString() {
        return "[" + sessionId + "] " + path;
    }

}
