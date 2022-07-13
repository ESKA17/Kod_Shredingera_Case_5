package com.example.mycli.services;

import com.example.mycli.entity.AuthData;
import com.example.mycli.entity.RoleEntity;
import com.example.mycli.entity.UserEntity;
import com.example.mycli.repository.RoleEntityRepo;
import com.example.mycli.repository.UserEntityRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Log
public class CreateAdminServiceImpl implements CreateAdminService{
    private final UserEntityRepo userEntityRepo;
    private final RoleEntityRepo roleEntityRepo;
    private final PasswordEncoder passwordEncoder;
    private final UserService userService;

    @Override
    public void createAdmin() {
        log.info("creating admin ...");
        List<UserEntity> admins = userService.findAdmins();
        if (admins.size() == 0) {
            RoleEntity userRoleEntity = roleEntityRepo.findByName("ROLE_ADMIN");
            AuthData authData = AuthData.builder()
                    .email("sakenovramazan@gmail.com")
                    .password(passwordEncoder.encode("admin"))
                    .roleEntity(userRoleEntity)
                    .firstTime(false)
                    .build();
            UserEntity user = UserEntity.builder()
                    .authdata(authData)
                    .fullName("admin")
                    .active(true)
                    .build();
            UserEntity admin = userService.saveUser(user);
            log.info("admin created - sakenovramazan@gmail.com:admin");
        } else {
            for (UserEntity admin: admins) {
                if (admin.getActive()) {
                    log.info("admin is already created :" + admin.getAuthdata().getEmail());
                }
            }
        }
    }
}
