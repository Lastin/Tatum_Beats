package com.tatum.handlers;

import java.util.Stack;
import com.tatum.Game;
import com.tatum.states.*;

public class GameStateManager {
    private Game game;
    private Stack<GameState> gameStates;

    public GameStateManager(Game game) {
        this.game = game;
        gameStates = new Stack<GameState>();
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
        GameState g = gameStates.pop();
        //g.dispose();
    }
}
