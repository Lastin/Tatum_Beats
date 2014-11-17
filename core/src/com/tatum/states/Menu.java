package com.tatum.states;

import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
//local stuff
import com.tatum.handlers.*;

public class Menu extends GameState{
    private Content cont;
    //private Background bg;
    //private Animation anim;
    private GameButton playButton;
    //private World world;
    private Box2DDebugRenderer b2dRenderer;
    public Menu(GameStateManager gsm) {
        super(gsm);
        loadContent();
    }
    private void loadContent(){
        cont = new Content();
        cont.loadTexture("res/images/TatumLogo.png");

    }
    //TODO: create menu and write code for below methods
    public void handleInput() {}
    public void update(float dt) {}
    public void render() {}
    public void dispose() {}
}
