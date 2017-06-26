package org.egordorichev.mf100.software;

import org.egordorichev.mf100.hardware.Drivers;
import org.egordorichev.mf100.hardware.Operation;
import org.egordorichev.mf100.hardware.monitor.Tty;

import java.util.ArrayList;

public class Story extends Operation {
	private ArrayList<StoryPart> storyParts = new ArrayList<>();

	public Story() {
		this.storyParts.add(new StoryPart("You found yourself in\na small room."));
		this.storyParts.add(new StoryPart("\"How did I get here?\""));
		this.storyParts.add(new StoryPart("..."));
		this.storyParts.add(new StoryPart("..."));
		this.storyParts.add(new StoryPart("Oh, I got a mail!"));
		this.storyParts.add(new StoryPart("It says: "));
		this.storyParts.add(new StoryPart("\"You thought, you'll\nmake a game, and\nend the LD?\n"
			+ "You are wrong. Now, you\nare going to write a game\nfor me, and hack\nthe Ludum Dare's servers\nMuha ha ha!\""));
		this.storyParts.add(new StoryPart("..."));
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