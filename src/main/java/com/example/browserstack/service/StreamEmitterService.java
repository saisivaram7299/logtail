package com.example.browserstack.service;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author Sivaram
 */
@Data
@Slf4j
@Service
public class StreamEmitterService {

    private final List<SseEmitter> dataTunnels = new CopyOnWriteArrayList<>();

    public SseEmitter openNewTunnel() {
        SseEmitter sseEmitter = new SseEmitter(Long.MAX_VALUE);
        dataTunnels.add(sseEmitter);
        return sseEmitter;
    }


    public void sendDataToSseEmitter(SseEmitter.SseEventBuilder data) {

        dataTunnels.forEach((stream) -> {
            try {
                stream.send(data);
            } catch (IOException e) {
                log.error("Failed to send data to emitter");

            }
        });
    }


}
