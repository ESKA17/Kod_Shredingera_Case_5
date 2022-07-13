package com.example.mycli.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
@Getter
@Setter
public class AuthRequest {
    @Email
    private String email;
    @NotNull
    @NotEmpty
    private String password;
}
