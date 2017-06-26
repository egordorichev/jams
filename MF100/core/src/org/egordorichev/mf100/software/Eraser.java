package org.egordorichev.mf100.software;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import org.egordorichev.mf100.graphics.Assets;
import org.egordorichev.mf100.hardware.Drivers;
import org.egordorichev.mf100.hardware.Graphics;
import org.egordorichev.mf100.hardware.monitor.Monitor;

public class Eraser extends ColorRegister {
	private Texture texture;

	public Eraser(Color color, Vector2 position) {
		super(color, position);

		this.texture = Assets.get("eraser.png");
	}

	@Override
	public void render() {
		super.render();
		Graphics.shapeRenderer.end();
		Monitor.batch.begin();
		Monitor.batch.draw(this.texture, this.position.x, this.position.y);
		Monitor.batch.end();
		Graphics.shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
	}
}