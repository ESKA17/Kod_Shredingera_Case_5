package com.example.mycli.services;

import com.example.mycli.entity.Connection;
import com.example.mycli.entity.UserEntity;
import com.example.mycli.exceptions.AccountBadRequest;
import com.example.mycli.exceptions.AccountNotFound;
import com.example.mycli.model.FindAllReturnIdWrap;
import com.example.mycli.repository.ConnectionRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ConnectionsServiceImpl implements ConnectionsService {
    private final UserService userService;
    private final EmitterService emitterService;
    private final ConnectionRepo connectionRepo;
    @Override
    public void matchFromMentee(Long matchID, HttpServletRequest httpServletRequest) {
        if (matchID == null) {
            throw new AccountBadRequest("check matchID");
        }
        log.info("starting matching ...");
        String email = userService.getEmailFromToken(httpServletRequest);
        UserEntity poster = userService.findByAuthDataEmail(email);
        Connection connection = Connection.builder()
                .userID(poster.getId())
                .friendID(matchID)
                .sseEmitter(emitterService.addEmitter())
                .connectionStatus(1)
                .messageHistory("")
                .build();
        connectionRepo.save(connection);
        log.info("users matched \u2665\u2665\u2665");
    }

    @Override
    public void breakMatch(Long matchID, HttpServletRequest httpServletRequest) {
        log.info("breaking match ...");
        String email = userService.getEmailFromToken(httpServletRequest);
        UserEntity poster = userService.findByAuthDataEmail(email);
        matchBreaker(matchID, poster);
    }

    @Override
    public void breakMatchByID(Long posterID, Long accepterID) {
        log.info("breaking match by id ...");
        if (posterID == null) {
            throw new AccountBadRequest("check matchID");
        }
        UserEntity poster = userService.findUserByID(posterID);
        matchBreaker(accepterID, poster);
    }

    @Override
    public FindAllReturnIdWrap getConnectionsStatusOne(HttpServletRequest httpServletRequest) {
        log.info("retrieving connections status one ...");
        String email = userService.getEmailFromToken(httpServletRequest);
        UserEntity userEntity = userService.findByAuthDataEmail(email);
        List<Connection> connectionList = connectionRepo.findAllByFriendIDAndConnectionStatus(
                userEntity.getId(), 1);
        List<Long> out = new ArrayList<>();
        for (Connection connection: connectionList) {
            out.add(connection.getFriendID());
        }
        FindAllReturnIdWrap findAllReturnIdWrap = new FindAllReturnIdWrap(out);
        log.info("connections with status 1 were retrieved: " + connectionList);
        return findAllReturnIdWrap;
    }

    @Override
    public FindAllReturnIdWrap getConnectionsStatusTwo(HttpServletRequest httpServletRequest) {
        log.info("retrieving connections status two ...");
        String email = userService.getEmailFromToken(httpServletRequest);
        UserEntity userEntity = userService.findByAuthDataEmail(email);
        List<Connection> connectionList = connectionRepo.findAllByFriendIDAndConnectionStatus(
                userEntity.getId(), 2);
        List<Long> out = new ArrayList<>();
        for (Connection connection: connectionList) {
            out.add(connection.getFriendID());
        }
        FindAllReturnIdWrap findAllReturnIdWrap = new FindAllReturnIdWrap(out);
        log.info("connections with status 2 were retrieved: " + out);
        return findAllReturnIdWrap;
    }

    @Override
    public void matchFromMentor(Long matchID, HttpServletRequest httpServletRequest) {
        if (matchID == null) {
            throw new AccountBadRequest("check matchID");
        }
        log.info("starting matching ...");
        String email = userService.getEmailFromToken(httpServletRequest);
        UserEntity poster = userService.findByAuthDataEmail(email);
        UserEntity accepter =  userService.findUserByID(matchID);
        Connection connection = connectionRepo.findByFriendIDAndUserID(matchID, poster.getId())
                .orElseThrow(() -> new AccountNotFound("connection between users "+ matchID + " and " +
                        poster.getId() + " was not found"));
        connection.setConnectionStatus(2);
        connectionRepo.save(connection);
        log.info("users matched \u2665\u2665\u2665");
    }


    private void matchBreaker(Long accepterID, UserEntity poster) {
        if (accepterID == null) {
            throw new AccountBadRequest("check matchID");
        }
        Connection connection = connectionRepo.findByFriendIDAndUserID(accepterID, poster.getId()).orElseThrow(
                () -> new AccountNotFound("emitter between users "+ accepterID + " and " +  poster.getId() +
                        " was not found")
        );
        connectionRepo.delete(connection);
        log.info("users unmatched(((: " + accepterID + " and " + poster.getId());
    }
}
