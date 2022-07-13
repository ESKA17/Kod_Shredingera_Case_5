package com.example.mycli.controllers;

import com.example.mycli.model.FileInfo;
import com.example.mycli.model.ResponseMessage;
import com.example.mycli.services.FilesStorageService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping()
//@SecurityRequirement(name = "basicauth")

@CrossOrigin(origins = "http://localhost:3000")
public class FilesUploadController {
    private final FilesStorageService storageService;
    @PostMapping("/upload")
    public ResponseEntity<ResponseMessage> uploadFile(@RequestParam("file") MultipartFile file,
                                                      HttpServletRequest httpServletRequest) {
        String message;
        try {
            storageService.save(file, httpServletRequest);
            message = "Uploaded the file successfully: " + file.getOriginalFilename();
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
        } catch (Exception e) {
            message = "Could not upload the file: " + file.getOriginalFilename() + "!";
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
        }
    }
    @GetMapping("/files")
    public ResponseEntity<List<FileInfo>> getListFiles() {
        List<FileInfo> fileInfos = storageService.loadAll().map(path -> {
            String filename = path.getFileName().toString();
            String url = MvcUriComponentsBuilder
                    .fromMethodName(FilesUploadController.class, "getFile",
                            path.getFileName().toString()).build().toString();
            return new FileInfo(filename, url);
        }).collect(Collectors.toList());
        return ResponseEntity.status(HttpStatus.OK).body(fileInfos);
    }
    @GetMapping("/files/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> getFile(@RequestParam Long id) {
        Resource file = storageService.load(id);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() +
                        "\"").body(file);
    }
    @GetMapping("/files/user")
    @ResponseBody
    public ResponseEntity<Resource> getFileJWT(HttpServletRequest httpServletRequest) {
        Resource file = storageService.loadUser(httpServletRequest);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() +
                        "\"").body(file);
    }

    @GetMapping("/mobile/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> getFileMobile(@RequestParam Long id) {
        Resource file = storageService.load(id);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() +
                        "\"").body(file);
    }
    @GetMapping("/mobile/user")
    @ResponseBody
    public ResponseEntity<Resource> getFileMobileJWT(HttpServletRequest httpServletRequest) {
        Resource file = storageService.loadUser(httpServletRequest);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() +
                        "\"").body(file);
    }


}
