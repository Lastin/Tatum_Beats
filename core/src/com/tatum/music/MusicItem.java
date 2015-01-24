package com.tatum.music;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
import com.badlogic.gdx.math.Vector3;
import com.tatum.handlers.Input;

public class MusicItem {
    private int x;
    private int y;
    private float width;
    private float height;
    private String text;
    private BitmapFont font;
    private Batch batch;
    private boolean clicked;
    private Vector3 vec = new Vector3();
    private OrthographicCamera cam;

    public MusicItem(Batch batch, BitmapFont font, String text, OrthographicCamera cam){
        this.font = font;
        this.text = text;
        this.batch = batch;
        this.cam = cam;
        TextBounds tb = font.draw(batch, text, x, y);
        width = tb.width;
        height = tb.height;

    }

    public void render(){
        font.draw(batch, text, x, y);
    }

    public boolean isClicker(){
        return clicked;
    }

    public void update(float dt) {
        vec.set(Input.x, Input.y, 0);
        cam.unproject(vec);
        if (Input.isPressed() &&
                vec.x > x - width  && vec.x < x + width &&
                vec.y > y - height && vec.y < y + height) {
            clicked = true;
        }
        else {
            clicked = false;
        }
    }
}




