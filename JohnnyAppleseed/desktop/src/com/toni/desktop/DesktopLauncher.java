package com.toni.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.toni.Game;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "Johnny Appleseed"; // Set title
		config.width = 1100;               // Set width
		config.height = 600;               // Set height
		config.useGL30 = false;            // Not using GL30
		config.resizable = false;          // Stage's not resizable

		new LwjglApplication(new Game(), config);
	}
}
