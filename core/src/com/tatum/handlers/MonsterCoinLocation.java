package com.tatum.handlers;

import com.tatum.entities.B2DSprite;

import java.util.ArrayList;

/**
 * Created by Ben on 20/02/2015.
 */
public class MonsterCoinLocation {

    ArrayList<GameEvent> contents;
    int position;

    public MonsterCoinLocation(){
        contents = new ArrayList<GameEvent>();
        position=0;
    }

    public void addEvent(String event,int bar,B2DSprite sprite){
        contents.add(new GameEvent(event,bar,sprite));
    }

    public GameEvent checkForEvent(int barToCheck){
        //System.out.println(barToCheck+" "+contents.get(position).getBar());
        try {
            if (contents.get(position).getBar() == barToCheck) {
                position++;
                return contents.get(position - 1);
            } else {
                return null;
            }
        }catch (IndexOutOfBoundsException e){
            //end of song
            return null;
        }
    }


}
