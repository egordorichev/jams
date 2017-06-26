package org.egordorichev.ldjam.game;

import asciiPanel.AsciiPanel;

import java.awt.event.KeyEvent;

public abstract class GameState {
	protected Game game;

	public GameState(Game game) {
		this.game = game;
	}

	public abstract void render(AsciiPanel terminal);

	public void handleInput(KeyEvent event) {

	}

	public void update() {

	}

	public Game getGame() {
		return this.game;
	}
}