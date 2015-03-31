package com.tatum.music;

import com.badlogic.gdx.graphics.Color;
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
    private TextBounds tb;
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
        tb = font.draw(batch, this.text, x, y);
        batch.end();
        width = tb.width;
        height = tb.height;

    }
    public void render() {
        batch.begin();
        font.draw(batch, text, x, y);
        batch.end();
    } //render the shortened string (for strings longer than 35 chars)
    public void renderFull() {
        batch.begin();
        font.draw(batch, originalText, x, y);
        batch.end();
    } // render full string no matter the size

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
        } // checks if the text has been clicked by seeing if any input was within the boundries of
            // where it has been rendered on screen

    }


    public String getText() {
        return originalText;
    }

    public void checkText(){
        if(text.length()>25){
            text = text.substring(0,22) + "...";
        }
    }   // if string is longer than 35, shorten and add "..."

    public float getWidth(){return width;}
    public float getHeight() {return height;}

    public BitmapFont getFont(){
        return font;
    }

    public void setText(String text){
        this.text = text;
    }
}




