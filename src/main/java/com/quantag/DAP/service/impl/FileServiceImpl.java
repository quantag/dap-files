package com.quantag.DAP.service.impl;

import com.quantag.DAP.dto.ListDto;
import com.quantag.DAP.dto.RenameDto;
import com.quantag.DAP.dto.ResponceDto;
import com.quantag.DAP.dto.StatDto;
import com.quantag.DAP.service.FileService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static com.quantag.DAP.Application.mainFolder;

@Service
@RequiredArgsConstructor
@Slf4j
public class FileServiceImpl implements FileService {

    @Override
    public List<ListDto> getList(String path, String userId) {
        List<ListDto> listDtos = new ArrayList<>();

        Path mainPath = getFullPath(userId);
        Path fullPath = (path.isEmpty()) ? mainPath : mainPath.resolve(path).normalize();

        try (Stream<Path> paths = Files.list(fullPath)) {
            paths
                .sorted()
                .forEach(pathT -> {
                    if (Files.isDirectory(pathT)) {
                        ListDto dtoList = new ListDto();
                        dtoList.setName(pathT.getFileName().toString());
                        dtoList.setDirectory(true);
                        listDtos.add(dtoList);
                    }
                    if (Files.isRegularFile(pathT)) {
                        ListDto dtoList = new ListDto();
                        dtoList.setName(pathT.getFileName().toString());
                        dtoList.setDirectory(false);
                        listDtos.add(dtoList);
                    }
            });
        } catch (IOException ex) {
            log.error("ERROR Cannot get list of files and folders for userId="+ userId);
//            throw new RuntimeException("Cannot get list of files and folders", ex);
        }

        return listDtos;
    }

    @Override
    public StatDto getStat(String path, String userId) {
        Path mainPath = getFullPath(userId);
        Path fullPath = (path.isEmpty()) ? mainPath : mainPath.resolve(path).normalize();

        StatDto dtoStat = new StatDto();

        try {
            BasicFileAttributes attrs = Files.readAttributes(fullPath, BasicFileAttributes.class);
            dtoStat.setDirectory(attrs.isDirectory());
            dtoStat.setCtime(attrs.creationTime().toString());
            dtoStat.setMtime(attrs.lastModifiedTime().toString());
            dtoStat.setSize(attrs.size());
        }
        catch (IOException ioException) {
            log.error("ERROR cannot get attributes from "+ path);
        }

        return dtoStat;
    }

    @Override
    public Resource getFile(String path, String userId) {
        Path mainPath = getFullPath(userId);
        Path fullPath = (path.isEmpty()) ? mainPath : mainPath.resolve(path).normalize();

        try {
            Resource resource = new UrlResource(fullPath.toUri());

            if (!resource.exists() || !resource.isReadable()) {
                throw new RuntimeException("ERROR: File not found: " + path);
            }
            return resource;
        }
        catch (IOException e) {
            throw new RuntimeException("ERROR reading file: " + path, e);
        }
    }

    @Override
    public boolean pathExists(String path, String userId) {
        Path mainPath = getFullPath(userId);
        Path fullPath = (path.isEmpty()) ? mainPath : mainPath.resolve(path).normalize();

        try {
            Resource resource = new UrlResource(fullPath.toUri());
            if(resource.exists() || resource.isReadable()) {
                return true;
            }
            else {
                log.info("File or directory does not exist: "+ fullPath);
                return false;
            }
        }
        catch (MalformedURLException malformedURLException) {
            return false;
        }
    }

    @Override
    public String getContentType(Resource resource) {
        try {
            String ct = Files.probeContentType(resource.getFile().toPath());
            if(ct == null) {
                if(resource.getFile().getName().toLowerCase().endsWith(".qasm")) {
                    return "text/x-qasm";
                }
                else {
                    return "application/octet-stream";
                }
            }
            else {
                return ct;
            }
        } catch (IOException e) {
            return "application/octet-stream";
        }
    }

    @Override
    public boolean uploadFile(String path, MultipartFile file, String userId) {
        Path mainPath = getFullPath(userId);
        Path fullPath = (path.isEmpty()) ? mainPath : mainPath.resolve(path).normalize();

        try {
            Files.copy(file.getInputStream(), fullPath, StandardCopyOption.REPLACE_EXISTING);
        }
        catch (IOException ioe) {
            log.error("Cannot upload the file "+ file.getOriginalFilename());
            throw new RuntimeException("Cannot upload the files and folders. ", ioe);
        }
        return true;
    }

    @Override
    public ResponceDto deleteFile(String path, String userId) {
        ResponceDto responceDto = new ResponceDto();

        Path mainPath = getFullPath(userId);
        Path fullPath = (path.isEmpty()) ? mainPath : mainPath.resolve(path).normalize();

        try {
            Files.deleteIfExists(fullPath);
            responceDto.setSuccess(true);
        }
        catch (IOException ioException) {
            log.error("ERROR: Could not delete "+ path);
            responceDto.setSuccess(false);
        }

        return responceDto;
    }

    @Override
    public ResponceDto mkDir(String path, String userId) {
        ResponceDto responceDto = new ResponceDto();

        Path mainPath = getFullPath(userId);
        Path newPath = (path.isEmpty()) ? mainPath : mainPath.resolve(path).normalize();

        try {
            if(!Files.exists(newPath)) {
                Files.createDirectories(newPath);
                responceDto.setSuccess(true);
            }
            else {
                log.error("New directory already exist "+ newPath);
                responceDto.setSuccess(false);
            }
        }
        catch (Exception ex) {
            log.error("Could not create new directory "+ newPath);
            responceDto.setSuccess(false);
        }

        return responceDto;
    }

    @Override
    public ResponceDto rename(RenameDto renameDto, String userId) {
        ResponceDto responceDto = new ResponceDto();

        Path mainPath = getFullPath(userId);
        Path oldPath = mainPath.resolve(renameDto.getOldPath()).normalize();
        Path newPath = mainPath.resolve(renameDto.getNewPath()).normalize();

        try {
            Files.move(oldPath, newPath, StandardCopyOption.REPLACE_EXISTING);
            responceDto.setSuccess(true);
        }
        catch (IOException ioException) {
            log.error("ERROR: Could not rename "+ renameDto);
            responceDto.setSuccess(false);
        }

        return responceDto;
    }

    private Path getFullPath(String userId) {
        Path mainPath = Paths.get(mainFolder).toAbsolutePath().normalize();
        Path fullPath = mainPath.resolve(userId).normalize();
        try {
            if(!Files.exists(fullPath)) {
                Files.createDirectories(fullPath);
            }
        }
        catch (IOException ex) {
            log.error("ERROR in getFullPath for userId: "+ userId);
            throw new RuntimeException("Cannot create folder. ", ex);
        }
        return fullPath;
    }

}
