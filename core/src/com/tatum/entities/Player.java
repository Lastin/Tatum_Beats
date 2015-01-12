package com.tatum.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.tatum.handlers.Animation;
import com.tatum.handlers.ContentManager;

public class Player extends B2DSprite {
    private int numCrystals;
    private int totalCrystals;
    private int score;
    private int multiplyer;
    private int step;
    private int stepStage;
    private int scorespeedVar;
    private int scorespeedConst;


    public Player(Body body, ContentManager resources) {
        super(body, resources);

        score=0;
        multiplyer=1;
        step=0;
        stepStage=0;
        scorespeedVar=0;
        scorespeedConst=5;
        //resources.loadTexture("res/images/bunny.png");
        //resources.loadTexture("res/images/dickbutt.png");
        //Texture tex = resources.getTexture("bunny");
        //Texture tex = resources.getTexture("dickbutt");
        //TextureRegion[] sprites = TextureRegion.split(tex, 32, 32)[0];
        resources.printKeys();
        for(int i=1;i<12;i++) {
            if(i<10)
                resources.loadTexture("res/images/PlatformerPack/Player/p1_walk/PNG/p1_walk0" + i + ".png");
            else
                resources.loadTexture("res/images/PlatformerPack/Player/p1_walk/PNG/p1_walk" + i + ".png");
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
        TextureRegion[] sprites = new TextureRegion[11];
        for(int i=0;i<11;i++) {
            sprites[i] = TextureRegion.split(tex[i], 72, 97)[0][0];
            System.out.println("split " + (i + 1/50f));
        }
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

        animation = new Animation(sprites);
        animation.setFrames(sprites, 1/15f);
        width = sprites[0].getRegionWidth();
        height = sprites[0].getRegionHeight();

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

    public boolean managescore(){

        scorespeedVar+=1;
        if(scorespeedVar==scorespeedConst) {
            scorespeedVar=0;
            return true;


        }
        return false;



    }

}
