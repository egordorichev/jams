package org.egordorichev.ldjam;

import asciiPanel.AsciiPanel;
import org.egordorichev.ldjam.game.Game;
import org.egordorichev.ldjam.player.Player;

import java.awt.event.KeyEvent;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class Shop {
	private Item[] items;
	private int[] prices;
	private Player player;

	public Shop(Player player) {
		this.player = player;
		this.items = new Item[5];
		this.prices = new int[5];

		this.enter();
	}

	public void enter() {
		this.items[0] = Item.keyFinder;
		this.prices[0] = 25;
		this.items[1] = Item.map;
		this.prices[1] = 17;
		this.items[2] = Item.masterKey;
		this.prices[2] = 7;
		this.items[3] = Item.lantern;
		this.prices[3] = 10;
		this.items[4] = Item.lighter;
		this.prices[4] = 20;
	}

	public boolean handleInput(KeyEvent event) {
		switch(event.getKeyCode()) {
			case KeyEvent.VK_ESCAPE:
				return true;
			case KeyEvent.VK_1:
				this.buy(0);
			break;
			case KeyEvent.VK_2:
				this.buy(1);
			break;
			case KeyEvent.VK_3:
				this.buy(2);
			break;
			case KeyEvent.VK_4:
				this.buy(3);
			break;
			case KeyEvent.VK_5:
				this.buy(4);
			break;
		}

		return false;
	}

	private void buy(int item) {
		if(this.items[item] == null) {
			return;
		}

		if(this.player.getMoney() < this.prices[item]) {
			this.player.notify("You don't have enough money to buy this item");
			return;
		}

		if(this.player.getInventory().isFull()) {
			this.player.notify("Your inventory is full");
			return;
		}

		this.player.receiveItem(this.items[item]);
		this.player.spendMoney(this.prices[item]);

		Game.audioManager.play("pickup");

		this.items[item] = null;
	}

	public void render(AsciiPanel terminal) {
		terminal.write("Shop", 10, 10);

		for(int i = 0; i < 5; i++) {
			Item item = this.items[i];

			terminal.write("[" + (i + 1) + "]", 10, 14 + i);

			if(item == null) {
				continue;
			}

			terminal.write(item.getGlyph() + " ", 14, 14 + i, item.getColor());
			terminal.write(item.getName() + " ($" + this.prices[i] + ")", 16, 14 + i);
		}
	}


}