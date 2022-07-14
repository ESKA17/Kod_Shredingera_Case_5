package com.example.mycli.repository;

import com.example.mycli.entity.Connection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface ConnectionRepo extends JpaRepository<Connection, Long> {
    Optional<Connection> findByFriendIDAndUserID(Long toWhom, Long from);
    List<Connection> findAllByUserIDAndConnectionStatus(Long userID, int status);
    List<Connection> findAllByFriendIDAndConnectionStatus(Long userID, int status);
}
