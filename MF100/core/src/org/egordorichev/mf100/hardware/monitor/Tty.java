package org.egordorichev.mf100.hardware.monitor;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import org.egordorichev.mf100.graphics.Assets;
import org.egordorichev.mf100.hardware.Driver;
import org.egordorichev.mf100.hardware.Graphics;
import org.egordorichev.mf100.hardware.monitor.Monitor;
import org.egordorichev.mf100.util.Log;

import java.util.Timer;
import java.util.TimerTask;

public class Tty extends Driver {
	public static final int TOTAL_ROWS = Monitor.HEIGHT / Monitor.CHAR_HEIGHT - 2;
	public static final int TOTAL_COLUMNS = Monitor.WIDTH / Monitor.CHAR_WIDTH - 2;

	public static final Color BLACK = new Color(0, 0, 0, 1f);
	public static final Color DARK_BLUE = new Color(29 / 255f, 43 / 255f, 83 / 255f, 1f);
	public static final Color DARK_PURPLE = new Color(126 / 255f, 37 / 255f, 83 / 255f, 1f);
	public static final Color DARK_GREEN = new Color(0, 135 / 255f, 81 / 255f, 1f);
	public static final Color BROWN = new Color(171 / 255f, 82 / 255f, 54 / 255f, 1f);
	public static final Color DARK_GRAY = new Color(95 / 255f, 87 / 255f, 79 / 255f, 1f);
	public static final Color LIGHT_GRAY = new Color(194 / 255f, 195 / 255f, 199 / 255f, 1f);
	public static final Color WHITE = new Color(1f, 241 / 255f, 232 / 255f, 1f);
	public static final Color RED = new Color(1f, 0, 77 / 255f, 1f);
	public static final Color ORANGE = new Color(1f, 163 / 255f, 0, 1f);
	public static final Color YELLOW = new Color(1f, 236 / 255f, 39 / 255f, 1f);
	public static final Color GREEN = new Color(0, 228 / 255f, 54 / 255f, 1f);
	public static final Color BLUE = new Color(41 / 255f, 173 / 255f, 1f, 1f);
	public static final Color INDIGO = new Color(131 / 255f, 118 / 255f, 156 / 255f, 1f);
	public static final Color PINK = new Color(255 / 255f, 119 / 255f, 168 / 255f, 1f);
	public static final Color PEACH = new Color(255 / 255f, 204 / 255f, 170 / 255f, 1f);

	public static final Color DEFAULT_COLOR = WHITE;

	private char[][] buffer = new char[TOTAL_COLUMNS][TOTAL_ROWS];
	private Color[][] colors = new Color[TOTAL_COLUMNS][TOTAL_ROWS];
	private int cursorX = 0;
	private Color currentColor = DEFAULT_COLOR;
	private boolean drawCursor = true;

	public Tty() {
		this.clear();

		for (int y = 0; y < TOTAL_ROWS; y++) {
			for (int x = 0; x < TOTAL_COLUMNS; x++) {
				this.colors[x][y] = DEFAULT_COLOR;
			}
		}

		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				drawCursor = !drawCursor;
			}
		}, 0, 700);
	}

	public void render() {
		for (int y = 0; y < TOTAL_ROWS; y++) {
			for (int x = 0; x < TOTAL_COLUMNS; x++) {
				Assets.font.setColor(this.colors[x][y]);
				Assets.font.draw(Monitor.batch,
					"" + this.buffer[x][y], (x + 1) * Monitor.CHAR_WIDTH, (y + 2) * Monitor.CHAR_HEIGHT - 2);
			}
		}

		// Assets.font.setColor(DEFAULT_COLOR);

		if (this.drawCursor) {
			Monitor.batch.end();
			Graphics.shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
			Graphics.shapeRenderer.setColor(RED);
			Graphics.shapeRenderer.rect((this.cursorX + 1) * Monitor.CHAR_WIDTH, Monitor.CHAR_HEIGHT, Monitor.CHAR_WIDTH, Monitor.CHAR_HEIGHT);
			Graphics.shapeRenderer.end();
			Monitor.batch.begin();
		}
	}

	public void setColor(Color color) {
		this.currentColor = color;
	}

	public void print(String message) {
		for (int i = 0; i < message.length(); i++) {
			this.putChar(message.charAt(i));
		}
	}

	public void println(String message) {
		this.print(message);
		this.putChar('\n');
	}

	public void print(Color color, String message) {
		this.setColor(color);

		for (int i = 0; i < message.length(); i++) {
			this.putChar(message.charAt(i));
		}

		this.setColor(DEFAULT_COLOR);
	}

	public void println(Color color, String message) {
		this.print(color, message);
		this.putChar('\n');
	}

	public void putChar(char character) {
		switch (character) {
			case '\n':
				this.scroll();
			break;
			default:
				this.buffer[this.cursorX][0] = character;
				this.colors[this.cursorX][0] = currentColor;

				if (++this.cursorX >= TOTAL_COLUMNS) {
					this.scroll();
				}
			break;
		}
	}

	public void deleteChar() {
		this.cursorX -= 1;

		if (this.cursorX < 0) {
			this.scrollBack();
			this.cursorX = TOTAL_COLUMNS - 1;
		}

		this.buffer[this.cursorX][0] = ' ';
		this.colors[this.cursorX][0] = DEFAULT_COLOR;
	}

	public void scroll() {
		for (int y = TOTAL_ROWS - 1; y >= 1; y--) {
			for (int x = 0; x < TOTAL_COLUMNS; x++) {
				this.buffer[x][y] = this.buffer[x][y - 1];
				this.colors[x][y] = this.colors[x][y - 1];
			}
		}

		for (int x = 0; x < TOTAL_COLUMNS; x++) {
			this.buffer[x][0] = ' ';
			this.colors[x][0] = DEFAULT_COLOR;
		}

		this.cursorX = 0;
	}

	public void scrollBack() {
		for (int y = 0; y < TOTAL_ROWS - 1; y++) {
			for (int x = 0; x < TOTAL_COLUMNS; x++) {
				this.buffer[x][y] = this.buffer[x][y + 1];
				this.colors[x][y] = this.colors[x][y + 1];
			}
		}

		this.cursorX = 0;
	}


	public void clear() {
		for (int y = 0; y < TOTAL_ROWS; y++) {
			for (int x = 0; x < TOTAL_COLUMNS; x++) {
				this.buffer[x][y] = ' ';
			}
		}
	}
}