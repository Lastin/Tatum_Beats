package com.tatum.handlers;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;

public class FontGenerator {

    //this class is used to create different fonts to write draw strings to the screen

    //colours
    public final Color royalBlue = new Color(0, 0.714f, 1, 1);
    public final Color red = Color.valueOf("FF7878");
    public final Color yellow = Color.valueOf("F1D974");
    public final Color green = Color.valueOf("68F367");
    public final Color darkGreen = Color.valueOf("00A30E");
    public final Color brightGray = Color.valueOf("787878");

    //standard ones used throughout the project for testing and drawing
    public final FreeTypeFontGenerator fontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("res/fonts/LiberationSans-Bold.ttf"));
    private final FileHandle customFNT = Gdx.files.internal("res/fonts/customFont.fnt");
    private final FileHandle customPNG = Gdx.files.internal("res/fonts/customFont.png");
    public final BitmapFont customFont = new BitmapFont(customFNT, customPNG, false);
    public final BitmapFont customFontSmall = new BitmapFont(customFNT, customPNG, false);
    public final BitmapFont customTitleFont = new BitmapFont(Gdx.files.internal("res/fonts/titleFont.fnt"), Gdx.files.internal("res/fonts/titleFont.png"), false);
    public final BitmapFont scoreFont = new BitmapFont(Gdx.files.internal("res/fonts/scoreFont.fnt"), Gdx.files.internal("res/fonts/scoreFont.png"), false);
    public final BitmapFont titleFont = makeFont(20, Color.BLACK);
    public final BitmapFont listFont = makeFont(36, Color.BLACK);
    public final BitmapFont listFolderFont = makeFont(36, brightGray);
    public final BitmapFont listLegalFormatFont = makeFont(36, darkGreen);
    public final BitmapFont underListFont = makeFont(20, Color.BLACK);
    public final BitmapFont loadingFont = makeFont(70, Color.WHITE);
    public final BitmapFont tipFont = makeFont(60, Color.BLACK);
    public final BitmapFont errorFont = makeFont(45, Color.RED);

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

    public BitmapFont getCustomTitleFont(){
        customTitleFont.setScale(0.5f);
        return customTitleFont;
    }

    public BitmapFont getMenuFont(){
        customFont.setScale(0.5f);
        return customFont;
    }

    public BitmapFont getTipFont(){
        tipFont.setScale(0.5f);
        return tipFont;
    }

    public BitmapFont getSmallMenuFont(){
        customFontSmall.setScale(0.5f);
        return customFontSmall;
    }

    public BitmapFont getScoreFont(){
        scoreFont.setScale(0.4f);
        return scoreFont;
    }
}
