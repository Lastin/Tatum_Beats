package com.tatum.handlers;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;

public class FontGenerator {
    private static FreeTypeFontGenerator fontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("res/fonts/LiberationSans-Bold.ttf"));
    //colours
    public static final Color royalBlue = new Color(0, 0.714f, 1, 1);

    public static BitmapFont royalMenu = makeFont(35, 0, 0.714f, 1, 1);

    public static BitmapFont titleFont = makeFont(20, Color.BLACK);

    public static BitmapFont listFont = makeFont(36, Color.BLACK);

    public static BitmapFont underListFont = makeFont(20, Color.BLACK);

    public static BitmapFont artistSongFont = makeFont(60, Color.BLACK);


    public static BitmapFont makeFont(int size, float r, float g, float b, float o){
        FreeTypeFontParameter parameters = new FreeTypeFontParameter();
        parameters.size = size;
        BitmapFont font = fontGenerator.generateFont(parameters);
        font.setColor(r, g, b, o);
        font.setScale(0.5f);
        return font;
    }

    public static BitmapFont makeFont(int size, Color color){
        FreeTypeFontParameter parameters = new FreeTypeFontParameter();
        parameters.size = size;
        BitmapFont font = fontGenerator.generateFont(parameters);
        font.setColor(color);
        font.setScale(0.5f);
        return font;
    }
}
