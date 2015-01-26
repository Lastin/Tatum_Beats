package com.tatum.handlers;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;

public class FontGenerator {
    private static FreeTypeFontGenerator fontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("res/fonts/LiberationSans-Bold.ttf"));
    public static BitmapFont royalMenu = makeFont(35, 0, 0.714f, 1, 1);
    public static BitmapFont listFont = makeFont(18, 0, 0, 0, 1);
    public static BitmapFont titleFont = makeFont(10, 0, 0, 0, 1);
    public static BitmapFont makeFont(int size, float r, float g, float b, float o){
        FreeTypeFontParameter parameters = new FreeTypeFontParameter();
        parameters.size = size;
        parameters.magFilter = Texture.TextureFilter.Linear;
        BitmapFont font = fontGenerator.generateFont(parameters);
        font.setColor(r, g, b, o);
        return font;
    }
}
