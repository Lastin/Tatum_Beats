package com.tatum.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import com.tatum.Game;
import com.tatum.handlers.B2DVars;
import com.tatum.handlers.ContentManager;
import com.tatum.handlers.FontGenerator;
import com.tatum.handlers.PaceMaker;
import com.tatum.states.Play;

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
    private PaceMaker paceMaker;
    private int timePoint;
    private int timeSig;
    private int currbeat;
    private Instructor2 instructor;
    private boolean debug = false;
    private FontGenerator fontGenerator;


    public HUD(ContentManager cont, FontGenerator fontGenerator, Game game, Player player, PaceMaker paceMaker, Play play) {
        this.fontGenerator = fontGenerator;
        this.game = game;
        this.cont = cont;
        this.player = player;
        Texture tex = cont.getTexture("hud2");
        container = new TextureRegion(tex, 0, 0, 32, 32);
        cont.loadTexture("res/images/letters.png");
        //create new instructor
        instructor = new Instructor2(cont,game,player,paceMaker,play);

        //load in textures for debug information
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
        //get time signature and store pacemaker
        timeSig=paceMaker.getTimeSig();
        timePoint=1;
        this.paceMaker=paceMaker;
        currbeat=paceMaker.getLastBeatHitId();

    }

    public void render(SpriteBatch sb) {
        sb.begin();
        instructor.render(sb);
        //fontGenerator.getScoreFont().draw(sb, "HIGHSCORE: " + player.getHighScore(), game.getWidth()-170,game.getHeight()-10); //render the Current highscore for the song
        fontGenerator.getScoreFont().draw(sb, "SCORE: " + player.getScore()+" x "+player.getMultiplyer(), 10,game.getHeight()-10); //renders the users current score and multiplier
        //fontGenerator.getScoreFont().draw(sb, "SCORE: " + "100000"+" x "+"100", 10,game.getHeight()-10); //renders the users current score and multiplier
        int space = 0;
        for(int i = 0;i<player.getStep();i++){
            if(i>4)
                break;
            sb.draw(blockSprites[0], 35 + space, game.getHeight() - 30);
            space+=15;
        } // renders the blocks that represent how many beats till an increase in multiplier
        if(debug) { // debug info, no longer used
            // X Y Z data
            String temp = game.getData()[1].replaceAll("\\.", " ");
            drawString(sb, "x " + temp, game.getWidth() - 132, game.getHeight() - 80);
            temp = game.getData()[2].replaceAll("\\.", " ");
            drawString(sb, "y " + temp, game.getWidth() - 132, game.getHeight() - 100);
            temp = game.getData()[3].replaceAll("\\.", " ");
            drawString(sb, "z " + temp, game.getWidth() - 132, game.getHeight() - 120);

            //Beat Bar Section
            temp = String.valueOf(paceMaker.getLastBeatHitId());
            drawString(sb, "beat " + temp, 30, game.getHeight() - 80);

             if (timePoint <= timeSig) {
                if (currbeat < Integer.parseInt(temp)) {
                    timePoint++;
                    currbeat = Integer.parseInt(temp);
                }
            } else {
                timePoint = 1;
            }
            drawString(sb, "beat out of " + timePoint + " " + timeSig, 30, game.getHeight() - 100);
            temp = String.valueOf(paceMaker.getLastBarHitId());
            drawString(sb, "bar " + temp, 30, game.getHeight() - 120);
            temp = String.valueOf(paceMaker.getLastSectionHitId());
            drawString(sb, "section "+ temp,30,game.getHeight() - 140);


        }
        sb.end();
    }

    private void drawString(SpriteBatch sb, String s, float x, float y) { //old text method used in debug
        for(int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if(c == '/') c = 10;
            else if(c >= '0' && c <= '9') c -= '0';
            else if(c ==' ') continue;
            else if(c== '-') continue;
            else {
                sb.draw(fullFont[c-97], x + i * 9, y);
                continue;
            }
            sb.draw(font[c], x + i * 9, y);
        }
    }
    public void setPaceMaker(PaceMaker paceMaker){this.paceMaker=paceMaker;}
    public Instructor2 getInstructor(){
        return instructor;
    }
}
