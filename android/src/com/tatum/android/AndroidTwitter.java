package com.tatum.android;

import android.content.Context;

import com.tatum.TwitterInterface;
import com.twitter.sdk.android.tweetcomposer.TweetComposer;

/**
 * Created by adam on 27/03/15.
 */
public class AndroidTwitter implements TwitterInterface {

    private final Context context;

    public AndroidTwitter(Context context) {
        this.context = context;
    }

    @Override
    public void share(String artist, String song, int score) {
        String message;
        if(song == null || song == ""){
            song = "";
        } else {
            song = " on " + song;
        }
        if(artist == null || artist == ""){
            artist = "";
        } else {
            artist = " by " + artist;
        }
        message = "I just scored " + score + song + artist + " on Tatum!";
        new TweetComposer.Builder(context).text(message).show();
    }
}
