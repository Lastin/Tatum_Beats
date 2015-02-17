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
        TextBounds tb = font.draw(batch, this.text, x, y);
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

    public boolean isClickedPlay(){
        //System.out.println("start x" + 5);
        //System.out.println("end x" + (backButton.getWidth()));
        //System.out.println("input x "+ Input.x);
        //System.out.println("start y" + 20);
        //System.out.println("end y" + ((backButton.getHeight()*2)+20));
        //System.out.println("input y "+ Input.y);
     /*   if((Input.x >=10)){
            System.out.println("first true");
        }
        else{
            System.out.println("first false");
        }
        if((Input.x <=(backButton.getWidth()*2)+5)){
            System.out.println("second true");
        }
        else{
            System.out.println("second false");
        }
        if((Input.y >= 20)){
            System.out.println("third true");
        }
        else{
            System.out.println("third false");
            System.out.println("start y" + 20);
            System.out.println("input y "+ Input.y);
        }
        if(Input.y<= ((backButton.getHeight()*2)+20)){
            System.out.println("forth true");
        }
        else{
            System.out.println("forth false");
        }
        */
        //double inverseY = gameHeight - height;

        //System.out.println(gameHeight+" das "+ height + " d ads " + inverseY*2);
        double inverseY = height*2;

        if((((Input.x >=x) && (Input.x <=(getWidth()*2)+x))&&((Input.y >= inverseY)&&(Input.y<= ((getHeight()*2)+inverseY))))) {
            return true;
        }
        else
            return false;
    }

    public String getText() {
        return originalText;
    }
    public void checkText(){
        if(text.length()>25){
            text = text.substring(0,22) + "...";
        }
    }
    public float getWidth(){return width;}

    public float getHeight() {return height;}
}




