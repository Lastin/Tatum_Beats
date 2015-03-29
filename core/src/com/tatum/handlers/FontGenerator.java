package com.tatum.handlers;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;

public class FontGenerator {
    public final FreeTypeFontGenerator fontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("res/fonts/LiberationSans-Bold.ttf"));
    public final BitmapFont customFont = new BitmapFont(Gdx.files.internal("res/fonts/customFont.fnt"), Gdx.files.internal("res/fonts/customFont.png"), false);
    public final BitmapFont customFontSmall = new BitmapFont(Gdx.files.internal("res/fonts/customFont.fnt"), Gdx.files.internal("res/fonts/customFont.png"), false);
    public final BitmapFont scoreFont = new BitmapFont(Gdx.files.internal("res/fonts/scoreFont.fnt"), Gdx.files.internal("res/fonts/scoreFont.png"), false);
    public final BitmapFont titleFont = makeFont(20, Color.BLACK);
    public final BitmapFont listFont = makeFont(36, Color.BLACK);
    public final BitmapFont underListFont = makeFont(20, Color.BLACK);
    public final BitmapFont loadingFont = makeFont(70, Color.WHITE);
    //colours
    public final Color royalBlue = new Color(0, 0.714f, 1, 1);
    public final Color red = Color.valueOf("FF7878");
    public final Color yellow = Color.valueOf("F1D974");
    public final Color green = Color.valueOf("68F367");

    public FontGenerator(){

    }

    public BitmapFont makeFont(int size, Color color){
        FreeTypeFontParameter parameters = new FreeTypeFontParameter();
        parameters.size = size;
        BitmapFont font = fontGenerator.generateFont(parameters);
        font.setColor(color);
        font.setScale(0.5f);
        return font;
    }

    public BitmapFont getMenuFont(){
        customFont.setScale(0.5f);
        return customFont;
    }

    public BitmapFont getScoreFont(){
        scoreFont.setScale(0.4f);
        return scoreFont;
    }
}
