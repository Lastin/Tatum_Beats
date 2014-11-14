package com.tatum;
//libgdx stuff
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
//local stuff
import com.tatum.handlers.*;

public class Game extends ApplicationAdapter {
    public static final String TITLE = "Tatum";
    public static int v_width;
    public static int v_height;
    public static final int SCALE = 2;
    public static final float STEP = 1/60f;
	private SpriteBatch batch;
	private BoundedCamera cam;
    private OrthographicCamera hudCam;
    //private GameStateManager gsm;
	private static Content cont;

	@Override
	public void create () {
        v_width = Gdx.graphics.getWidth();
        v_height = Gdx.graphics.getHeight();
        Gdx.input.setInputProcessor(new InputProcessor());
        cont = new Content();
        //Initialise cameras
        cam = new BoundedCamera();
        cam.setToOrtho(false, v_width, v_height);
        //hudCam = new OrthographicCamera();
        //hudCam.setToOrtho(false, v_width, v_height);
        //
        batch = new SpriteBatch();
        //cam = new BoundedCamera();

	}

	@Override
	public void render () {

	}
	@Override
	public void dispose(){
		if(cont != null)
            cont.removeAll();
	}
}
