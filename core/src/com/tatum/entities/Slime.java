package com.tatum.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.tatum.handlers.ContentManager;
import com.tatum.music.TrackData;

/**
 * Created by Ben on 18/02/2015.
 */
public class Slime extends B2DSprite {


    public Slime(Body body, ContentManager cont,String theme) {
        super(body, cont);

        switch(theme){
            case ("pop"):
                pop();
                break;
            case ("rock"):
                rock();
                break;
            case ("indie"):
                indie();
                break;
            case ("jazz"):
                jazz();
                break;
            case ("asian"):
                asian();
                break;
            case ("metal"):
                metal();
                break;
            case ("death-metal"):
                deathMetal();
                break;
            case ("hip-hop"):
                hipHop();
                break;
            case ("punk"):
                punk();
                break;
            case ("classical"):
                classical();
                break;
            case ("electronic"):
                electronic();
                break;

        }

    }
    public void pop(){
        cont.loadTexture("res/images/PlatformerPack/Enemies/ground/fence.png");
        Texture tex = cont.getTexture("fence");
        TextureRegion[] sprites = new TextureRegion[2];
        sprites[0] = TextureRegion.split(tex, 35, 35)[0][0];
        sprites[1] = TextureRegion.split(tex, 35, 35)[0][0];
        animation.setFrames(sprites, 1 / 12f);
        width = sprites[0].getRegionWidth();
        height = sprites[0].getRegionHeight();
    }
    public void rock(){

    }
    public void indie(){
        cont.loadTexture("res/images/PlatformerPack/Enemies/ground/fence.png");
        Texture tex = cont.getTexture("fence");
        TextureRegion[] sprites = new TextureRegion[2];
        sprites[0] = TextureRegion.split(tex, 35, 35)[0][0];
        sprites[1] = TextureRegion.split(tex, 35, 35)[0][0];
        animation.setFrames(sprites, 1 / 12f);
        width = sprites[0].getRegionWidth();
        height = sprites[0].getRegionHeight();
    }
    public void jazz(){
        cont.loadTexture("res/images/PlatformerPack/Enemies/slime.png");
        cont.loadTexture("res/images/PlatformerPack/Enemies/slime_walk.png");

        Texture tex = cont.getTexture("slime");
        TextureRegion[] sprites = new TextureRegion[2];
        sprites[0] = TextureRegion.split(tex, 25, 17)[0][0];
        tex = cont.getTexture("slime_walk");
        sprites[1] = TextureRegion.split(tex, 29, 15)[0][0];
        animation.setFrames(sprites, 1 / 12f);
        width = sprites[0].getRegionWidth();
        height = sprites[0].getRegionHeight();
    }
    public void asian(){

    }
    public void metal(){
        cont.loadTexture("res/images/PlatformerPack/Enemies/ground/spinnerHalf.png");
        cont.loadTexture("res/images/PlatformerPack/Enemies/ground/spinnerHalf_spin.png");

        Texture tex = cont.getTexture("spinnerHalf");
        TextureRegion[] sprites = new TextureRegion[2];
        sprites[0] = TextureRegion.split(tex, 32, 16)[0][0];
        tex = cont.getTexture("spinnerHalf_spin");
        sprites[1] = TextureRegion.split(tex, 31, 15)[0][0];
        animation.setFrames(sprites, 1 / 12f);
        width = sprites[0].getRegionWidth();
        height = sprites[0].getRegionHeight();
    }
    public void deathMetal(){
        cont.loadTexture("res/images/PlatformerPack/Enemies/ground/death_ground.png");
        cont.loadTexture("res/images/PlatformerPack/Enemies/ground/death_spikes.png");

        Texture tex = cont.getTexture("death_spikes");
        TextureRegion[] sprites = new TextureRegion[2];
        sprites[0] = TextureRegion.split(tex, 35, 20)[0][0];
        tex = cont.getTexture("death_spikes");
        sprites[1] = TextureRegion.split(tex, 35, 20)[0][0];
        animation.setFrames(sprites, 1 / 12f);
        width = sprites[0].getRegionWidth();
        height = sprites[0].getRegionHeight();
    }
    public void hipHop(){

    }
    public void punk(){
        cont.loadTexture("res/images/PlatformerPack/Enemies/ground/slimeRed.png");
        cont.loadTexture("res/images/PlatformerPack/Enemies/ground/slimeRed_walk.png");

        Texture tex = cont.getTexture("slimeRed");
        TextureRegion[] sprites = new TextureRegion[2];
        sprites[0] = TextureRegion.split(tex, 25, 17)[0][0];
        tex = cont.getTexture("slimeRed_walk");
        sprites[1] = TextureRegion.split(tex, 29, 15)[0][0];
        animation.setFrames(sprites, 1 / 12f);
        width = sprites[0].getRegionWidth();
        height = sprites[0].getRegionHeight();
    }
    public void classical(){
        //cont.loadTexture("res/images/PlatformerPack/Enemies/ground/fence.png");
        cont.loadTexture("res/images/PlatformerPack/Enemies/ground/signGround.png");
        //Texture tex = cont.getTexture("fence");
        Texture tex = cont.getTexture("signGround");
        TextureRegion[] sprites = new TextureRegion[2];
        sprites[0] = TextureRegion.split(tex, 35, 35)[0][0];
        sprites[1] = TextureRegion.split(tex, 35, 35)[0][0];
        animation.setFrames(sprites, 1 / 12f);
        width = sprites[0].getRegionWidth();
        height = sprites[0].getRegionHeight();
    }
    public void electronic(){
        cont.loadTexture("res/images/PlatformerPack/Enemies/ground/slimeBlue.png");
        cont.loadTexture("res/images/PlatformerPack/Enemies/ground/slimeBlue_walk.png");

        Texture tex = cont.getTexture("slimeBlue");
        TextureRegion[] sprites = new TextureRegion[2];
        sprites[0] = TextureRegion.split(tex, 25, 17)[0][0];
        tex = cont.getTexture("slimeBlue_walk");
        sprites[1] = TextureRegion.split(tex, 29, 15)[0][0];
        animation.setFrames(sprites, 1 / 12f);
        width = sprites[0].getRegionWidth();
        height = sprites[0].getRegionHeight();
    }
}
