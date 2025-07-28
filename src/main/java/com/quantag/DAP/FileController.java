package com.quantag.DAP;

import com.quantag.DAP.dto.RenameDto;
import com.quantag.DAP.service.FileService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;


@RestController
@AllArgsConstructor
@Slf4j
public class FileController {
    private static final Logger logger = LoggerFactory.getLogger(FileController.class);

    private final FileService fileService;

    @GetMapping("/list{path}")
    public ResponseEntity<?> getList(@RequestParam String path,
                                     HttpServletRequest request) {
        if(pathVerifier(path)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        String userId = (String)request.getAttribute("userId");
        if(userId.equals("null")) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        if(!fileService.pathExists(path, userId)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        logger.info("GET: List directory contents "+ path);
        return new ResponseEntity<>(fileService.getList(path, userId), HttpStatus.OK);
    }

    @GetMapping("/stat{path}")
    public ResponseEntity<?> getStat(@RequestParam String path,
                                     HttpServletRequest request) {
        if(pathVerifier(path)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        String userId = (String)request.getAttribute("userId");
        if(userId.equals("null")) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        if(!fileService.pathExists(path, userId)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        logger.info("GET: Get file/folder metadata (stat) "+ path);
        return new ResponseEntity<>(fileService.getStat(path, userId), HttpStatus.OK);
    }

    @GetMapping("/file{path}")
    public ResponseEntity<?> getFile(@RequestParam String path,
                                     HttpServletRequest request) {
        if(pathVerifier(path)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        String userId = (String)request.getAttribute("userId");
        if(userId.equals("null")) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        if(!fileService.pathExists(path, userId)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        logger.info("GET: download file content "+ path);

        Resource resource = fileService.getFile(path, userId);
        String contentType = fileService.getContentType(resource);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

    @PostMapping("/file")
    public ResponseEntity<?> uploadFile(@RequestParam String path,
                                        @RequestParam("file") MultipartFile file,
                                        HttpServletRequest request) {
        if(pathVerifier(path)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        String userId = (String)request.getAttribute("userId");
        if(userId.equals("null")) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        logger.info("POST: Write file content "+ path);
        boolean result = fileService.uploadFile(path, file, userId);
        if(!result) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/delete{path}")
    public ResponseEntity<?> deleteFile(@RequestParam String path,
                                        HttpServletRequest request) {
        if(pathVerifier(path)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        String userId = (String)request.getAttribute("userId");
        if(userId.equals("null")) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        if(!fileService.pathExists(path, userId)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        logger.info("DELETE: Delete file or folder "+ path);
        return new ResponseEntity<>(fileService.deleteFile(path, userId), HttpStatus.OK);
    }

    @PostMapping("/mkdir")
    public ResponseEntity<?> mkDir(@RequestParam String path,
                                   HttpServletRequest request) {
        if(pathVerifier(path)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        String userId = (String)request.getAttribute("userId");
        if(userId.equals("null")) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        logger.info("POST: Create new folder "+ path);
        return new ResponseEntity<>(fileService.mkDir(path, userId), HttpStatus.OK);
    }

    @PostMapping("/rename")
    public ResponseEntity<?> rename(@Valid @RequestBody RenameDto renameDto,
                                    HttpServletRequest request) {
        if(renameDto == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        String userId = (String)request.getAttribute("userId");
        if(userId.equals("null")) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        logger.info("POST: Rename or move a file/folder "+ renameDto);
        return new ResponseEntity<>(fileService.rename(renameDto, userId), HttpStatus.OK);
    }

    private boolean pathVerifier(String path) {
        if (path.contains("..") || path.contains("\\")) {
            log.error("ERROR: Invalid folder name in request: "+ path);
            return true;
        }
        return false;
    }
}