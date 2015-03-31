package com.tatum.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
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
    private TextureRegion   spriteHurt;
    private boolean isJumping = false;
    private boolean isDucking = false;
    private boolean isHurt = false;
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
        loadPlayers(resources); // load in all character skins

        animation = new Animation(sprites1);
        animation.setFrames(sprites1, 1/15f);
        width = sprites1[0].getRegionWidth();
        height = sprites1[0].getRegionHeight();

    }
    public void scoreStep(){
        //increase the score
        score = score +2*multiplyer;

        if(paceMaker.getLastBeatHitId()>stepStage) {
            //increase the multiplyer step if a new beat is hit
            step = step + 1;
            stepStage=paceMaker.getLastBeatHitId();
        }
        if(step == 5) // if five beats past increase multiplier
            if(multiplyer<100) {
                multiplyer += 1;
                step = 0;
            }

    }
    public void scoreBreak(){
        //if user done something wrong cut multiplier and score in half
        score = score/2;
        if(multiplyer==1)
            multiplyer=1;
        else
            multiplyer= multiplyer/2;
        step=0;
        setHurt(); //se the hurt animation
    }
    public boolean manageScore(double time){ //used to make sure phones with quicker processors don't call scoreStep more often
        if(time >= lastTime + 0.1) {
            lastTime = time;
            return true;
        }
        return false;
    }
    public void randomSprite(){ //move charaacter one to the right - ORDER: Green - Blue - Red
    if(!isJumping&&!isDucking) { //if user is not ducking or jumping
        if (playerNum == 3) { // if user is red
            animation = new Animation(sprites1); //change to green
            animation.setFrames(sprites1, 1 / 15f);
            playerNum = 1; //set num to green
        } else if (playerNum == 2) { // if user is blue
            animation = new Animation(sprites3); //set to red
            animation.setFrames(sprites3, 1 / 15f);
            playerNum = 3; // set number to red
        } else { //else user green
            animation = new Animation(sprites2); //set to blue
            animation.setFrames(sprites2, 1 / 15f);
            playerNum = 2; // set num to blue
        }

    }
    }
    public void randomSpriteReverse(){ // same method as above but for left
        if(!isJumping&&!isDucking) {
            if (playerNum == 3) { //user red change to blue
                animation = new Animation(sprites2);
                animation.setFrames(sprites2, 1 / 15f);
                playerNum = 2;
            } else if (playerNum == 2) { //user blue change to green
                animation = new Animation(sprites1);
                animation.setFrames(sprites1, 1 / 15f);
                playerNum = 1;
            } else { // user green change to red
                animation = new Animation(sprites3);
                animation.setFrames(sprites3, 1 / 15f);
                playerNum = 3;
            }

        }
    }
    public void loadPlayers(ContentManager resources){

        for(int i=1;i<12;i++) //load in all the walking textures for green char
        if(i<10)
            resources.loadTexture("res/images/PlatformerPack/Player/p1_walk/PNG/mini/p1_walk0" + i + ".png");
        else
            resources.loadTexture("res/images/PlatformerPack/Player/p1_walk/PNG/mini/p1_walk" + i + ".png");

        Texture[] tex = new Texture[11];
        for(int i=0;i<11;i++) { //cut textures out and store in sprites1
            int j = i + 1;
            if(j<10)
                tex[i] = resources.getTexture("p1_walk0" + j);
            else
                tex[i] = resources.getTexture("p1_walk" + j);
        }
        for(int i=0;i<11;i++) {
            sprites1[i] = TextureRegion.split(tex[i], 36, 47)[0][0];

        }

        for(int i=1;i<12;i++) //load in all the walking texture for blue
            if(i<10)
                resources.loadTexture("res/images/PlatformerPack/Player/p2_walk/PNG/mini/p2_walk0" + i + ".png");
            else
                resources.loadTexture("res/images/PlatformerPack/Player/p2_walk/PNG/mini/p2_walk" + i + ".png");


        tex = new Texture[11];
        for(int i=0;i<11;i++) { // cut out all textures and save in sprites2
            int j = i + 1;
            if(j<10)
                tex[i] = resources.getTexture("p2_walk0" + j);
            else
                tex[i] = resources.getTexture("p2_walk" + j);
        }
        for(int i=0;i<11;i++)
            sprites2[i] = TextureRegion.split(tex[i], 36, 47)[0][0];


        //load in all textures for walking red
        for(int i=1;i<12;i++)
            if(i<10)
                resources.loadTexture("res/images/PlatformerPack/Player/p3_walk/PNG/mini/p3_walk0" + i + ".png");
            else
                resources.loadTexture("res/images/PlatformerPack/Player/p3_walk/PNG/mini/p3_walk" + i + ".png");

        //cut out and save all textures into sprites 3
        tex = new Texture[11];
        for(int i=0;i<11;i++) {
            int j = i + 1;
            if(j<10)
                tex[i] = resources.getTexture("p3_walk0" + j);
            else
                tex[i] = resources.getTexture("p3_walk" + j);
        }
        for(int i=0;i<11;i++) {
            sprites3[i] = TextureRegion.split(tex[i], 36, 47)[0][0];
        }

        setExtraSkins(resources); // call method below to load jump/dunk/ hurt

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
        spriteHurt  = TextureRegion.split(resources.getTexture("p1_hurt"), 35, 46)[0][1];
        sprite2Hurt = TextureRegion.split(resources.getTexture("p2_hurt"), 34, 46)[0][0];
        sprite3Hurt = TextureRegion.split(resources.getTexture("p3_hurt"), 35, 46)[0][0];

    }
    public int loadHighScore(){
        //check if a previous highscore existed
        //if so load it in and return it
        //else return 0
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
            if(!file.exists()){ // if no previous high score, make folder to save it in
                file.mkdir();
            }
            FileHandle ff = Gdx.files.external("musicdata/"+trackname+"/"+"userData/"+playerName+"/score.json"); // create/open score file
            OutputStream OS = ff.write(false);
            try {
                OS.write(("{\"Score\": "+highScore+"}").getBytes()); // write score to file
                OS.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public void setJumpSkin(){
        setIsHurt(false);
        if(playerNum ==3){ // if player red set red jump
            TextureRegion[] temp = {sprite3Jump,sprite3Jump};
            animation = new Animation(temp);
            animation.setFrames(temp, 1f);
        }
        else if(playerNum ==2){ // if player blue set blue jump
            TextureRegion[] temp = {sprite2Jump,sprite2Jump};
            animation = new Animation(temp);
            animation.setFrames(temp, 1f);
        }
        else { // if player green set green jump
            TextureRegion[] temp = {sprite1Jump,sprite1Jump};
            animation = new Animation(temp);
            animation.setFrames(temp, 1f);
        }
        setIsJumping(true); // set jumping to be true
    }
    public void removeSkin(){
        //sets the character back to the walking skin after hurt/jumping/ducking
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
    if(!isJumping&&!isDucking) {//check if the user not already jumping/ducking
        setIsHurt(false);
        if (playerNum == 3) { // if red set red duck
            TextureRegion[] temp = {sprite3Duck, sprite3Duck};
            animation = new Animation(temp);
            animation.setFrames(temp, 1f);
        } else if (playerNum == 2) { //blue - blue duck
            TextureRegion[] temp = {sprite2Duck, sprite2Duck};
            animation = new Animation(temp);
            animation.setFrames(temp, 1f);
        } else { // green - green duck
            TextureRegion[] temp = {sprite1Duck, sprite1Duck};
            animation = new Animation(temp);
            animation.setFrames(temp, 1f);
        }
        setDuckBeat(beat);
        setIsDucking(true);
    }
    }
    public void setHurt(){
        if(playerNum ==3){ //if red set between red hurt and blank for flashing effect
            TextureRegion[] temp = {sprite3Hurt,spriteHurt};
            animation = new Animation(temp);
            animation.setFrames(temp, 1/12f);
        }
        else if(playerNum ==2){ // blue - blue hurt and blank
            TextureRegion[] temp = {sprite2Hurt,spriteHurt};
            animation = new Animation(temp);
            animation.setFrames(temp, 1/12f);
        }
        else { // green - green hurt and blank
            TextureRegion[] temp = {sprite1Hurt,spriteHurt};
            animation = new Animation(temp);
            animation.setFrames(temp, 1/12f);
        }
        setIsHurt(true);
    }
    public void coinCollect(){
        // additional bonus for collecting a coin
        score+=1000;
        multiplyer+=2;
        if(multiplyer>100){
            multiplyer=100;
        }
    }


    //*****************HERE BE GETTERS AND SETTERS ***************\\
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
    public void setIsHurt(boolean temp ){isHurt = temp;}
    public boolean getIsHurt(){return isHurt;}
    public int getPlayerNum(){return playerNum;}
    public int getScore(){return score;}
    public int getStep(){return step;}
    public int getMultiplyer(){return  multiplyer;}
    public void newHighScore(){
        newHighScore=true;
    }
    public void setHighScore(){
        highScore=score;
    }
    public int getHighScore(){return highScore;}
    public String getPlayerName(){return playerName;}
}
