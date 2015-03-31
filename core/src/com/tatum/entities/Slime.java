package com.tatum.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.tatum.handlers.B2DVars;
import com.tatum.handlers.ContentManager;
import com.tatum.music.TrackData;

/**
 * Created by Ben on 18/02/2015.
 */
public class Slime extends B2DSprite {
    private static TextureRegion[] sprites;
    String theme;
    //the comments in this class are minimal as everything is explained in bat (as the two are basically identical)
    //the only difference being that the textures in slimes are placed onto the floor in game not flying above it
    public Slime(Body body, ContentManager cont,String theme) {
        super(body, cont);
        this.theme=theme;
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
        cont.loadTexture("res/images/PlatformerPack/Enemies/ground/rock_rock.png");
        Texture tex = cont.getTexture("rock_rock");
        TextureRegion[] sprites = new TextureRegion[2];
        sprites[0] = TextureRegion.split(tex, 35, 23)[0][0];
        sprites[1] = TextureRegion.split(tex, 35, 23)[0][0];
        animation.setFrames(sprites, 1 / 12f);
        width = sprites[0].getRegionWidth();
        height = sprites[0].getRegionHeight();
    }
    public void indie(){
        if(this.sprites==null){
            cont.loadTexture("res/images/PlatformerPack/Enemies/ground/cactus.png");
            Texture tex = cont.getTexture("cactus");
            TextureRegion[] sprites = new TextureRegion[44];
            TextureRegion[][] temp = TextureRegion.split(tex, 157, 126);

            int count = 0;
            for(int i = 0;i<8;i++){
                for(int j = 0; j <6; j++){
                    if(i==7&&j==2)
                        break;
                    sprites[count]= temp[i][j];
                    count++;
                }
            }
            this.sprites = sprites;
        }
        animation.setFrames(sprites, 1 / 30f);
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
        cont.loadTexture("res/images/PlatformerPack/Enemies/ground/rock_snowy.png");
        Texture tex = cont.getTexture("rock_snowy");
        TextureRegion[] sprites = new TextureRegion[2];
        sprites[0] = TextureRegion.split(tex, 35, 26)[0][0];
        sprites[1] = TextureRegion.split(tex, 35, 26)[0][0];
        animation.setFrames(sprites, 1 / 12f);
        width = sprites[0].getRegionWidth();
        height = sprites[0].getRegionHeight();
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
        cont.loadTexture("res/images/PlatformerPack/Enemies/ground/hip-hop-jump.png");
        Texture tex = cont.getTexture("hip-hop-jump");
        TextureRegion[] sprites = new TextureRegion[2];
        sprites[0] = TextureRegion.split(tex, 124, 111)[0][0];
        sprites[1] = TextureRegion.split(tex, 124, 111)[0][0];
        animation.setFrames(sprites, 1 / 12f);
        width = sprites[0].getRegionWidth();
        height = sprites[0].getRegionHeight();
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
    @Override
    public void render(SpriteBatch sb) {
        if(theme.equals("hip-hop")){
            sb.begin();
            sb.draw(animation.getFrame(), (body.getPosition().x * B2DVars.PPM - width / 2), (int) (body.getPosition().y * B2DVars.PPM - height / 2),  65f, 62f, 124, 111, 0.35f, 0.35f, 0f);
            sb.end();
        }
        else if(theme.equals("indie")){
            sb.begin();
            sb.draw(animation.getFrame(), (body.getPosition().x * B2DVars.PPM - width / 2), (int) (body.getPosition().y * B2DVars.PPM - height / 2),  81f, 80f, 157, 126, 0.35f, 0.35f, 0f);
            sb.end();
        }
        else {
            sb.begin();
            sb.draw(animation.getFrame(), (body.getPosition().x * B2DVars.PPM - width / 2), (int) (body.getPosition().y * B2DVars.PPM - height / 2));
            sb.end();
        }
    }
}
