package com.tatum.handlers;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Vector2;
import com.tatum.entities.Player;
import com.tatum.music.TimedEvent;
import com.tatum.music.TrackData;

import java.util.ArrayList;

public class PaceMaker {

    private final TrackData trackData;
    private final TiledMap map;
    private final ArrayList<TimedEvent> beats;
    private int lastBeatHitId = 0;


    public PaceMaker(TrackData trackData, TiledMap map){
        this.trackData = trackData;
        this.map = map;
        beats = trackData.getBeats();
    }



    public void updateVelocity(Player player, double musicTime){

        /*TODO:
        - find beat_id matching musicTime
        - find distance to make: ((beat_id+1) * block_size * blocks per beat) - position
        - find time to make that distance in: beats.get(beat_id+1).getStart() - musicTime
        - calculate speed
         */
        //System.out.println("Position " + position*100/32 + " Time: " + (int)musicTime);
        int beatId = findMatchingBeat(musicTime);
        if(beatId < 0) return;
        Vector2 velocity = player.getBody().getLinearVelocity();
        velocity.x = 1.0f;
        player.getBody().setLinearVelocity(velocity);
    }

    private int findMatchingBeat(double musicTime){
        int attempts = 0;
        for(int i=lastBeatHitId;i>-1 && i< beats.size() && attempts<2;){
            double beatStart = beats.get(i).getStart();
            if(musicTime > beatStart){
                double beatDuration = beats.get(i).getduration();
                if(musicTime < beatStart+beatDuration) {
                    //System.out.println(beatStart + " < " + musicTime + " < " + (beatStart+beatDuration));
                    lastBeatHitId = i;
                    return i;
                }
                i++;
                attempts++;
            }
            else{
                i--;
                attempts++;
            }
        }
        return -1;
    }

    private float calculateDistance(){
        return 0f;
    }
}
