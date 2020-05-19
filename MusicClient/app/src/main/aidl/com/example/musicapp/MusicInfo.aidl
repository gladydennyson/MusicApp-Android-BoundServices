package com.example.musicapp;
import com.example.musicclient.SongInfo;
interface MusicInfo {

     List<SongInfo> getAllInfo();
     SongInfo getSpecificSongInfo(int selectedSong);
     String getSongURL(int selectedSong);

}