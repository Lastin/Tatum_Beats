package com.tatum.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.tatum.Game;
import com.tatum.handlers.Content;

public class Crystal extends B2DSprite {
    private Content cont;
    public Crystal(Game game, Body body) {
        super(body);
        cont = game.get_content();
        Texture tex = cont.getTexture("crystal");
        TextureRegion[] sprites = TextureRegion.split(tex, 16, 16)[0];
        animation.setFrames(sprites, 1 / 30f);

        width = sprites[0].getRegionWidth();
        height = sprites[0].getRegionHeight();

    }

}
