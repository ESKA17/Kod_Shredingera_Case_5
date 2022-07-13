package com.example.mycli;

import com.example.mycli.repository.UserEntityRepo;
import com.example.mycli.repository.UserInfoRepo;
import com.example.mycli.services.*;
//import com.example.mycli.services.CreateAdminService;
//import com.example.mycli.services.UserInformationService;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.CommandLineRunner;

import javax.annotation.Resource;

@SpringBootApplication
@RequiredArgsConstructor
//@SecurityScheme(name = "basicauth", scheme = "basic", type = SecuritySchemeType.HTTP, bearerFormat = "[token]")
public class MyCliApplication implements CommandLineRunner {
    @Resource
    FilesStorageService storageService;
    @Autowired
    private final TemplatesService templatesService;
    @Autowired
    private final CreateAdminService createAdminService;

    public static void main(String[] args)  {
        SpringApplication.run(MyCliApplication.class);
    }
    @Override
    public void run(String... arg0) {
        templatesService.initRoles();
        createAdminService.createAdmin();
    }
}
