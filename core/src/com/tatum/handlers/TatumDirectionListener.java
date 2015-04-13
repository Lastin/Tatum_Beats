package com.tatum.handlers;

import com.echonest.api.v4.Track;

/**
 * Created by Ben on 22/02/2015.
 */
public class TatumDirectionListener implements DirectionListener {
    //This class stores the information on the users swipe directions
    private boolean[] bools;
    public TatumDirectionListener(){
        bools = new boolean[4];
        resetBools();
    }
    //
    @Override
    public void onLeft() {
        bools[2]=true;
    }

    @Override
    public void onRight() {
        bools[3]=true;
    }

    @Override
    public void onUp() {
        bools[0]=true;
    }

    @Override
    public void onDown() {
        bools[1]=true;
    }
    public void resetBools(){
        bools[0] = bools[1] = bools[2] = bools[3] = false;
    }
    public boolean left(){return bools[2];}

    public boolean right(){return bools[3];}

    public boolean up(){return bools[0];}

    public boolean down(){return bools[1];}
}
