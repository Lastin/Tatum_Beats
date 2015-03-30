package com.tatum.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.tatum.handlers.B2DVars;
import com.tatum.handlers.ContentManager;

/**
 * Created by Ben on 18/02/2015.
 */
public class Bat extends B2DSprite {
    String theme;
    private static TextureRegion[] sprites;
    public Bat(Body body, ContentManager cont, String theme) {
        super(body, cont);
        this.theme = theme;
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
        cont.loadTexture("res/images/PlatformerPack/Enemies/flying/fly.png");
        cont.loadTexture("res/images/PlatformerPack/Enemies/flying/fly_fly.png");
        Texture tex = cont.getTexture("fly");
        TextureRegion[] sprites = new TextureRegion[2];
        sprites[0] = TextureRegion.split(tex, 36, 18)[0][0];
        tex = cont.getTexture("fly_fly");
        sprites[1] = TextureRegion.split(tex, 38, 16)[0][0];
        animation.setFrames(sprites, 1 / 12f);
        width = sprites[0].getRegionWidth();
        height = sprites[0].getRegionHeight();
    }
    public void rock(){
        cont.loadTexture("res/images/PlatformerPack/Enemies/flying/hangingRock.png");
        Texture tex = cont.getTexture("hangingRock");
        TextureRegion[] sprites = new TextureRegion[2];
        sprites[0] = TextureRegion.split(tex, 35, 35)[0][0];
        sprites[1] = TextureRegion.split(tex, 35, 35)[0][0];
        animation.setFrames(sprites, 1 / 6f);
        width = sprites[0].getRegionWidth();
        height = sprites[0].getRegionHeight();
    }
    public void indie(){
        if(this.sprites==null) {
            cont.loadTexture("res/images/PlatformerPack/Enemies/flying/butterfly.png");
            Texture tex = cont.getTexture("butterfly");
            TextureRegion[] sprites = new TextureRegion[84];
            TextureRegion[][] temp = TextureRegion.split(tex, 70, 65);

            int count = 0;
            for (int i = 0; i < 6; i++) {
                for (int j = 13; j >= 0; j--) {
                    sprites[count] = temp[i][j];
                    count++;
                }
            }
            this.sprites = sprites;
        }
        animation.setFrames(sprites, 1 / 35f);
        width = sprites[0].getRegionWidth();
        height = sprites[0].getRegionHeight();
    }
    public void jazz(){
        cont.loadTexture("res/images/PlatformerPack/Enemies/flying/boxExplosivePurple.png");
        Texture tex = cont.getTexture("boxExplosivePurple");
        TextureRegion[] sprites = new TextureRegion[2];
        sprites[0] = TextureRegion.split(tex, 30, 30)[0][0];
        sprites[1] = TextureRegion.split(tex, 30, 30)[0][0];
        animation.setFrames(sprites, 1 / 12f);
        width = sprites[0].getRegionWidth();
        height = sprites[0].getRegionHeight();
    }
    public void asian(){
        cont.loadTexture("res/images/PlatformerPack/Enemies/flying/kpop_duck.png");
        Texture tex = cont.getTexture("kpop_duck");
        TextureRegion[] sprites = new TextureRegion[2];
        sprites[0] = TextureRegion.split(tex, 45,27)[0][0];
        sprites[1] = TextureRegion.split(tex, 45, 27)[0][0];
        animation.setFrames(sprites, 1 / 12f);
        width = sprites[0].getRegionWidth();
        height = sprites[0].getRegionHeight();
    }
    public void metal(){
        cont.loadTexture("res/images/PlatformerPack/Enemies/flying/spinner.png");
        cont.loadTexture("res/images/PlatformerPack/Enemies/flying/spinner_spin.png");
        Texture tex = cont.getTexture("spinner");
        TextureRegion[] sprites = new TextureRegion[2];
        sprites[0] = TextureRegion.split(tex, 32, 31)[0][0];
        tex = cont.getTexture("spinner_spin");
        sprites[1] = TextureRegion.split(tex, 31, 31)[0][0];
        animation.setFrames(sprites, 1 / 12f);
        width = sprites[0].getRegionWidth();
        height = sprites[0].getRegionHeight();
    }
    public void deathMetal(){
        cont.loadTexture("res/images/PlatformerPack/Enemies/flying/death_ghost.png");
        Texture tex = cont.getTexture("death_ghost");
        TextureRegion[] sprites = new TextureRegion[2];
        sprites[0] = TextureRegion.split(tex, 22, 45)[0][0];
        tex = cont.getTexture("death_ghost");
        sprites[1] = TextureRegion.split(tex, 22, 45)[0][0];
        animation.setFrames(sprites, 1 / 6f);
        width = sprites[0].getRegionWidth();
        height = sprites[0].getRegionHeight();
    }
    public void hipHop(){
        cont.loadTexture("res/images/PlatformerPack/Enemies/flying/firefly.png");
        Texture tex = cont.getTexture("firefly");
        TextureRegion[] sprites = new TextureRegion[40];
        TextureRegion[][] temp = TextureRegion.split(tex, 87, 40);

        int count = 0;
        for(int i = 0;i<4;i++){
            for(int j = 0; j <10; j++){
                sprites[count]= temp[i][j];
                count++;
            }
        }

        animation.setFrames(sprites, 1 / 35f);
        width = sprites[0].getRegionWidth();
        height = sprites[0].getRegionHeight();
    }
    public void punk(){
        cont.loadTexture("res/images/PlatformerPack/Enemies/flying/boxExplosive.png");
        Texture tex = cont.getTexture("boxExplosive");
        TextureRegion[] sprites = new TextureRegion[2];
        sprites[0] = TextureRegion.split(tex, 30, 30)[0][0];
        sprites[1] = TextureRegion.split(tex, 30, 30)[0][0];
        animation.setFrames(sprites, 1 / 12f);
        width = sprites[0].getRegionWidth();
        height = sprites[0].getRegionHeight();
    }
    public void classical(){
        cont.loadTexture("res/images/PlatformerPack/Enemies/flying/duckSign.png");
        Texture tex = cont.getTexture("duckSign");
        TextureRegion[] sprites = new TextureRegion[2];
        sprites[0] = TextureRegion.split(tex, 35, 35)[0][0];
        tex = cont.getTexture("duckSign");
        sprites[1] = TextureRegion.split(tex, 35, 35)[0][0];
        animation.setFrames(sprites, 1 / 12f);
        width = sprites[0].getRegionWidth();
        height = sprites[0].getRegionHeight();
    }
    public void electronic(){
        cont.loadTexture("res/images/PlatformerPack/Enemies/flying/boxExplosiveBlue.png");
        Texture tex = cont.getTexture("boxExplosiveBlue");
        TextureRegion[] sprites = new TextureRegion[2];
        sprites[0] = TextureRegion.split(tex, 30, 30)[0][0];
        sprites[1] = TextureRegion.split(tex, 30, 30)[0][0];
        animation.setFrames(sprites, 1 / 12f);
        width = sprites[0].getRegionWidth();
        height = sprites[0].getRegionHeight();
    }

    public void render(SpriteBatch sb) {
        if(theme.equals("hip-hop")){
            sb.begin();
            sb.draw(animation.getFrame(), (body.getPosition().x * B2DVars.PPM - width / 2), (int) (body.getPosition().y * B2DVars.PPM - height / 2));
            sb.end();
        }
        else if(theme.equals("indie")){
            sb.begin();
            sb.draw(animation.getFrame(), (body.getPosition().x * B2DVars.PPM - width / 2), (int) (body.getPosition().y * B2DVars.PPM - height / 2),  35f, 20f, 70, 65, 0.80f, 0.80f, 0f);
            sb.end();
        }

        else {
            sb.begin();
            sb.draw(animation.getFrame(), (body.getPosition().x * B2DVars.PPM - width / 2), (int) (body.getPosition().y * B2DVars.PPM - height / 2));
            sb.end();
        }
    }

    }
