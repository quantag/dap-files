package com.quantag.DAP.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PrepareDataRequest {
    String sessionId;
    String userId; //or token?

    public String toString() {
        return "[" + sessionId + ", " + userId + "]";
    }
}
