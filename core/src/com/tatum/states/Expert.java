package com.tatum.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.tatum.handlers.Background;
import com.tatum.handlers.ContentManager;
import com.tatum.handlers.FontGenerator;
import com.tatum.handlers.GameButton;
import com.tatum.handlers.GameStateManager;
import com.tatum.handlers.InputProcessor;
import com.tatum.music.MusicItem;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Ben on 02/04/2015.
 */
public class Expert extends GameState{

    private boolean debug = false;
    private Background bg;
    private World world;
    private Box2DDebugRenderer b2dRenderer;
    private ContentManager cont;
    private FontGenerator fontGenerator;
    private MusicItem twist;
    private MusicItem yes;
    private MusicItem no;
    private String path;


    public Expert(GameStateManager gsm, Background bg,String path) {
        super(gsm);
        fontGenerator = new FontGenerator();
        this.bg = bg;
        cont = gsm.getGame().getResources();

        Gdx.app.postRunnable(new Runnable() {
            @Override
            public void run() {
                setText();
            }
        }); // creates the "Go play first" string (in separate thread to improve performance)

        cam.setToOrtho(false, game.getWidth(), game.getHeight());
        world = new World(new Vector2(0, -9.8f * 5), true);
        b2dRenderer = new Box2DDebugRenderer();
        game.setTouchInput(); // change input to touch incase has come from game and input set to swipe
        this.path= path;
    }
    public void setText(){
        BitmapFont font;
        int size = 70;
        float widthA = 0;
        float widthS = 0;
        float widthL = 0;
        font = fontGenerator.makeFont(size, Color.BLACK);
        widthS = font.getBounds("Yes!").width;
        widthL = font.getBounds("No!").width;
        do {
            size -= 5;
            font = fontGenerator.makeFont(size, Color.BLACK);
            widthA = font.getBounds("Do you want to play on expert Mode?").width;
        } while(widthA > 300f);
        float middle = game.getWidth()/2;
        twist = new MusicItem(sb, font,"Do you want to play on expert Mode?",cam, (int)(middle - widthA/2),game.getHeight()-30);
        font = fontGenerator.makeFont(70, Color.BLACK);
        yes =  new MusicItem(sb, font,"Yes!",cam, (int)(middle - widthS/2),game.getHeight()-70);
        no =  new MusicItem(sb, font,"No!",cam, (int)(middle - widthL/2),game.getHeight()-130);
    }
    @Override
    public void handleInput() {
        if(yes.isClicked()){
            gsm.setState(new Menu(gsm,path,bg,true));
        }
        if(no.isClicked()){
            gsm.setState(new Menu(gsm,path,bg,false));
        }
    }

    @Override
    public void update(float dt) {
        handleInput();
        world.step(dt / 5, 8, 3);
        bg.update(dt);
        twist.update(dt);
        yes.update(dt);
        no.update(dt);
    }

    @Override
    public void render() {
        sb.setProjectionMatrix(cam.combined);
        bg.render(sb);

        if(twist!=null&&yes!=null&no!=null){
            twist.renderFull();
            yes.render();
            no.render();
        }
    }

    @Override
    public void dispose() {

    }
}
