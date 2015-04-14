package com.tatum;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import com.badlogic.gdx.input.GestureDetector;
import com.tatum.handlers.*;
import com.tatum.handlers.Input;
import com.tatum.handlers.InputProcessor;
import com.tatum.states.Play;

public class Game extends ApplicationAdapter {
    public static final String TITLE = "Tatum";
    public static final int SCALE = 2;
    public static final float STEP = 1/60f;
    public final int width;
    public final int height;
    private final TwitterInterface twitterInterface;
    private SpriteBatch sb;
    private BoundedCamera cam;
    private OrthographicCamera hudCam;
    private GameStateManager gsm;
    private static ContentManager resources;
    private String[] data;
    private TatumDirectionListener tatumDirectionListener;
    private final InputProcessor inputProcessor = new InputProcessor();

    public Game(String[] data, TwitterInterface twitterInterface){
        super();
        this.twitterInterface = twitterInterface;
        this.data=data;
        width=320;
        height=240;

    }

    public Game(TwitterInterface twitterInterface){
        this.twitterInterface = twitterInterface;
        width = 320;//Gdx.graphics.getWidth();
        height = 240; //Gdx.graphics.getHeight();
    }

    @Override
    public void create () {
        resources = new ContentManager();
        //Cameras
        cam = new BoundedCamera();
        cam.setToOrtho(false, width, height);
        hudCam = new OrthographicCamera();
        hudCam.setToOrtho(false, width, height);
        //
        sb = new SpriteBatch();
        gsm = new GameStateManager(this);
    }

    public SpriteBatch getSpriteBatch() {
        return sb;
    }

    public OrthographicCamera getHUDCamera() {
        return hudCam;
    }

    public BoundedCamera getCamera() {
        return cam;
    }

    @Override
    public void render () {
        gsm.update(Gdx.graphics.getDeltaTime());
        gsm.render();
        Input.update();
    }

    @Override
    public void dispose() {
        if(resources != null)
            resources.removeAll();
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public ContentManager getResources(){
        return resources;
    }

    public String[] getData(){
        return data;
    }

    public void getParent(){

    }
    public SimpleDirectionGestureDetector setSwipeInput(Play play){
        tatumDirectionListener = new TatumDirectionListener();
        SimpleDirectionGestureDetector temp2 = new SimpleDirectionGestureDetector(tatumDirectionListener, play);
        Gdx.input.setInputProcessor(temp2);
        return temp2;
    }   //create the swipe listener so the player can use the swipe in the play state

    public void setTouchInput(){
        Input.down = false;
        Gdx.input.setInputProcessor(inputProcessor);
    }

    public TwitterInterface getTwitterInterface(){
        return twitterInterface;
    }

    public TatumDirectionListener getTatumDirectionListener(){
        return tatumDirectionListener;
    }
}