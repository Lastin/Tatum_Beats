package com.tatum.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.tatum.handlers.ContentManager;

/**
 * Created by Ben on 22/02/2015.
 */
public class Coin extends B2DSprite {
    private boolean doRender;
    public Coin(Body body, ContentManager cont,String Colour) {
        super(body, cont);
        Texture tex;
        doRender=true;

        if(Colour.equals("Blue")) {
            cont.loadTexture("res/images/PlatformerPack/Items/BlueCoin.png");
            tex = cont.getTexture("BlueCoin");

        }
        else if(Colour.equals("Green")) {
            cont.loadTexture("res/images/PlatformerPack/Items/GreenCoin.png");
            tex = cont.getTexture("GreenCoin");
        }
        else {
            cont.loadTexture("res/images/PlatformerPack/Items/PinkCoin.png");
            tex = cont.getTexture("PinkCoin");
        }
        TextureRegion[] sprites = new TextureRegion[1];
        sprites[0] = TextureRegion.split(tex, 35, 35)[0][0];
        animation.setFrames(sprites, 1 / 12f);
        width = sprites[0].getRegionWidth();
        height = sprites[0].getRegionHeight();
    }
    public boolean doRender(){
        return doRender;
    }
    public void collected(){
        doRender=false;
    }
}
