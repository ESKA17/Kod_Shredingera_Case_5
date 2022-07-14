package com.example.mycli.services;

import com.example.mycli.exceptions.AccountBadRequest;
import com.example.mycli.model.*;
import com.example.mycli.entity.UserEntity;
import com.example.mycli.entity.UserInformation;
import com.example.mycli.utils.Utils;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.example.mycli.model.SubjectType.*;

@Service
@RequiredArgsConstructor
@Log
public class UserInformationServiceImpl implements UserInformationService {
    private final UserService userService;
    @Override
    public void fillUserInformationForm(ScreeningRequest screeningRequest, HttpServletRequest httpServletRequest) {
        log.info("filling information form ...");
        String email = userService.getEmailFromToken(httpServletRequest);
        String[] strings = screeningRequest.getDateOfBirth().split("-");
        int[] numbers = Arrays.stream(strings)
                .mapToInt(Integer::parseInt)
                .toArray();
        LocalDate localDate;
        if (strings[2].length() == 4) {
            localDate = LocalDate.of(numbers[2], numbers[1], numbers[0]);
        } else {
            localDate = LocalDate.of(numbers[0], numbers[1], numbers[2]);
        }
        UserInformation userInformation = UserInformation.builder()
                .dateOfBirth(localDate)
                .city(screeningRequest.getCity())
                .school(screeningRequest.getSchool())
                .IIN(screeningRequest.getIIN())
                .phoneNumber(screeningRequest.getPhoneNumber())
                .university(screeningRequest.getUniversity())
                .aboutMe(screeningRequest.getAboutMe())
                .build();
        UserEntity user = userService.findByAuthDataEmail(email);
        user.setUserInformation(userInformation);
        userService.saveUser(user);
        log.info("user: " + user.getFullName());
        log.info("user information was saved: " + userInformation);
    }

    @Override
    public UserInfoResponse getUserInformationForm(HttpServletRequest httpServletRequest) {
        log.info("retrieving user info ...");
        String email = userService.getEmailFromToken(httpServletRequest);
        UserEntity user = userService.findByAuthDataEmail(email);
        Integer roleID = user.getAuthdata().getRoleEntity().getId();
        UserInfoResponse userInfoResponse = UserInfoResponse.builder()
                .fullName(user.getFullName())
                .aboutMe(user.getUserInformation().getAboutMe())
                .city(user.getUserInformation().getCity())
                .dateOfBirth(user.getUserInformation().getDateOfBirth())
                .IIN(user.getUserInformation().getIIN())
                .phoneNumber(user.getUserInformation().getPhoneNumber())
                .university(user.getUserInformation().getUniversity())
                .school(user.getUserInformation().getSchool())
                .roleID(roleID)
                .build();
        log.info("getting screening form for email " + email + ": " + user);
        log.info("user info: " + userInfoResponse);
        return userInfoResponse;
    }

    @Override
    public void fillMajors(Majors majors, HttpServletRequest httpServletRequest) {
        log.info("filling majors ...");
        String email = userService.getEmailFromToken(httpServletRequest);
        UserEntity user = userService.findByAuthDataEmail(email);
        List<Integer> majorsInt = majors.getMajors();
        List<SubjectType> subjectList = user.getSubjectTypeList();
        List<SubjectType>  newSubjectList = Utils.fromIntegerToSubjectType(majorsInt, subjectList);
        user.setSubjectTypeList(newSubjectList);
        userService.saveUser(user);
        log.info("majors were filled");
    }

    @Override
    public void changeFullName(String fullName, HttpServletRequest httpServletRequest) {
        log.info("changing full name ...");
        if (fullName.isEmpty()) {
            throw new AccountBadRequest("check full name");
        }
        String email = userService.getEmailFromToken(httpServletRequest);
        UserEntity user = userService.findByAuthDataEmail(email);
        user.setFullName(fullName);
        userService.saveUser(user);
        log.info("full name was changed");
    }

    @Override
    public List<Integer> getMajors(HttpServletRequest httpServletRequest) {
        log.info("getting majors ...");
        String email = userService.getEmailFromToken(httpServletRequest);
        UserEntity userEntity = userService.findByAuthDataEmail(email);
        List<SubjectType> subjectTypeList = userEntity.getSubjectTypeList();
        log.info("majors sent");
        return Utils.fromSubjectTypeToInteger(subjectTypeList);
    }



    @Override
    public String getFullName(HttpServletRequest httpServletRequest) {
        log.info("getting full name ...");
        String email = userService.getEmailFromToken(httpServletRequest);
        UserEntity userEntity = userService.findByAuthDataEmail(email);
        log.info("full name was retrieved");
        return userEntity.getFullName();
    }

}
