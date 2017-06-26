package org.egordorichev.mf100.software;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import org.egordorichev.mf100.MF100;
import org.egordorichev.mf100.graphics.Assets;
import org.egordorichev.mf100.hardware.Drivers;
import org.egordorichev.mf100.hardware.Graphics;
import org.egordorichev.mf100.hardware.Operation;
import org.egordorichev.mf100.hardware.monitor.Monitor;
import org.egordorichev.mf100.hardware.monitor.Tty;

import java.util.ArrayList;

public class Ping extends Operation {
	private int cartX = 200;
	private Ball ball = new Ball();
	private ArrayList<Rectangle> rectangles = new ArrayList<>();
	private byte state = 3;
	public static Sound ping;
	public static Sound pong;

	public Ping() {
		Drivers.monitor.state = Monitor.State.GRAPHIC;
		ping = Assets.getSound("ping.mp3");
		pong = Assets.getSound("pong.mp3");
	}

	@Override
	public void onRun() {
		Drivers.tty.println("");

		for (int y = 0; y < 3; y++) {
			for (int x = 0; x < 10; x++) {
				this.rectangles.add(new Rectangle(x * 48 + 4, Monitor.HEIGHT - 100 - y * 20 - 4, 40, 16));
			}
		}

		this.state = 0;
	}

	@Override
	public void render() {
		super.render();

		if (this.state == 3) {
			return;
		}

		if (this.state == 1) {
			Assets.font.draw(Monitor.batch, "game over!", (Monitor.WIDTH - 130) / 2, Monitor.HEIGHT / 2 + 20);
		} else if (this.state == 2) {
			Assets.font.draw(Monitor.batch, "you won!", (Monitor.WIDTH - 100) / 2, Monitor.HEIGHT / 2 + 20);
		} else {
			if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
				this.cartX = Math.max(0, this.cartX - 2);
			}

			if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
				this.cartX = Math.min(Monitor.WIDTH - 100, this.cartX + 2);
			}

			if (this.ball.update()) {
				Assets.getSound("gameover.mp3").play();
				this.state = 1;
				Drivers.tty.println(Tty.RED, "game over!");

				new java.util.Timer().schedule(
						new java.util.TimerTask() {
							@Override
							public void run() {
								kill();
							}
						}, 5000);
			}  else if (this.rectangles.size() == 0) {
				this.state = 2;
				Assets.getSound("won.mp3").play();
				Drivers.tty.println(Tty.RED, "you won!");

				new java.util.Timer().schedule(
						new java.util.TimerTask() {
							@Override
							public void run() {
								kill();
								MF100.currentOperation = new Socket();
								MF100.currentOperation.start();
							}
						}, 5000);

				this.state = 2;
				Drivers.tty.println(Tty.RED, "you won!"); // Server rpg
			}

			Rectangle rect = new Rectangle(this.cartX, 0, 100, Monitor.CHAR_HEIGHT);

			for (int i = this.rectangles.size() - 1; i >= 0; i--) {
				if (this.ball.isColliding(this.rectangles.get(i))) {
					Ping.pong.play();

					this.ball.bounce(false);
					this.rectangles.remove(i); // TODO: sounds
					break;
				}
			}

			if (this.ball.isColliding(rect)) {
				this.ball.bounce(false);
				Ping.ping.play();

			}

			Monitor.batch.end();
			Graphics.shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
			Graphics.shapeRenderer.setColor(Tty.RED);
			Graphics.shapeRenderer.rect(cartX, 0, 100, Monitor.CHAR_HEIGHT);
			Graphics.shapeRenderer.setColor(Tty.GREEN);

			for (Rectangle rectangle : this.rectangles) { // TODO: colors?
				Graphics.shapeRenderer.rect(rectangle.x, rectangle.y, rectangle.width, rectangle.height);
			}

			this.ball.render();
			Graphics.shapeRenderer.end();
			Monitor.batch.begin();
		}
	}

	@Override
	public void kill() {
		super.kill();
		Drivers.keyboard.returnLine = true;
	}
}