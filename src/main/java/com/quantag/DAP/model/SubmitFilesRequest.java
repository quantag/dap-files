package com.quantag.DAP.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class SubmitFilesRequest {
    String sessionId;
    String root;
    List<FileData> files = null;

    public String toString() {
        String res = "";

        if(sessionId!=null)
            res += "sessionId = " + sessionId;

        if(root!=null)
            res += ", root = [" + root + "] ";

        if(files != null) {
            res += files.size() + " files: ";
            for(FileData file : files) {
                res += file.getPath() +"\n";
            }
        }
        return res;
    }

    public boolean validate() {
        return (sessionId != null && files != null);
    }

    // get path relative to root folder
    public String getRelativePath(String path) {
        if(root==null || path==null)
            return path;

        if(path.contains(root)) {
            return path.substring( root.length()+1, path.length() );
        }
        return path;
    }
}
