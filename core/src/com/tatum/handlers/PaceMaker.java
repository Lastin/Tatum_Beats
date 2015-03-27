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

        int beatId = findMatchingBeat(musicTime);
        if(beatId < 0) return;
        //if(newBeat) {
        //    System.out.println(lastBeatHitId + " " + lastBarHitId + " " + lastSectionHitId);
            //System.out.println(beats.get(lastBarHitId).getStart()+" ");
        //}
        if(lastBeatHitId>0)
            setBar();
        if(lastBarHitId>0)
            setSection(musicTime);

        //Vector2 velocity = player.getBody().getLinearVelocity();
        //velocity.x = 1.0f;
        setTimeSig();
        if(newBeat) {
            System.out.println(renderCounter);
            setPixelPoints();
            //float xPos = 32*(lastBeatHitId-1)/PPM;
            //player.getBody().setTransform(new Vector2(xPos,player.getPosition().y),0);

            GameEvent gm = monsterCoinLocation.checkForEvent(lastBeatHitId-1);
            String event = "null";
            if(gm!=null) {
               event = gm.getEvent();
            }
            if(hitTime<=hitTime+0.4&&player.getIsHurt()){
                player.setIsHurt(false);
                player.removeSkin();
            }

            System.out.println(event);
            if (event.equals("Bat")) {
                renderCounter++;
                if (player.getIsDucking()) {
                    //they safe from dem bats doe
                } else{
                    player.scoreBreak();
                    hitTime = musicTime;
                }
            } else if (event.equals("Slime")) {
                renderCounter++;
                if (player.getIsJumping()) {
                    //they safe from dem slimes doe
                } else{
                    player.scoreBreak();
                    hitTime = musicTime;
                }
            } else if (event.equals("PinkCoin")) {
                renderCounter++;
                if (player.getPlayerNum() == 3) {
                    //they get dem coins doe
                    player.coinCollect();
                    Coin coin =(Coin) gm.getSprite();
                    coin.collected();
                }else{
                    player.scoreBreak();
                    hitTime = musicTime;
                }
            } else if (event.equals("BlueCoin")) {
                    renderCounter++;
                if (player.getPlayerNum() == 2) {
                    //they get dem coins doe
                    player.coinCollect();
                    Coin coin =(Coin) gm.getSprite();
                    coin.collected();
                }else{
                    player.scoreBreak();
                    hitTime = musicTime;
                }
            } else if (event.equals("GreenCoin")) {
                renderCounter++;
                if (player.getPlayerNum() == 1) {
                    //they get dem coins doe
                    player.coinCollect();
                    Coin coin =(Coin) gm.getSprite();
                    coin.collected();
                }else{
                    player.scoreBreak();
                    hitTime = musicTime;
                }
            }
        }

        try {
              if (musicTime > pixelPoints.get(pixelPoint)) {
                  float xPos = ((32*(lastBeatHitId-1))+((pixelPoint+1)*2))/PPM;

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
                    //System.out.println("TRUE TRUEREUREUREURUEUERU "+ pixelPoint);

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


    public int findMatchingBeat(double musicTime){
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
                    if(lastBarHitId>newBarChecker){
                        newBarChecker=lastBarHitId;
                        newBar=true;
                        canShake=true;
                    }else{
                        newBar=false;
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
    private void setSection(double musicTime){
        lastSectionHitId = bars.get(lastBarHitId).getContainedIn();
        //lastSectionHitId = trackData.getSectionInRedux(musicTime,lastSectionHitId);
    }
    private void setTimeSig(){
       timeSig= sections.get(lastSectionHitId).getTimeSignature();
    }

    public TrackData getTrackData(){return trackData;}
    public void setMonsterCoinLocation(MonsterCoinLocation monsterCoinLocation){
        this.monsterCoinLocation = monsterCoinLocation;
    }
    public int getRenderCounter(){
        return renderCounter;
    }
    public boolean canShake(){
        return canShake;
    }
    public void cantShake(){
        canShake=false;
    }
    public void setJumping(boolean jumping){ this.jumping = jumping;}

    public boolean getJumping(){return jumping;}
}
