package org.egordorichev.mf100.software;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import org.egordorichev.mf100.graphics.Assets;
import org.egordorichev.mf100.hardware.Drivers;
import org.egordorichev.mf100.hardware.Graphics;
import org.egordorichev.mf100.hardware.Keyboard;
import org.egordorichev.mf100.hardware.Operation;
import org.egordorichev.mf100.hardware.monitor.Monitor;
import org.egordorichev.mf100.hardware.monitor.Tty;
import org.egordorichev.mf100.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class CodeEditor extends Operation implements InputProcessor {
	private ArrayList<String> lines = new ArrayList<>();
	private int cursorX;
	private int cursorY;
	private int moveX;
	private int moveY;
	private boolean drawCursor = true;
	private String path;

	public CodeEditor(String path) {
		this.path = path;

		try {
			String currentLine;
			BufferedReader br = new BufferedReader(new FileReader(this.path));

			while ((currentLine = br.readLine()) != null) {
				this.lines.add(currentLine);
			}

			br.close();
		} catch (Exception exception) {
			Log.error(exception.getMessage());
			this.kill();
			Drivers.tty.println(Tty.RED, "Failed to load file");
		}

		Drivers.monitor.state = Monitor.State.GRAPHIC;
		Keyboard.multiplexer.addProcessor(this);

		Timer saveTimer = new Timer();
		saveTimer.schedule(new TimerTask() {
			@Override
			public void run() {
				save();
			}
		}, 0, 10000);
	}

	public void save() {
		try {
			BufferedWriter br = new BufferedWriter(new FileWriter(this.path));

			for (String line : this.lines) {
				line = line.toLowerCase(); //TODO: Temporary fix!
				br.write(line + "\n");
			}

			br.close();
		} catch (Exception exception) {
			Log.error(exception.getMessage());
			this.kill();
			Drivers.tty.println(Tty.RED, "Failed to save file");
		}
	}

	@Override
	public void kill() {
		super.kill();
		this.save();
		Drivers.keyboard.returnLine = true;
	}

	@Override
	public void onRun() {
		Drivers.tty.println("");
	}

	@Override
	public void render() {
		super.render();

		if (this.cursorX - moveX > Tty.TOTAL_COLUMNS - 4) {
			this.moveX = -(this.cursorX - (Tty.TOTAL_COLUMNS - 4));
		} else {
			this.moveX = 0;
		}

		if (this.cursorY - moveY > Tty.TOTAL_ROWS - 4) {
			this.moveY = -(this.cursorY - (Tty.TOTAL_ROWS - 4));
		} else {
			this.moveY = 0;
		}


		int total = this.lines.size() - 1;
		int start = (total < Tty.TOTAL_ROWS) ? total : Math.min(total, cursorY + Tty.TOTAL_ROWS);
		int end = (total < Tty.TOTAL_ROWS) ? 0 : Math.max(0, cursorY - Tty.TOTAL_ROWS);

		for (int i = start; i >= end; i--) {
			Assets.font.draw(Monitor.batch, this.lines.get(i), (this.moveX + 1) * Monitor.CHAR_WIDTH, Monitor.HEIGHT - this.moveY * Monitor.CHAR_HEIGHT - (24 + (i) * Monitor.CHAR_HEIGHT));
		}

		if (this.drawCursor) {
			Monitor.batch.end();
			Graphics.shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
			Graphics.shapeRenderer.setColor(Tty.GREEN);
			Graphics.shapeRenderer.rect((this.cursorX + this.moveX + 1) * Monitor.CHAR_WIDTH, (Monitor.HEIGHT - this.moveY * Monitor.CHAR_HEIGHT  - (this.cursorY + 2) * Monitor.CHAR_HEIGHT), Monitor.CHAR_WIDTH, Monitor.CHAR_HEIGHT);
			Graphics.shapeRenderer.end();
			Monitor.batch.begin();
		}
	}

	@Override
	public boolean keyDown(int keycode) {
		if (Drivers.monitor.state == Monitor.State.TTY) {
			return false;
		}

		if (keycode == Input.Keys.DOWN && this.cursorY < this.lines.size() - 1) {
			this.cursorY++;

			if (this.cursorX > this.lines.get(this.cursorY).length()) {
				this.cursorX = this.lines.get(this.cursorY).length();
			}
		} else if (keycode == Input.Keys.UP && this.cursorY > 0) {
			this.cursorY--;

			if (this.cursorX > this.lines.get(this.cursorY).length()) {
				this.cursorX = this.lines.get(this.cursorY).length();
			}
		} else if (keycode == Input.Keys.LEFT && this.cursorX > 0) {
			this.cursorX--;
		} else if (keycode == Input.Keys.RIGHT && this.cursorX < this.lines.get(this.cursorY).length()) {
			this.cursorX++;
		} else if (keycode == Input.Keys.ENTER) {
			if (this.lines.size() == 0) {
				this.lines.add(++this.cursorY, "");
				this.cursorX = 0;
				return false;
			} else if (this.lines.get(this.cursorY).length() == 0) {
				this.lines.add(this.cursorY, "");
				this.cursorX = 0;
				this.cursorY++;
				return false;
			} else if (this.cursorX == 0) {
				this.lines.add(this.cursorY, "");
				this.cursorY++;
				return false;
			}

			String line = this.lines.get(this.cursorY);
			String start = line.substring(0, this.cursorX);
			String end = line.substring(this.cursorX, line.length());

			this.lines.set(this.cursorY, start);
			this.lines.add(++this.cursorY, end);

			this.cursorX = 0;
		} else if (keycode == Input.Keys.BACKSPACE) {
			if (this.cursorX == 0 && this.cursorY > 0) {
				int length = this.lines.get(this.cursorY - 1).length();
				this.lines.set(this.cursorY - 1, this.lines.get(this.cursorY - 1) + this.lines.get(this.cursorY));
				this.lines.remove(this.cursorY);
				this.cursorY--;
				this.cursorX = length;
				return false;
			}

			String line = this.lines.get(this.cursorY);
			String start = line.substring(0, Math.max(0, this.cursorX - 1));
			String end = line.substring(this.cursorX, line.length());

			if (this.cursorX > 0) {
				this.cursorX--;
			}

			this.lines.set(this.cursorY, start + end);
		} else if (keycode == Input.Keys.FORWARD_DEL) {
			StringBuilder line = new StringBuilder(this.lines.get(this.cursorY));
			line.delete(this.cursorX, this.cursorX + 1);
			this.lines.set(this.cursorY, line.toString());
		} else if (keycode == Input.Keys.TAB) {
			this.keyTyped(' ');
			this.keyTyped(' ');
		}

		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		if (Drivers.monitor.state == Monitor.State.TTY) {
			return false;
		}

		if (this.lines.size() == 0) {
			this.lines.add("");
		}

		if (Keyboard.isPrintableChar(character)) {
			StringBuilder line = new StringBuilder(this.lines.get(this.cursorY));

			line.insert(this.cursorX, character);
			this.cursorX++;

			this.lines.set(this.cursorY, line.toString());
		}

		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}
}