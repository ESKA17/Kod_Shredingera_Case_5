package com.example.mycli.controllers;

import com.example.mycli.entity.News;
import com.example.mycli.model.JSONNewsWrap;
import com.example.mycli.model.NewsRequest;
import com.example.mycli.model.NewsResponse;
import com.example.mycli.services.NewsService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
//@SecurityRequirement(name = "basicauth")
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/news")
public class NewsController {
    private final NewsService newsService;

    @PostMapping()
    public ResponseEntity<String> addNews(@RequestBody @Valid NewsRequest newsRequest,
                                          HttpServletRequest httpServletRequest){
        newsService.addNews(newsRequest.getNews(), httpServletRequest);
        return ResponseEntity.ok("News were posted");
    }

    @GetMapping("/available")
    public List<Long> getAllUnacceptedNews(){
        return newsService.getAllUnacceptedNews();
    }
    @GetMapping("/availableJSON")
    public List<JSONNewsWrap> getAllUnacceptedNewsInJSON(){
        return newsService.getAllUnacceptedNewsJSON();
    }

    @GetMapping("/by_op_unaccepted")
    public List<Long> getUnacceptedNewsByOP(HttpServletRequest httpServletRequest){
        return newsService.getUnacceptedNewsByOP(httpServletRequest);
    }

    @GetMapping("/by_op_accepted")
    public List<Long> getAcceptedNewsByOP(HttpServletRequest httpServletRequest){
        return newsService.getAcceptedNewsByOP(httpServletRequest);
    }

    @GetMapping(value = "/by_id", produces = MediaType.APPLICATION_JSON_VALUE)
    public NewsResponse getNewsByID(@RequestParam Long id){
        return newsService.getNewsResponseByID(id);
    }

    @PostMapping("/mark_accepted")
    public ResponseEntity<String> markAccepted(@RequestParam Long newsID, HttpServletRequest httpServletRequest) {
        newsService.markAccepted(newsID, httpServletRequest);
        return ResponseEntity.ok("marked as accepted");
    }

    @PostMapping("/mark_finished")
    public ResponseEntity<String> markFinished(@RequestParam Long newsID, HttpServletRequest httpServletRequest) {
        newsService.markFinished(newsID, httpServletRequest);
        return ResponseEntity.ok("marked as finished");
    }
    @PostMapping("/edit_news")
    public ResponseEntity<String> editNews(@RequestParam Long newsID, @RequestBody NewsRequest newsRequest) {
        newsService.editNews(newsID, newsRequest.getNews());
        return ResponseEntity.ok("news were edited");
    }
}
