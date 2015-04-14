package com.tatum.handlers;

import com.badlogic.gdx.input.GestureDetector;
import com.tatum.states.Play;

/**
 * Created by Ben on 22/02/2015.
 */
public class SimpleDirectionGestureDetector extends GestureDetector {
    private int tapCount = 0;

    public SimpleDirectionGestureDetector(DirectionListener directionListener, Play play) {
        super(new DirectionGestureListener(directionListener, play));
    }

    private static class DirectionGestureListener extends GestureDetector.GestureAdapter {
        DirectionListener directionListener;
        Play play;

        public DirectionGestureListener(DirectionListener directionListener, Play play){
            this.directionListener = directionListener;
            this.play = play;
        }

        @Override
        public boolean fling(float velocityX, float velocityY, int button) {
            if(Math.abs(velocityX)>Math.abs(velocityY)){
                if(velocityX>0){
                    directionListener.onRight();
                }else{
                    directionListener.onLeft();
                }
            }else{
                if(velocityY>0){
                    directionListener.onDown();
                }else{
                    directionListener.onUp();
                }
            }
            return super.fling(velocityX, velocityY, button);
        }

        @Override
        public boolean tap(float velocityX, float velocityY, int count, int button) {
            if(count == 2) {
                play.pause();
            } else if (count  == 3) {
                play.backToMenu();
            }
            return super.tap(velocityX, velocityY, count, button);
        }
    }

}
