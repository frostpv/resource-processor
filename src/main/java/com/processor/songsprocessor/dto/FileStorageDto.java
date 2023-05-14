package com.processor.songsprocessor.dto;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class FileStorageDto {
    private Long id;
    private String name;
    private String generatedId;
}
