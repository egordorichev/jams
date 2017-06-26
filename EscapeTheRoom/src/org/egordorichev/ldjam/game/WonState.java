package org.egordorichev.ldjam.game;

import asciiPanel.AsciiPanel;
import org.egordorichev.ldjam.game.Game;
import org.egordorichev.ldjam.game.GamePlayState;
import org.egordorichev.ldjam.game.GameState;

import java.awt.event.KeyEvent;

public class WonState extends GameState {
	public WonState(Game game) {
		super(game);

		this.game.nextLevel();
	}

	@Override
	public void handleInput(KeyEvent event) {
		this.game.setState(new GamePlayState(this.game));
	}

	@Override
	public void render(AsciiPanel terminal) {
		if(this.game.getCurrentLevel() > 4) {
			this.game.setState(new GameWonState(this.game));
		}

		terminal.writeCenter("Level complete!", terminal.getHeightInCharacters() / 2);
		terminal.writeCenter("[Press any key to continue]", terminal.getHeightInCharacters() / 2 + 2);
	}
}