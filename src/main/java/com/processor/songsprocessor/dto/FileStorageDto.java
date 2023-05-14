package com.processor.songsprocessor.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class FileStorageDto {
    private Long id;
    private String name;
    private String generatedId;
}
