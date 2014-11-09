package com.neet.blockbunny.main;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.echonest.api.v4.EchoNestException;
import com.neet.blockbunny.handlers.BBInput;
import com.neet.blockbunny.handlers.BBInputProcessor;
import com.neet.blockbunny.handlers.BoundedCamera;
import com.neet.blockbunny.handlers.Content;
import com.neet.blockbunny.handlers.GameStateManager;

public class Game implements ApplicationListener {
	
	public static final String TITLE = "Block Bunny";
	public static final int V_WIDTH = 320;
	public static final int V_HEIGHT = 240;
	public static final int SCALE = 2;
	public static final float STEP = 1 / 60f;
	
	private SpriteBatch sb;
	private BoundedCamera cam;
	private OrthographicCamera hudCam;

    private MetaGrabber meta;
	private GameStateManager gsm;
	
	public static Content res;
	
	public void create() {
		
		//Texture.setEnforcePotImages(false);
		Gdx.input.setInputProcessor(new BBInputProcessor());
		
		res = new Content();
		res.loadTexture("res/images/menu.png");
		res.loadTexture("res/images/bgs.png");
		res.loadTexture("res/images/hud.png");
		res.loadTexture("res/images/bunny.png");
		res.loadTexture("res/images/crystal.png");
		res.loadTexture("res/images/spikes.png");
		
		res.loadSound("res/sfx/jump.wav");
		res.loadSound("res/sfx/crystal.wav");
		res.loadSound("res/sfx/levelselect.wav");
		res.loadSound("res/sfx/hit.wav");
		res.loadSound("res/sfx/changeblock.wav");
		
		res.loadMusic("test.mp3");
///*
        try {
            final MetaGrabber meta2;
            meta2 = new MetaGrabber();
            Thread thread = new Thread(){
                    public void run(){
                    meta2.upload();
            }
            };
            thread.run();
            while(meta2.getUploadProgress().equals("pending")){
                if(meta2.getUploadProgress().equals("error")){
                    System.out.println("ERROR");
                }
            }
            meta2.setMeta();
            meta = meta2;
        } catch (EchoNestException e) {
            e.printStackTrace();
        }
//*/
        cam = new BoundedCamera();
		cam.setToOrtho(false, V_WIDTH, V_HEIGHT);
		hudCam = new OrthographicCamera();
		hudCam.setToOrtho(false, V_WIDTH, V_HEIGHT);
		
		sb = new SpriteBatch();
		
		gsm = new GameStateManager(this);
		
	}
	
	public void render() {
		
		Gdx.graphics.setTitle(TITLE + " -- FPS: " + Gdx.graphics.getFramesPerSecond());
		
		gsm.update(Gdx.graphics.getDeltaTime());
		gsm.render();
		BBInput.update();
		
	}
	
	public void dispose() {
		res.removeAll();
	}
	
	public void resize(int w, int h) {}
	
	public void pause() {}
	
	public void resume() {}

    public MetaGrabber getMeta(){
        return meta;
    }
	
	public SpriteBatch getSpriteBatch() { return sb; }
	public BoundedCamera getCamera() { return cam; }
	public OrthographicCamera getHUDCamera() { return hudCam; }
	
}
