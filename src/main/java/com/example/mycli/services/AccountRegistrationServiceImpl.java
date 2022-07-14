package com.example.mycli.services;

import com.example.mycli.entity.AuthData;
import com.example.mycli.entity.RoleEntity;
import com.example.mycli.exceptions.AccountBadRequest;
import com.example.mycli.exceptions.AccountConflict;
import com.example.mycli.entity.UserEntity;
import com.example.mycli.exceptions.AccountNotFound;
import com.example.mycli.exceptions.RoleNotFound;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import net.bytebuddy.utility.RandomString;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.transaction.Transactional;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

@RequiredArgsConstructor
@Service
@Log
@EnableAsync
public class AccountRegistrationServiceImpl implements AccountRegistrationService{

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JavaMailSender mailSender;

    @Override
    public void registerAccount(String email, String password, String fullName, int role) {
        log.info("account registration started ...");
        if (email.isEmpty() || password.isEmpty()) {
            throw new AccountBadRequest("check login/password");
        }
        UserEntity userEntityAuthData= userService.checkByAuthDataEmail(email);
        if (userEntityAuthData != null && userEntityAuthData.getAuthdata() != null) {
            throw new AccountConflict(email);
        }
        String roleName;
        if (role == 1) {
            log.info("role mentor");
            roleName = "ROLE_MENTOR";
        } else if (role == 2) {
            log.info("role mentee");
            roleName = "ROLE_MENTEE";
        } else {
            throw new RoleNotFound(role);
        }
        String randomCode = RandomString.make(64);
        RoleEntity userRoleEntity = userService.findRoleEntityByName(roleName);
        AuthData authData = AuthData.builder()
                .email(email)
                .password(passwordEncoder.encode(password))
                .roleEntity(userRoleEntity)
                .firstTime(true)
                .build();
        UserEntity user = UserEntity.builder()
                .fullName(fullName)
                .active(false)
                .verificationCode(randomCode)
                .authdata(authData)
                .build();
        UserEntity userEntity = userService.saveUser(user);
        log.info("user created: " + userEntity);
    }

    @Override
    @Transactional
    @Async
    public void twoStepVerificationEmail(String email, String siteURL)
            throws MessagingException, UnsupportedEncodingException {
        if (email.isEmpty()) {
            throw new AccountBadRequest("check email");
        }
        UserEntity user = userService.findByEmail(email);
        log.info("sending email started ...");
        String toAddress = user.getAuthdata().getEmail();
        String fromAddress = "test.spring.test@mail.ru";
        String senderName = "Mentorship Alumni NIS.";
        String subject = "Please verify your registration";
        String content = "Dear [[name]],<br>"
                + "Please click the link below to verify your registration:<br>"
                + "<h3><a href=\"[[URL]]\" target=\"_self\">VERIFY</a></h3>"
                + "Thank you,<br>"
                + "Mentorship Alumni NIS.";

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom(fromAddress, senderName);
        helper.setTo(toAddress);
        helper.setSubject(subject);

        content = content.replace("[[name]]", user.getFullName());
        String verifyURL = siteURL + "/verify?code=" + user.getVerificationCode();
        content = content.replace("[[URL]]", verifyURL);

        helper.setText(content, true);
        mailSender.send(message);
        log.info("email is sent");
    }

    @Override
    public boolean verifyUser(String code) {
        log.info("verification started ...");
        UserEntity user = userService.findByVerificationCode(code);
        if (user == null ) {
            log.info("no user was found");
            throw new AccountNotFound("account with verification code " + code);
        } else if (user.getActive()) {
            log.info("user is already active");
            return false;
        } else {
            user.setVerificationCode(null);
            user.setActive(true);
            userService.saveUser(user);
            log.info("successful verification");
            return true;
        }
    }

}
