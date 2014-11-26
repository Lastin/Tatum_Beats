package com.tatum.handlers;

import java.util.Stack;
import com.tatum.Game;
import com.tatum.states.*;

public class GameStateManager {
    private Game game;
    private Stack<GameState> gameStates = new Stack<GameState>();

    public GameStateManager(Game game) {
        this.game = game;
        pushState(new Menu(this));
    }
    public void pushState(GameState state) {
        gameStates.push(state);
    }
    public void setState(GameState state) {
        popState();
        pushState(state);
    }
    public void popState() {
        gameStates.pop().dispose();
    }
    //getters
    public Game getGame() {
        return game;
    }
    public void update(float dt) {
        gameStates.peek().update(dt);
    }
    public void render() {
        gameStates.peek().render();
    }
}
