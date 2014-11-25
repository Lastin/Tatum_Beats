package com.tatum.states;


//local stuff
import com.tatum.handlers.*;

public class Menu extends GameState{
    private Content cont;
    public Menu(GameStateManager gsm) {
        super(gsm);
        cont = game.get_content();
        loadContent();
    }
    private void loadContent(){
        cont.loadTexture("res/images/TatumLogo.png");

    }
    //TODO: create menu and write code for below methods
    public void handleInput() {}
    public void update(float dt) {}
    public void render() {}
    public void dispose() {}
}