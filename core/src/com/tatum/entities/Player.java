package com.tatum.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.tatum.Game;
import com.tatum.handlers.Content;

public class Player extends B2DSprite {
    private Content cont;
    private int numCrystals;
    private int totalCrystals;

    public Player(Game game, Body body) {
        super(body);
        this.cont = game.get_content();

        Texture tex = cont.getTexture("bunny");
        TextureRegion[] sprites = new TextureRegion[4];
        for(int i = 0; i < sprites.length; i++) {
            sprites[i] = new TextureRegion(tex, i * 32, 0, 32, 32);
        }

        animation.setFrames(sprites, 1 / 12f);

        width = sprites[0].getRegionWidth();
        height = sprites[0].getRegionHeight();

    }

    public void collectCrystal() { numCrystals++; }
    public int getNumCrystals() { return numCrystals; }

    public void setTotalCrystals(int i) { totalCrystals = i; }
    public int getTotalCrystals() { return totalCrystals; }

}
