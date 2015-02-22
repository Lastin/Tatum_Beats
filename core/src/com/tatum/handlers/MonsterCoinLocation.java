package com.tatum.handlers;

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

    public void addEvent(String event,int bar){
        contents.add(new GameEvent(event,bar));
    }

    public String checkForEvent(int barToCheck){
        //System.out.println(barToCheck+" "+contents.get(position).getBar());
        try {
            if (contents.get(position).getBar() == barToCheck) {
                position++;
                return contents.get(position - 1).getEvent();
            } else {
                return "There be no events here boyo";
            }
        }catch (IndexOutOfBoundsException e){
            //end of song
            return "End of song";
        }
    }


}
