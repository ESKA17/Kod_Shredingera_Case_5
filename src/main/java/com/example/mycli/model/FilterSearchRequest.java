package com.example.mycli.model;

import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class FilterSearchRequest {
    private List<Integer> subjects;
}
