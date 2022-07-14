package com.example.mycli.model;

import lombok.*;

import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class NewsResponse {
    private Long posterID;
    private List<Integer> subjects;
    private String fullName;
    private LocalDate date;
    private String news;
}
