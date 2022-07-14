package com.example.mycli.model;

import com.example.mycli.entity.UserInformation;
import lombok.*;

import javax.persistence.Column;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UserInfoResponse {
    private String fullName;
    private String city;
    private String school;
    private String IIN;
    private String phoneNumber;
    private String university;
    private LocalDate dateOfBirth;
    private String aboutMe;
    private Integer roleID;
}
