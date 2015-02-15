package com.tatum.handlers;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Vector2;
import com.tatum.entities.Player;
import com.tatum.music.Section;
import com.tatum.music.TimedEvent;
import com.tatum.music.TrackData;
import com.tatum.handlers.B2DVars;

import java.util.ArrayList;

public class PaceMaker {

    private final TrackData trackData;
    private final TiledMap map;
    private final ArrayList<TimedEvent> beats;
    private final ArrayList<TimedEvent> bars;
    private final ArrayList<Section> sections;
    private int lastBeatHitId = 0;
    private int lastBarHitId = 0;
    private int lastSectionHitId = 0;
    private int timeSig;
    private boolean newBeat = false;
    private final float PPM = B2DVars.PPM;
    private ArrayList<Double> pixelPoints;
    private int pixelPoint=0;
    private boolean gotFirstBeat = false;
    private boolean hitSecondSection = false;
    public PaceMaker(TrackData trackData, TiledMap map){
        this.trackData = trackData;
        this.map = map;
        beats = trackData.getBeats();
        bars = trackData.getBars();
        sections = trackData.getSections();
        timeSig=trackData.getTimeSignature();
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
        if(newBeat) {
            System.out.println(lastBeatHitId + " " + lastBarHitId + " " + lastSectionHitId);
            //System.out.println(beats.get(lastBarHitId).getStart()+" ");
        }
        if(lastBeatHitId>0)
            setBar();
        if(lastBarHitId>0)
            setSection();

        //Vector2 velocity = player.getBody().getLinearVelocity();
        //velocity.x = 1.0f;

        if(newBeat){
            setPixelPoints();
            //float xPos = 32*(lastBeatHitId-1)/PPM;
            //player.getBody().setTransform(new Vector2(xPos,player.getPosition().y),0);
        }
        try {
              if (musicTime > pixelPoints.get(pixelPoint)) {
                    //System.out.println("TRUE TRUEREUREUREURUEUERU "+ pixelPoint);
                    float xPos = ((32*(lastBeatHitId-1))+((pixelPoint+1)*2))/PPM;
                    player.getBody().setTransform(new Vector2(xPos,player.getPosition().y),0);
                    //player.getBody().setTransform(new Vector2(player.getPosition().x + (1/PPM), player.getPosition().y), 0);
                    //pixelPoints.remove(i);
                    //System.out.println("Pixel point: " +pixelPoint);
                    pixelPoint++;
                }

        }catch(NullPointerException e){
            //bullshit start crap
        }catch(IndexOutOfBoundsException e){
            //end of bar
        }
        //player.getBody().setLinearVelocity(velocity);
    }

    private int findMatchingBeat(double musicTime){
        int attempts = 0;
        for(int i=lastBeatHitId;i>-1 && i< beats.size() && attempts<2;){
            double beatStart = beats.get(i).getStart();
            if(musicTime > beatStart){
                double beatDuration = beats.get(i).getduration();
                if(musicTime < beatStart+beatDuration) {
                    //System.out.println(beatStart + " < " + musicTime + " < " + (beatStart+beatDuration));
                    if(i>lastBeatHitId){
                        newBeat=true;
                    }else{
                        newBeat=false;
                    }
                    if(i == 1){
                        gotFirstBeat =true;
                    }
                    if(lastSectionHitId == 1){
                        hitSecondSection=true;
                    }
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
    public int getLastBeatHitId(){return lastBeatHitId;}
    public int getLastBarHitId() {return lastBarHitId;}
    public int getLastSectionHitId() { return lastSectionHitId;}
    public int getTimeSig(){return timeSig;}
    private void setPixelPoints(){
        TimedEvent beat = beats.get(lastBeatHitId);
        pixelPoints = new ArrayList<Double>();
        double duration = beat.getduration()/16;
        for(int i =0;i<16;i++){
            pixelPoints.add(beat.getStart()+(duration*i));
        }
        pixelPoint=0;
    }
    public boolean getNewBeat(){
        return newBeat;
    }

    public boolean gotFirstBeat(){
        return gotFirstBeat;
    }
    public boolean hitSecondSection(){
        return hitSecondSection;
    }
    private void setBar(){
        lastBarHitId = beats.get(lastBeatHitId).getContainedIn();
    }
    private void setSection(){
        lastSectionHitId = bars.get(lastBarHitId).getContainedIn();
    }
}
