package com.example.browserstack.controller;

import com.example.browserstack.service.LogFileStreamService;
import com.example.browserstack.service.StreamEmitterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

/**
 * @author Sivaram
 */

@RestController
public class LogDetectionController {

    @Autowired
    LogFileStreamService service;



    @GetMapping(value = "/log" , produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter streamLogFile (){
        return service.createNewSubscriber();
    }
}
