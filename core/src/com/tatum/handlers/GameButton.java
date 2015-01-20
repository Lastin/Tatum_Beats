package com.tatum.handlers;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;

public class GameButton {
    ContentManager cont;
    // center at x, y
    private float x;
    private float y;
    private float width;
    private float height;
    private TextureRegion reg;
    Vector3 vec;
    private OrthographicCamera cam;
    private boolean clicked;
    private boolean enabled = true;
    private String text;
    private TextureRegion[] font;

    public GameButton(ContentManager cont, TextureRegion reg, float x, float y, OrthographicCamera cam) {
        this.cont = cont;
        this.reg = reg;
        this.x = x;
        this.y = y;
        this.cam = cam;
        width = reg.getRegionWidth();
        height = reg.getRegionHeight();
        vec = new Vector3();
        Texture tex = cont.getTexture("hud2");
        font = new TextureRegion[11];
        for(int i = 0; i < 6; i++) {
            font[i] = new TextureRegion(tex, 32 + i * 9, 16, 9, 9);
        }
        for(int i = 0; i < 5; i++) {
            font[i + 6] = new TextureRegion(tex, 32 + i * 9, 25, 9, 9);
        }
    }

    public boolean isClicked() { return clicked; }
    public void setText(String s) { text = s; }

    public void update(float dt) {
        vec.set(Input.x, Input.y, 0);
        cam.unproject(vec);
        if (Input.isPressed() &&
                vec.x > x - width / 4 && vec.x < x + width / 4 &&
                vec.y > y - height / 4 && vec.y < y + height / 4) {
            clicked = true;
        }
        else {
            clicked = false;
        }
    }

    public void render(SpriteBatch sb) {
        sb.begin();
        sb.draw(reg, x - width / 4, y - height / 4, width/2, height/2);
        if (text != null) {
            drawString(sb, text, x, y);
        }
        sb.end();
    }

    private void drawString(SpriteBatch sb, String s, float x, float y) {
        int len = s.length();
        float xo = len * font[0].getRegionWidth() / 2;
        float yo = font[0].getRegionHeight() / 2;
        for(int i = 0; i < len; i++) {
            char c = s.charAt(i);
            if(c == '/') c = 10;
            else if(c >= '0' && c <= '9') c -= '0';
            else continue;
            sb.draw(font[c], x + i * 9 - xo, y - yo);
        }
    }

    public void setEnabled(boolean enabled){
        this.enabled = enabled;
    }
    public boolean isEnabled(){
        return enabled;
    }
}
