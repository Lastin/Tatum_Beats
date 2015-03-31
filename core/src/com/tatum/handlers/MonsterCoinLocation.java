package com.tatum.handlers;

import com.tatum.entities.B2DSprite;

import java.util.ArrayList;

/**
 * Created by Ben on 20/02/2015.
 */
public class MonsterCoinLocation {
    //this class stores all of the events within a level
    // this can then be checked to see if at a given bar
    //what the event is if there is one
    ArrayList<GameEvent> contents;
    int position;

    public MonsterCoinLocation(){
        contents = new ArrayList<GameEvent>();
        position=0;
    }

    public void addEvent(String event,int bar,B2DSprite sprite){
        //add an event at the given bar
        contents.add(new GameEvent(event,bar,sprite));
    }

    public GameEvent checkForEvent(int barToCheck){
        try {
            if (contents.get(position).getBar() == barToCheck) { // events are check for in chronological order
                position++;
                return contents.get(position - 1); // if event found, return it
            } else {
                return null;
            }
        }catch (IndexOutOfBoundsException e){
            //end of song
            return null;
        }
    }


}
