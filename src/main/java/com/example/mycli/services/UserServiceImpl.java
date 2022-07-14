package com.example.mycli.services;

import com.example.mycli.entity.AuthData;
import com.example.mycli.entity.RoleEntity;
import com.example.mycli.entity.UserEntity;
import com.example.mycli.exceptions.AccountBadRequest;
import com.example.mycli.exceptions.AccountNotFound;
import com.example.mycli.exceptions.AuthenticationFailed;
import com.example.mycli.exceptions.PasswordFailed;
import com.example.mycli.model.FilterSearchRequest;
import com.example.mycli.model.FindAllReturnIdWrap;
import com.example.mycli.model.FindUserByIDWrap;
import com.example.mycli.model.SubjectType;
import com.example.mycli.repository.*;
import com.example.mycli.utils.Utils;
import com.example.mycli.web.JwtProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService{

    private final UserEntityRepo userEntityRepo;
    private final RoleEntityRepo roleEntityRepo;
    private final UserInfoRepo userInfoRepo;
    private final AuthDataRepo authDataRepo;
    private final PasswordEncoder passwordEncoder;
    private final RankingRepo rankingRepo;
    private final JwtProvider jwtProvider;


    @Transactional
    @Override
    public UserEntity saveUser(UserEntity user) {
        log.info("saving user ...");
        if (user.getRanking() != null) {
            rankingRepo.save(user.getRanking());
        }
        if (user.getUserInformation() != null) {
            userInfoRepo.save(user.getUserInformation());
        }
        authDataRepo.save(user.getAuthdata());
        return userEntityRepo.save(user);
    }
    @Override
    public UserEntity findByAuthDataEmail(String email) {
        log.info("finding UserEntity by AuthData_email ...");
        return userEntityRepo.findByAuthdata_Email(email).orElseThrow(
                () -> new AccountNotFound("account: " + email));
    }

    @Override
    public RoleEntity findRoleEntityByName(String name) {
        log.info("finding RoleEntity by name ...");
        return roleEntityRepo.findByName(name);
    }

    @Override
    public UserEntity findByLoginAndPassword(String email, String password) {
        log.info("finding UserEntity by email and password ...");
        UserEntity userEntity = findByAuthDataEmail(email);
        if (userEntity != null) {
            log.info(userEntity + " - user entity");
            if (passwordEncoder.matches(password, userEntity.getAuthdata().getPassword())) {
                log.info("password was verified");
                return userEntity;
            } else {

                throw new PasswordFailed();
            }
        } else {
            throw new AccountNotFound("account: " + email);
        }
    }

    @Override
    public UserEntity findByEmail(String email) {
        log.info("finding UserEntity by email ...");
        UserEntity userEntity = findByAuthDataEmail(email);
        if (userEntity != null) {
            log.info("email verification: " + userEntity + " - user entity");
                return userEntity;
        } else {
            log.info("email verification: user not found");
            throw new AccountNotFound("account: " + email);
        }
    }

    @Override
    public UserEntity findByVerificationCode(String verificationCode) {
        log.info("finding UserEntity by verification code ...");
        return userEntityRepo.findByVerificationCode(verificationCode).orElseThrow(
                () -> new AccountNotFound("account: " + verificationCode));
    }
    @Override
    public UserEntity findUserByID(Long id) {
        log.info("finding UserEntity by id ...");
        if (id == null) {
            throw new AccountBadRequest("no ID");
        }
        UserEntity user = userEntityRepo.findById(id).orElseThrow(() -> new AccountNotFound("with id: " + id));
        log.info("userEntity found: " + user);
        return user;
    }

    @Override
    public Boolean checkFirstTime(HttpServletRequest httpServletRequest) {
        log.info("first time? ...");
        String email = getEmailFromToken(httpServletRequest);
        AuthData authData = findByAuthDataEmail(email).getAuthdata();
        Boolean status = authData.getFirstTime();
        log.info("first time? " + status);
        return status;
    }

    @Override
    public void wasHere(HttpServletRequest httpServletRequest) {
        log.info("he was here ...");
        String email = getEmailFromToken(httpServletRequest);
        UserEntity user = findByAuthDataEmail(email);
        AuthData authData = user.getAuthdata();
        authData.setFirstTime(false);
        user.setAuthdata(authData);
        saveUser(user);
        log.info("now everybody knows");
    }

    @Override
    public List<Long> filter(FilterSearchRequest filterSearchRequest) {
        List<SubjectType> subjects = Utils.fromIntToSubjectType(filterSearchRequest.getSubjects());
        List<UserEntity> allUsers = userEntityRepo.findAllByAuthdata_RoleEntity_Id(1);
        List<Long> subjectFilter = new ArrayList<>();
        for (UserEntity userFiltered : allUsers) {
            for (SubjectType subject: subjects) {
                if(subjects.contains(subject)) {
                    subjectFilter.add(userFiltered.getId());
                    break;
                }
            }
        }
        return subjectFilter;
    }

    @Override
    public FindAllReturnIdWrap findAllReturnID() {
        List<UserEntity> allUsers = userEntityRepo.findAllByAuthdata_RoleEntity_Id(1);
        List<Long> allUsersReturnID = new ArrayList<>();
        for (UserEntity user: allUsers) {
            allUsersReturnID.add(user.getId());
        }
        return new FindAllReturnIdWrap(allUsersReturnID);
    }

    @Override
    public Integer findRoleEntity(HttpServletRequest httpServletRequest) {
        log.info("getting role id ...");
        String email = getEmailFromToken(httpServletRequest);
        UserEntity userEntity = findByAuthDataEmail(email);
        log.info("role id was found");
        return userEntity.getAuthdata().getRoleEntity().getId();

    }

    @Override
    public FindUserByIDWrap findUserByIDInWrap(Long id) {
        log.info("finding user by id and creating FindUserByIDWrap ... ");
        UserEntity userEntity = findUserByID(id);
        List<Integer> subjectList = Utils.fromSubjectTypeToInteger(userEntity.getSubjectTypeList());
        FindUserByIDWrap findUserByIDWrap = FindUserByIDWrap.builder()
                .fullName(userEntity.getFullName())
                .university(userEntity.getUserInformation().getUniversity())
                .subjectList(subjectList)
                .build();
        log.info("wrap was prepared");
        return findUserByIDWrap;
    }

    @Override
    public List<UserEntity> findAdmins() {
        log.info("getting all admins");
        return userEntityRepo.findAllByAuthdata_RoleEntity_Id(0);

    }
    @Override
    public List<UserEntity> findAllUsers() {
        log.info("accessing user database for all users");
        return userEntityRepo.findAll();
    }
    @Override
    public UserEntity checkByAuthDataEmail(String email) {
        log.info("checking UserEntity by AuthData_email ...");
        return userEntityRepo.findByAuthdata_Email(email).orElse(null);
    }

    @Override
    public String getEmailFromToken(HttpServletRequest httpServletRequest) {
        String token = httpServletRequest.getHeader("token");
        String email;
        if (token != null) {
            email = jwtProvider.getLoginFromToken(token);
            log.info("account was found in database: " + email);
            return email;
        } else {
            throw new AuthenticationFailed("token is null");
        }
    }
    @Override
    public List<UserEntity> findAllMentors(){
        log.info("getting all mentors");
        return userEntityRepo.findAllByAuthdata_RoleEntity_Id(1);
    }
}
