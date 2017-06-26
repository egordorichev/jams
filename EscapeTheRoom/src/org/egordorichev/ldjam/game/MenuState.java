package org.egordorichev.ldjam.game;

import asciiPanel.AsciiPanel;

import java.awt.event.KeyEvent;

public class MenuState extends GameState {
	private int currentButton;

	public MenuState(Game game) {
		super(game);

		this.currentButton = 0;
	}

	@Override
	public void handleInput(KeyEvent event) {
		switch(event.getKeyCode()) {
			case KeyEvent.VK_DOWN:
				this.currentButton++;

				if(this.currentButton > 2) {
					this.currentButton = 2;
				}
			break;
			case KeyEvent.VK_UP:
				this.currentButton--;

				if(this.currentButton < 0) {
					this.currentButton = 0;
				}
			break;
			case KeyEvent.VK_ENTER:
				switch(this.currentButton) {
					case 0:
						this.game.setState(new GamePlayState(this.game));
					break;
					case 1:
						this.game.setState(new HelpState(this.game));
					break;
					case 2:
						System.exit(0);
					break;
				}
		}
	}

	@Override
	public void render(AsciiPanel terminal) {
		int y = (terminal.getHeightInCharacters() - 6) / 2;

		terminal.writeCenter("Escape the Room v.0.3", y++);

		y += 2;

		if(this.currentButton == 0) {
			terminal.writeCenter("++    Start     ++", y++, AsciiPanel.brightYellow);
		} else {
			terminal.writeCenter("--    Start     --", y++);
		}

		if(this.currentButton == 1) {
			terminal.writeCenter("++ How to play  ++", y++, AsciiPanel.brightYellow);
		} else {
			terminal.writeCenter("-- How to play  --", y++);
		}

		if(this.currentButton == 2) {
			terminal.writeCenter("++     Exit     ++", y++, AsciiPanel.brightYellow);
		} else {
			terminal.writeCenter("--     Exit     --", y++);
		}

		terminal.write("Use arrow keys, [enter] and [esc] for navigating", 1, terminal.getHeightInCharacters() - 2);
	}
}