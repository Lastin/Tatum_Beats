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
    private Rotation rotation;
    private boolean swapper;
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
        right = "P_RIGHT"; //set all directions to default
        loadSkins();    //load in characters to display on arrow
        this.play = play;
        this.paceMaker = paceMaker;
        rotation = Rotation.NONE; //set rotation to none by default
        swapper = false;
    }
    private void loadSkins(){
        Texture tex;
        //load in all walking and jumping textures and save into walk/jump array
        for(int i =0; i <3; i++) {
            cont.loadTexture("res/images/PlatformerPack/Player/icons/p" + (i + 1) + "_icon.png");
            tex = cont.getTexture("p"+(i+1)+"_icon");
            walking[i] = TextureRegion.split(tex, 22, 29)[0][0];
            cont.loadTexture("res/images/PlatformerPack/Player/icons/p"+(i+1)+"_icon_jump.png");
            tex = cont.getTexture("p"+(i+1)+"_icon_jump");
            jumping[i] = TextureRegion.split(tex, 20, 28)[0][0];
        }
        //duck done separately as player 2 duck is one pixel short
        cont.loadTexture("res/images/PlatformerPack/Player/icons/p1_icon_duck.png");
        tex = cont.getTexture("p1_icon_duck");
        ducking[0] = TextureRegion.split(tex, 21, 22)[0][0];

        cont.loadTexture("res/images/PlatformerPack/Player/icons/p2_icon_duck.png");
        tex = cont.getTexture("p2_icon_duck");
        ducking[1] = TextureRegion.split(tex, 20, 22)[0][0];

        cont.loadTexture("res/images/PlatformerPack/Player/icons/p3_icon_duck.png");
        tex = cont.getTexture("p3_icon_duck");
        ducking[2] = TextureRegion.split(tex, 21, 22)[0][0];

        //load in instructor texture
        cont.loadTexture("res/images/instructor.png");
        tex = cont.getTexture("instructor");
        instructor = TextureRegion.split(tex,77,77)[0][0];
    }
    public void render(SpriteBatch sb){
        sb.draw(instructor,xPos,yPos);
        drawTop(sb);//check what up arrow is set to and draws char accordingly
        drawBot(sb); //checks down arrow
        drawLeft(sb); // left arrow
        drawRight(sb); // right arrow
    }   //render instructor and all 4 characters
    private void drawTop(SpriteBatch sb){
        if(top.equals("P_RIGHT")){ //if up arrow is set to charchange right
            if(player.getPlayerNum() == 1){ // if character green draw blue guy
                sb.draw(walking[1],xPos+28,yPos+47);
            }
            else if(player.getPlayerNum() == 2){ // blue draw red guy
                sb.draw(walking[2],xPos+28,yPos+47);
            }
            else{ // red draw green guy
                sb.draw(walking[0],xPos+28,yPos+47);
            }
        }
        else if (top.equals("P_LEFT")){ // if up arrow is set to char change left
            if(player.getPlayerNum() == 1){ // if green draw red guy
                sb.draw(walking[2],xPos+28,yPos+47);
            }
            else if(player.getPlayerNum() == 2){ //blue draw green guy
                sb.draw(walking[0],xPos+28,yPos+47);
            }
            else{ // red draw blue guy
                sb.draw(walking[1],xPos+28,yPos+47);
            }
        }
        else if (top.equals("P_DUCK")){ // if up arrow is set to duck
            if(player.getPlayerNum() == 1){ //green draw green ducking
                sb.draw(ducking[0],xPos+28,yPos+53);
            }
            else if(player.getPlayerNum() == 2){ // blue draw blue ducking
                sb.draw(ducking[1],xPos+28,yPos+53);
            }
            else{   // red draw red ducking
                sb.draw(ducking[2],xPos+28,yPos+53);
            }
        }
        else{ // else if up arrow set to jumping
            if(player.getPlayerNum() == 1){ // if char green draw green jumping
                sb.draw(jumping[0],xPos+29,yPos+47);
            }
            else if(player.getPlayerNum() == 2){ // if char blue draw blue jumping
                sb.draw(jumping[1],xPos+29,yPos+47);
            }
            else{ // if char red draw red jumping
                sb.draw(jumping[2],xPos+29,yPos+47);
            }
        }
    }
    //these methods do the exact same as above but for the down arrow, left arrow and right arrow
    //respectively. The only difference is the position that they are rendered in
    // to allow them to be drawn onto the correct arrow
    private void drawBot(SpriteBatch sb){
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
    private void drawLeft(SpriteBatch sb){
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
    private void drawRight(SpriteBatch sb){
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

    //these methods are called when a users swipes up, down, left, right respectively
    // they then interact with the player and perform the users action
    public void doTop(){
        if(top.equals("P_RIGHT")){ //checks if the up arrow is set to change char to the right
            player.randomSprite(); // changes char to the right
        }
        else if (top.equals("P_LEFT")){ // check if the up arrow is set to change char to the left
            player.randomSpriteReverse(); // set the player model to the let
        }
        else if (top.equals("P_DUCK")){ // check if the up arrow is set to make the character duck
            player.setCrouchSkin(paceMaker.getLastBeatHitId()); // make the char duck
        }
        else{
            play.playerJump(); // else must be set to jump - make player jump
        }
        checkRotation(); // check if we need to rotate the instructor
    }
    //these methods do the exact same as above but for the down arrow, left arrow and right arrow
    //respectively.
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

        checkRotation();
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

        checkRotation();
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

        checkRotation();
    }

    //these methods change the arrow instruction values around in several ways, each explained by
    //the signature of the method
    private void rotateClockWise(){
        String tempTop   = top;
        String tempBot   = bot;
        String tempLeft  = left;
        String tempRight = right;
        top   = tempLeft;
        right = tempTop;
        bot   = tempRight;
        left  =  tempBot;
    }
    private void rotateCounterClockWise(){
        String tempTop   = top;
        String tempBot   = bot;
        String tempLeft  = left;
        String tempRight = right;
        top   = tempRight;
        right = tempBot;
        bot   = tempLeft;
        left  =  tempTop;
    }
    private void swapHorizontal(){

        String tempLeft  = left;
        String tempRight = right;
        right = tempLeft;
        left  = tempRight;
    }
    private void swapVertical(){
        String tempTop = top;
        String tempBot = bot;
        top = tempBot;
        bot = tempTop;
    }
    private void swapBoth(){
        swapHorizontal();
        swapVertical();
    }
    private void botRight(){
        String tempRight = right;
        String tempBot = bot;
        bot = tempRight;
        right = tempBot;
    }
    private void botLeft(){
        String tempLeft = left;
        String tempBot = bot;
        left = tempBot;
        bot = tempLeft;
    }
    private void topRight(){
        String tempTop = top;
        String tempRight = right;
        top = tempRight;
        right = tempTop;
    }
    private void topLeft(){
        String tempTop = top;
        String tempLeft = left;
        top = tempLeft;
        left = tempTop;
    }
    private void top(){
        if(swapper){
            topLeft();
            swapper=!swapper;
        }
        else {
            topRight();
            swapper=!swapper;
        }
    }
    private void bot(){
        if(swapper){
            botLeft();
            swapper=!swapper;
        }
        else {
            botRight();
            swapper=!swapper;
        }
    }
    private void randomise(){
        Random random = new Random();
        int rotate = random.nextInt(3);
        int flip = random.nextInt(4);
        int corner = random.nextInt(4);
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
        if(corner==0)
            botRight();
        else if (corner==1)
            botLeft();
        else if (corner==2)
            topRight();
        else
            topLeft();

    }

    private void checkRotation(){
        // in here we check the rotation that has been set by the user
        // based on the value of the stored enum and call the equivalent method
        switch (rotation){
            case NONE:
                //do nothing
                break;
            case COUNTER_CLOCKWISE:
                rotateCounterClockWise();
                break;
            case CLOCKWISE:
                rotateClockWise();
                break;
            case SWAP_HORIZONTAL:
                swapHorizontal();
                break;
            case SWAP_VERTICAL:
                swapVertical();
                break;
            case INVERT:
                swapBoth();
                break;
            case RANDOMISE:
                randomise();
                break;
            case BOTTOM:
                bot();
                break;
            case TOP:
                top();
                break;
        }
    }
    public enum Rotation {
        // for each of the rotations available to the user
        // we create an enum which can then be used to choose the rotation
        NONE,COUNTER_CLOCKWISE,CLOCKWISE, SWAP_HORIZONTAL,
        SWAP_VERTICAL,INVERT, RANDOMISE, BOTTOM,
        TOP
    }
    public void setRotation(Rotation r){
        rotation=r;
    }
}
