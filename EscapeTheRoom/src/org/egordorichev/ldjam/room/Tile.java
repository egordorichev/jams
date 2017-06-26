package org.egordorichev.ldjam.room;

import asciiPanel.AsciiPanel;

import java.awt.Color;

public class Tile {
	public static final Tile bounds = new Tile('#', Color.white, "");
	public static final Tile floor = new Tile('.', AsciiPanel.yellow, "plain floor");
	public static final Tile decoratedFloor = new Tile(',', AsciiPanel.yellow, "plain floor");
	public static final Tile wall = new Tile((char) 219, AsciiPanel.yellow, "old and dirty wall");
	public static final Tile decoratedWall = new Tile((char) 178, AsciiPanel.yellow, "old and dirty wall");
	public static final Tile door = new Tile('+', Color.white, "heavy oak door");
	public static final Tile lockedDoor = new Tile('*', Color.white, "locked oak door");
	public static final Tile verticalMovable = new Tile((char) 18, Color.white, "movable box");
	public static final Tile horizontalMovable = new Tile((char) 29, Color.white, "movable box");
	public static final Tile table = new Tile((char) 210, Color.white, "table");
	public static final Tile chair = new Tile('l', Color.white, "chair");
	public static final Tile shop = new Tile('&', AsciiPanel.brightYellow, "shop");
	public static final Tile spaceshipDoor = new Tile('|', AsciiPanel.green, "massive locked door");
	public static final Tile bush = new Tile((char) 5, AsciiPanel.green, "bush");
	public static final Tile burntBush = new Tile((char) 7, new Color(153, 76, 0), "burnt bush");

	private char glyph;
	private Color color;
	private String description;

	public Tile(char glyph, Color color, String description) {
		this.glyph = glyph;
		this.color = color;
		this.description = description;
	}

	public boolean isSolid() {
		return (this == wall || this == bounds || this == verticalMovable || this == horizontalMovable || this == decoratedWall ||
			this == lockedDoor || this == table || this == bush);
	}

	public char getGlyph() {
		return this.glyph;
	}

	public Color getColor() {
		return this.color;
	}

	public String getDescription() {
		return this.description;
	}
}