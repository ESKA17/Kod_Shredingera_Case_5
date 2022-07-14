package com.example.mycli.entity;

import lombok.*;

import javax.persistence.*;
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "auth_table")
public class AuthData {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "id", nullable = false)
        private Long id;

        @Column(name = "email", nullable = false)
        private String email;

        @Column(name = "password", nullable = false)
        private String password;

        @Column(name = "first_time", nullable = false)
        private Boolean firstTime;

        @ManyToOne
        @JoinColumn(name="role_table", nullable = false)
        private RoleEntity roleEntity;


}
