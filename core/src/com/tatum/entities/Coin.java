package com.tatum.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Affine2;
import com.badlogic.gdx.physics.box2d.Body;
import com.tatum.handlers.B2DVars;
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
        } //load in the coin texture for the passed colour

        TextureRegion[] sprites = new TextureRegion[1];
        sprites[0] = TextureRegion.split(tex, 35, 35)[0][0]; // cut out coin texture and set in sprites
        animation.setFrames(sprites, 1 / 12f); //set animation to appease render method
        width = sprites[0].getRegionWidth();
        height = sprites[0].getRegionHeight();
    }
    public boolean doRender(){
        return doRender;
    }
    public void collected(){
        doRender=false;
    } // checks if the coin has been collected by the player, because it should not be rendered after this
    @Override
    public void render(SpriteBatch sb) {
        sb.begin();
        sb.draw(animation.getFrame(), (body.getPosition().x * B2DVars.PPM - width / 2), (int) (body.getPosition().y * B2DVars.PPM - height / 2),  17.5f, 17.5f, 35, 35, 0.5f, 0.5f, 0f);
        //scale down to 0.5 and reposition as default coins were too big
        sb.end();
    }
}
