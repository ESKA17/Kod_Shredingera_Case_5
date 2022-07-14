package com.example.mycli.controllers;

import com.example.mycli.model.FileInfo;
import com.example.mycli.model.ImageWrap;
import com.example.mycli.model.ResponseMessage;
import com.example.mycli.services.FilesStorageService;
import com.example.mycli.services.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping()
//@SecurityRequirement(name = "basicauth")

@CrossOrigin(origins = "http://localhost:3000")
public class FilesUploadController {
    private final FilesStorageService storageService;
    private final UserService userService;
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
    @GetMapping("/all_files")
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

    @GetMapping(value = "/files/{filename:.+}", produces = MediaType.IMAGE_JPEG_VALUE)
    @ResponseBody
    public ResponseEntity<byte []> getFile(@RequestParam Long id) throws IOException {
        BufferedImage file = storageService.load(id);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(file, "jpeg", baos);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + id +
                        "\"").body(baos.toByteArray());
    }
    @GetMapping(value = "/files/user", produces = MediaType.IMAGE_JPEG_VALUE)
    @ResponseBody
    public ResponseEntity<BufferedImage> getFileJWT(HttpServletRequest httpServletRequest) {
        BufferedImage file =  storageService.loadUser(httpServletRequest);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + 2 +
                        "\"").body(file);
    }

    @GetMapping(value = "/mobile")
    @ResponseBody
    public ImageWrap getFileMobile(@RequestParam Long id) throws IOException {
        BufferedImage file = storageService.loadMobile(id);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(file, "jpeg", baos);
        return new ImageWrap(baos.toByteArray());
    }
    @GetMapping(value = "/mobile/user")
    @ResponseBody
    public ImageWrap getFileMobileJWT(HttpServletRequest httpServletRequest) throws IOException {
        BufferedImage file = storageService.loadUserMobile(httpServletRequest);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(file, "jpeg", baos);
        return new ImageWrap(baos.toByteArray());
    }

}
