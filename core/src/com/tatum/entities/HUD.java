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
    private TextureRegion[] fullFont;
    TextureRegion[] blockSprites;

    public HUD(ContentManager cont, Game game, Player player) {
        this.game = game;
        this.cont = cont;
        this.player = player;
        cont.loadTexture("res/images/hud2.png");
        Texture tex = cont.getTexture("hud2");
        container = new TextureRegion(tex, 0, 0, 32, 32);
        cont.loadTexture("res/images/letters.png");
        Texture letter = cont.getTexture("letters");
        fullFont = new TextureRegion[26];
        TextureRegion[][] regi = TextureRegion.split(letter,9,9);
        int count = 0;
        for (int i =0; i<10;i++){
            fullFont[count] = regi[0][i];
            count++;
        }
        for(int i =0;i<10;i++){
            fullFont[count]= regi[1][i];
            count++;
        }
        for(int i =0;i<6;i++){
            fullFont[count]= regi[2][i];
            count++;
        }

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
        blockSprites = new TextureRegion[3];
        for(int i = 0; i < blockSprites.length; i++) {
            blockSprites[i] = new TextureRegion(tex, 58 + i * 5, 34, 5, 5);
        }
    }

    public void render(SpriteBatch sb) {
        sb.begin();
        sb.draw(container, 32, game.getHeight()-50);
        short bits = player.getBody().getFixtureList().first().getFilterData().maskBits;
        if ((bits & B2DVars.BIT_GRASS_BLOCK) != 0) {
            sb.draw(blocks[0], 40, game.getHeight()-42);
        }
        else if ((bits & B2DVars.BIT_ICE_BLOCK) != 0) {
            sb.draw(blocks[1], 40, game.getHeight()-42);
        }
        else if ((bits & B2DVars.BIT_SAND_BLOCK) != 0) {
            sb.draw(blocks[2], 40, game.getHeight()-42);
        }
        //draw crystals
        //sb.draw(crystal, game.getWidth()-50, game.getHeight()-50);
        //drawString(sb, player.getNumCrystals() + " / " + player.getTotalCrystals(), game.getWidth()-132, game.getHeight()-45);
        drawString(sb,"highscore "+ String.valueOf(player.getHighScore()),game.getWidth()-132,game.getHeight()-10);
        drawString(sb,"score "+ String.valueOf(player.getScore()),game.getWidth()-132,game.getHeight()-30);
        drawString(sb,"multiplier "+ String.valueOf(player.getMultiplyer()),game.getWidth()-132,game.getHeight()-50);
        int space = 0;
        //System.out.println("here");
        for(int i = 0;i<player.getStep();i++){
            sb.draw(blockSprites[0], game.getWidth()-110+space,game.getHeight()-60);
            space+=15;
        }
        String temp = game.getData()[1].replaceAll("\\.", " ");
        drawString(sb,"x "+ temp,game.getWidth()-200,game.getHeight()-70);
        temp = game.getData()[2].replaceAll("\\.", " ");
        drawString(sb,"y "+ temp,game.getWidth()-200,game.getHeight()-90);
        temp = game.getData()[3].replaceAll("\\.", " ");
        drawString(sb,"z "+ temp,game.getWidth()-200,game.getHeight()-110);
        sb.end();
    }

    private void drawString(SpriteBatch sb, String s, float x, float y) {
        for(int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if(c == '/') c = 10;
            else if(c >= '0' && c <= '9') c -= '0';
            else if(c ==' ') continue;
            else if(c== '-') continue;
            else {
            //    System.out.println(c);
            //    System.out.println(c-97);
                sb.draw(fullFont[c-97], x + i * 9, y);
                //sb.draw(fullFont[24], x + i * 9, y);
                continue;
            }
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
