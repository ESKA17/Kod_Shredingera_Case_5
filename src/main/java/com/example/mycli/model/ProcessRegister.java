package com.example.mycli.model;

import lombok.*;
import org.springframework.stereotype.Service;

import javax.validation.constraints.Email;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ProcessRegister {
    @Email
    private String email;
}
