package org.egordorichev.mf100.software;

import org.egordorichev.mf100.hardware.Drivers;

public class StoryPart {
	private String text;

	public StoryPart(String text) {
		this.text = text;
	}

	public void run() {
		Drivers.tty.println(this.text + "\n");
	}
}