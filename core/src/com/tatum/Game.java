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
    private int width;
    private int height;
	private SpriteBatch sb;
	private BoundedCamera cam;
    private OrthographicCamera hudCam;
    private GameStateManager gsm;
	private static ContentManager cont;

	@Override
	public void create () {
        width = Gdx.graphics.getWidth();
        height = Gdx.graphics.getHeight();
        Gdx.input.setInputProcessor(new InputProcessor());
        cont = new ContentManager();
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
        if(cont != null)
            cont.removeAll();
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public ContentManager get_content(){
        return cont;
    }
}
