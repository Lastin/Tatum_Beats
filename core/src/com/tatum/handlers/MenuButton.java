package com.tatum.handlers;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;


public class MenuButton {
    private TextButton button;

    public MenuButton(FontGenerator fontGenerator, String text, float x, float y){
        TextButtonStyle style = new TextButtonStyle();
        style.font = fontGenerator.getMenuFont();
        button = new TextButton(text, style);
        button.setPosition(x, y, 5);
    }

    public void render(Batch sb){
        button.draw(sb, 1);
    }

    public TextButton getButton(){
        return button;
    }
}
