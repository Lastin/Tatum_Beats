package com.tatum.handlers;

import java.util.ArrayList;

/**
 * Created by Ben on 20/02/2015.
 */
public class MonsterCoinLocation {

    ArrayList<String> contents;
    int position;

    public MonsterCoinLocation(){
        contents = new ArrayList<String>();
        position=1;
    }

    public void addEvent(String event){
        contents.add(event);
    }

    public String getNextEvent(){
        try {
            position++;
            return contents.get(position);
        }catch(IndexOutOfBoundsException e){
            position--;
            return "No more events";
        }
    }
    public String getExactEvent(int i){
        try{
            return contents.get(i);
        }catch (IndexOutOfBoundsException e){
            return "No such Event";
        }
    }

}
