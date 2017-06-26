package org.egordorichev.mf100.util;

import org.egordorichev.mf100.hardware.Drivers;

import java.util.concurrent.TimeUnit;

public class Util {
	public static float map(float x, float inMin, float inMax, float outMin, float outMax) {
		return (x - inMin) * (outMax - outMin) / (inMax - inMin) + outMin;
	}

	public static boolean isColliding(int px, int py, int x, int y, int width, int height) {
		return px >= x && px <= x + width && py >= y && py <= y + height;
	}

	public static int randomInt(int min, int max) {
		return Drivers.random.nextInt((max - min) + 1) + min;
	}

	public static void delay() {
		try {
			TimeUnit.MILLISECONDS.sleep(200);
		} catch (InterruptedException exception) {
			Log.error("Failed to sleep");
		}
	}
}