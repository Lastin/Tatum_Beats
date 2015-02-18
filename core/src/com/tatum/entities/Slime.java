package com.tatum.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.tatum.handlers.ContentManager;

/**
 * Created by Ben on 18/02/2015.
 */
public class Slime extends B2DSprite {

    public Slime(Body body, ContentManager cont) {
        super(body, cont);
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
}
