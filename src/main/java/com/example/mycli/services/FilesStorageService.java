package com.example.mycli.services;

import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.awt.image.BufferedImage;
import java.nio.file.Path;
import java.util.stream.Stream;

public interface FilesStorageService {
    void init();
    void save(MultipartFile file, HttpServletRequest httpServletRequest);
    BufferedImage load(Long id);

    BufferedImage loadMobile(Long id);

    void deleteAll();
    Stream<Path> loadAll();

    BufferedImage loadUser(HttpServletRequest httpServletRequest);

    BufferedImage loadUserMobile(HttpServletRequest httpServletRequest);
}
