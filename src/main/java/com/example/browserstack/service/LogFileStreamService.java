package com.example.browserstack.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author Sivaram
 */

@Service
@Slf4j
public class LogFileStreamService {


    private final StreamEmitterService streamEmitterService;

    private static final AtomicLong linesCheck = new AtomicLong(0);

    public LogFileStreamService(StreamEmitterService streamEmitterService , WatcherService watcherService) {
        this.streamEmitterService = streamEmitterService;
        watcherService.listener(x->{
//            FileInputStream inputStream = null;
//            Scanner sc = null;
//            int count =0 ;
//            List<String> newLines = new ArrayList<>();
//            try {
//                inputStream = new FileInputStream(x.toFile());
//                sc = new Scanner(inputStream, "UTF-8");
//                while (sc.hasNextLine() ) {
//                    if(count>= linesCheck.intValue()){
//                        String line = sc.nextLine();
//                        newLines.add(line);
//                    }
//                    count ++;
//                    linesCheck.incrementAndGet();
//                }
//                count= 0;
//                newLines = Collections.emptyList();
//                for(int i =0 ; i < newLines.size(); i++){
//                    streamEmitterService.sendDataToSseEmitter(SseEmitter.event().id(String.valueOf(linesCheck.intValue() - i))
//                            .data(newLines.get(i)));
//
//                }
//                // note that Scanner suppresses exceptions
//                if (sc.ioException() != null) {
//                    throw sc.ioException();
//                }
//
//
//            }
//            catch (Exception ex){
//                log.error("reading issue");
//            }
//            finally {
//                if (inputStream != null) {
//                    try {
//                        inputStream.close();
//                    } catch (IOException e) {
//                        throw new RuntimeException(e);
//                    }
//                }
//                if (sc != null) {
//                    sc.close();
//                }
//            }
//            // read file -->from p

            try {

                Files.lines(x).skip(linesCheck.get()).forEach(line ->streamEmitterService.sendDataToSseEmitter( SseEmitter.event().id(String.valueOf(linesCheck.incrementAndGet())).data(line)) );
            }
            catch (Exception ex){

                log.error("error occuresd");
            }
        });

    }

    public SseEmitter createNewSubscriber() {
       return streamEmitterService.openNewTunnel();
    }
}
