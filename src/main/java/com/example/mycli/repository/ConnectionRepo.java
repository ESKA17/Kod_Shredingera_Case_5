package com.example.mycli.repository;

import com.example.mycli.entity.Connection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface ConnectionRepo extends JpaRepository<Connection, Long> {
    Optional<Connection> findByFriendIDAndUserID(Long toWhom, Long from);
}