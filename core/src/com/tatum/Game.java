package com.tatum;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import com.tatum.handlers.*;
import com.tatum.handlers.Input;
import com.tatum.handlers.InputProcessor;

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
    private com.badlogic.gdx.InputProcessor inputProcessor;

    public Game(String[] data, TwitterInterface twitterInterface){
        super();
        this.twitterInterface = twitterInterface;
        this.data=data;
        width=320;
        height=240;
        this.inputProcessor = new InputProcessor();

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
    public void setSwipeInput(){
        tatumDirectionListener = new TatumDirectionListener();
        SimpleDirectionGestureDetector temp2 = new SimpleDirectionGestureDetector(tatumDirectionListener);
        Gdx.input.setInputProcessor(temp2);
    }   //create the swipe listener so the player can use the swipe in the play state

    public void setTouchInput(){
        Gdx.input.setInputProcessor(inputProcessor);
    }

    public TwitterInterface getTwitterInterface(){
        return twitterInterface;
    }

    public TatumDirectionListener getTatumDirectionListener(){
        return tatumDirectionListener;
    }
}