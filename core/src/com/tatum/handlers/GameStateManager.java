package com.tatum.handlers;

import java.util.Stack;
import com.tatum.Game;
import com.tatum.states.*;

public class GameStateManager {
    //this class manages what state the game is currently in
    private Game game;
    private Stack<GameState> gameStates = new Stack<GameState>();

    public GameStateManager(Game game) {
        this.game = game;
        pushState(new Menu(this)); // sets the menu as the initial stat
    }
    public void pushState(GameState state) {
        gameStates.push(state);
    } // push the given state to the top of the stack
    public void setState(GameState state) {
        popState();// remove previous state
        pushState(state); // put given state on stack
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
