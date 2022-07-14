package com.example.mycli.repository;

import com.example.mycli.entity.Ranking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RankingRepo extends JpaRepository<Ranking, Long> {
}
