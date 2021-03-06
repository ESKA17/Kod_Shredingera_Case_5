package com.example.mycli.repository;

import com.example.mycli.entity.AuthData;
import com.example.mycli.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthDataRepo extends JpaRepository<AuthData, Long> {

    AuthData findByEmail(String login);

    void deleteById(Long id);


}
