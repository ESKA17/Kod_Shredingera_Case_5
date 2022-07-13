package com.example.mycli.services;

import com.example.mycli.entity.Ranking;
import com.example.mycli.model.RankingForm;

import javax.servlet.http.HttpServletRequest;

public interface RankingService {
    void fillRankingForm(RankingForm rankingForm, HttpServletRequest httpServletRequest);

    Ranking getRankingForm(HttpServletRequest httpServletRequest);
}
