package com.example.mycli.model;

import com.example.mycli.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class FindAllMentors {
    private List<UserEntity> mentors;
}
