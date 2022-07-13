package com.example.mycli.controllers;

import com.example.mycli.entity.UserEntity;
import com.example.mycli.model.FilterSearchRequest;
import com.example.mycli.model.FindAllMentors;
import com.example.mycli.model.FindAllReturnIdWrap;
import com.example.mycli.model.FindUserByIDWrap;
import com.example.mycli.services.UserService;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin("http://localhost:3000")
@RequiredArgsConstructor
@RequestMapping("/filter_search")
public class FilterController {

    private final UserService userService;

    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public FindAllReturnIdWrap showAll() {
        return userService.findAllReturnID();
    }

    @GetMapping("/get_user_by_id")
    public FindUserByIDWrap getUser(Long id) {
        return userService.findUserByIDInWrap(id);
    }

    @GetMapping("/by_filter")
    public List<Long> filterSearch(@RequestBody FilterSearchRequest filterSearchRequest) {
        return userService.filter(filterSearchRequest);
    }
    @GetMapping(value= "/allMentors")
    public List<UserEntity> showMentors(){
        return userService.findAllMentors();
    }
}
