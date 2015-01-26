package com.tatum.handlers;

import com.tatum.music.TimedEvent;
import com.tatum.music.TrackData;

import java.util.ArrayList;

public class PaceMaker {

    private final TrackData trackData;
    private final ArrayList<TimedEvent> beats;
    private int lastBeatId = 0;


    public PaceMaker(TrackData trackData){
        this.trackData = trackData;
        beats = trackData.getBeats();
    }

    public float calculateVelocity(float deltaTime, double musicTime, float position){
        /*TODO:
        - find beat_id matching musicTime
        - find distance to make: ((beat_id+1) * block_size) - position
        - find time to make that distance in: beats.get(beat_id+1).getStart() - deltaTime
        - calculate speed
         */
        return 1.0f;
    }

    private int findMatchingBeat(double musicTime){
        for(int i=lastBeatId;;){

        }
    }

    private float calculateDistance(){
        return 0f;
    }
}
