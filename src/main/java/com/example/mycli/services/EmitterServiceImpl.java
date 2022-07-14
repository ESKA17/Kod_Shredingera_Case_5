package com.example.mycli.services;

import com.example.mycli.entity.Connection;
import com.example.mycli.entity.UserEntity;
import com.example.mycli.exceptions.AccountNotFound;
import com.example.mycli.exceptions.ServerFail;
import com.example.mycli.model.Message;
import com.example.mycli.repository.ConnectionRepo;
import com.example.mycli.web.SerializableSSE;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmitterServiceImpl implements EmitterService {
    private final UserService userService;
    private  final ConnectionRepo connectionRepo;
    @Override
    public SerializableSSE addEmitter() {
        log.info("subscribing...");
        SerializableSSE sseEmitter = new SerializableSSE(-1L);
        log.info("subscribed");
        return sseEmitter;
    }
    @Override
    public void pushMessage(Long toWhom, String text, HttpServletRequest httpServletRequest) {
        String email = userService.getEmailFromToken(httpServletRequest);
        UserEntity fromUser = userService.findByAuthDataEmail(email);
        UserEntity toUser = userService.findUserByID(toWhom);
        log.info("pushing {} notification for user {}", fromUser.getFullName(), toUser.getFullName());
        Message payload = Message
                .builder()
                .from(fromUser.getFullName())
                .message(text)
                .build();
        Connection connection = connectionRepo.findByFriendIDAndUserID(toWhom, fromUser.getId()).orElseThrow(
                () -> new AccountNotFound("emitter between users "+ toWhom + " and " +  fromUser.getId()));
        SerializableSSE sse = connection.getSseEmitter();
        try {
            sse.send(SerializableSSE
                    .event()
                    .name(toUser.getFullName())
                    .data(payload));
        } catch (IOException e) {
            throw new ServerFail("failed to send message using emitter. Also " + e);
        }
        log.info("everything pushed");
    }
}