package com.example.mycli.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import javax.persistence.Column;
import java.time.LocalDate;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class JSONNewsWrap {

    private String fullName;

    private String news;

    private LocalDate date;

}
