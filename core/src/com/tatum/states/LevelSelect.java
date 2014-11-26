package com.tatum.states;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
//import com.echonest.api.v4.EchoNestException;
import com.tatum.handlers.GameButton;
import com.tatum.handlers.GameStateManager;
import com.tatum.handlers.Content;
import com.tatum.Game;
import com.tatum.music.TrackData;

public class LevelSelect extends GameState {
    private Game game;
    private Content cont;
    private TextureRegion reg;
    private GameButton[][] buttons;
    public LevelSelect(GameStateManager gsm) {
        super(gsm);
        game = gsm.getGame();
        cont = game.get_content();
        reg = new TextureRegion(cont.getTexture("bgs"), 0, 0, 320, 240);

        TextureRegion buttonReg = new TextureRegion(cont.getTexture("hud"), 0, 0, 32, 32);
        buttons = new GameButton[5][5];
        for(int row = 0; row < 1; row++) {
            for(int col = 0; col < 2; col++) {
                buttons[row][col] = new GameButton(game, buttonReg, 80 + col * 40, 200 - row * 40, cam);
                buttons[row][col].setText(row * buttons[0].length + col + 1 + "");
            }
        }

        cam.setToOrtho(false, game.get_width(), game.get_height());

    }

    public void handleInput() {
    }

    public void update(float dt) {
        handleInput();

    }
    public void render() {
        sb.setProjectionMatrix(cam.combined);
        sb.begin();
        sb.draw(reg, 0, 0);
        sb.end();
        for (int row = 0; row < 1; row++) {
            for(int col = 0; col < 2; col++) {
                buttons[row][col].render(sb);
            }
        }
    }

    public void dispose() {

    }
}
