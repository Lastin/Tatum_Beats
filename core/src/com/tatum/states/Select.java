package com.tatum.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
// stage
// finish
import com.tatum.Game;
import com.tatum.handlers.Background;
import com.tatum.handlers.SelectionHandler;
import com.tatum.handlers.ContentManager;
import com.tatum.handlers.GameButton;
import com.tatum.handlers.GameStateManager;
import com.tatum.handlers.LevelGenerator;

/**
 * Created by Ben on 24/01/2015.
 */
public class Select extends GameState {

    private boolean debug = false;
    private Background bg;
    private World world;
    private Box2DDebugRenderer b2dRenderer;

    private LevelGenerator levelGenerator;
    private String musicSelectionPath;
    private SelectionHandler selectionHandler;
    private ContentManager cont;
    private GameButton[] gameButtons;
    private GameButton upButton;
    private GameButton downButton;

    public Select(GameStateManager gsm) {
        super(gsm);
        levelGenerator = new LevelGenerator(resources);
        selectionHandler = new SelectionHandler(Gdx.files.external(""));
        gameButtons = new GameButton[5];
        Texture menu = resources.getTexture("menu2");
        bg = new Background(game, new TextureRegion(menu), cam, 1f);
        bg.setVector(-20, 0);

        cont = gsm.getGame().getResources();
        cont.loadTexture("res/images/buttondown.png");
        cont.loadTexture("res/images/buttonup.png");
        Texture downArrow = cont.getTexture("buttondown");
        Texture upArrow = cont.getTexture("buttonup");
        upButton = new GameButton(resources, new TextureRegion(upArrow,70,70), game.getWidth()-30, game.getHeight()-50, cam);
        downButton = new GameButton(resources, new TextureRegion(downArrow,70,70), game.getWidth()-30, game.getHeight()-100, cam);
        Texture hud = resources.getTexture("hud2");
        Texture myStyle = resources.getTexture("sprites");


        cam.setToOrtho(false, game.getWidth(), game.getHeight());
        world = new World(new Vector2(0, -9.8f * 5), true);
        //world = new World(new Vector2(10, 10), true);
        b2dRenderer = new Box2DDebugRenderer();

    }


    @Override
    public void handleInput() {

        if(upButton.isClicked()){

        }
        if(downButton.isClicked()){

        }
        
    }

    @Override
    public void update(float dt) {
        handleInput();
        world.step(dt / 5, 8, 3);
        bg.update(dt);
        upButton.update(dt);
        downButton.update(dt);
    }

    @Override
    public void render() {

        sb.setProjectionMatrix(cam.combined);
        bg.render(sb);

        upButton.render(sb);
        downButton.render(sb);
        sb.begin();
        sb.end();

    }

    @Override
    public void dispose() {

    }

}
