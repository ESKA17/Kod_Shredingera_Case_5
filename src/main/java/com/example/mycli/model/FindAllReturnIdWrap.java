package com.example.mycli.model;

import lombok.*;

import java.time.LocalDate;
import java.util.List;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class FindAllReturnIdWrap {

    private List<Long> Ids;

}