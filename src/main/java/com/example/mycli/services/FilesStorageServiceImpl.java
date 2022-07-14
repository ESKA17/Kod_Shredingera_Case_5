package com.example.mycli.services;

import com.example.mycli.entity.UserEntity;
import com.example.mycli.exceptions.FolderInit;
import com.example.mycli.model.ImageWrap;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.Objects;
import java.util.stream.Stream;
@Transactional
@Service
@RequiredArgsConstructor
@Slf4j
public class FilesStorageServiceImpl implements FilesStorageService {
    private final Path root = Paths.get("uploads");
    private final UserService userService;
    @Override
    public void init() {
        try {
            Files.createDirectory(root);
        } catch (IOException e) {
            throw new FolderInit();
        }
    }

    @Override
    public void save(MultipartFile file, HttpServletRequest httpServletRequest) {
        try {
            String email = userService.getEmailFromToken(httpServletRequest);
            UserEntity user = userService.findByEmail(email);
            String extension = file.getOriginalFilename();
            assert extension != null;
            int size = extension.length();
            extension = extension.substring(size-5);
            String newName = user.getId().toString() + extension;
            Files.copy(file.getInputStream(), this.root.resolve(Objects.requireNonNull(newName)));
        } catch (Exception e) {
            throw new RuntimeException("Could not store the file. Error: " + e.getMessage());
        }
    }

    @Override
    public BufferedImage load(Long id) {
        try {
            return ImageIO.read(new File("uploads/" + id + ".jpeg"));
        } catch (MalformedURLException e) {
            throw new RuntimeException("Error: " + e.getMessage());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public BufferedImage loadMobile(Long id) {
        try {
            return ImageIO.read(new File("uploads/" + id + ".jpeg"));
        } catch (MalformedURLException e) {
            throw new RuntimeException("Error: " + e.getMessage());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteAll() {
        FileSystemUtils.deleteRecursively(root.toFile());
    }

    @Override
    public Stream<Path> loadAll() {
        try {
            return Files.walk(this.root, 1).filter(path -> !path.equals(this.root)).map(this.root::relativize);
        } catch (IOException e) {
            throw new RuntimeException("Could not load the files!");
        }
    }

    @Override
    public BufferedImage loadUser(HttpServletRequest httpServletRequest) {
        try {
            String email = userService.getEmailFromToken(httpServletRequest);
            UserEntity userEntity = userService.findByAuthDataEmail(email);
            return ImageIO.read(new File("uploads/" + userEntity.getId() + ".jpeg"));
        } catch (MalformedURLException e) {
            throw new RuntimeException("Error: " + e.getMessage());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public BufferedImage loadUserMobile(HttpServletRequest httpServletRequest) {
        String email = userService.getEmailFromToken(httpServletRequest);
        UserEntity userEntity = userService.findByAuthDataEmail(email);
        try {
            return ImageIO.read(new File("uploads/" + userEntity.getId() + ".jpeg"));
        } catch (MalformedURLException e) {
            throw new RuntimeException("Error: " + e.getMessage());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
