package com.tatum.android;

import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.facebook.UiLifecycleHelper;
import com.tatum.Game;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.tweetcomposer.TweetComposer;

import org.sensingkit.sensingkitlib.SKException;
import org.sensingkit.sensingkitlib.SKSensorDataListener;
import org.sensingkit.sensingkitlib.SensingKitLib;
import org.sensingkit.sensingkitlib.SensingKitLibInterface;
import org.sensingkit.sensingkitlib.model.data.DataInterface;
import org.sensingkit.sensingkitlib.modules.SensorModuleType;

import io.fabric.sdk.android.Fabric;

public class AndroidLauncher extends AndroidApplication {

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "ri1uHFmjtrgna9WC5wfbkdspQ";
    private static final String TWITTER_SECRET = "vCitep3IVDY8NNM9zQ3eouKSRnzNkOCqfJY0ZwVX4kI5tMScp1";
    private final String[] data = new String[4];
    // Tag used when logging messages
    private static final String TAG = AndroidLauncher.class.getSimpleName();

    // Uri used in handleError() below
    private static final Uri M_FACEBOOK_URL = Uri.parse("http://m.facebook.com");

    // Declare the UiLifecycleHelper for Facebook session management
    private UiLifecycleHelper fbUiLifecycleHelper;

    // Fragment attributes
    private static final int FB_LOGGED_OUT_HOME = 0;
    private static final int HOME = 1;
    private static final int FRAGMENT_COUNT = HOME +1;
    private Fragment[] fragments = new Fragment[FRAGMENT_COUNT];

    // Boolean recording whether the activity has been resumed so that
    // the logic in onSessionStateChange is only executed if this is the case
    private boolean isResumed = false;


    public UiLifecycleHelper getFbUiLifecycleHelper() {
        return fbUiLifecycleHelper;
    }
    private final void sensorData(String[] senseData) {

        data[0]=senseData[0];
        data[1]=senseData[1];
        data[2]=senseData[2];
        data[3]=senseData[3];

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        fbUiLifecycleHelper.onActivityResult(requestCode, resultCode, data);
    }

    @Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
		Fabric.with(this, new Twitter(authConfig), new TweetComposer());
        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();

        try {
            SensingKitLibInterface mSensingKitLib = SensingKitLib.getSensingKitLib(this); //create new senseing kit interface

            mSensingKitLib.subscribeToSensor(SensorModuleType.ACCELEROMETER, new SKSensorDataListener() { // attach to the accelerometer
                // when new accelerometer data is received, split it and store in data array
                @Override
                public void onDataReceived(final SensorModuleType moduleType, final DataInterface data) {
                    String[] dataArray = data.getDataInString().split(",");
                    sensorData(dataArray);
                }
            });
            mSensingKitLib.startContinuousSensingWithSensor(SensorModuleType.ACCELEROMETER); // start listen


        } catch (SKException e) {
            e.printStackTrace();
        }


        initialize(new Game(data, new AndroidTwitter(this)), config);

	}
}
