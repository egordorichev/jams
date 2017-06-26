package org.egordorichev.mf100.hardware;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class Graphics {
	public static Color fillColor = Color.WHITE;
	public static boolean fill = true;
	public static Color strokeColor = Color.WHITE;
	public static int strokeWeight = 1;

	public static ShapeRenderer shapeRenderer;

	public Graphics() {
		shapeRenderer = new ShapeRenderer();
	}

	public void render() {
		shapeRenderer.setColor(fillColor);
	}
}