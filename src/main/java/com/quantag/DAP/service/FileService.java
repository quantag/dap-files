package com.quantag.DAP.service;

import com.quantag.DAP.dto.ListDto;
import com.quantag.DAP.dto.RenameDto;
import com.quantag.DAP.dto.ResponceDto;
import com.quantag.DAP.dto.StatDto;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FileService {
    List<ListDto> getList(String path, String userId);
    StatDto getStat(String path, String userId);
    Resource getFile(String path, String userId);
    String getContentType(Resource resource);
    boolean uploadFile(String path, MultipartFile file, String userId);
    ResponceDto deleteFile(String path, String userId);
    ResponceDto mkDir(String path, String userId);
    ResponceDto rename(RenameDto renameDto, String userId);
}
