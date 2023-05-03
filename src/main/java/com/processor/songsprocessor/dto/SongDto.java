package com.processor.songsprocessor.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.retry.RetryCallback;

@Getter
@Setter
@ToString
public class SongDto {
    private long id;
    private String name;
    private String artist;
    private String album;
    private String length;
    private int resourceId;
    private int year;
}
