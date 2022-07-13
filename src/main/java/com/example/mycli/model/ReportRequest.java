package com.example.mycli.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
@Getter
@Setter
public class ReportRequest {
    @NotNull
    @NotEmpty
    private String reason;
    @NotNull
    @NotEmpty
    private String reporter;
    @NotNull
    @NotEmpty
    private String ignore;
    @NotNull
    @NotEmpty
    private String mentor;
    @NotNull
    private Long reportedPersonId;
    @NotNull
    @NotEmpty
    private String reportedPerson;

}
