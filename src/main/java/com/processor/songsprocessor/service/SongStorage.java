package com.processor.songsprocessor.service;

import com.processor.songsprocessor.dto.SongDto;

public interface SongStorage {
   SongDto getSongMetaData(long id, String name);
}
