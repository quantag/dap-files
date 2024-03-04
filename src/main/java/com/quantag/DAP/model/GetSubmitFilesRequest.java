package com.quantag.DAP.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class GetSubmitFilesRequest {
    @Setter @Getter
    String sessionId;
    @Setter @Getter
    List<FileData> files = null;
}
