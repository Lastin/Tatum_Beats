package com.tatum.handlers;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Animation {
    private TextureRegion[] frames;
    private float time;
    private float delay;
    private int currentFrame;
    private int timesPlayed;

    public Animation() {
        //This got to be removed later somehow, otherwise it will always end up in null pointer expcetion.
    }

    public Animation(TextureRegion[] frames) {
        this(frames, 1 / 12f);
    }

    public Animation(TextureRegion[] frames, float delay) {
        setFrames(frames, delay);
    }

    public void setFrames(TextureRegion[] frames, float delay) {
        this.frames = frames;
        this.delay = delay;
        time = 0;
        currentFrame = 0;
        timesPlayed = 0;
    }

    public void update(float dt) {
        if (delay <= 0) return;
        time += dt;
        while (time >= delay) {
            step();
        }
    }

    private void step(){
        time -= delay;
        if (++currentFrame == frames.length) {
            currentFrame = 0;
            timesPlayed++;
        }
    }

    public TextureRegion getFrame() { return frames[currentFrame]; }
    public int getTimesPlayed() { return timesPlayed; }
    public boolean hasPlayedOnce() { return timesPlayed > 0; }
}
