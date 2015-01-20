package com.tatum.handlers;

import com.badlogic.gdx.audio.Music;
import com.tatum.music.TrackData;
import com.tatum.states.Menu;

public class TrackLoader {

    private ContentManager resources;
    private String path;
    private String key;
    private Music music;
    private TrackData trackData;
    private boolean ready = false;

    public TrackLoader(ContentManager resources, String path) throws NullPointerException{
        this.resources = resources;
        this.path = path;
        this.key = resources.makeKey(path);
        music = loadMusic();
        if(music != null){
            throw new NullPointerException("Music was not found on given path");
        }
        trackData = loadTrackData();
        ready = true;
    }

    private Music loadMusic(){
        Music music = resources.getMusic(key);
        if(music == null){
            resources.loadMusic(path);
        }
        return music;
    }

    private TrackData loadTrackData(){
        trackData = resources.getTrackData(key);
        if(trackData == null){
            trackData = new TrackData(path);
            trackData.initilize();
        }
        return trackData;
    }

    public Music getMusic(){
        return music;
    }

    public TrackData getTrackData(){
        return trackData;
    }
}
