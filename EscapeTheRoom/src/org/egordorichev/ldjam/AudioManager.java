package org.egordorichev.ldjam;

import java.applet.Applet;
import java.applet.AudioClip;
import java.util.HashMap;
import java.util.Map;

public class AudioManager {
	private Map<String, AudioClip> sounds;

	public AudioManager() {
		this.sounds = new HashMap<String, AudioClip>();
	}

	public void play(String name) {
		this.sounds.get(name).play();
	}

	public void playLooped(String name) {
		this.sounds.get(name).loop();
	}

	public void load(String name, String path) {
		AudioClip clip = Applet.newAudioClip(this.getClass().getResource(path));

		this.sounds.put(name, clip);
	}
}