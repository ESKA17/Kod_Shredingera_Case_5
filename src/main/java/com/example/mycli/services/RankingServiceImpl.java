package com.example.mycli.services;

import com.example.mycli.entity.Ranking;
import com.example.mycli.entity.UserEntity;
import com.example.mycli.exceptions.AccountBadRequest;
import com.example.mycli.model.RankingForm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service
@RequiredArgsConstructor
@Slf4j
public class RankingServiceImpl implements RankingService {
    private final UserService userService;
    @Override
    public void fillRankingForm(RankingForm rankingForm, HttpServletRequest httpServletRequest) {
        log.info("filling ranking form ...");
        String email =  userService.getEmailFromToken(httpServletRequest);
        UserEntity userEntity = userService.findByAuthDataEmail(email);
        Ranking ranking = Ranking.builder()
                .comments(rankingForm.getComments())
                .goalsReachingLevel(rankingForm.getGoalsReachingLevel())
                .overallRanking(rankingForm.getOverallRanking())
                .infoGivenAboutProgram(rankingForm.getInfoGivenAboutProgram())
                .satisfactionWithProgram(rankingForm.getSatisfactionWithProgram())
                .userAccessibility(rankingForm.getUserAccessibility())
                .userCommunicationSkills(rankingForm.getUserCommunicationSkills())
                .userKnowledge(rankingForm.getUserKnowledge())
                .build();
        userEntity.setRanking(ranking);
        userService.saveUser(userEntity);
        log.info("ranking was saved: " + ranking);
    }

    @Override
    public Ranking getRankingForm(HttpServletRequest httpServletRequest) {
        log.info("retrieving ranking by jwt ...");
        String email = userService.getEmailFromToken(httpServletRequest);
        Ranking ranking = userService.findByAuthDataEmail(email).getRanking();
        log.info("ranking info was retrieved");
        return ranking;
    }
}
