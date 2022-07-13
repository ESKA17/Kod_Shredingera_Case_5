package com.example.mycli.services;

import com.example.mycli.entity.RoleEntity;
import com.example.mycli.entity.UserEntity;
import com.example.mycli.model.FilterSearchRequest;
import com.example.mycli.model.FindAllReturnIdWrap;
import com.example.mycli.model.FindUserByIDWrap;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface UserService {
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
    List<UserEntity> findAllMentors();
    Boolean checkFirstTime(HttpServletRequest httpServletRequest);
    void wasHere(HttpServletRequest httpServletRequest);
    List<Long> filter(FilterSearchRequest filterSearchRequest);
    FindAllReturnIdWrap findAllReturnID();
    Integer findRoleEntity(HttpServletRequest httpServletRequest);
    FindUserByIDWrap findUserByIDInWrap(Long id);
}
