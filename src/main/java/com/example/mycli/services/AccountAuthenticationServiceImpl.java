package com.example.mycli.services;

import com.example.mycli.entity.UserEntity;
import com.example.mycli.exceptions.AccountBadRequest;
import com.example.mycli.web.JwtProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;

@RequiredArgsConstructor
@Service
@Log
public class AccountAuthenticationServiceImpl implements AccountAuthenticationService{
    private final UserService userService;
    private final JwtProvider jwtProvider;

    @Override
    public String authenticateAccount(String email, String password,
                                      HttpServletResponse httpServletResponse) {
        log.info("account authentication ...");
        if (email.isEmpty() || password.isEmpty()) {
            throw new AccountBadRequest("check login/password");
        }
        UserEntity userEntity = userService.findByLoginAndPassword(email, password);
        String token = null;
        if (userEntity != null) {
            token = jwtProvider.generateToken(email);
            httpServletResponse.addHeader("token", token);
            log.info("Token: " + token);
            log.info("successful authentication");

        }
        return token;
    }
}
