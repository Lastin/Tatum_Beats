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
    public void share(int score) {
        new TweetComposer.Builder(context).text("I just scored " + score + " points on Tatum!").show();
    }
}
