package com.example.mycli.services;

import com.example.mycli.entity.Connection;
import com.example.mycli.entity.UserEntity;
import com.example.mycli.exceptions.AccountBadRequest;
import com.example.mycli.exceptions.AccountNotFound;
import com.example.mycli.repository.ConnectionRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
@Service
@Slf4j
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {
    private final UserService userService;
    private final ConnectionRepo connectionRepo;
    @Override
    public Connection getAllMessages(Long friendID, HttpServletRequest httpServletRequest) {
        String email = userService.getEmailFromToken(httpServletRequest);
        UserEntity user = userService.findByAuthDataEmail(email);
        UserEntity friend = userService.findUserByID(friendID);
        log.info("finding message history for users " + user.getFullName() + " and " + friend.getFullName());
        Connection connection = connectionRepo.findByFriendIDAndUserID(friendID, user.getId())
                .orElseThrow(() -> new AccountNotFound("message history for users " + user.getFullName() +
                        " and " + friend.getFullName()));
        log.info("message history for users " + user.getFullName() + " and " + friend.getFullName() + " was found");
        return connection;
    }

    @Override
    public void saveMessage(Long toWhom, String message, HttpServletRequest httpServletRequest) {
        String email = userService.getEmailFromToken(httpServletRequest);
        UserEntity user = userService.findByAuthDataEmail(email);
        UserEntity friend = userService.findUserByID(toWhom);
        log.info("saving message for users " + user.getFullName() + " and " + friend.getFullName());
        if (toWhom == null || message.isEmpty()) {
            throw new AccountBadRequest(" to whom ID / empty message");
        }
        Connection connection = connectionRepo.findByFriendIDAndUserID(toWhom, user.getId())
                .orElseThrow(() -> new AccountNotFound("connection between users " + user.getFullName() +
                        " and " + friend.getFullName() + " was not found"));
        if (connection.getMessageHistory() == null) {
            connection.setMessageHistory(message);
        } else {
            connection.setMessageHistory(connection.getMessageHistory() + "\n" + message);
        }
        connectionRepo.save(connection);
        log.info("message for users " + user.getFullName() + " and " + friend.getFullName() + " was saved");
    }
}
