package com.tatum.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.tatum.Game;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        String[] temp ={"2","2","2","2"}; // passing gesture value placeholders
		new LwjglApplication(new Game(temp, new DesktopTwitter()), config);
	}
}
