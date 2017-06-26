package org.egordorichev.ldjam.game;

import asciiPanel.AsciiPanel;

import java.awt.event.KeyEvent;

public class HelpState extends GameState {
	public HelpState(Game game) {
		super(game);
	}

	@Override
	public void handleInput(KeyEvent event) {
		if(event.getKeyCode() == KeyEvent.VK_ESCAPE) {
			this.game.setState(new MenuState(this.game));
		}
	}

	@Override
	public void render(AsciiPanel terminal) {
		int y = (terminal.getHeightInCharacters() - 6) / 2;

		terminal.writeCenter("Escape the Room v.0.3", y++);
		y += 2;

		terminal.writeCenter("Solve the maze to escape the room.", y++);
		terminal.writeCenter("Use arrow keys, [esc] and [enter] to navigate.", y++);
		terminal.writeCenter("Collect gold to obtain helpful items.", y++);
		y++;

		terminal.writeCenter("[1-9] - use or drop item   ", y++);
		terminal.writeCenter("[g] - pick up an item      ", y++);
		terminal.writeCenter("[e] - toggle examine mode   ", y++);
	}
}