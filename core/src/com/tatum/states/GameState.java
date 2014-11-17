package com.tatum.states;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

//local stuff
import com.tatum.handlers.*;
import com.tatum.Game;

public abstract class GameState {
    protected GameStateManager gsm;
    protected Game game;
    protected SpriteBatch sb;
    protected BoundedCamera cam;
    protected OrthographicCamera hudCam;

    public GameState(GameStateManager gsm) {
        this.gsm = gsm;
        game = gsm.getGame();
        sb = game.getSpriteBatch();
        cam = game.getCamera();
        hudCam = game.getHUDCamera();
    }

    public abstract void handleInput();
    public abstract void update(float dt);
    public abstract void render();
    public abstract void dispose();
}
