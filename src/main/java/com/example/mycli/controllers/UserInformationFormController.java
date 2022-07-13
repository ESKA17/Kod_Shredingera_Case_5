package com.example.mycli.controllers;

import com.example.mycli.entity.UserInformation;
import com.example.mycli.model.Majors;
import com.example.mycli.model.UserInfoResponse;
import com.example.mycli.services.UserInformationService;
import com.example.mycli.model.ScreeningRequest;
import com.example.mycli.services.UserService;
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
@RequestMapping("/user_info")
//@SecurityRequirement(name = "basicauth")

@CrossOrigin(origins = "http://localhost:3000")
public class UserInformationFormController {
    private final UserInformationService userInformationService;
    private final UserService userService;
    @PostMapping()
    public ResponseEntity<String> fillScreeningForm(@RequestBody @Valid ScreeningRequest screeningRequest,
                                            HttpServletRequest httpServletRequest) {
        userInformationService.fillUserInformationForm(screeningRequest, httpServletRequest);
        return ResponseEntity.ok("Form was successfully filled");
    }

    @PostMapping("/majors")
    public ResponseEntity<String> fillMajors(@RequestBody @Valid Majors majors,
                                                    HttpServletRequest httpServletRequest) {
        userInformationService.fillMajors(majors, httpServletRequest);
        return ResponseEntity.ok("Majors were successfully filled");
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public UserInfoResponse getScreeningForm(HttpServletRequest httpServletRequest) {
        return userInformationService.getUserInformationForm(httpServletRequest);
    }

    @GetMapping("/majors")
    public List<Integer> getMajors(HttpServletRequest httpServletRequest) {
        return userInformationService.getMajors(httpServletRequest);
    }

    @PostMapping("/name")
    public ResponseEntity<String> changeFullName(@RequestParam String fullName,
                                             HttpServletRequest httpServletRequest) {
        userInformationService.changeFullName(fullName, httpServletRequest);
        return ResponseEntity.ok("Name was successfully changed");
    }
    @GetMapping("/name")
    public ResponseEntity<String> getFullName(HttpServletRequest httpServletRequest) {
        String fullName = userInformationService.getFullName(httpServletRequest);
        return ResponseEntity.ok(fullName);
    }
    @GetMapping("/get_role")
    public ResponseEntity<Integer> getRoleByName(HttpServletRequest httpServletRequest){
        int roleID = userService.findRoleEntity(httpServletRequest);
        return ResponseEntity.ok(roleID);
    }

}
