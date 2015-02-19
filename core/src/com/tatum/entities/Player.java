package com.tatum.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.tatum.handlers.Animation;
import com.tatum.handlers.B2DVars;
import com.tatum.handlers.ContentManager;
import com.tatum.handlers.PaceMaker;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;

import static com.tatum.handlers.B2DVars.PPM;


public class Player extends B2DSprite {
    private int numCrystals;
    private int totalCrystals;
    private int score;
    private int multiplyer;
    private int step;
    private int stepStage;
    private double lastTime;
    private String playerName;
    private int highScore;
    private TextureRegion[] sprites1 = new TextureRegion[11];
    private TextureRegion   sprite1Jump;
    private TextureRegion   sprite1Duck;
    private TextureRegion   sprite1Hurt;
    private TextureRegion[] sprites2 = new TextureRegion[11];
    private TextureRegion   sprite2Jump;
    private TextureRegion   sprite2Duck;
    private TextureRegion   sprite2Hurt;
    private TextureRegion[] sprites3 = new TextureRegion[11];
    private TextureRegion   sprite3Jump;
    private TextureRegion   sprite3Duck;
    private TextureRegion   sprite3Hurt;
    private boolean isJumping = false;
    private boolean isDucking = false;
    private int playerNum;
    private boolean newHighScore;
    private String path;
    private PaceMaker paceMaker;

    //reskin timers
    private double jumpTime = 0;
    private int duckBeat = 0;



    public Player(Body body, ContentManager resources,String name,String path,PaceMaker paceMaker) {
        super(body, resources);

        playerName=name;
        this.path=path;
        highScore=loadHighScore();
        score=0;
        newHighScore=false;
        multiplyer=1;
        step=0;
        stepStage=0;
        lastTime = 0;
        playerNum = 1;
        this.paceMaker = paceMaker;
        loadPlayers(resources);
        /*TextureRegion[][] spriteSplit = TextureRegion.split(tex,71,93);
        int count =0;
        for(int i =0;i<4;i++){
            for(int j=0;j<3;j++){
                if(count==11){
                    break;
                }
                sprites[count] = spriteSplit[i][j];
                count++;
            }
        }
    */

        animation = new Animation(sprites1);
        animation.setFrames(sprites1, 1/15f);
        width = sprites1[0].getRegionWidth();
        height = sprites1[0].getRegionHeight();

    }

    public void collectCrystal() { numCrystals++; }
    public int getNumCrystals() { return numCrystals; }

    public void setTotalCrystals(int i) { totalCrystals = i; }
    public int getTotalCrystals() { return totalCrystals; }

    public void scoreStep(){

        score = score +2*multiplyer;

        if(paceMaker.getLastBeatHitId()>stepStage) {
            step = step + 1;
            stepStage=paceMaker.getLastBeatHitId();
        }
        if(step == 5)
            if(multiplyer<100) {
                multiplyer += 1;
                step = 0;
            }


    }
    public void scoreBreak(){
        score = score/2;
        if(multiplyer==1)
            multiplyer=1;
        else
            multiplyer= multiplyer/2;
        step=0;
    }

    public int getScore(){return score;}
    public int getStep(){return step;}
    public int getMultiplyer(){return  multiplyer;}

    public boolean manageScore(double time){


        if(time >= lastTime + 0.1) {
            lastTime = time;
            return true;


        }
        return false;
    }
    public int getHighScore(){return highScore;}
    public String getPlayerName(){return playerName;}
    public void randomSprite(){
    if(!isJumping&&!isDucking) {
        if (playerNum == 3) {
            animation = new Animation(sprites1);
            animation.setFrames(sprites1, 1 / 15f);
            playerNum = 1;
        } else if (playerNum == 2) {
            animation = new Animation(sprites3);
            animation.setFrames(sprites3, 1 / 15f);
            playerNum = 3;
        } else {
            animation = new Animation(sprites2);
            animation.setFrames(sprites2, 1 / 15f);
            playerNum = 2;
        }

    }
    }
    public int getPlayerNum(){return playerNum;}

    public void loadPlayers(ContentManager resources){

        for(int i=1;i<12;i++) {
        if(i<10)
            resources.loadTexture("res/images/PlatformerPack/Player/p1_walk/PNG/mini/p1_walk0" + i + ".png");
        else
            resources.loadTexture("res/images/PlatformerPack/Player/p1_walk/PNG/mini/p1_walk" + i + ".png");
            //System.out.println("Load " +i);
        }
        Texture[] tex = new Texture[11];
        for(int i=0;i<11;i++) {
            int j = i + 1;
            if(j<10)
                tex[i] = resources.getTexture("p1_walk0" + j);
            else
                tex[i] = resources.getTexture("p1_walk" + j);
            //System.out.println("get " +(i+1));
        }
        for(int i=0;i<11;i++) {
            sprites1[i] = TextureRegion.split(tex[i], 36, 47)[0][0];

        }
        for(int i=1;i<12;i++) {
            if(i<10)
                resources.loadTexture("res/images/PlatformerPack/Player/p2_walk/PNG/mini/p2_walk0" + i + ".png");
            else
                resources.loadTexture("res/images/PlatformerPack/Player/p2_walk/PNG/mini/p2_walk" + i + ".png");
            //System.out.println("Load " +i);
        }
        tex = new Texture[11];
        for(int i=0;i<11;i++) {
            int j = i + 1;
            if(j<10)
                tex[i] = resources.getTexture("p2_walk0" + j);
            else
                tex[i] = resources.getTexture("p2_walk" + j);
            //System.out.println("get " +(i+1));
        }
        for(int i=0;i<11;i++) {
            sprites2[i] = TextureRegion.split(tex[i], 36, 47)[0][0];

        }for(int i=1;i<12;i++) {
            if(i<10)
                resources.loadTexture("res/images/PlatformerPack/Player/p3_walk/PNG/mini/p3_walk0" + i + ".png");
            else
                resources.loadTexture("res/images/PlatformerPack/Player/p3_walk/PNG/mini/p3_walk" + i + ".png");
            //System.out.println("Load " +i);
        }
        tex = new Texture[11];
        for(int i=0;i<11;i++) {
            int j = i + 1;
            if(j<10)
                tex[i] = resources.getTexture("p3_walk0" + j);
            else
                tex[i] = resources.getTexture("p3_walk" + j);
            //System.out.println("get " +(i+1));
        }
        for(int i=0;i<11;i++) {
            sprites3[i] = TextureRegion.split(tex[i], 36, 47)[0][0];
        }

        setExtraSkins(resources);

    }
    public void setExtraSkins(ContentManager resources){

        //load duck skins
        resources.loadTexture("res/images/PlatformerPack/Player/extraSkins/p1_duck.png");
        resources.loadTexture("res/images/PlatformerPack/Player/extraSkins/p2_duck.png");
        resources.loadTexture("res/images/PlatformerPack/Player/extraSkins/p3_duck.png");
        sprite1Duck = TextureRegion.split(resources.getTexture("p1_duck"), 35, 36)[0][0];
        sprite2Duck = TextureRegion.split(resources.getTexture("p2_duck"), 34, 36)[0][0];
        sprite3Duck = TextureRegion.split(resources.getTexture("p3_duck"), 35, 36)[0][0];

        //load jump skins
        resources.loadTexture("res/images/PlatformerPack/Player/extraSkins/p1_jump.png");
        resources.loadTexture("res/images/PlatformerPack/Player/extraSkins/p2_jump.png");
        resources.loadTexture("res/images/PlatformerPack/Player/extraSkins/p3_jump.png");
        sprite1Jump = TextureRegion.split(resources.getTexture("p1_jump"), 34, 47)[0][0];
        sprite2Jump = TextureRegion.split(resources.getTexture("p2_jump"), 33, 47)[0][0];
        sprite3Jump = TextureRegion.split(resources.getTexture("p3_jump"), 34, 47)[0][0];

        //load hurt skins
        resources.loadTexture("res/images/PlatformerPack/Player/extraSkins/p1_hurt.png");
        resources.loadTexture("res/images/PlatformerPack/Player/extraSkins/p2_hurt.png");
        resources.loadTexture("res/images/PlatformerPack/Player/extraSkins/p3_hurt.png");
        sprite1Hurt = TextureRegion.split(resources.getTexture("p1_hurt"), 35, 46)[0][0];
        sprite2Hurt = TextureRegion.split(resources.getTexture("p2_hurt"), 34, 46)[0][0];
        sprite3Hurt = TextureRegion.split(resources.getTexture("p3_hurt"), 35, 46)[0][0];

    }
    public int loadHighScore(){
        String trackName = path.replaceAll("/","");
        File file = Gdx.files.external("musicdata/"+trackName+"/"+"userData/"+playerName+"/score.json").file();
        if(file.exists()){
            InputStream is = Gdx.files.external("musicdata/"+trackName+"/"+"userData/"+playerName+"/score.json").read();
            JsonReader rdr = Json.createReader(is);
            JsonObject hi = rdr.readObject();
            return hi.getInt("Score");
        }
        else{
            return 0;
        }
    }
    public void saveHighScore(){
        String trackname = path.replaceAll("/","");
        if(newHighScore){
            File file = Gdx.files.external("musicdata/"+trackname+"/"+"userData/"+playerName).file();
            if(!file.exists()){
                file.mkdir();
            }
            FileHandle ff = Gdx.files.external("musicdata/"+trackname+"/"+"userData/"+playerName+"/score.json");
            OutputStream OS = ff.write(false);
            try {
                OS.write(("{\"Score\": "+highScore+"}").getBytes());
                OS.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
    public void newHighScore(){
        newHighScore=true;
    }
    public void setHighScore(){
        highScore=score;
    }


    public void setJumpSkin(){
        if(playerNum ==3){
            TextureRegion[] temp = {sprite3Jump,sprite3Jump};
            animation = new Animation(temp);
            animation.setFrames(temp, 1f);
        }
        else if(playerNum ==2){
            TextureRegion[] temp = {sprite2Jump,sprite2Jump};
            animation = new Animation(temp);
            animation.setFrames(temp, 1f);
        }
        else {
            TextureRegion[] temp = {sprite1Jump,sprite1Jump};
            animation = new Animation(temp);
            animation.setFrames(temp, 1f);
        }
        setIsJumping(true);
    }
    public void removeSkin(){
        if(playerNum ==3){
            animation = new Animation(sprites3);
            animation.setFrames(sprites3, 1/15f);
        }
        else if(playerNum ==2){
            animation = new Animation(sprites2);
            animation.setFrames(sprites2, 1/15f);
        }
        else {
            animation = new Animation(sprites1);
            animation.setFrames(sprites1, 1/15f);
        }
    }
    public void setCrouchSkin(int beat){
    if(!isJumping&&!isDucking) {
        if (playerNum == 3) {
            TextureRegion[] temp = {sprite3Duck, sprite3Duck};
            animation = new Animation(temp);
            animation.setFrames(temp, 1f);
        } else if (playerNum == 2) {
            TextureRegion[] temp = {sprite2Duck, sprite2Duck};
            animation = new Animation(temp);
            animation.setFrames(temp, 1f);
        } else {
            TextureRegion[] temp = {sprite1Duck, sprite1Duck};
            animation = new Animation(temp);
            animation.setFrames(temp, 1f);
        }
        setDuckBeat(beat);
        setIsDucking(true);
    }
    }


    public boolean getIsJumping(){
        return isJumping;
    }
    public void setIsJumping(boolean temp){
        isJumping = temp;
    }
    public boolean getIsDucking(){
        return isDucking;
    }
    public void setIsDucking(boolean temp){
        isDucking = temp;
    }
    public double getJumpTime(){
        return jumpTime;
    }
    public void setJumpTime(double time){
        jumpTime=time;
    }
    public double getDuckBeat(){
        return duckBeat;
    }
    public void setDuckBeat(int beat){
        duckBeat = beat;
    }

}
