package com.example.mycli.services;

import com.example.mycli.model.Majors;
import com.example.mycli.model.ScreeningRequest;
import com.example.mycli.model.SubjectType;
import com.example.mycli.model.UserInfoResponse;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface UserInformationService {
    void fillUserInformationForm(ScreeningRequest screeningRequest, HttpServletRequest httpServletRequest);
    UserInfoResponse getUserInformationForm(HttpServletRequest httpServletRequest);

    void fillMajors(Majors majors, HttpServletRequest httpServletRequest);

    void changeFullName(String fullName, HttpServletRequest httpServletRequest);

    List<Integer> getMajors(HttpServletRequest httpServletRequest);


    String getFullName(HttpServletRequest httpServletRequest);
}
