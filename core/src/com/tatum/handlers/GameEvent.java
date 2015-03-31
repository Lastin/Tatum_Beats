package com.tatum.handlers;

import com.tatum.entities.B2DSprite;

/**
 * Created by Ben on 21/02/2015.
 */
public class GameEvent {
    String event;
    int bar;
    B2DSprite sprite;
    public GameEvent(String event, int bar,B2DSprite sprite){
        this.event=event;
        this.bar=bar;
        this.sprite = sprite;
    }
    public int getBar(){
        return bar;
    } // which bar the event occurs at
    public String getEvent(){
        return event;
    }// the type of event (ground/flying enemy or coin)
    public B2DSprite getSprite(){return sprite;} // the slime/bat/coin entity

}
