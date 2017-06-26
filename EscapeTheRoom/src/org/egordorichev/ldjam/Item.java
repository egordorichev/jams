package org.egordorichev.ldjam;

import asciiPanel.AsciiPanel;

import java.awt.Color;

public class Item {
	public static final Item doorKey = new Item("key from the door", (char) 213, AsciiPanel.white);
	public static final Item stone = new Item("big chunk of rock", (char) 111, Color.gray);
	public static final Item fiveDollars = new Item("five dollars", '$', AsciiPanel.brightYellow);
	public static final Item keyFinder = new Item("key finder", (char) 26, AsciiPanel.brightGreen);
	public static final Item map = new Item("map", (char) 254, AsciiPanel.white);
	public static final Item masterKey = new Item("master key", (char) 169, AsciiPanel.brightMagenta);
	public static final Item lantern = new Item("lantern", (char) 15, AsciiPanel.brightYellow);
	public static final Item lighter = new Item("lighter", (char) 19, AsciiPanel.red);

	private String name;
	private char glyph;
	private Color color;

	public Item(String name, char glyph, Color color) {
		this.name = name;
		this.glyph = glyph;
		this.color = color;
	}

	public String getName() {
		return this.name;
	}

	public char getGlyph() {
		return this.glyph;
	}

	public Color getColor() {
		return this.color;
	}
}