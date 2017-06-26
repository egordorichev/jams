package org.egordorichev.ldjam.game;

import asciiPanel.AsciiPanel;
import org.egordorichev.ldjam.Item;
import org.egordorichev.ldjam.Language;
import org.egordorichev.ldjam.Shop;
import org.egordorichev.ldjam.player.Inventory;
import org.egordorichev.ldjam.player.Player;
import org.egordorichev.ldjam.room.Room;
import org.egordorichev.ldjam.room.Tile;

import java.awt.Point;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class GamePlayState extends GameState {
	private Room room;
	private Player player;
	private Point cursor;
	private Shop shop;

	enum State {
		NORMAL,
		EXAMINE,
		SHOP
	}

	private State state;

	public GamePlayState(Game game) {
		super(game);

		this.room = new Room(this.game.getCurrentLevel());
		this.player = game.getPlayer();
		this.player.setRoom(this.room);
		this.player.setPosition(1, 3);
		this.state = State.NORMAL;
		this.shop = new Shop(this.player);
		this.cursor = new Point(10, 10);
	}

	@Override
	public void handleInput(KeyEvent event) {
		if(this.state == State.EXAMINE) {
			switch(event.getKeyCode()) {
				case KeyEvent.VK_UP:
					this.cursor.y--;

					if(this.cursor.y < 0) {
						this.cursor.y = 0;
					}
					break;
				case KeyEvent.VK_DOWN:
					this.cursor.y++;

					if(this.cursor.y > this.room.getHeight() - 1) {
						this.cursor.y = this.room.getHeight() - 1;
					}
					break;
				case KeyEvent.VK_LEFT:
					this.cursor.x--;

					if(this.cursor.x < 0) {
						this.cursor.x = 0;
					}
					break;
				case KeyEvent.VK_RIGHT:
					this.cursor.x++;

					if(this.cursor.x > this.room.getWidth() - 1) {
						this.cursor.x = this.room.getWidth() - 1;
					}
					break;
				case KeyEvent.VK_ESCAPE: case KeyEvent.VK_E:
					this.state = State.NORMAL;
					break;
			}
		} else if(this.state == State.SHOP) {
			if(this.shop.handleInput(event)) {
				this.state = State.NORMAL;
			}
		} else {
			this.player.clearMessage();

			switch(event.getKeyCode()) {
				case KeyEvent.VK_UP:
					this.player.moveBy(0, -1);
					break;
				case KeyEvent.VK_DOWN:
					this.player.moveBy(0, 1);
					break;
				case KeyEvent.VK_LEFT:
					this.player.moveBy(-1, 0);
					break;
				case KeyEvent.VK_RIGHT:
					this.player.moveBy(1, 0);
					break;
				case KeyEvent.VK_G:
					this.player.pickupItem();
					break;
				case KeyEvent.VK_E:
					this.state = State.EXAMINE;
					this.cursor.x = this.player.getX();
					this.cursor.y = this.player.getY();
					break;
				default:
					char keyCode = event.getKeyChar();

					if(keyCode >= '1' && keyCode <= '9') {
						this.player.useItem(keyCode - '0');
					}
					break;
			}

			if(this.player.getX() == 0 && this.player.getY() == 3) {
				this.game.setState(new WonState(this.game));
			}
		}
	}

	@Override
	public void render(AsciiPanel terminal) {
		int screenWidth = terminal.getWidthInCharacters();
		int screenHeight = terminal.getHeightInCharacters();

		if(this.state == State.SHOP) {
			this.shop.render(terminal);
		} else {
			this.room.render(terminal, this.player);

			terminal.write('@', this.player.getX(), this.player.getY(), AsciiPanel.brightYellow);
		}

		// GUI

		terminal.clear(' ', screenWidth - 29, 0, 29, screenHeight);
		terminal.clear((char) 186, screenWidth - 30, 0, 1, screenHeight);

		Inventory inventory = this.player.getInventory();
		int x = screenWidth - 27;

		terminal.write("Inventory", x, 1);

		if(this.player.getMoney() == 0) {
			terminal.write("$0", screenWidth - 4, 1);
		} else {
			terminal.write("$" + this.player.getMoney(), screenWidth - 3 - (int) (Math.log10(this.player.getMoney()) + 1), 1);
		}

		for(int i = 0; i < inventory.getSize(); i++) {
			Item item = inventory.getItem(i);

			if(item != null) {
				terminal.write("[" + (i + 1) + "] " + Language.addArticle(item.getName()), x, 3 + i);
			} else {
				terminal.write("[" + (i + 1) + "]", x, 3 + i);
			}
		}

		terminal.write((char) 204, x - 3, inventory.getSize() + 4);
		terminal.clear((char) 205, x - 2, inventory.getSize() + 4, 29, 1);

		terminal.write("[1-9] - use or drop item", x, terminal.getHeightInCharacters() - 4);
		terminal.write("Use arrow keys to move", x, terminal.getHeightInCharacters() - 2);
		terminal.write("[e] - enter examine mode", x, terminal.getHeightInCharacters() - 5);

		if(this.state != State.NORMAL) {
			terminal.write("[esc] - back", x, terminal.getHeightInCharacters() - 6);
		} else {
			terminal.write("[g] - pick up item", x, terminal.getHeightInCharacters() - 6);
		}

		if(this.state == State.EXAMINE) {
			if(this.cursor.y > 0) {
				terminal.write("|", this.cursor.x, this.cursor.y - 1, AsciiPanel.brightMagenta);
			}

			if(this.cursor.x > 0) {
				terminal.write("-", this.cursor.x - 1, this.cursor.y, AsciiPanel.brightMagenta);
			}

			terminal.write("-", this.cursor.x + 1, this.cursor.y, AsciiPanel.brightMagenta);
			terminal.write("|", this.cursor.x, this.cursor.y + 1, AsciiPanel.brightMagenta);

			String description;

			if(this.player.getX() == this.cursor.x && this.player.getY() == this.cursor.y) {
				description = "It's you!";
			} else {
				if(this.room.isDiscovered(this.cursor.x, this.cursor.y) || this.player.canSee(this.cursor.x, this.cursor.y)) {

					Item item = this.room.getItem(this.cursor.x, this.cursor.y);

					if(item != null) {
						description = Language.addArticle(item.getName());
					} else {
						Tile tile = this.room.getTile(this.cursor.x, this.cursor.y);
						description = "It's " + Language.addArticle(tile.getDescription());
					}
				} else {
					description = "You can't see anything";
				}

				terminal.write(description, x, inventory.getSize() + 6);
			}
		} else if(this.player.getMessage() != "") {
			if(this.player.getMessage() == "Welcome to the shop!" && this.state != State.SHOP) {
				this.state = State.SHOP;
				this.shop.enter();
				this.player.clearMessage();
				this.game.repaint();
			}

			List<String> messages = this.splitString(this.player.getMessage(), 26);

			int i = 0;
			for(String message : messages) {
				terminal.write(messages.get(i), x, inventory.getSize() + 6 + i);
				i++;
			}
		}
	}

	@Override
	public void update() {
		if(this.state == State.NORMAL) {
			if(Game.keys[KeyEvent.VK_UP] == 1) {
				this.player.moveBy(0, -1);
				this.game.repaint();
			}

			if(Game.keys[KeyEvent.VK_DOWN] == 1) {
				this.player.moveBy(0, 1);
				this.game.repaint();
			}

			if(Game.keys[KeyEvent.VK_LEFT] == 1) {
				this.player.moveBy(-1, 0);
				this.game.repaint();
			}

			if(Game.keys[KeyEvent.VK_RIGHT] == 1) {
				this.player.moveBy(1, 0);
				this.game.repaint();
			}
		} else if(this.state == State.EXAMINE) {
			if(Game.keys[KeyEvent.VK_UP] == 1) {
				this.cursor.y--;

				if(this.cursor.y < 0) {
					this.cursor.y = 0;
				}
			}

			if(Game.keys[KeyEvent.VK_DOWN] == 1) {
				this.cursor.y++;

				if(this.cursor.y > this.room.getHeight() - 1) {
					this.cursor.y = this.room.getHeight() - 1;
				}
			}

			if(Game.keys[KeyEvent.VK_LEFT] == 1) {
				this.cursor.x--;

				if(this.cursor.x < 0) {
					this.cursor.x = 0;
				}
			}

			if(Game.keys[KeyEvent.VK_RIGHT] == 1) {
				this.cursor.x++;

				if(this.cursor.x > this.room.getWidth() - 1) {
					this.cursor.x = this.room.getWidth() - 1;
				}
			}
		}
	}

	private List<String> splitString(String msg, int lineSize) {
		List<String> res = new ArrayList<>();

		Pattern p = Pattern.compile("\\b.{1," + (lineSize-1) + "}\\b\\W?");
		Matcher m = p.matcher(msg);

		while(m.find()) {
			res.add(m.group());
		}
		return res;
	}
}
