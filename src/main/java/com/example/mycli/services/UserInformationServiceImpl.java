package com.example.mycli.services;

import com.example.mycli.exceptions.AccountBadRequest;
import com.example.mycli.exceptions.AccountNotFound;
import com.example.mycli.exceptions.AuthenticationFailed;
import com.example.mycli.model.Majors;
import com.example.mycli.model.StudyDegree;
import com.example.mycli.entity.UserEntity;
import com.example.mycli.entity.UserInformation;
import com.example.mycli.model.SubjectType;
import com.example.mycli.repository.UserEntityRepo;
import com.example.mycli.repository.UserInfoRepo;
import com.example.mycli.model.ScreeningRequest;
import com.example.mycli.web.JwtProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

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
                .toArray();;
        LocalDate localDate = LocalDate.of(numbers[2], numbers[1], numbers[0]);
        UserInformation userInformation = UserInformation.builder()
                .dateOfBirth(localDate)
                .city(screeningRequest.getCity())
                .school(screeningRequest.getSchool())
                .IIN(screeningRequest.getIIN())
                .phoneNumber(screeningRequest.getPhoneNumber())
                .university(screeningRequest.getUniversity())
                .build();
        UserEntity user = userService.findByAuthDataEmail(email);
        user.setUserInformation(userInformation);
        userService.saveUser(user);
        log.info("user: " + user);
        log.info("user information was saved: " + userInformation);
    }

    @Override
    public UserInformation getUserInformationForm(HttpServletRequest httpServletRequest) {
        log.info("retrieving user info ...");
        String email = userService.getEmailFromToken(httpServletRequest);
        UserEntity user = userService.findByAuthDataEmail(email);
        log.info("getting screening form for email " + email + ": " + user);
        log.info("user info: " + user.getUserInformation());
        return user.getUserInformation();
    }

    @Override
    public void fillMajors(Majors majors, HttpServletRequest httpServletRequest) {
        log.info("filling majors ...");
        String email = userService.getEmailFromToken(httpServletRequest);
        UserEntity user = userService.findByAuthDataEmail(email);
        List<SubjectType> subjectList = user.getSubjectTypeList();
        for (int in: majors.getMajors()) {
            switch (in) {
                case 0: {
                    if (!subjectList.contains(SubjectType.MATH)) {subjectList.add(SubjectType.MATH);}
                    break;
                }
                case 1: {
                    if (!subjectList.contains(SubjectType.PHYSICS)) {subjectList.add(SubjectType.PHYSICS);}
                    break;
                }
                case 2: {
                    if (!subjectList.contains(SubjectType.CHEMISTRY)) {subjectList.add(SubjectType.CHEMISTRY);}
                    break;
                }
                case 3: {
                    if (!subjectList.contains(SubjectType.BIOLOGY)) {subjectList.add(SubjectType.BIOLOGY);}
                    break;
                }
                case 4: {
                    if (!subjectList.contains(SubjectType.INFORMATICS)) {subjectList.add(SubjectType.INFORMATICS);}
                    break;
                }
                case 5: {
                    if (!subjectList.contains(SubjectType.HISTORY)) {subjectList.add(SubjectType.HISTORY);}
                    break;
                }
                default: throw new AccountBadRequest("major with id " + in + " does not match any subject");
            }
        }
        user.setSubjectTypeList(subjectList);
        userService.saveUser(user);
        log.info("majors were filled");
    }

    @Override
    public void changeFullName(String fullName, HttpServletRequest httpServletRequest) {
        log.info("changing full name ...");
        String email = userService.getEmailFromToken(httpServletRequest);
        UserEntity user = userService.findByAuthDataEmail(email);
        user.setFullName(fullName);
        userService.saveUser(user);
        log.info("full name was changed");
    }
}
