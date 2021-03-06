package com.example.mycli.services;

import com.example.mycli.entity.RoleEntity;
import com.example.mycli.entity.UserEntity;
import com.example.mycli.model.FilterSearchRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface UserService {

    void initRoles();
    UserEntity saveUser(UserEntity userEntity);

    UserEntity findByLoginAndPassword(String email, String password);
    UserEntity findByEmail(String email);
    UserEntity findByVerificationCode(String verificationCode);
    UserEntity findByAuthDataEmail(String email);

    RoleEntity findRoleEntityByName(String name);
    UserEntity checkByAuthDataEmail(String email);
    List<UserEntity> findAllUsers();
    String getEmailFromToken(HttpServletRequest httpServletRequest);
    UserEntity findUserByID(Long id);
    List<UserEntity> findAdmins();
    Boolean checkFirstTime(HttpServletRequest httpServletRequest);
    void wasHere(HttpServletRequest httpServletRequest);
    List<UserEntity> filter(FilterSearchRequest filterSearchRequest);
}
