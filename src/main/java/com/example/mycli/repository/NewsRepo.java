package com.example.mycli.repository;

import com.example.mycli.entity.AuthData;
import com.example.mycli.entity.News;
import com.example.mycli.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NewsRepo extends JpaRepository<News, Long> {
    List<News> findAllByAcceptedIsFalse();
    List<News> findAllByAcceptedIsFalseAndUserID(Long userID);
    List<News> findAllByAcceptedIsTrueAndFinishedIsFalseAndUserID(Long userID);

}
