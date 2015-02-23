package com.tatum;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import com.tatum.handlers.*;

public class Game extends ApplicationAdapter {
    public static final String TITLE = "Tatum";
    public static final int SCALE = 2;
    public static final float STEP = 1/60f;
    public final int width;
    public final int height;
    private SpriteBatch sb;
    private BoundedCamera cam;
    private OrthographicCamera hudCam;
    private GameStateManager gsm;
    private static ContentManager resources;
    private String[] data;
    private TatumDirectionListener tatumDirectionListener;

    public Game(String[] data){
        super();
        this.data=data;
        width=320;
        height=240;

    }
    public Game(){
        width = 320;//Gdx.graphics.getWidth();
        height = 240; //Gdx.graphics.getHeight();
    }

    @Override
    public void create () {
        Gdx.input.setInputProcessor(new InputProcessor());

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
    }
    public void setTouchInput(){

        Gdx.input.setInputProcessor(new InputProcessor());
    }
    public TatumDirectionListener getTatumDirectionListener(){
        return tatumDirectionListener;
    }
}