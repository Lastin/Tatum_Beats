package com.tatum.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import com.tatum.Game;
import com.tatum.handlers.B2DVars;
import com.tatum.handlers.ContentManager;

public class HUD {
    private Player player;
    private ContentManager cont;
    private Game game;
    private TextureRegion container;
    private TextureRegion[] blocks;
    private TextureRegion crystal;
    private TextureRegion[] font;

    public HUD(ContentManager cont, Game game, Player player) {
        this.game = game;
        this.cont = cont;
        this.player = player;
        cont.loadTexture("res/images/hud.png");
        Texture tex = cont.getTexture("hud");
        container = new TextureRegion(tex, 0, 0, 32, 32);

        blocks = new TextureRegion[3];
        for(int i = 0; i < blocks.length; i++) {
            blocks[i] = new TextureRegion(tex, 32 + i * 16, 0, 16, 16);
        }

        crystal = new TextureRegion(tex, 80, 0, 16, 16);

        font = new TextureRegion[11];
        for(int i = 0; i < 6; i++) {
            font[i] = new TextureRegion(tex, 32 + i * 9, 16, 9, 9);
        }
        for(int i = 0; i < 5; i++) {
            font[i + 6] = new TextureRegion(tex, 32 + i * 9, 25, 9, 9);
        }

    }

    public void render(SpriteBatch sb) {
        sb.begin();
        sb.draw(container, 32, game.getHeight()-50);
        short bits = player.getBody().getFixtureList().first().getFilterData().maskBits;
        if ((bits & B2DVars.BIT_RED_BLOCK) != 0) {
            sb.draw(blocks[0], 40, game.getHeight()-42);
        }
        else if ((bits & B2DVars.BIT_GREEN_BLOCK) != 0) {
            sb.draw(blocks[1], 40, game.getHeight()-42);
        }
        else if ((bits & B2DVars.BIT_BLUE_BLOCK) != 0) {
            sb.draw(blocks[2], 40, game.getHeight()-42);
        }
        sb.draw(crystal, game.getWidth()-50, game.getHeight()-50);
        drawString(sb, player.getNumCrystals() + " / " + player.getTotalCrystals(), game.getWidth()-132, game.getHeight()-45);
        sb.end();

    }

    private void drawString(SpriteBatch sb, String s, float x, float y) {
        for(int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if(c == '/') c = 10;
            else if(c >= '0' && c <= '9') c -= '0';
            else continue;
            sb.draw(font[c], x + i * 9, y);
        }
    }

    public TextureRegion getCrystal() {
        return crystal;
    }
    public TextureRegion[] getBlocks() {
        return blocks;
    }
}
