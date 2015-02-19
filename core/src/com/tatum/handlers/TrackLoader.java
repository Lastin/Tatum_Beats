package com.tatum.handlers;

import com.badlogic.gdx.audio.Music;
import com.tatum.errors.MusicNotFoundException;
import com.tatum.music.TrackData;
import com.tatum.states.Menu;

public class TrackLoader {

    private ContentManager resources;
    private String path;
    private String key;
    private Music music;
    private TrackData trackData;
    private boolean ready = false;
    private boolean uploadingFinished = false;


    public TrackLoader(ContentManager resources, String path) {
        this.resources = resources;
        this.path = path;
        this.key = resources.makeKey(path);
        music = loadMusic();
        if(music == null){
            throw new NullPointerException("Music was not found on given path");
        }
       }

    private Music loadMusic(){
        Music music = resources.getMusic(key);
        if(music == null){
            music = resources.loadMusic(path);
        }
        return music;
    }

    public void loadTrackData(){
        trackData = resources.getTrackData(key);
        if(trackData == null){
            trackData = new TrackData(path);

        }

    }

    public Music getMusic(){
        return music;
    }

    public TrackData getTrackData(){
        return trackData;
    }


}
