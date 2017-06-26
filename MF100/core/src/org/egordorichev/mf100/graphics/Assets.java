package org.egordorichev.mf100.graphics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

public class Assets {
	public static AssetManager assets = new AssetManager();
	public static BitmapFont font;

	public static void load() {
		font = new BitmapFont(Gdx.files.internal("font.fnt"));

		loadTexture("eraser.png");

		loadMusic("music.mp3");
		loadSound("ping.mp3");
		loadSound("pong.mp3");
		loadSound("gameover.mp3");
		loadSound("won.mp3");
		loadSound("keypress.mp3");
		
		assets.finishLoading();
	}

	public static void dispose() {
		assets.dispose();
	}

	public static void loadTexture(String fileName) {
		assets.load(fileName, Texture.class);
	}

	public static void loadSound(String fileName) {
		assets.load(fileName, Sound.class);
	}

	public static void loadMusic(String fileName) {
		assets.load(fileName, Music.class);
	}

	public static Texture get(String fileName) {
		return assets.get(fileName, Texture.class);
	}

	public static Sound getSound(String fileName) {
		return assets.get(fileName, Sound.class);
	}

	public static Music getMusic(String fileName) {
		return assets.get(fileName, Music.class);
	}
}