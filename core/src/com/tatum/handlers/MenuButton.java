package com.tatum.handlers;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;


public class MenuButton {
    private TextButton button;

    public MenuButton(FontGenerator fontGenerator, String text, float x, float y){
        TextButtonStyle style = new TextButtonStyle();
        style.font = fontGenerator.getMenuFont(); //get font for buttons
        button = new TextButton(text, style); //create new button
        button.setPosition(x, y, 5); // set at given co-ordinance
    }

    public void render(Batch sb){
        button.draw(sb, 1);
    }

    public TextButton getButton(){
        return button;
    }
}
