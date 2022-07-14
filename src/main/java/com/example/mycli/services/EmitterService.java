package com.example.mycli.services;

import com.example.mycli.entity.Connection;
import com.example.mycli.web.SerializableSSE;

import javax.servlet.http.HttpServletRequest;

public interface EmitterService {

    SerializableSSE addEmitter();


    void pushMessage(Long toWhom, String message, HttpServletRequest httpServletRequest);

}
