package org.egordorichev.mf100.hardware;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.audio.Sound;
import org.egordorichev.mf100.MF100;
import org.egordorichev.mf100.graphics.Assets;
import org.egordorichev.mf100.hardware.monitor.Monitor;
import org.egordorichev.mf100.software.ImageEditor;
import org.egordorichev.mf100.software.Shell;
import org.egordorichev.mf100.util.Log;

import java.util.concurrent.TimeUnit;

public class Keyboard extends Driver implements InputProcessor {
	private String line = "";
	public boolean returnLine = false;
	private boolean printChars = true;
	public static InputMultiplexer multiplexer = new InputMultiplexer();

	public Keyboard() {
		multiplexer.addProcessor(this);
		Gdx.input.setInputProcessor(multiplexer);
	}

	public String getLine() {
		this.line = "";

		while (true) {
			try {
				TimeUnit.MICROSECONDS.sleep(10);
			} catch (InterruptedException exception) {
				Log.error("Failed to sleep");
			}

			if (this.returnLine) {
				break;
			}
		}

		this.returnLine = false;

		return this.line;
	}

	public void waitForEnter() {
		this.printChars = false;

		while (true) {
			try {
				TimeUnit.MICROSECONDS.sleep(10);
			} catch (InterruptedException exception) {
				Log.error("Failed to sleep");
			}

			if (this.returnLine) {
				break;
			}
		}

		this.printChars = true;
		this.returnLine = false;
	}

	@Override
	public boolean keyDown(int keycode) {
		Sound keyPress = Assets.getSound("keypress.mp3");
		keyPress.play();

		if (keycode == Input.Keys.ESCAPE && Drivers.monitor.state != Monitor.State.TTY) {
			Drivers.monitor.state = Monitor.State.TTY;
			this.returnLine = true;

			MF100.currentOperation.kill();

			Drivers.tty.clear();

			Drivers.tty.print(">");
			return true;
		}

		if (keycode == Input.Keys.ENTER) {
			if (Drivers.monitor.state == Monitor.State.TTY && this.printChars && !(MF100.currentOperation instanceof ImageEditor)) {
				Drivers.tty.putChar('\n');
			}

			this.returnLine = true;
		} else if (keycode == Input.Keys.BACKSPACE) {
			if (Drivers.monitor.state == Monitor.State.TTY && this.printChars) {
				if (this.line.length() > 0) {
					this.line = this.line.substring(0, this.line.length() - 1);
					Drivers.tty.deleteChar();
				}
			}
		} else if ((keycode == Input.Keys.C && Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT))){// || keycode == Input.Keys.ESCAPE) {
			Drivers.monitor.state = Monitor.State.TTY;
			this.returnLine = true;

			if (MF100.currentOperation != null && !(MF100.currentOperation instanceof Shell)) {
				MF100.currentOperation.kill();
			}

			Drivers.tty.clear();
		}

		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		return false;
	}

	public static boolean isPrintableChar(char c) {
		Character.UnicodeBlock block = Character.UnicodeBlock.of(c);

		return (!Character.isISOControl(c)) &&
				block != null &&
				block != Character.UnicodeBlock.SPECIALS;
	}

	@Override
	public boolean keyTyped(char character) {
		if (Drivers.monitor.state == Monitor.State.TTY) {
			if (isPrintableChar(character)) {
				this.line += character;
				Drivers.tty.putChar(character);
			}
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