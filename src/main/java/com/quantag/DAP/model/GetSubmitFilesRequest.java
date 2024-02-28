package com.quantag.DAP.model;

import lombok.Getter;
import lombok.Setter;

public class GetSubmitFilesRequest {
    @Setter @Getter
    String sessionId;
    @Setter @Getter
    String path;
    @Setter @Getter
    String source;
}
