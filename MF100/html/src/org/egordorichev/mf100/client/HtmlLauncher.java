package org.egordorichev.mf100.client;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;
import org.egordorichev.mf100.Game;
import org.egordorichev.mf100.hardware.monitor.Monitor;

public class HtmlLauncher extends GwtApplication {
        @Override
        public GwtApplicationConfiguration getConfig() {
	        return new GwtApplicationConfiguration(Monitor.WIDTH, Monitor.HEIGHT);
        }

        @Override
        public ApplicationListener createApplicationListener() {
	        return new Game();
        }
}