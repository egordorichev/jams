package org.egordorichev.mf100;

import org.egordorichev.mf100.graphics.Assets;

public class Game extends com.badlogic.gdx.Game {
	public static MF100 mf100;

	@Override
	public void create() {
		Assets.load();
		mf100 = new MF100();
	}

	@Override
	public void render() {
		mf100.render();
	}

	@Override
	public void dispose() {
		super.dispose();
	}
}
