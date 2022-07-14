package com.example.mycli.services;

import com.example.mycli.entity.AuthData;
import com.example.mycli.entity.RoleEntity;
import com.example.mycli.entity.UserInformation;
import com.example.mycli.repository.*;
import com.example.mycli.utils.TemplateConstants;
import com.example.mycli.web.JwtProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@Slf4j
@RequiredArgsConstructor

public class TemplatesServiceImpl implements TemplatesService {
    private final UserEntityRepo userEntityRepo;
    private final RoleEntityRepo roleEntityRepo;
    private final UserInfoRepo userInfoRepo;
    private final AuthDataRepo authDataRepo;
    private final PasswordEncoder passwordEncoder;
    private final RankingRepo rankingRepo;
    private final JwtProvider jwtProvider;
    @Override
    public void initRoles() {
        log.info("roles initialization ...");
        if (roleEntityRepo.count() == 0) {
            RoleEntity roleAdmin = new RoleEntity(0, "ROLE_ADMIN");
            RoleEntity roleMentor = new RoleEntity(1, "ROLE_MENTOR");
            RoleEntity roleMentee = new RoleEntity(2, "ROLE_MENTEE");
            roleEntityRepo.save(roleAdmin);
            roleEntityRepo.save(roleMentor);
            roleEntityRepo.save(roleMentee);
            log.info("roles initialization was successful");
        } else {
            log.info("roles have been already added");
        }
    }

    @Override
    public void initUsers() {
        AuthData authData = AuthData.builder()
                .roleEntity(roleEntityRepo.getById(1))
                .password(passwordEncoder.encode("mentor1"))
                .email("mentor1@gmail.com")
                .firstTime(false)
                .build();
        UserInformation userInformation = UserInformation.builder()
                .dateOfBirth(LocalDate.of(2000, 5, 7))
                .university("")
                .build();
    }
}
