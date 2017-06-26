package org.egordorichev.ldjam.game;

import asciiPanel.AsciiPanel;

import java.awt.event.KeyEvent;

public class GameWonState extends GameState {
	public GameWonState(Game game) {
		super(game);
	}

	@Override
	public void handleInput(KeyEvent event) {
		this.game.setState(new MenuState(this.game));
	}

	@Override
	public void render(AsciiPanel terminal) {
		terminal.writeCenter("You won the game!", terminal.getHeightInCharacters() / 2);
		terminal.writeCenter("[Press any key to continue]", terminal.getHeightInCharacters() / 2 + 2);
	}
}