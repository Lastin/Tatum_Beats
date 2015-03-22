package com.tatum.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.tatum.Game;
import com.tatum.handlers.ContentManager;
import com.tatum.handlers.PaceMaker;
import com.tatum.states.Play;

import java.util.Random;

/**
 * Created by Ben on 21/03/2015.
 */
public class Instructor {

    private ContentManager cont;
    private Game game;
    private Player player;
    private TextureRegion[] walking;
    private TextureRegion[] jumping;
    private TextureRegion[] ducking;
    private TextureRegion instructor;
    private int xPos =20;
    private int yPos = 160;
    private String top;
    private String bot;
    private String left;
    private String right;
    private PaceMaker paceMaker;
    private Play play;
    public Instructor(ContentManager cont, Game game, Player player, PaceMaker paceMaker, Play play){

        this.cont=cont;
        this.game=game;
        this.player=player;
        walking = new TextureRegion[3];
        jumping = new TextureRegion[3];
        ducking = new TextureRegion[3];
        top = "P_TOP";
        bot = "P_DUCK";
        left = "P_LEFT";
        right = "P_RIGHT";
        loadSkins();
        this.play = play;
        this.paceMaker = paceMaker;
    }
    public void loadSkins(){
        Texture tex;
        for(int i =0; i <3; i++) {
            cont.loadTexture("res/images/PlatformerPack/Player/icons/p" + (i + 1) + "_icon.png");
            tex = cont.getTexture("p"+(i+1)+"_icon");
            walking[i] = TextureRegion.split(tex, 22, 29)[0][0];
            cont.loadTexture("res/images/PlatformerPack/Player/icons/p"+(i+1)+"_icon_jump.png");
            tex = cont.getTexture("p"+(i+1)+"_icon_jump");
            jumping[i] = TextureRegion.split(tex, 20, 28)[0][0];
        }
        cont.loadTexture("res/images/PlatformerPack/Player/icons/p1_icon_duck.png");
        tex = cont.getTexture("p1_icon_duck");
        ducking[0] = TextureRegion.split(tex, 21, 22)[0][0];

        cont.loadTexture("res/images/PlatformerPack/Player/icons/p2_icon_duck.png");
        tex = cont.getTexture("p2_icon_duck");
        ducking[1] = TextureRegion.split(tex, 20, 22)[0][0];

        cont.loadTexture("res/images/PlatformerPack/Player/icons/p3_icon_duck.png");
        tex = cont.getTexture("p3_icon_duck");
        ducking[2] = TextureRegion.split(tex, 21, 22)[0][0];

        cont.loadTexture("res/images/instructor.png");
        tex = cont.getTexture("instructor");
        instructor = TextureRegion.split(tex,77,77)[0][0];
    }
    public void render(SpriteBatch sb){
        sb.draw(instructor,xPos,yPos);
        drawTop(sb);
        drawBot(sb);
        drawLeft(sb);
        drawRight(sb);
    }
    public void drawTop(SpriteBatch sb){
        if(top.equals("P_RIGHT")){
            if(player.getPlayerNum() == 1){
                sb.draw(walking[1],xPos+28,yPos+47);
            }
            else if(player.getPlayerNum() == 2){
                sb.draw(walking[2],xPos+28,yPos+47);
            }
            else{
                sb.draw(walking[0],xPos+28,yPos+47);
            }
        }
        else if (top.equals("P_LEFT")){
            if(player.getPlayerNum() == 1){
                sb.draw(walking[2],xPos+28,yPos+47);
            }
            else if(player.getPlayerNum() == 2){
                sb.draw(walking[0],xPos+28,yPos+47);
            }
            else{
                sb.draw(walking[1],xPos+28,yPos+47);
            }
        }
        else if (top.equals("P_DUCK")){
            if(player.getPlayerNum() == 1){
                sb.draw(ducking[0],xPos+28,yPos+53);
            }
            else if(player.getPlayerNum() == 2){
                sb.draw(ducking[1],xPos+28,yPos+53);
            }
            else{
                sb.draw(ducking[2],xPos+28,yPos+53);
            }
        }
        else{
            if(player.getPlayerNum() == 1){
                sb.draw(jumping[0],xPos+29,yPos+47);
            }
            else if(player.getPlayerNum() == 2){
                sb.draw(jumping[1],xPos+29,yPos+47);
            }
            else{
                sb.draw(jumping[2],xPos+29,yPos+47);
            }
        }
    }
    public void drawBot(SpriteBatch sb){
        if(bot.equals("P_RIGHT")){
            if(player.getPlayerNum() == 1){
                sb.draw(walking[1],xPos+28,yPos+1);
            }
            else if(player.getPlayerNum() == 2){
                sb.draw(walking[2],xPos+28,yPos+1);
            }
            else{
                sb.draw(walking[0],xPos+28,yPos+1);
            }
        }
        else if (bot.equals("P_LEFT")){
            if(player.getPlayerNum() == 1){
                sb.draw(walking[2],xPos+28,yPos+1);
            }
            else if(player.getPlayerNum() == 2){
                sb.draw(walking[0],xPos+28,yPos+1);
            }
            else{
                sb.draw(walking[1],xPos+28,yPos+1);
            }
        }
        else if (bot.equals("P_DUCK")){
            if(player.getPlayerNum() == 1){
                sb.draw(ducking[0],xPos+29,yPos+2);
            }
            else if(player.getPlayerNum() == 2){
                sb.draw(ducking[1],xPos+29,yPos+2);
            }
            else{
                sb.draw(ducking[2],xPos+29,yPos+2);
            }
        }
        else{
            if(player.getPlayerNum() == 1){
                sb.draw(jumping[0],xPos+28,yPos+1);
            }
            else if(player.getPlayerNum() == 2){
                sb.draw(jumping[1],xPos+28,yPos+1);
            }
            else{
                sb.draw(jumping[2],xPos+28,yPos+1);
            }
        }
    }
    public void drawLeft(SpriteBatch sb){
        if(left.equals("P_RIGHT")){
            if(player.getPlayerNum() == 1){
                sb.draw(walking[1],xPos+1,yPos+24);
            }
            else if(player.getPlayerNum() == 2){
                sb.draw(walking[2],xPos+1,yPos+24);
            }
            else{
                sb.draw(walking[0],xPos+1,yPos+24);
            }
        }
        else if (left.equals("P_LEFT")){
            if(player.getPlayerNum() == 1){
                sb.draw(walking[2],xPos+1,yPos+24);
            }
            else if(player.getPlayerNum() == 2){
                sb.draw(walking[0],xPos+1,yPos+24);
            }
            else{
                sb.draw(walking[1],xPos+1,yPos+24);
            }
        }
        else if (left.equals("P_DUCK")){
            if(player.getPlayerNum() == 1){
                sb.draw(ducking[0],xPos+2,yPos+26);
            }
            else if(player.getPlayerNum() == 2){
                sb.draw(ducking[1],xPos+2,yPos+26);
            }
            else{
                sb.draw(ducking[2],xPos+2,yPos+26);
            }
        }
        else{
            if(player.getPlayerNum() == 1){
                sb.draw(jumping[0],xPos+2,yPos+24);
            }
            else if(player.getPlayerNum() == 2){
                sb.draw(jumping[1],xPos+2,yPos+24);
            }
            else{
                sb.draw(jumping[2],xPos+2,yPos+24);
            }
        }
    }
    public void drawRight(SpriteBatch sb){
        if(right.equals("P_RIGHT")){
            if(player.getPlayerNum() == 1){
                sb.draw(walking[1],xPos+54,yPos+24);
            }
            else if(player.getPlayerNum() == 2){
                sb.draw(walking[2],xPos+54,yPos+24);
            }
            else{
                sb.draw(walking[0],xPos+54,yPos+24);
            }
        }
        else if (right.equals("P_LEFT")){
            if(player.getPlayerNum() == 1){
                sb.draw(walking[2],xPos+54,yPos+24);
            }
            else if(player.getPlayerNum() == 2){
                sb.draw(walking[0],xPos+54,yPos+24);
            }
            else{
                sb.draw(walking[1],xPos+54,yPos+24);
            }
        }
        else if (right.equals("P_DUCK")){
            if(player.getPlayerNum() == 1){
                sb.draw(ducking[0],xPos+55,yPos+26);
            }
            else if(player.getPlayerNum() == 2){
                sb.draw(ducking[1],xPos+55,yPos+26);
            }
            else{
                sb.draw(ducking[2],xPos+55,yPos+26);
            }
        }
        else{
            if(player.getPlayerNum() == 1){
                sb.draw(jumping[0],xPos+55,yPos+24);
            }
            else if(player.getPlayerNum() == 2){
                sb.draw(jumping[1],xPos+55,yPos+24);
            }
            else{
                sb.draw(jumping[2],xPos+55,yPos+24);
            }
        }
    }

    public void doTop(){
        if(top.equals("P_RIGHT")){
            player.randomSprite();
        }
        else if (top.equals("P_LEFT")){
            player.randomSpriteReverse();
        }
        else if (top.equals("P_DUCK")){
            player.setCrouchSkin(paceMaker.getLastBeatHitId());
        }
        else{
            play.playerJump();
        }
    }
    public void doBot(){
        if(bot.equals("P_RIGHT")){
            player.randomSprite();
        }
        else if (bot.equals("P_LEFT")){
            player.randomSpriteReverse();
        }
        else if (bot.equals("P_DUCK")){
            player.setCrouchSkin(paceMaker.getLastBeatHitId());
        }
        else{
            play.playerJump();
        }
    }
    public void doLeft(){
        if(left.equals("P_RIGHT")){
            player.randomSprite();
        }
        else if (left.equals("P_LEFT")){
            player.randomSpriteReverse();
        }
        else if (left.equals("P_DUCK")){
            player.setCrouchSkin(paceMaker.getLastBeatHitId());
        }
        else{
            play.playerJump();
        }
    }
    public void doRight(){
        if(right.equals("P_RIGHT")){
            player.randomSprite();
        }
        else if (right.equals("P_LEFT")){
            player.randomSpriteReverse();
        }
        else if (right.equals("P_DUCK")){
            player.setCrouchSkin(paceMaker.getLastBeatHitId());
        }
        else{
            play.playerJump();
        }
    }
    public void rotateClockWise(){
        String tempTop   = top;
        String tempBot   = bot;
        String tempLeft  = left;
        String tempRight = right;
        top   = tempLeft;
        right = tempTop;
        bot   = tempRight;
        left  =  tempBot;
    }
    public void rotateCounterClockWise(){
        String tempTop   = top;
        String tempBot   = bot;
        String tempLeft  = left;
        String tempRight = right;
        top   = tempRight;
        right = tempBot;
        bot   = tempLeft;
        left  =  tempTop;
    }
    public void swapHorizontal(){

        String tempLeft  = left;
        String tempRight = right;
        right = tempLeft;
        left  = tempRight;
    }
    public void swapVertical(){
        String tempTop = top;
        String tempBot = bot;
        top = tempBot;
        bot = tempTop;
    }
    public void swapBoth(){
        swapHorizontal();
        swapVertical();
    }
    public void randomise(){
        Random random = new Random();
        int rotate = random.nextInt(3);
        int flip = random.nextInt(4);
        if(rotate==0)
            rotateClockWise();
        else if (rotate==1)
            rotateCounterClockWise();
        if(flip==0)
            swapVertical();
        else if(flip==1)
            swapHorizontal();
        else if(flip==2)
            swapBoth();
    }
}
