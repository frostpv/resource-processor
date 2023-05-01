package com.processor.songsprocessor.component;

import com.processor.songsprocessor.config.MqConfig;
import com.processor.songsprocessor.dto.SongDto;
import com.processor.songsprocessor.model.RabbitMessage;
import com.processor.songsprocessor.service.SongService;
import com.processor.songsprocessor.service.SongStorage;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.util.concurrent.CompletableFuture;

@Component
public class SongStorageListener {

    @Autowired
    SongStorage songStorage;

    @Autowired
    Properties properties;

    @Autowired
    SongService songService;

    @RabbitListener(queues = MqConfig.QUEUE_NAME)
    public void Listener(RabbitMessage rabbitMessage) {
        SongDto songDto = null;


            SongDto songMetaData = tryGetMetaDataFromStorage(rabbitMessage);
            songDto = songService.saveSongMeta(songMetaData);


        System.out.println(songDto);
    }

    private SongDto tryGetMetaDataFromStorage(RabbitMessage rabbitMessage) {



            return songStorage.getSongMetaData(rabbitMessage.getId(), rabbitMessage.getName());

    }


}
