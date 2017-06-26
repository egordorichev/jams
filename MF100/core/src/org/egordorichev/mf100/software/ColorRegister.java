package org.egordorichev.mf100.software;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import org.egordorichev.mf100.hardware.Graphics;
import org.egordorichev.mf100.hardware.monitor.Monitor;
import org.egordorichev.mf100.util.Util;

public class ColorRegister {
	private Color color;
	protected Vector2 position;

	public ColorRegister(Color color, Vector2 position) {
		this.color = color;
		this.position = position;
	}

	public void render() {
		Graphics.shapeRenderer.setColor(this.color);
		Graphics.shapeRenderer.rect(this.position.x, this.position.y, 48, 48);
	}

	public boolean isPressed(int x, int y) {
		return Util.isColliding(x, y, (int) this.position.x, (int) (Monitor.HEIGHT - 48 - this.position.y), 48, 48);
	}

	public Color getColor() {
		return this.color;
	}

	public Vector2 getPosition() {
		return this.position;
	}
}