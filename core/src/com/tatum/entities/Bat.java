package com.tatum.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.tatum.handlers.ContentManager;

/**
 * Created by Ben on 18/02/2015.
 */
public class Bat extends B2DSprite {

    public Bat(Body body, ContentManager cont) {
        super(body, cont);
        cont.loadTexture("res/images/PlatformerPack/Enemies/bat.png");
        cont.loadTexture("res/images/PlatformerPack/Enemies/bat_fly.png");
        Texture tex = cont.getTexture("bat");
        TextureRegion[] sprites = new TextureRegion[2];
        sprites[0] = TextureRegion.split(tex, 35, 24)[0][0];
        tex = cont.getTexture("bat_fly");
        sprites[1] = TextureRegion.split(tex, 44, 19)[0][0];
        animation.setFrames(sprites, 1 / 12f);
        width = sprites[0].getRegionWidth();
        height = sprites[0].getRegionHeight();

    }
}
