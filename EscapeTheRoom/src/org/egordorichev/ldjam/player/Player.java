package org.egordorichev.ldjam.player;

import org.egordorichev.ldjam.Item;
import org.egordorichev.ldjam.Language;
import org.egordorichev.ldjam.Line;
import org.egordorichev.ldjam.game.Game;
import org.egordorichev.ldjam.room.Room;
import org.egordorichev.ldjam.room.Tile;

import java.awt.*;

public class Player {
	private int x;
	private int y;
	private int visionRadius;
	private int money;
	private String message;
	private Room room;
	private Inventory inventory;

	public Player(Room room, int x, int y) {
		this.room = room;
		this.visionRadius = 12;
		this.x = x;
		this.y = y;
		this.money = 0;
		this.message = "";
		this.inventory = new Inventory(9);
	}

	public void moveBy(int x, int y) {
		int newX = this.x + x;
		int newY = this.y + y;

		Tile tile = this.room.getTile(newX, newY);

		if(tile == Tile.lockedDoor) {
			if(this.inventory.hasItem(Item.doorKey)) {
				this.inventory.removeItem(Item.doorKey);
				this.room.setTile(Tile.door, newX, newY);
				this.message = "You unlocked the door";
			} else {
				this.message = "You don't have matching key";
			}
		} else if(tile == Tile.spaceshipDoor) {
			if(this.inventory.hasItem(Item.masterKey)) {
				this.inventory.removeItem(Item.masterKey);
				this.room.setTile(Tile.door, newX, newY);
				this.message = "You unlocked the door";
			} else {
				this.message = "You don't have master key";
			}
		} else if(tile == Tile.verticalMovable) {
			if(y != 0 && !this.room.getTile(newX, newY + y).isSolid()) {
				this.room.setTile(Tile.floor, newX, newY);
				this.room.setTile(Tile.verticalMovable, newX, newY + y);
				Game.audioManager.play("hit");
			}
		} else if(tile == Tile.horizontalMovable) {
			if(x != 0 && !this.room.getTile(newX + x, newY).isSolid()) {
				this.room.setTile(Tile.floor, newX, newY);
				this.room.setTile(Tile.horizontalMovable, newX + x, newY);
				Game.audioManager.play("hit");
			}
		} else if(tile == Tile.shop) {
			this.message = "Welcome to the shop!";
		} else if(tile == Tile.bush) {
			if(this.inventory.hasItem(Item.lighter)) {
				this.room.setTile(Tile.burntBush, newX, newY);
			} else {
				this.message = "You don't have a lighter";
			}
		} else if(!tile.isSolid()) {
			this.setPosition(newX, newY);

			Item item = this.room.getItem(newX, newY);

			if(item != null) {
				if(item == Item.fiveDollars) {
					this.message = "There is some money";
				} else {
					this.message = "There is " + Language.addArticle(item.getName());
				}
			}
		} else {
			this.message = "You bumped your head";
			Game.audioManager.play("hit");
		}
	}

	public void useItem(int index) {
		Item item = this.inventory.getItem(index - 1);

		if(item != null) {
			if(item == Item.keyFinder) {
				this.room.showKey();
				this.inventory.removeItem(Item.keyFinder);
				Game.audioManager.play("keyfinder");
			} else if(item == Item.map) {
				this.room.discover();
				this.inventory.removeItem(Item.map);
				Game.audioManager.play("keyfinder");
			} else if(item == Item.lantern) {
				this.visionRadius = 16;
				this.inventory.removeItem(Item.lantern);
				Game.audioManager.play("keyfinder");
			} else {
				this.inventory.removeItem(item);
				this.room.addItem(item, this.x, this.y);
				Game.audioManager.play("pickup");
			}
		}
	}

	public void setPosition(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public void pickupItem() {
		Item item = this.room.pickupItem(this.x, this.y);

		if(item == Item.fiveDollars) {
			this.receiveItem(item);
			Game.audioManager.play("pickup");

			return;
		}

		if(this.inventory.isFull()) {
			return;
		}

		if(item != null) {
			this.message = "You picked up " + Language.addArticle(item.getName());

			this.receiveItem(item);
			Game.audioManager.play("pickup");
		}
	}

	public void receiveItem(Item item) {
		if(item == Item.fiveDollars) {
			this.money += 5;
		} else {
			this.inventory.addItem(item);
		}
	}

	public void spendMoney(int amount) {
		this.money -= amount;

		if(this.money < 0) {
			this.money = 0;
		}
	}

	public void clearMessage() {
		this.message = "";
	}

	public void setRoom(Room room) {
		this.room = room;
	}

	public void notify(String message) {
		this.message = message;
	}

	public int getX() {
		return this.x;
	}

	public int getY() {
		return this.y;
	}

	public int getMoney() {
		return this.money;
	}

	public Room getRoom() {
		return this.room;
	}

	public Inventory getInventory() {
		return this.inventory;
	}

	public String getMessage() {
		return this.message;
	}

	public boolean canSee(int x, int y) {
		if((this.x - x) * (this.x - x) + (this.y - y) * (this.y - y) > this.visionRadius * this.visionRadius) {
			return false;
		}

		boolean door = false;

		for(Point point : new Line(this.x, this.y, x, y).getPoints()) {
			if(door) {
				return false;
			}

			Tile tile = this.room.getTile(point.x, point.y);

			if(tile == Tile.door || tile == Tile.lockedDoor) {
				if(this.x == point.x && this.y == point.y) {
					continue;
				} else {
					door = true;
				}
			}

			if(!tile.isSolid() || point.x == x && point.y == y) {
				continue;
			}

			return false;
		}

		return true;
	}

}