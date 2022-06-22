package com.example.browserstack.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;

/**
 * @author Sivaram
 */

@Service
@Slf4j
public class WatcherService {

    private  final Path fileDirectory;
    private final Path actualFile;

    private final WatchKey watchKey;

    private final List<Consumer<Path>> consumerList = new CopyOnWriteArrayList<>();

    private final ExecutorService executorService =  Executors.newSingleThreadExecutor();





    public WatcherService() throws IOException {
        this.fileDirectory = new FileSystemResource("C:\\Users\\sipappu\\Documents").getFile().toPath();
        this.actualFile = this.fileDirectory.resolve("browserstackapp.log");
        final WatchService watchService = FileSystems.getDefault().newWatchService();
        this.watchKey = fileDirectory.register(watchService ,ENTRY_MODIFY  );

        executorService.submit(this::catchChanges);


    }


    // this is a lambda to invoke the listener
    public void listener (Consumer<Path> path){
        consumerList.add(path);
    }

    private void catchChanges() {


        while (1==1){
            try{
                Thread.sleep(50);

                watchKey.pollEvents().forEach((event)->{
                    Path eventForChangeDetection =fileDirectory.resolve((Path) event.context());

                    if(event.kind() .equals(ENTRY_MODIFY) && eventForChangeDetection.equals(actualFile)){

                        consumerList.forEach((consumer)->{
                            consumer.accept(eventForChangeDetection);
                        });

                    }
                });

            }
            catch (Exception exception){

                log.error("unable to watch the file ");
            }

        }
    }
}
