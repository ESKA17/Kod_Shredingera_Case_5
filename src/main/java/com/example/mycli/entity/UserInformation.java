package com.example.mycli.entity;

import com.example.mycli.model.StudyDegree;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "user_info_table")
public class UserInformation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "city")
    private String city;
    @Column(name = "school" )
    private String school;
    @Column(name = "IIN")
    private String IIN;
    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "university")
    private String university;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Column(name = "about_me")
    private String aboutMe;

    @Override
    public String toString() {
        return "UserInformation{" +
                "id=" + id +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", city='" + city + '\'' +
                ", dateOfBirth=" + dateOfBirth +
                ", IIN=" + IIN +
                '}';
    }
}
