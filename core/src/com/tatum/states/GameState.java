package com.tatum.states;

import com.tatum.handlers.GameStateManager;

public abstract class GameState {
    private GameStateManager gsm;
    public GameState(GameStateManager gsm) {
        this.gsm = gsm;
    }
}
