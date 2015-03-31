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

    //This class is pretty much been replaced by MenuButton and Music Item
    // only in use now by the up and down arrows
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

    }

    public boolean isClicked() { return clicked; }

    public void update(float dt) {
        vec.set(Input.x, Input.y, 0);
        cam.unproject(vec);
        if (Input.isPressed() &&
                vec.x > x - width / 4 && vec.x < x + width / 4 &&
                vec.y > y - height / 4 && vec.y < y + height / 4) { // check if the arrow has been clicked
            clicked = true;
        }
        else {
            clicked = false;
        }
    }

    public void render(SpriteBatch sb) {
        sb.begin();
        sb.draw(reg, x - width / 4, y - height / 4, width/2, height/2);
        sb.end();
    }

    public void setEnabled(boolean enabled){
        this.enabled = enabled;
    }
    public boolean isEnabled(){
        return enabled;
    }
}
