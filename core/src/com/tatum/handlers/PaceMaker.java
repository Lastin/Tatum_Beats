package com.tatum.handlers;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.tatum.music.TimedEvent;
import com.tatum.music.TrackData;

import java.util.ArrayList;

public class PaceMaker {

    private final TrackData trackData;
    private final TiledMap map;
    private final ArrayList<TimedEvent> beats;
    private int lastBeatId = 0;


    public PaceMaker(TrackData trackData, TiledMap map){
        this.trackData = trackData;
        this.map = map;
        beats = trackData.getBeats();
    }

    public float calculateVelocity(float deltaTime, double musicTime, float position){
        /*TODO:
        - find beat_id matching musicTime
        - find distance to make: ((beat_id+1) * block_size) - position
        - find time to make that distance in: beats.get(beat_id+1).getStart() - deltaTime
        - calculate speed
         */
        System.out.println("Position " + position*100/32 + " Time: " + (int)musicTime);
        //System.out.println("Matching beat is: " + findMatchingBeat(musicTime));
        return 1f;
    }

    private int findMatchingBeat(double musicTime){
        for(int i=lastBeatId;;){
            return -1;
        }
    }

    private float calculateDistance(){
        return 0f;
    }
}
