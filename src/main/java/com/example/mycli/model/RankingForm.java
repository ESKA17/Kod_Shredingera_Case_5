package com.example.mycli.model;

import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class RankingForm {
    @NotNull
    private Integer overallRanking;
    @NotNull
    private Integer infoGivenAboutProgram;
    @NotNull
    private Integer userAccessibility;
    @NotNull
    private Integer userKnowledge;
    @NotNull
    private Integer userCommunicationSkills;
    @NotNull
    private Integer satisfactionWithProgram;
    @NotNull
    private Integer goalsReachingLevel;
    @NotNull
    @NotEmpty
    private String comments;
}
