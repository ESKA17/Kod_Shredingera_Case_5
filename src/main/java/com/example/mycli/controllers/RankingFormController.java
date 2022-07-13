package com.example.mycli.controllers;

import com.example.mycli.entity.Ranking;
import com.example.mycli.model.RankingForm;
import com.example.mycli.services.RankingService;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@CrossOrigin("http://localhost:3000")
@RequestMapping("/ranking_form")
@RequiredArgsConstructor
public class RankingFormController {
    private final RankingService rankingService;

    @PostMapping()
    public ResponseEntity<String> fillRankingForm(@RequestBody @Valid RankingForm rankingForm,
                                                  HttpServletRequest httpServletRequest) {
        rankingService.fillRankingForm(rankingForm, httpServletRequest);
        return ResponseEntity.ok("Form was successfully filled");
    }
    @GetMapping()
    public Ranking getRankingForm(HttpServletRequest httpServletRequest) {
        return rankingService.getRankingForm(httpServletRequest);
    }

}
