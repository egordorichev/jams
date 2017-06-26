package org.egordorichev.ldjam.room;

import asciiPanel.AsciiPanel;
import org.egordorichev.ldjam.Item;
import org.egordorichev.ldjam.player.Player;

import java.awt.*;
import java.io.*;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.ArrayList;

public class Room {
	public static final int MAX_ITEMS = 5;

	private int width;
	private int height;
	private Tile[][] tiles;
	private boolean[][] exploredTiles;
	private Item[][][] items;
	private Point keyPosition;
	private boolean showKey;

	public Room(int level) {
		this.showKey = false;
		this.keyPosition = new Point(0, 0);
		this.load(level);
	}

	public void render(AsciiPanel terminal, Player player) {
		for(int y = 0; y < Math.min(this.height, terminal.getHeightInCharacters()); y++) {
			for(int x = 0; x < Math.min(this.width, terminal.getWidthInCharacters()); x++) {
				if(player.canSee(x, y)) {
					this.exploredTiles[x][y] = true;
					Item item = this.getItem(x, y);

					if(item != null) {
						terminal.write(item.getGlyph(), x, y, item.getColor());
					} else {
						Tile tile = this.getTile(x, y);
						terminal.write(tile.getGlyph(), x, y, tile.getColor());
					}
				} else if(this.keyPosition.x == x && this.keyPosition.y == y && this.showKey) {
					terminal.write(Item.doorKey.getGlyph(), x, y, AsciiPanel.red);
				} else if(this.exploredTiles[x][y]) {
					Tile tile = this.getTile(x, y);
					terminal.write(tile.getGlyph(), x, y, new Color(32, 32, 32));
				}
			}
		}
	}

	public void setTile(Tile tile, int x, int y) {
		if(this.isInside(x, y)) {
			this.tiles[x][y] = tile;
		}
	}

	public void addItem(Item item, int x, int y) {
		if(this.isInside(x, y)) {
			for(int i = 0; i < MAX_ITEMS; i++) {
				if(this.items[x][y][i] == null) {
					this.items[x][y][i] = item;
					return;
				}
			}
		}
	}

	public void discover() {
		for(int y = 0; y < this.height; y++) {
			for(int x = 0; x < this.width; x++) {
				this.exploredTiles[x][y] = true;
			}
		}
	}

	public void showKey() {
		this.showKey = true;
	}

	public Item pickupItem(int x, int y) {
		if(!this.isInside(x, y)) {
			return null;
		}

		for(int i = MAX_ITEMS - 1; i > -1; i--) {
			if(this.items[x][y][i] != null) {
				Item item = this.items[x][y][i];
				this.items[x][y][i] = null;

				return item;
			}
		}

		return null;
	}

	public Item getItem(int x, int y) {
		if(!this.isInside(x, y)) {
			return null;
		}

		for(int i = MAX_ITEMS - 1; i > -1; i--) {
			if(this.items[x][y][i] != null) {
				return this.items[x][y][i];
			}
		}

		return null;
	}

	public Tile getTile(int x, int y) {
		if(this.isInside(x, y)) {
			return this.tiles[x][y];
		}

		return Tile.bounds;
	}

	public char getTileGlyph(int x, int y) {
		Tile tile = this.getTile(x, y);

		return tile.getGlyph();
	}

	public boolean isInside(int x, int y) {
		return (x >= 0 && x < this.width && y >= 0 && y < this.height);
	}

	public boolean isOnScreen(AsciiPanel terminal, int x, int y) {
		return (x >= 0 && x < terminal.getWidthInCharacters() &&
			y >= 0 && y < terminal.getHeightInCharacters());
	}

	public int getWidth() {
		return this.width;
	}

	public int getHeight() {
		return this.height;
	}

	public boolean isDiscovered(int x, int y) {
		if(!this.isInside(x, y)) {
			return false;
		}

		return this.exploredTiles[x][y];
	}

	private void load(int level) {
		try {
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(this.getClass().getResourceAsStream("levels/" + level + ".lvl")));

			String line;
			ArrayList<ArrayList<Integer>> tempMap = new ArrayList<>();

			while((line = bufferedReader.readLine()).charAt(0) != '[') {
				if(line.isEmpty()) {
					continue;
				}

				ArrayList<Integer> row = new ArrayList<>();
				String[] values = line.trim().split(" ");

				for(String value : values) {
					if(value.isEmpty()) {
						continue;
					}

					row.add(Integer.parseInt(value));
				}

				tempMap.add(row);
			}

			this.width = tempMap.get(0).size();
			this.height = tempMap.size();
			this.tiles = new Tile[this.width][this.height];
			this.exploredTiles = new boolean[this.width][this.height];
			this.items = new Item[this.width][this.height][MAX_ITEMS];

			for(int y = 0; y < this.height; y++) {
				for(int x = 0; x < this.width; x++) {
					switch(tempMap.get(y).get(x)) {
						case 2:
							this.tiles[x][y] = (Math.random() < 0.5) ? Tile.floor : Tile.decoratedFloor;
						break;
						case 1:
							this.tiles[x][y] = (Math.random() < 0.5) ? Tile.wall : Tile.decoratedWall;
						break;
						case 3:
							this.tiles[x][y] = Tile.door;
						break;
						case 4:
							this.tiles[x][y] = Tile.lockedDoor;
						break;
						case 5:
							this.tiles[x][y] = Tile.verticalMovable;
						break;
						case 6:
							this.tiles[x][y] = Tile.horizontalMovable;
						break;
						case 7:
							this.tiles[x][y] = Tile.table;
						break;
						case 8:
							this.tiles[x][y] = Tile.chair;
						break;
						case 9:
							this.tiles[x][y] = Tile.shop;
						break;
						case 10:
							this.tiles[x][y] = Tile.spaceshipDoor;
						break;
						case 11:
							this.tiles[x][y] = Tile.bush;
						break;
						case 12:
							this.tiles[x][y] = Tile.floor;
							this.addItem(Item.fiveDollars, x, y);
						break;
						case 13:
							this.tiles[x][y] = Tile.floor;
							this.addItem(Item.map, x, y);
						break;
						case 14:
							this.tiles[x][y] = Tile.floor;
							this.addItem(Item.lantern, x, y);
						break;
						case 15:
							this.tiles[x][y] = Tile.floor;
							this.addItem(Item.stone, x, y);
						break;
						default:
							this.tiles[x][y] = Tile.floor;
						break;
					}

					this.exploredTiles[x][y] = false;
				}
			}

			while((line = bufferedReader.readLine()) != null) {
				String[] values = line.trim().split(" ");

				for(int i = 0; i < values.length; i++) {
					String value = values[i];

					if(value.isEmpty()) {
						continue;
					}

					int x = Integer.parseInt(values[++i]);
					int y = Integer.parseInt(values[++i]);

					switch(Integer.parseInt(value)) {
						case 213:
							this.addItem(Item.doorKey, x, y);
							this.keyPosition.x = x;
							this.keyPosition.y = y;
						break;
						case 111:
							this.addItem(Item.stone, x, y);
						break;
						case 36:
							this.addItem(Item.fiveDollars, x, y);
						break;
						case 169:
							this.addItem(Item.masterKey, x, y);
						break;
					}
				}
			}
		} catch (IOException exception) {
			exception.printStackTrace();
		}
	}
}