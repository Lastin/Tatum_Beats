package com.tatum.handlers;

import com.tatum.Game;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Background {

    Game game;
    private TextureRegion image;
    private OrthographicCamera gameCam;
    private float scale;
    private float x, y;
    private int numDrawX, numDrawY;
    private float dx, dy;

    public Background(Game game, TextureRegion image, OrthographicCamera gameCam, float scale) {
        this.game = game;
        this.image = image;
        this.gameCam = gameCam;
        this.scale = scale;
        numDrawX = game.getWidth() / image.getRegionWidth() + 1;
        numDrawY = game.getHeight() / image.getRegionHeight() + 1;
    }

    public void setVector(float dx, float dy) {
        this.dx = dx;
        this.dy = dy;
    }

    public void update(float dt) {
        x += (dx * scale) * dt;
        y += (dy * scale) * dt;
    }

    public void render(SpriteBatch sb) {
        float x = ((this.x + gameCam.viewportWidth / 2 - gameCam.position.x) * scale) % image.getRegionWidth();
        float y = ((this.y + gameCam.viewportHeight / 2 - gameCam.position.y) * scale) % image.getRegionHeight();
        sb.begin();
        int colOffset = x > 0 ? -1 : 0;
        int rowOffset = y > 0 ? -1 : 0;
        for (int row = 0; row < numDrawY; row++) {
            for(int col = 0; col < numDrawX; col++) {
                sb.draw(image, x + (col + colOffset) * image.getRegionWidth(), y + (rowOffset + row) * image.getRegionHeight());
            }
        }
        sb.end();

    }

}
