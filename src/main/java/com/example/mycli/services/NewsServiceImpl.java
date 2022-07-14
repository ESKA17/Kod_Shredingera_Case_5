package com.example.mycli.services;

import com.example.mycli.entity.Connection;
import com.example.mycli.entity.News;
import com.example.mycli.entity.UserEntity;
import com.example.mycli.exceptions.AccountBadRequest;
import com.example.mycli.exceptions.AccountNotFound;
import com.example.mycli.model.JSONNewsWrap;
import com.example.mycli.model.NewsResponse;
import com.example.mycli.model.SubjectType;
import com.example.mycli.repository.ConnectionRepo;
import com.example.mycli.repository.NewsRepo;
import com.example.mycli.utils.Utils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class NewsServiceImpl implements NewsService{
    private final UserService userService;
    private final NewsRepo newsRepo;
    private final EmitterService emitterService;
    private final ConnectionRepo connectionRepo;
    @Override
    @Transactional
    public void addNews(String news, HttpServletRequest httpServletRequest) {
        log.info("adding new news ...");
        if (news.isEmpty()) {
            throw new AccountBadRequest("no news");
        }
        String email = userService.getEmailFromToken(httpServletRequest);
        UserEntity userEntity = userService.findByAuthDataEmail(email);
        News addedNews = News.builder()
                .accepted(false)
                .news(news)
                .date(LocalDate.now())
                .userID(userEntity.getId())
                .finished(false)
                .build();
        newsRepo.save(addedNews);
        log.info("news were saved in repository");
    }

    @Override
    public List<Long> getAllUnacceptedNews() {
        log.info("accessing database for all unaccepted news in int list ...");
        List<News> allNews = newsRepo.findAllByAcceptedIsFalse();
        List<Long> listByID = new ArrayList<>();
        for (News news: allNews) {
            listByID.add(news.getId());
        }
        log.info("news in int list successfully retrieved");
        return listByID;
    }
    @Override
    public List<JSONNewsWrap> getAllUnacceptedNewsJSON() {
        log.info("accessing database for all unaccepted news in JSON ...");
        List<News> allNews = newsRepo.findAllByAcceptedIsFalse();
        List<JSONNewsWrap> jsonNewsWrapList = new ArrayList<>();
        for (News news: allNews) {
            String fullName = userService.findUserByID(news.getUserID()).getFullName();
            JSONNewsWrap jsonNewsWrap = JSONNewsWrap.builder()
                    .date(news.getDate())
                    .fullName(fullName)
                    .news(news.getNews())
                    .build();
            jsonNewsWrapList.add(jsonNewsWrap);
        }
        log.info("news in JSON successfully retrieved");
        return jsonNewsWrapList;
    }

    @Override
    public List<Long> getUnacceptedNewsByOP(HttpServletRequest httpServletRequest) {
        log.info("retrieving unaccepted news by OP ... ");
        String email = userService.getEmailFromToken(httpServletRequest);
        UserEntity userEntity = userService.findByAuthDataEmail(email);
        List<News> allNews = newsRepo.findAllByAcceptedIsFalseAndUserID(userEntity.getId());
        List<Long> listByID = new ArrayList<>();
        for (News news: allNews) {
            listByID.add(news.getId());
        }
        log.info("news successfully retrieved");
        return listByID;
    }

    @Override
    public List<Long> getAcceptedNewsByOP(HttpServletRequest httpServletRequest) {
        log.info("retrieving accepted news by OP ... ");
        String email = userService.getEmailFromToken(httpServletRequest);
        UserEntity userEntity = userService.findByAuthDataEmail(email);
        List<News> allNews = newsRepo.findAllByAcceptedIsTrueAndFinishedIsFalseAndUserID(userEntity.getId());
        List<Long> listByID = new ArrayList<>();
        for (News news: allNews) {
            listByID.add(news.getId());
        }
        log.info("news successfully retrieved");
        return listByID;
    }

    @Override
    public News getNewsByID(Long id) {
        log.info("getting news by id ...");
        if (id == null) {
            throw new AccountBadRequest("check ID");
        }
        News news = newsRepo.findById(id).orElseThrow(() -> new AccountNotFound("news with id: " + id));
        log.info("successfully retrieved by id: " + news);
        return news;
    }

    @Override
    public NewsResponse getNewsResponseByID(Long id) {
        log.info("getting news response by id ...");
        if (id == null) {
            throw new AccountBadRequest("check ID");
        }
        News news = newsRepo.findById(id).orElseThrow(() -> new AccountNotFound("news with id: " + id));
        UserEntity userEntity = userService.findUserByID(news.getUserID());
        List<Integer> listOfSubjects = getMajorsByInt(userEntity.getSubjectTypeList());
        NewsResponse newsResponse = NewsResponse.builder()
                .fullName(userEntity.getFullName())
                .posterID(userEntity.getId())
                .subjects(listOfSubjects)
                .date(news.getDate())
                .news(news.getNews())
                .build();
        log.info("successfully retrieved by id: " + newsResponse);
        return newsResponse;
    }


    @Transactional
    @Override
    public void markAccepted(Long newsID, HttpServletRequest httpServletRequest) {
     log.info("marking news as accepted ...");
        if (newsID == null) {
            throw new AccountBadRequest("check ID");
        }
        String email = userService.getEmailFromToken(httpServletRequest);
        UserEntity user = userService.findByEmail(email);

        News news = getNewsByID(newsID);
        news.setAccepted(true);
        news.setAccepter_id(user.getId());

        UserEntity poster = userService.findUserByID(news.getUserID());
        Connection connection = Connection.builder()
                .userID(poster.getId())
                .friendID(user.getId())
                .sseEmitter(emitterService.addEmitter())
                .connectionStatus(2)
                .messageHistory("")
                .build();
        connectionRepo.save(connection);
        newsRepo.save(news);
     log.info("successfully marked as accepted");
    }
    @Transactional
    @Override
    public void markFinished(Long newsID, HttpServletRequest httpServletRequest) {
        log.info("marking news as finished ...");
        if (newsID == null) {
            throw new AccountBadRequest("check ID");
        }
        News news = getNewsByID(newsID);
        news.setFinished(true);
        newsRepo.save(news);
        log.info("successfully marked as finished");
    }
    @Transactional
    @Override
    public void editNews(Long newsID, String news) {
        log.info("editing news ...");
        if (newsID == null) {
            throw new AccountBadRequest("check ID");
        }
        News newsEdited = newsRepo.findById(newsID).orElseThrow(
                () -> new AccountNotFound("news with id: " + newsID));
        newsEdited.setNews(news);
        newsRepo.save(newsEdited);
        log.info("successfully edited");
    }
    @Override
    public List<Integer> getMajorsByInt(List<SubjectType> subjectList) {
        log.info("getting majors from list of integers ...");
        List<Integer> out = Utils.fromSubjectTypeToInteger(subjectList);
        log.info("majors sent");
        return out;
    }


}
