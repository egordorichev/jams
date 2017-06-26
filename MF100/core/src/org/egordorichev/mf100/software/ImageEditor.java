package org.egordorichev.mf100.software;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.PixmapIO;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.TextureData;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import org.egordorichev.mf100.hardware.Drivers;
import org.egordorichev.mf100.hardware.Graphics;
import org.egordorichev.mf100.hardware.Keyboard;
import org.egordorichev.mf100.hardware.Operation;
import org.egordorichev.mf100.hardware.monitor.Monitor;
import org.egordorichev.mf100.hardware.monitor.Tty;
import org.egordorichev.mf100.util.Log;
import org.egordorichev.mf100.util.Util;

import java.io.File;

public class ImageEditor extends Operation implements InputProcessor {
	private Texture texture;
	private ColorRegister[] colors = new ColorRegister[17];
	private ColorRegister currentColor;
	private String path;

	public ImageEditor(String path) {
		this.path = path;

		Gdx.app.postRunnable(() -> {
			if (new File(path).exists()) {
				// texture = new Texture(new Pixmap(new FileHandle(path)));
				texture = new Texture(new Pixmap(32, 32, Pixmap.Format.RGBA8888));
			} else {
				texture = new Texture(new Pixmap(32, 32, Pixmap.Format.RGBA8888));
			}
		});

		this.colors[0] = new ColorRegister(Tty.WHITE, new Vector2(Monitor.WIDTH - 48 - 16, Monitor.HEIGHT - 48 - 16));
		this.colors[1] = new ColorRegister(Tty.BLACK, new Vector2(Monitor.WIDTH - 96 - 16, Monitor.HEIGHT - 96 - 16));
		this.colors[2] = new ColorRegister(Tty.BROWN, new Vector2(Monitor.WIDTH - 96 - 16, Monitor.HEIGHT - 48 - 16));
		this.colors[3] = new ColorRegister(Tty.DARK_BLUE, new Vector2(Monitor.WIDTH - 48 - 16, Monitor.HEIGHT - 96 - 16));
		this.colors[4] = new ColorRegister(Tty.DARK_GRAY, new Vector2(Monitor.WIDTH - 144 - 16, Monitor.HEIGHT - 48 - 16));
		this.colors[5] = new ColorRegister(Tty.BLUE, new Vector2(Monitor.WIDTH - 144 - 16, Monitor.HEIGHT - 96 - 16));
		this.colors[6] = new ColorRegister(Tty.LIGHT_GRAY, new Vector2(Monitor.WIDTH - 48 - 16, Monitor.HEIGHT - 144 - 16));
		this.colors[7] = new ColorRegister(Tty.GREEN, new Vector2(Monitor.WIDTH - 96 - 16, Monitor.HEIGHT - 48 - 144 - 16));
		this.colors[8] = new ColorRegister(Tty.DARK_GREEN, new Vector2(Monitor.WIDTH - 96 - 16, Monitor.HEIGHT - 144 - 16));
		this.colors[9] = new ColorRegister(Tty.PEACH, new Vector2(Monitor.WIDTH - 48 - 16, Monitor.HEIGHT - 48 - 144 - 16));
		this.colors[10] = new ColorRegister(Tty.DARK_PURPLE, new Vector2(Monitor.WIDTH - 144 - 16, Monitor.HEIGHT - 144 - 16));
		this.colors[11] = new ColorRegister(Tty.RED, new Vector2(Monitor.WIDTH - 16, Monitor.HEIGHT - 96 - 192 - 16));
		this.colors[12] = new ColorRegister(Tty.YELLOW, new Vector2(Monitor.WIDTH - 96 - 16, Monitor.HEIGHT - 96 - 144 - 16));
		this.colors[13] = new ColorRegister(Tty.ORANGE, new Vector2(Monitor.WIDTH - 48 - 16, Monitor.HEIGHT - 96 - 144 - 16));
		this.colors[14] = new ColorRegister(Tty.INDIGO, new Vector2(Monitor.WIDTH - 144 - 16, Monitor.HEIGHT - 192 - 16));
		this.colors[15] = new ColorRegister(Tty.PINK, new Vector2(Monitor.WIDTH - 144 - 16, Monitor.HEIGHT - 48 - 192 - 16));
		this.colors[16] = new Eraser(Tty.BLACK, new Vector2(Monitor.WIDTH - 48 - 16, Monitor.HEIGHT - 48 - 240 - 16));

		this.currentColor = this.colors[0];

		Drivers.monitor.state = Monitor.State.GRAPHIC;
		Keyboard.multiplexer.addProcessor(this); // DARN IT!
	}

	public void save() {
		TextureData data = this.texture.getTextureData();
		Pixmap pixmap = data.consumePixmap();
		PixmapIO.writePNG(new FileHandle(this.path), pixmap);
	}

	@Override
	public void kill() {
		super.kill();
		this.save();
		Drivers.keyboard.returnLine = true;
	}

	@Override
	public void render() {
		super.render();

		// Draw ui

		Monitor.batch.end();

		Graphics.shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
		Graphics.shapeRenderer.setColor(Tty.RED);
		Graphics.shapeRenderer.rect(0, 0, Monitor.WIDTH, Monitor.HEIGHT);
		Graphics.shapeRenderer.setColor(Tty.DARK_GRAY);
		Graphics.shapeRenderer.rect(16, Monitor.HEIGHT - 256 - 16, 128, 128);
		Graphics.shapeRenderer.rect(16 + 128, Monitor.HEIGHT - 128 - 16, 128, 128);
		Graphics.shapeRenderer.setColor(Tty.LIGHT_GRAY);
		Graphics.shapeRenderer.rect(16, Monitor.HEIGHT - 128 - 16, 128, 128);
		Graphics.shapeRenderer.rect(16 + 128, Monitor.HEIGHT - 256 - 16, 128, 128);
		Graphics.shapeRenderer.end();

		Monitor.batch.begin();

		if (this.texture != null) {
			Monitor.batch.draw(this.texture, 16, Monitor.HEIGHT - 256 - 16, 256, 256);
			Monitor.batch.draw(this.texture, 16, Monitor.HEIGHT - 256 - 96, 32, 32);
		}

		Monitor.batch.end();

		Graphics.shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

		for (ColorRegister c : this.colors) {
			c.render();
		}

		Graphics.shapeRenderer.setColor(this.currentColor.getColor());
		Graphics.shapeRenderer.rect(Gdx.input.getX() - 4, (Monitor.HEIGHT - Gdx.input.getY() - 2) / 8 * 8, 8, 8);
		Graphics.shapeRenderer.end();

		Monitor.batch.begin();
	}

	@Override
	public void onRun() {
		Drivers.tty.println("");
	}

	@Override
	public boolean keyDown(int keycode) {
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
		if (Util.isColliding(screenX, screenY, 16, 16, 256, 256)) {
			TextureData data = this.texture.getTextureData();

			if (!data.isPrepared()) {
				data.prepare();
			}

			Pixmap pixmap = this.texture.getTextureData().consumePixmap();
			pixmap.setBlending(Pixmap.Blending.None);

			if (this.currentColor instanceof Eraser) {
				pixmap.setColor(Color.CLEAR);
			} else {
				pixmap.setColor(this.currentColor.getColor());
			}

			pixmap.drawPixel((int) Util.map(screenX - 16, 0, 256, 0, 32), (int) Util.map(screenY - 16, 0, 256, 0, 32));

			this.texture = new Texture(pixmap);
		} else {
			for (ColorRegister c : this.colors) {
				if (c.isPressed(screenX, screenY)) {
					this.currentColor = c;
					return false;
				}
			}

			this.currentColor = colors[11];
		}

		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		if (Util.isColliding(screenX, screenY, 16, 16, 256, 256)) {
			TextureData data = this.texture.getTextureData();

			if (!data.isPrepared()) {
				data.prepare();
			}

			Pixmap pixmap = this.texture.getTextureData().consumePixmap();
			pixmap.setBlending(Pixmap.Blending.None);

			if (this.currentColor instanceof Eraser) {
				pixmap.setColor(Color.CLEAR);
			} else {
				pixmap.setColor(this.currentColor.getColor());
			}

			pixmap.drawPixel((int) Util.map(screenX - 16, 0, 256, 0, 32), (int) Util.map(screenY - 16, 0, 256, 0, 32));

			this.texture = new Texture(pixmap);
		}

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