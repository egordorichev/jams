package org.egordorichev.mf100.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import org.egordorichev.mf100.Game;
import org.egordorichev.mf100.hardware.monitor.Monitor;

public class DesktopLauncher {
	public static void main(String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

		config.width = Monitor.WIDTH;
		config.height = Monitor.HEIGHT;
		config.resizable = false;
		config.title = "MF100";
		config.samples = 8;

		new LwjglApplication(new Game(), config);
	}
}
