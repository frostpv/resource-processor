package com.processor.songsprocessor.service;

import com.processor.songsprocessor.dto.SongDto;

public interface SongService {
    SongDto saveSongMeta(SongDto songDto);
}
