package com.example.mycli.controllers;

import com.example.mycli.entity.News;
import com.example.mycli.entity.Report;
import com.example.mycli.model.MessageReport;
import com.example.mycli.model.NewsRequest;
import com.example.mycli.model.ReportRequest;
import com.example.mycli.services.AccountDeactivationService;
import com.example.mycli.services.ConnectionsService;
import com.example.mycli.services.NewsService;
import com.example.mycli.services.ReportsService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.UnsupportedEncodingException;
import java.util.List;

@RestController
@RequiredArgsConstructor
//@SecurityRequirement(name = "basicauth")

@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/reports")
public class ReportsController {
    private final ReportsService reportsService;

    private final AccountDeactivationService accountDeactivationService;

    @PostMapping("/report_by_id")
    public ResponseEntity<String> report(@RequestBody @Valid ReportRequest reportRequest,
                                         HttpServletRequest httpServletRequest)
            throws MessagingException, UnsupportedEncodingException {
        reportsService.reportPerson(reportRequest.getReportedPersonId(), reportRequest.getReason(), httpServletRequest);
        reportsService.sendingNotificationReport();
        return ResponseEntity.ok(reportRequest.getReportedPerson() +" is reported");
    }

    @PostMapping("/mark_ignore")
    public ResponseEntity<String> ignoreReport(@RequestParam Long reportId, HttpServletRequest httpServletRequest){
        reportsService.reportIgnore(reportId, httpServletRequest);
        return ResponseEntity.ok(reportId + " is ignored");
    }
    @GetMapping("/by_id")
    public Report getReport(@RequestParam Long reportId){
        return reportsService.getReportById(reportId);
   }
    @GetMapping("/get_all_reports")
    public List<Long> getAllReports(){
        return reportsService.getReportsAll();
    }
    @PostMapping("/deactivate_account_by_email")
    public ResponseEntity<String> deactivateAccountByEmail(@RequestParam String email){
        accountDeactivationService.deactivateAccount(email);

        return ResponseEntity.ok("Account "+ email + "is deactivated");
    }

}
