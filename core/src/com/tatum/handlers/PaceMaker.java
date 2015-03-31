package com.tatum.handlers;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Vector2;
import com.tatum.entities.Coin;
import com.tatum.entities.Player;
import com.tatum.music.Section;
import com.tatum.music.TimedEvent;
import com.tatum.music.TrackData;
import com.tatum.handlers.B2DVars;

import java.util.ArrayList;

public class PaceMaker {
    //this class is used to keep the player in time with the song, and deals with the collisions between the player and events
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
    private MonsterCoinLocation monsterCoinLocation;
    private boolean newBar = false;
    private int renderCounter=0;
    private int newBarChecker = 0;
    private boolean canShake =false;
    private int pixelPointCount =0;
    private boolean jumping = false;
    private int PixelPointJumpCount =0;
    private double hitTime =0;

    public PaceMaker(TrackData trackData, TiledMap map){
        this.trackData = trackData;
        this.map = map;
        beats = trackData.getBeats();
        bars = trackData.getBars();
        sections = trackData.getSections();
        timeSig=trackData.getTimeSignature();
    }


    public void updateVelocity(Player player, double musicTime){

        int beatId = findMatchingBeat(musicTime); // get the beat we are currently at
        if(beatId < 0) return; // if song has yet to start

        if(lastBeatHitId>0) // buffer to allow everything to load
            setBar();
        if(lastBarHitId>0) //^^^^^^^^^
            setSection(musicTime);

        setTimeSig();

        if(newBeat) { //if there is a new beat
            setPixelPoints();

            GameEvent gm = monsterCoinLocation.checkForEvent(lastBeatHitId-1); // check if there is an event
            String event = "null";
            if(gm!=null) {
               event = gm.getEvent(); // if the event is not null, set event string
            }
            if(hitTime<=hitTime+0.4&&player.getIsHurt()){ // if the player was hurt, remove the hurt skin
                player.setIsHurt(false);                    //after 0.4 seconds
                player.removeSkin();
            }

            if (event.equals("Bat")) { // check if the event == bat
                renderCounter++;    // invrement render counter, so that render window moves up one
                if (player.getIsDucking()) {
                    //if the player is ducking they are safe from the flying enemy
                } else{
                    player.scoreBreak(); //else hurt them
                    hitTime = musicTime;
                }
            } else if (event.equals("Slime")) { // check if event == slime
                renderCounter++;
                if (player.getIsJumping()) {
                    //if they are jumping they are safe
                } else{
                    player.scoreBreak(); //else hurt them
                    hitTime = musicTime;
                }
            } else if (event.equals("PinkCoin")) { //check if event == pink coint
                renderCounter++;
                if (player.getPlayerNum() == 3) { // if they are the red char collect coin

                    player.coinCollect();
                    Coin coin =(Coin) gm.getSprite();
                    coin.collected();
                }else{ //else hurt them
                    player.scoreBreak();
                    hitTime = musicTime;
                }
            } else if (event.equals("BlueCoin")) { // check if event == blue coin
                    renderCounter++;
                if (player.getPlayerNum() == 2) { // if they are blue char collect coin

                    player.coinCollect();
                    Coin coin =(Coin) gm.getSprite();
                    coin.collected();
                }else{  //else hurt them
                    player.scoreBreak();
                    hitTime = musicTime;
                }
            } else if (event.equals("GreenCoin")) { // check if event == green coin
                renderCounter++;
                if (player.getPlayerNum() == 1) { //if player is green char collect coin

                    player.coinCollect();
                    Coin coin =(Coin) gm.getSprite();
                    coin.collected();
                }else{
                    player.scoreBreak(); //else hurt them
                    hitTime = musicTime;
                }
            }
        }

        try {
              if (musicTime > pixelPoints.get(pixelPoint)) { // if the song has moved past the next 1/16th of the beat
                  float xPos = ((32*(lastBeatHitId-1))+((pixelPoint+1)*2))/PPM; // set the plays X to the next position along

                  //this is here to allow the jumps to be in time with the music, however it does not work as setting the x and y
                  //causes confilt between the jumping and walking
                  if(jumping){
                      if(PixelPointJumpCount<16) {
                          player.getBody().setTransform(new Vector2(xPos, player.getPosition().y + (2 / PPM)), 0);
                          PixelPointJumpCount++;
                      }
                      else if(PixelPointJumpCount == 32){
                          jumping=false;
                          PixelPointJumpCount=0;
                          player.getBody().setTransform(new Vector2(xPos,player.getPosition().y),0);
                      }
                      else if((PixelPointJumpCount>=16)&& (PixelPointJumpCount<32)){
                          player.getBody().setTransform(new Vector2(xPos, player.getPosition().y - (2 / PPM)), 0);
                          PixelPointJumpCount++;
                      }// THIS DEALS WITH EEPING THE JUMPING IN TIME, CONFLICTS WITH WALKING
                  }else{
                      player.getBody().setTransform(new Vector2(xPos,player.getPosition().y),0);
                  }
                    pixelPoint++; //wait for next 1/16 of the bar
                }

        }catch(NullPointerException e){
            //some of the variables will be null at the very first run
        }catch(IndexOutOfBoundsException e){
            //end of bar
        }
    }


    public int findMatchingBeat(double musicTime){
        int attempts = 0;
        //this meathod checks what beat we are in
        for(int i=lastBeatHitId;i>-1 && i< beats.size() && attempts<2;){
            double beatStart = beats.get(i).getStart();
            if(musicTime > beatStart){
                double beatDuration = beats.get(i).getduration();
                if(musicTime < beatStart+beatDuration) {
                    if(i>lastBeatHitId){ // if the beat we find is higher than the current beat in
                        newBeat=true;   //pacemaker  we have hit a new beat
                    }else{
                        newBeat=false;
                    }
                    if(lastBarHitId>newBarChecker){ //if we hit a new bar
                        newBarChecker=lastBarHitId;
                        newBar=true;
                        canShake=true;
                    }else{
                        newBar=false;
                    }
                    if(i == 1){ // if we have hit the first beat, used above for buffer
                        gotFirstBeat =true;
                    }
                    if(lastSectionHitId == 1){ // used to check if we reached the second session
                        hitSecondSection=true;// where the events start
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


    private void setPixelPoints(){
        TimedEvent beat = beats.get(lastBeatHitId);
        pixelPoints = new ArrayList<Double>();
        double duration = beat.getduration()/16; //break the current beat into 16 sub sections,
        for(int i =0;i<16;i++){                 // so that the char can be moved along in time
            pixelPoints.add(beat.getStart()+(duration*i));  // 2 px per section
        }
        pixelPoint=0;
    }

    //***************************HERE BE SETTERS AND GETTERS***************\\
    private void setBar(){
        lastBarHitId = beats.get(lastBeatHitId).getContainedIn();
    }
    private void setSection(double musicTime){
        lastSectionHitId = bars.get(lastBarHitId).getContainedIn();
    }
    private void setTimeSig(){
       timeSig= sections.get(lastSectionHitId).getTimeSignature();
    }

    public TrackData getTrackData(){return trackData;}
    public void setMonsterCoinLocation(MonsterCoinLocation monsterCoinLocation){
        this.monsterCoinLocation = monsterCoinLocation;
    }

    public boolean canShake(){
        return canShake;
    }
    public void cantShake(){
        canShake=false;
    }
    public void setJumping(boolean jumping){ this.jumping = jumping;}

    public boolean getJumping(){return jumping;}
    public int getLastBeatHitId(){return lastBeatHitId;}
    public int getLastBarHitId() {return lastBarHitId;}
    public int getLastSectionHitId() { return lastSectionHitId;}
    public int getTimeSig(){return timeSig;}
    public boolean getNewBeat(){
        return newBeat;
    }
    public boolean gotFirstBeat(){
        return gotFirstBeat;
    }
    public boolean hitSecondSection(){
        return hitSecondSection;
    }
    public int getRenderCounter(){
        return renderCounter;
    }
}
