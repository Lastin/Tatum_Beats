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
    private int vWidth;
    private int vHeight;
	private SpriteBatch sb;
	private BoundedCamera cam;
    private OrthographicCamera hudCam;
    private GameStateManager gsm;
	private static ContentManager cont;

	@Override
	public void create () {
        vWidth = 320;//Gdx.graphics.getWidth();
        vHeight = 240;//Gdx.graphics.getHeight();
        Gdx.input.setInputProcessor(new InputProcessor());
        cont = new ContentManager();
        //Cameras
        cam = new BoundedCamera();
        cam.setToOrtho(false, vWidth, vHeight);
        hudCam = new OrthographicCamera();
        hudCam.setToOrtho(false, vWidth, vHeight);
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
        if(cont != null)
            cont.removeAll();
    }

    public int get_width() {
        return vWidth;
    }

    public int get_height() {
        return vHeight;
    }

    public ContentManager get_content(){
        return cont;
    }
}
