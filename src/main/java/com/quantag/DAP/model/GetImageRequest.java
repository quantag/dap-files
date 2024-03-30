package com.quantag.DAP.model;

import lombok.Setter;
import lombok.Getter;
public class GetImageRequest {
    @Setter
    @Getter

    String sessionId;

    public String toString() {
        return "[" + sessionId + "]";
    }
}
