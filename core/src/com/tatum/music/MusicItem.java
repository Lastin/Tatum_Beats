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
    private String originalText;
    private BitmapFont font;
    private Batch batch;
    private boolean clicked;
    private Vector3 vec = new Vector3();
    private OrthographicCamera cam;

    public MusicItem(Batch batch, BitmapFont font, String text, OrthographicCamera cam, int x, int y) {
        this.font = font;
        this.text = text;
        originalText = text;
        checkText();
        this.batch = batch;
        this.cam = cam;
        this.x = x;
        this.y = y;
        batch.begin();
        TextBounds tb = font.draw(batch, text, x, y);
        batch.end();
        width = tb.width;
        height = tb.height;

    }

    public void render() {
        batch.begin();
        font.draw(batch, text, x, y);
        batch.end();
    }
    public void renderFull() {
        batch.begin();
        font.draw(batch, originalText, x, y);
        batch.end();
    }

    public boolean isClicked() {
        return clicked;
    }

    public void update(float dt) {
        vec.set(Input.x, Input.y, 0);
        cam.unproject(vec);
        if (Input.isPressed() &&
                vec.x > x - width && vec.x < x + width &&
                vec.y > y - height && vec.y < y + height) {
            clicked = true;
        } else {
            clicked = false;
        }
    }

    public String getText() {
        return originalText;
    }
    public void checkText(){
        if(text.length()>25){
            text = text.substring(0,22) + "...";
        }
    }
}




