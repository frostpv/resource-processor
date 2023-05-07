package com.processor.songsprocessor.component;

import com.processor.songsprocessor.config.MqConfig;
import com.processor.songsprocessor.dto.SongDto;
import com.processor.songsprocessor.model.RabbitMessage;
import com.processor.songsprocessor.service.SongService;
import com.processor.songsprocessor.service.SongStorage;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

@Component
public class SongStorageListener {

    @Autowired
    SongStorage songStorage;

    @Autowired
    SongService songService;

    @RabbitListener(queues = MqConfig.QUEUE_NAME)
    @Retryable(value = RuntimeException.class, maxAttempts = 3, backoff = @Backoff(delay = 3000))
    public void listener(RabbitMessage rabbitMessage) {
        SongDto execute = tryGetMetaDataFromStorage(rabbitMessage);
    }

    @Recover
    public void listenerIsDown(){
    }

    private SongDto tryGetMetaDataFromStorage(RabbitMessage rabbitMessage) {
        SongDto songMetaData = songStorage.getSongMetaData(rabbitMessage.getId(), rabbitMessage.getName());
        return songService.saveSongMeta(songMetaData);
    }

}
