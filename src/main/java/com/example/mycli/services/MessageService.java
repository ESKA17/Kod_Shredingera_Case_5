package com.example.mycli.services;

import com.example.mycli.entity.Connection;

import javax.servlet.http.HttpServletRequest;

public interface MessageService {

    Connection getAllMessages(Long friendID, HttpServletRequest httpServletRequest);

    void saveMessage(Long toWhom, String message, HttpServletRequest httpServletRequest);
}
