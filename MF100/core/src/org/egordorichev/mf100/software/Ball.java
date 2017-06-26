package org.egordorichev.mf100.software;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import org.egordorichev.mf100.hardware.Drivers;
import org.egordorichev.mf100.hardware.Graphics;
import org.egordorichev.mf100.hardware.monitor.Monitor;
import org.egordorichev.mf100.hardware.monitor.Tty;

public class Ball {
	public static final int RADIUS = 12;

	private Vector2 position = new Vector2(Monitor.WIDTH / 2, Monitor.HEIGHT - 180);
	private Vector2 velocity = new Vector2(Drivers.random.nextBoolean() ? 3 : -3, -3);

	public boolean update() {
		this.position.add(this.velocity);

		if (this.position.x < RADIUS || this.position.x + RADIUS * 2 > Monitor.WIDTH) {
			this.bounce(true);
			Ping.ping.play();
		}

		if (this.position.y + RADIUS > Monitor.HEIGHT) {
			this.bounce(false);
			Ping.ping.play();
		} else if (this.position.y - RADIUS <= 0) {
			return true;
		}

		return false;
	}

	public void render() {
		Graphics.shapeRenderer.setColor(Tty.PINK);
		Graphics.shapeRenderer.circle(this.position.x, this.position.y, RADIUS);
	}

	public boolean isColliding(Rectangle rect) {
		return rect.overlaps(new Rectangle(this.position.x - RADIUS, this.position.y - RADIUS, RADIUS * 2, RADIUS * 2));
	}

	public void bounce(boolean side) {
		if (side) {
			this.velocity.x *= -1;
		} else {
			this.velocity.y *= -1;
		}
	}
}