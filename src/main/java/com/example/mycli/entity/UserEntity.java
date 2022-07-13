package com.example.mycli.entity;

import com.example.mycli.model.SubjectType;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "user_table")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_en")
    @SequenceGenerator(name = "user_en", sequenceName = "user_en", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "full_name")
    private String fullName;

    @OneToOne
    @JoinColumn(name = "auth_data")
    private AuthData authdata;

    @ElementCollection
    @Column(name = "subjects")
    @Enumerated(EnumType.STRING)
    private List<SubjectType> subjectTypeList;

    @OneToOne
    @JoinColumn(name = "user_information")
    private UserInformation userInformation;

    @OneToOne
    private Ranking ranking;

    @Column(name = "active")
    private Boolean active;

    @Column(name = "verification_code", length = 64)
    private String verificationCode;

    @Override
    public String toString() {
        return "UserEntity{" +
                "id=" + id +
                ", fullName='" + fullName + '\'' +
                '}';
    }
}
