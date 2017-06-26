package org.egordorichev.mf100;

import com.badlogic.gdx.audio.Music;
import org.egordorichev.mf100.graphics.Assets;
import org.egordorichev.mf100.hardware.Drivers;
import org.egordorichev.mf100.hardware.Operation;
import org.egordorichev.mf100.software.BootProcess;

public class MF100 {
	public static boolean booted = false;
	public static boolean showStory = false;
	public static Operation currentOperation = null;

	public MF100() {
		Drivers.init();

		Music music = Assets.getMusic("music.mp3");

		music.setLooping(true);
		music.setVolume(0.7f);
		music.play();

		new BootProcess().start();
	}

	public void render() {
		Drivers.monitor.render();
	}
}