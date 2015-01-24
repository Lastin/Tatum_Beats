package com.tatum.android;

import android.os.Bundle;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.tatum.Game;
import android.os.Environment;

import org.sensingkit.sensingkitlib.SKException;
import org.sensingkit.sensingkitlib.SKSensorDataListener;
import org.sensingkit.sensingkitlib.SensingKitLib;
import org.sensingkit.sensingkitlib.SensingKitLibInterface;
import org.sensingkit.sensingkitlib.model.data.DataInterface;
import org.sensingkit.sensingkitlib.modules.SensorModuleType;

public class AndroidLauncher extends AndroidApplication {
    private final String[] data = new String[4];
    private final void sensorData(String[] senseData) {

        data[0]=senseData[0];
        data[1]=senseData[1];
        data[2]=senseData[2];
        data[3]=senseData[3];

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
	}
}
