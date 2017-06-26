package org.egordorichev.mf100.hardware.monitor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import org.egordorichev.mf100.MF100;
import org.egordorichev.mf100.hardware.Driver;
import org.egordorichev.mf100.hardware.Drivers;
import org.egordorichev.mf100.software.Shell;

public class Monitor extends Driver {
	public static final int HEIGHT = 480;
	public static final int WIDTH = 480;
	public static final int CHAR_WIDTH = 16;
	public static final int CHAR_HEIGHT = 24;

	public enum State {
		TTY,
		GRAPHIC
	}

	public State state = State.TTY;
	public static SpriteBatch batch;
	public OrthographicCamera camera;

	public Monitor() {
		batch = new SpriteBatch();

		this.camera = new OrthographicCamera(WIDTH, HEIGHT);
		this.camera.position.x = WIDTH / 2;
		this.camera.position.y = HEIGHT / 2;
		this.camera.update();
	}

	public void render() {
		this.clear();

		batch.setProjectionMatrix(this.camera.combined);
		batch.begin();

		if (this.state == State.TTY) {
			Drivers.tty.render();
		} else {
			Drivers.graphics.render();
		}

		if (MF100.currentOperation != null) {
			MF100.currentOperation.render();
		}

		batch.end();
	}

	public void clear() {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
	}
}