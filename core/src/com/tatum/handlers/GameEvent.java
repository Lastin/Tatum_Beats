package com.tatum.handlers;

/**
 * Created by Ben on 21/02/2015.
 */
public class GameEvent {
    String event;
    int bar;
    public GameEvent(String event, int bar){
        this.event=event;
        this.bar=bar;
    }
    public int getBar(){
        return bar;
    }
    public String getEvent(){
        return event;
    }

}
