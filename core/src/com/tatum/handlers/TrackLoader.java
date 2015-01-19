package com.tatum.handlers;

import com.tatum.music.TrackData;

public class TrackLoader {
    private ContentManager contentManager;
    private TrackData trackData;
    public TrackLoader(ContentManager contentManager){
        this.contentManager = contentManager;
    }
    public boolean loadTrack(String path){
        try{
            contentManager.loadMusic(path);
        } catch (Exception e){
            return false;
        }
        return true;
    }
}
