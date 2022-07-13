package com.example.mycli.repository;

import com.example.mycli.entity.AuthData;
import com.example.mycli.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserEntityRepo extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByAuthdata_Email(String email);

    Optional<UserEntity> findByVerificationCode(String verificationCode);

    List<UserEntity> findAllByAuthdata_RoleEntity_Id(int id);

}
