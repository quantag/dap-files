package com.quantag.DAP.model;

import lombok.Setter;
import lombok.Getter;
@Setter
@Getter
public class GetImageRequest {
    String sessionId;

    public String toString() {
        return "[" + sessionId + "]";
    }
}
