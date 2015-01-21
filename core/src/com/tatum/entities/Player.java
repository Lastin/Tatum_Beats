package com.tatum.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.tatum.handlers.Animation;
import com.tatum.handlers.ContentManager;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;


public class Player extends B2DSprite {
    private int numCrystals;
    private int totalCrystals;
    private int score;
    private int multiplyer;
    private int step;
    private int stepStage;
    private int scorespeedVar;
    private int scorespeedConst;
    private String playerName;
    private int highScore;
    private TextureRegion[] sprites1 = new TextureRegion[11];
    private TextureRegion[] sprites2 = new TextureRegion[11];
    private TextureRegion[] sprites3 = new TextureRegion[11];
    private int playerNum;
    private boolean newHighScore;
    private String path;

    public Player(Body body, ContentManager resources,String name,String path) {
        super(body, resources);

        playerName=name;
        this.path=path;
        highScore=loadHighScore();
        score=0;
        newHighScore=false;
        multiplyer=1;
        step=0;
        stepStage=0;
        scorespeedVar=0;
        scorespeedConst=5;
        playerNum = 1;
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
        stepStage+=1;
        if(stepStage==7) {
            step = step + 1;
            stepStage=0;
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

    public boolean manageScore(){

        scorespeedVar+=1;
        if(scorespeedVar==scorespeedConst) {
            scorespeedVar=0;
            return true;


        }
        return false;
    }
    public int getHighScore(){return highScore;}
    public String getPlayerName(){return playerName;}
    public void randomSprite(){

        if(playerNum ==3){
            animation = new Animation(sprites1);
            animation.setFrames(sprites1, 1/15f);
            playerNum=1;
        }
        else if(playerNum ==2){
            animation = new Animation(sprites3);
            animation.setFrames(sprites3, 1/15f);
            playerNum=3;
        }
        else {
            animation = new Animation(sprites2);
            animation.setFrames(sprites2, 1/15f);
            playerNum=2;
        }


    }
   public int getPlayerNum(){return playerNum;}

    public void loadPlayers(ContentManager resources){ for(int i=1;i<12;i++) {
        if(i<10)
            resources.loadTexture("res/images/PlatformerPack/Player/p1_walk/PNG/mini/p1_walk0" + i + ".png");
        else
            resources.loadTexture("res/images/PlatformerPack/Player/p1_walk/PNG/mini/p1_walk" + i + ".png");
        System.out.println("Load " +i);
    }
        Texture[] tex = new Texture[11];
        for(int i=0;i<11;i++) {
            int j = i + 1;
            if(j<10)
                tex[i] = resources.getTexture("p1_walk0" + j);
            else
                tex[i] = resources.getTexture("p1_walk" + j);
            System.out.println("get " +(i+1));
        }
        for(int i=0;i<11;i++) {
            sprites1[i] = TextureRegion.split(tex[i], 36, 47)[0][0];

        }
        for(int i=1;i<12;i++) {
            if(i<10)
                resources.loadTexture("res/images/PlatformerPack/Player/p2_walk/PNG/mini/p2_walk0" + i + ".png");
            else
                resources.loadTexture("res/images/PlatformerPack/Player/p2_walk/PNG/mini/p2_walk" + i + ".png");
            System.out.println("Load " +i);
        }
        tex = new Texture[11];
        for(int i=0;i<11;i++) {
            int j = i + 1;
            if(j<10)
                tex[i] = resources.getTexture("p2_walk0" + j);
            else
                tex[i] = resources.getTexture("p2_walk" + j);
            System.out.println("get " +(i+1));
        }
        for(int i=0;i<11;i++) {
            sprites2[i] = TextureRegion.split(tex[i], 36, 47)[0][0];

        }for(int i=1;i<12;i++) {
            if(i<10)
                resources.loadTexture("res/images/PlatformerPack/Player/p3_walk/PNG/mini/p3_walk0" + i + ".png");
            else
                resources.loadTexture("res/images/PlatformerPack/Player/p3_walk/PNG/mini/p3_walk" + i + ".png");
            System.out.println("Load " +i);
        }
        tex = new Texture[11];
        for(int i=0;i<11;i++) {
            int j = i + 1;
            if(j<10)
                tex[i] = resources.getTexture("p3_walk0" + j);
            else
                tex[i] = resources.getTexture("p3_walk" + j);
            System.out.println("get " +(i+1));
        }
        for(int i=0;i<11;i++) {
            sprites3[i] = TextureRegion.split(tex[i], 36, 47)[0][0];
        }
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
}
