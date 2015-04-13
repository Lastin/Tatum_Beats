package com.tatum.entities;

/**
 * Created by Ben on 05/04/2015.
 */

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.tatum.Game;
import com.tatum.handlers.ContentManager;
import com.tatum.handlers.FontGenerator;
import com.tatum.handlers.PaceMaker;
import com.tatum.states.Play;

import java.util.Random;

/**
 * Created by Ben on 21/03/2015.
 */
public class Instructor2 {

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
    private  FontGenerator fontGenerator;
    private TextureRegion[] coins;
    private TextureRegion arrow;
    private boolean debug = true;
    public Instructor2(ContentManager cont, Game game, Player player, PaceMaker paceMaker, Play play){

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
        fontGenerator = new FontGenerator();


    }
    private void loadSkins(){
        Texture tex;
        //load in all walking and jumping textures and save into walk/jump array

        coins = new TextureRegion[3];
        cont.loadTexture("res/images/PlatformerPack/Items/BlueCoin.png");
        tex = cont.getTexture("BlueCoin");

        coins[1] = TextureRegion.split(tex, 35, 35)[0][0];
        cont.loadTexture("res/images/PlatformerPack/Items/GreenCoin.png");
        tex = cont.getTexture("GreenCoin");

        coins[0] = TextureRegion.split(tex, 35, 35)[0][0];
        cont.loadTexture("res/images/PlatformerPack/Items/PinkCoin.png");
        tex = cont.getTexture("PinkCoin");

        coins[2] = TextureRegion.split(tex, 35, 35)[0][0];

        cont.loadTexture("res/images/PlatformerPack/arrow.png");
        tex = cont.getTexture("arrow");
        arrow = TextureRegion.split(tex,50,50)[0][0];
    }
    public void render(SpriteBatch sb){
        drawTitles(sb);
        drawJump(sb);//check what up arrow is set to and draws char accordingly
        drawDuck(sb); //checks down arrow
        if(debug){
            System.out.println(top);
            System.out.println(bot);
            System.out.println(left);
            System.out.println(right);
            System.out.println("");
            debug=false;
        }
        drawLeft(sb); // left arrow
        drawRight(sb); // right arrow
    }   //render instructor and all 4 characters
    private void drawTitles(SpriteBatch sb){
        //fontGenerator.getScoreFont().draw(sb, "Swipe", 70,game.getHeight()-10); //render the Current highscore for the song
        fontGenerator.getScoreFont().draw(sb, "Jump", game.getWidth()-150,game.getHeight()-10); //render the Current highscore for the song
        fontGenerator.getScoreFont().draw(sb, "Duck", game.getWidth()-103,game.getHeight()-10); //render the Current highscore for the song
        if(player.getPlayerNum() == 1){ // if green draw red guy
            sb.draw(coins[2],game.getWidth() - 64 , game.getHeight()-30 ,  17.5f, 17.5f, 35, 35, 0.5f, 0.5f, 0f);
            sb.draw(coins[1],game.getWidth() - 32 , game.getHeight()-30,  17.5f, 17.5f, 35, 35, 0.5f, 0.5f, 0f);

        }
        else if(player.getPlayerNum() == 2){ //blue draw green guy
            sb.draw(coins[0],game.getWidth() - 64 , game.getHeight()- 30 ,  17.5f, 17.5f, 35, 35, 0.5f, 0.5f, 0f);
            sb.draw(coins[2],game.getWidth() - 32 , game.getHeight()-30 ,  17.5f, 17.5f, 35, 35, 0.5f, 0.5f, 0f);
        }
        else{ // red draw blue guy
            sb.draw(coins[1],game.getWidth() - 64, game.getHeight()-30 ,  17.5f, 17.5f, 35, 35, 0.5f, 0.5f, 0f);
            sb.draw(coins[0],game.getWidth() - 32 , game.getHeight()-30 ,  17.5f, 17.5f, 35, 35, 0.5f, 0.5f, 0f);
        }

    }
    private void drawJump(SpriteBatch sb){
        if(top.equals("P_RIGHT")){ //if up arrow is set to charchange right
            sb.draw(arrow,game.getWidth() - 155 , game.getHeight()-56 ,  25f, 25f, 50, 50, 0.5f, 0.5f, 90f);
        }
        else if (top.equals("P_LEFT")){ // if up arrow is set to char change left
            sb.draw(arrow,game.getWidth() - 155 , game.getHeight()-56 ,  25f, 25f, 50, 50, 0.5f, 0.5f, 270f);
        }
        else if (top.equals("P_DUCK")){ // if up arrow is set to duck
            sb.draw(arrow,game.getWidth() - 155 , game.getHeight()-56 ,  25f, 25f, 50, 50, 0.5f, 0.5f, 180f);
        }
        else{ // else if up arrow set to jumping
            sb.draw(arrow,game.getWidth() - 155 , game.getHeight()-56 ,  25f, 25f, 50, 50, 0.5f, 0.5f, 0f);
        }
    }
    //these methods do the exact same as above but for the down arrow, left arrow and right arrow
    //respectively. The only difference is the position that they are rendered in
    // to allow them to be drawn onto the correct arrow
    private void drawDuck(SpriteBatch sb){
        if(bot.equals("P_RIGHT")){
            sb.draw(arrow,game.getWidth() - 110 , game.getHeight()-56 ,  25f, 25f, 50, 50, 0.5f, 0.5f, 90f);
        }
        else if (bot.equals("P_LEFT")){
            sb.draw(arrow,game.getWidth() - 110 , game.getHeight()-56 ,  25f, 25f, 50, 50, 0.5f, 0.5f, 270f);
        }
        else if (bot.equals("P_DUCK")){
            sb.draw(arrow,game.getWidth() - 110 , game.getHeight()-56 ,  25f, 25f, 50, 50, 0.5f, 0.5f, 180f);
        }
        else{
            sb.draw(arrow,game.getWidth() - 110 , game.getHeight()-56 ,  25f, 25f, 50, 50, 0.5f, 0.5f, 0f);
        }
    }
    private void drawLeft(SpriteBatch sb){
        if(left.equals("P_RIGHT")){
            sb.draw(arrow,game.getWidth() - 40 , game.getHeight()-56 ,  25f, 25f, 50, 50, 0.5f, 0.5f, 90f);
        }
        else if (left.equals("P_LEFT")){
            sb.draw(arrow,game.getWidth() - 40 , game.getHeight()-56 ,  25f, 25f, 50, 50, 0.5f, 0.5f, 270f);
        }
        else if (left.equals("P_DUCK")){
            sb.draw(arrow,game.getWidth() - 40 , game.getHeight()-56 ,  25f, 25f, 50, 50, 0.5f, 0.5f, 180f);
        }
        else{
            sb.draw(arrow,game.getWidth() - 40 , game.getHeight()-56 ,  25f, 25f, 50, 50, 0.5f, 0.5f, 0f);
        }
    }
    private void drawRight(SpriteBatch sb){
        if(right.equals("P_RIGHT")){
            sb.draw(arrow,game.getWidth() - 72 , game.getHeight()-56 ,  25f, 25f, 50, 50, 0.5f, 0.5f, 90f);
        }
        else if (right.equals("P_LEFT")){
            sb.draw(arrow,game.getWidth() - 72 , game.getHeight()-56 ,  25f, 25f, 50, 50, 0.5f, 0.5f, 270f);
        }
        else if (right.equals("P_DUCK")){
            sb.draw(arrow,game.getWidth() - 72 , game.getHeight()-56 ,  25f, 25f, 50, 50, 0.5f, 0.5f, 180f);
        }
        else{
            sb.draw(arrow,game.getWidth() - 72 , game.getHeight()-56 ,  25f, 25f, 50, 50, 0.5f, 0.5f, 0f);
        }
    }

    //these methods are called when a users swipes up, down, left, right respectively
    // they then interact with the player and perform the users action
    public void doTop(){
        debug=true;
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
        debug=true;
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
        debug=true;
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
        debug=true;
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
