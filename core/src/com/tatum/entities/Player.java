package com.tatum.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.tatum.handlers.Animation;
import com.tatum.handlers.ContentManager;

public class Player extends B2DSprite {
    private int numCrystals;
    private int totalCrystals;

    public Player(Body body, ContentManager cont) {
        super(body, cont);
        cont.loadTexture("res/images/bunny.png");
        Texture tex = cont.getTexture("bunny");
        TextureRegion[] sprites = TextureRegion.split(tex, 32, 32)[0];
        animation = new Animation(sprites);
        animation.setFrames(sprites, 1 / 12f);
        width = sprites[0].getRegionWidth();
        height = sprites[0].getRegionHeight();

    }

    public void collectCrystal() { numCrystals++; }
    public int getNumCrystals() { return numCrystals; }

    public void setTotalCrystals(int i) { totalCrystals = i; }
    public int getTotalCrystals() { return totalCrystals; }

}
