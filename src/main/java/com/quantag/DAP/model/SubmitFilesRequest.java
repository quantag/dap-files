package com.quantag.DAP.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class SubmitFilesRequest {
    @Setter @Getter
    String sessionId;

    @Setter @Getter
    String root;

    @Setter @Getter
    List<FileData> files = null;

    public String toString() {
        String res = "sessionId = " + sessionId +", root = [" + root + "] ";
        if(files != null) {
            for(FileData file : files) {
                res += file.getPath() +"\n";
            }
        }
        return res;
    }
}
