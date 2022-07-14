package com.example.mycli.services;

import com.example.mycli.entity.Connection;
import com.example.mycli.entity.Report;
import com.example.mycli.entity.UserEntity;
import com.example.mycli.exceptions.AccountBadRequest;
import com.example.mycli.exceptions.AccountNotFound;
import com.example.mycli.repository.ConnectionRepo;
import com.example.mycli.repository.ReportsRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ReportsServiceImpl implements ReportsService{
    private final UserService userService;
    private final ReportsRepo reportsRepo;
    private final ConnectionsService connectionsService;
    private final ConnectionRepo connectionRepo;
    private final JavaMailSender mailSender;
    @Override
    @Transactional
    public void reportPerson(Long harasserId, String reason, HttpServletRequest httpServletRequest) {
        log.info("reporting person ...");
        String email = userService.getEmailFromToken(httpServletRequest);
        UserEntity reporter = userService.findByEmail(email);
        UserEntity harasser = userService.findUserByID(harasserId);
        connectionsService.breakMatchByID(reporter.getId(), harasser.getId());
        Connection connection = connectionRepo.findByFriendIDAndUserID(harasserId, reporter.getId())
                .orElseThrow(() -> new AccountNotFound("emitter between users "+ harasserId + " and " +
                        reporter.getId() + " was not found"));
        Report newReport = Report.builder()
                .ignore(false)
                .reason(reason)
                .connection(connection)
                .build();
        reportsRepo.save(newReport);
        log.info("report is saved ...");
    }

    @Override
    @Transactional
    @Async
    public void sendingNotificationReport()
            throws MessagingException, UnsupportedEncodingException {
        log.info("sending email started ...");
        List<UserEntity> listOfAdmins = userService.findAdmins();
        String fromAddress = "test.spring.test@mail.ru";
        String senderName = "Mentorship Alumni NIS.";
        String subject = "You got new report";
        String content = "Dear [[name]],<br>"
                + "You have new report to be judged:<br>"
                + "Thank you,<br>"
                + "Mentorship Alumni NIS.";

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom(fromAddress, senderName);

        helper.setSubject(subject);


        helper.setText(content, true);

        for(UserEntity admin:listOfAdmins){
            String toAddress = admin.getAuthdata().getEmail();
            helper.setTo(toAddress);
            mailSender.send(message);
        }
        log.info("emails are sent");
    }

    @Override
    public void reportIgnore(Long reportId, HttpServletRequest httpServletRequest){
        log.info("ignoring report ...");
        if (reportId == null) {
            throw new AccountBadRequest("check ID");
        }
        String email = userService.getEmailFromToken(httpServletRequest);
        UserEntity harasser = userService.findByEmail(email);

        Report report = getReportById(reportId);
        report.setIgnore(true);
        reportsRepo.save(report);
        log.info(reportId + "report is ignored ");
    }

    @Override
    @Transactional
    public Report getReportById(Long reportId){
        log.info("getting report by id ...");
        if (reportId == null) {
            throw new AccountBadRequest("check ID");
        }
        Report report = reportsRepo.findById(reportId).orElseThrow(() -> new AccountNotFound("report with id: " + reportId));
        log.info("report successfully retrieved by id");
        return report;
    }
    @Override
    public List<Long> getReportsAll(){
        log.info("accessing database for all reports");

        List<Report> allReports = reportsRepo.findAll();
        List<Long> listById = new ArrayList<>();
        for( Report report : allReports){
            listById.add(report.getId());
        }
        log.info("reports are retrieved");
        return listById;
    }


}
