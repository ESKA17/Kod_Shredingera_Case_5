package com.example.mycli.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
@Setter
public class ScreeningRequest {
    @NotNull
    @NotEmpty
    private String city;
    @NotNull
    @NotEmpty
    private String school;
    @NotNull
    @NotEmpty
    private String IIN;
    @NotNull
    @NotEmpty
    private String phoneNumber;
    @NotNull
    @NotEmpty
    private String university;
    @NotNull
    @NotEmpty
    private String dateOfBirth;
    @NotNull
    @NotEmpty
    private String aboutMe;
}
