package org.egordorichev.mf100.software;

import org.egordorichev.mf100.hardware.Drivers;
import org.egordorichev.mf100.hardware.Operation;
import org.egordorichev.mf100.hardware.monitor.Tty;

import java.util.ArrayList;

public class EndStory extends Operation {
	private ArrayList<StoryPart> storyParts = new ArrayList<>();

	public EndStory() {
		Drivers.tty.clear();
		this.storyParts.add(new StoryPart("...\n"));
		this.storyParts.add(new StoryPart("the server went down\n"));
		this.storyParts.add(new StoryPart("happy end?\n"));
		this.storyParts.add(new StoryPart("...\n"));
		this.storyParts.add(new StoryPart("wait, but how then, your\ngame will be voted?"));
		this.storyParts.add(new StoryPart("...\n"));
		this.storyParts.add(new StoryPart("#ds@32!@!*&&!\n"));
	}

	@Override
	public void onRun() {
		Drivers.tty.clear();

		this.storyParts.get(0).run();
		Drivers.tty.println(Tty.LIGHT_GRAY, "[ press enter to continue ]");
		Drivers.keyboard.waitForEnter();
		Drivers.tty.println("\n");
		this.storyParts.remove(0);

		while (this.storyParts.size() != 0) {
			this.storyParts.get(0).run();
			Drivers.keyboard.waitForEnter();
			this.storyParts.remove(0);
		}

		Drivers.tty.clear();
		new Menu().start();
	}
}