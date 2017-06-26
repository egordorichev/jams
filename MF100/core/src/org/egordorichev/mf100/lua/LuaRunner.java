package org.egordorichev.mf100.lua;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import org.egordorichev.mf100.hardware.Drivers;
import org.egordorichev.mf100.hardware.Keyboard;
import org.egordorichev.mf100.hardware.Operation;
import org.egordorichev.mf100.hardware.monitor.Monitor;
import org.egordorichev.mf100.util.Log;
import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.jse.CoerceJavaToLua;

import java.util.concurrent.TimeUnit;

import static org.luaj.vm2.LuaValue.NIL;

public class LuaRunner extends Operation implements InputProcessor {
	private String code;
	private LuaValue draw;
	private LuaValue update;
	private LuaValue keyPressed;
	private LuaValue mousePressed;
	private LuaValue mouseReleased;
	private boolean drawExists = false;
	private boolean updateExists = false;
	private boolean keyPressedExists = false;
	private boolean mousePressedExists = false;
	private boolean mouseReleasedExists = false;
	private Globals globals;
	private boolean running = true;

	public LuaRunner(String code) {
		this.code = code;
		Keyboard.multiplexer.addProcessor(this);
	}

	@Override
	public void kill() {
		super.kill();
		this.running = false;
	}

	@Override
	public void onRun() {
		Drivers.tty.println("");
		this.globals = LuaAPI.createGlobals();

		if (!LuaAPI.run(code, this.globals)) {
			try {
				TimeUnit.MICROSECONDS.sleep(10);
			} catch (InterruptedException exception) {
				Log.error("Failed to sleep");
			}

			this.kill();
			return;
		} else {
			Drivers.monitor.state = Monitor.State.GRAPHIC;
		}

		LuaValue init = this.globals.get("init");

		if (init != null && init != NIL) {
			init.invoke();
		}

		this.draw = this.globals.get("draw");
		this.update = this.globals.get("update");
		this.keyPressed = this.globals.get("key_pressed");
		this.mousePressed = this.globals.get("mouse_pressed");
		this.mouseReleased = this.globals.get("mouse_released");

		if (this.draw != NIL) {
			this.drawExists = true;
		}

		if (this.update != NIL) {
			this.updateExists = true;
		}

		if (this.keyPressed != NIL) {
			this.keyPressedExists = true;
		}

		if (this.mousePressed != NIL) {
			this.mousePressedExists = true;
		}

		if (this.mouseReleased != NIL) {
			this.mouseReleasedExists = true;
		}
	}

	@Override
	public void render() {
		super.render();

		if (!this.running) {
			return;
		}

		if (this.updateExists) {
			this.update.invoke(LuaValue.valueOf(Gdx.graphics.getDeltaTime()));
		}

		if (this.drawExists) {
			this.draw.invoke();
		}
	}

	@Override
	public boolean keyDown(int keycode) {
		if (!this.running) {
			return false;
		}

		if (this.keyPressedExists) {
			this.keyPressed.invoke(LuaValue.valueOf(Input.Keys.toString(keycode).toLowerCase()));
		}

		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		if (this.running && this.mousePressedExists) {
			this.mousePressed.invoke(LuaValue.valueOf(screenX), LuaValue.valueOf(screenY));
		}

		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		if (this.running && this.mouseReleasedExists) {
			this.mouseReleased.invoke(LuaValue.valueOf(screenX), LuaValue.valueOf(screenY));
		}

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