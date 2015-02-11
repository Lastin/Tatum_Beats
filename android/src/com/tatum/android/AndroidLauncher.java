package com.tatum.android;

import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.tatum.Game;
import com.facebook.AppEventsLogger;

import org.sensingkit.sensingkitlib.SKException;
import org.sensingkit.sensingkitlib.SKSensorDataListener;
import org.sensingkit.sensingkitlib.SensingKitLib;
import org.sensingkit.sensingkitlib.SensingKitLibInterface;
import org.sensingkit.sensingkitlib.model.data.DataInterface;
import org.sensingkit.sensingkitlib.modules.SensorModuleType;

public class AndroidLauncher extends AndroidApplication {
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
        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();

        try {
            SensingKitLibInterface mSensingKitLib = SensingKitLib.getSensingKitLib(this);

            mSensingKitLib.subscribeToSensor(SensorModuleType.ACCELEROMETER, new SKSensorDataListener() {
                @Override
                public void onDataReceived(final SensorModuleType moduleType, final DataInterface data) {
                    String[] dataArray = data.getDataInString().split(",");
                    sensorData(dataArray);
                }
            });
            mSensingKitLib.startContinuousSensingWithSensor(SensorModuleType.ACCELEROMETER);


        } catch (SKException e) {
            e.printStackTrace();
        }

        initialize(new Game(data), config);
 //       fbUiLifecycleHelper = new UiLifecycleHelper(this, new Session.StatusCallback() {
  //          @Override
   //         public void call(Session session, SessionState state, Exception exception) {
    //            // Add code here to accommodate session changes
     //           updateView();
     //       }
     //   });
     //   fbUiLifecycleHelper.onCreate(savedInstanceState);
	}

  //  private void updateView() {
   //     if (isResumed) {
    //        Session session = Session.getActiveSession();
     //       if (session.isOpened() && !((tatum)getApplication()).isLoggedIn() &&
      //              fragments[HOME] != null) {
       //         fetchUserInformationAndLogin();
       //     } else if (session.isClosed() && ((FriendSmashApplication)getApplication()).isLoggedIn() &&
        //            fragments[FB_LOGGED_OUT_HOME] != null) {
         //       showFragment(FB_LOGGED_OUT_HOME, false);
          //  }
        //}
   // }

//    @Override
//    protected void onResume() {
//        super.onResume();
//        fbUiLifecycleHelper.onResume();
//        // Logs 'install' and 'app activate' App Events.
///        AppEventsLogger.activateApp(this);
 //   }
  //  @Override
  //  protected void onPause() {
 //       super.onPause();
 //       fbUiLifecycleHelper.onPause();
 //       // Logs 'app deactivate' App Event.
  //      AppEventsLogger.deactivateApp(this);
   // }
    //@Override
   // protected void onSaveInstanceState(Bundle outState){
   //     super.onSaveInstanceState(outState);

   //     fbUiLifecycleHelper.onSaveInstanceState(outState);
    //}

   // @Override
   // protected void onDestroy(){
    //    super.onDestroy();

    //    fbUiLifecycleHelper.onDestroy();
   // }
}
