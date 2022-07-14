package com.example.mycli.web;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.Serializable;

public class SerializableSSE extends SseEmitter implements Serializable {

    public SerializableSSE() {
    }

    public SerializableSSE(Long timeout) {
        super(timeout);
    }
}
