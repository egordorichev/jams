package org.egordorichev.mf100.lua;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import org.egordorichev.mf100.graphics.Assets;
import org.egordorichev.mf100.hardware.Drivers;
import org.egordorichev.mf100.hardware.Graphics;
import org.egordorichev.mf100.hardware.monitor.Monitor;
import org.egordorichev.mf100.hardware.monitor.Tty;
import org.egordorichev.mf100.hardware.monitor.TtyStream;
import org.egordorichev.mf100.util.Log;
import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaError;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;
import org.luaj.vm2.lib.*;
import org.luaj.vm2.lib.jse.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.TimeUnit;

public class LuaAPI {
	public static Globals globals;
	public static Texture texture;
	public static Sound sound;
	public static Music music;
	public static boolean error = false;

	public static void init() {
		globals = createGlobals();
	}

	public static void defineFunction(String name, ZeroArgFunction function) {
		globals.set(name, function);
	}

	public static void defineFunction(String name, OneArgFunction function) {
		globals.set(name, function);
	}

	public static void defineFunction(String name, TwoArgFunction function) {
		globals.set(name, function);
	}

	public static void defineFunction(String name, ThreeArgFunction function) {
		globals.set(name, function);
	}

	public static void defineFunction(String name, FourArgFunction function) {
		globals.set(name, function);
	}

	public static void defineFunction(String name, FiveArgFunction function) {
		globals.set(name, function);
	}

	public static Varargs callFunction(String name) {
		try {
			LuaValue chunk = LuaAPI.globals.load(name.toLowerCase());
			return chunk.call();
		} catch (LuaError exception) {
			Log.error(exception.getMessage());
		}

		return null;
	}

	public static Varargs callFunction(String name, LuaValue[] args) {
		try {
			return globals.get(name.toLowerCase()).invoke(LuaValue.listOf(args));
		} catch (LuaError exception) {
			Log.error(exception.getMessage());
			Drivers.tty.println(Tty.RED, exception.getMessage());
		}

		return null;
	}

	public static void bindClass(String name, String aClass) {
		globals.load(name + " = luajava.bindClass('" + aClass + "')");
	}

	public static boolean run(String filePath, Globals g) {
		filePath = filePath.replace("\\", "/");

		try {
			if (g != null) {
				globals.get("dofile").call(LuaValue.valueOf(new URI(filePath).normalize().toString()));
			} else {
				globals.get("dofile").call(LuaValue.valueOf(new URI(filePath).normalize().toString()));
			}

			return true;
		} catch (LuaError exception) {
			Log.error(exception.getMessage());
			Drivers.tty.println(Tty.RED, exception.getMessage());
		} catch(URISyntaxException exception) {
			exception.printStackTrace();
		}

		return false;
	}

	public static Globals createGlobals() {
		globals = JsePlatform.standardGlobals();
		globals.STDOUT = new TtyStream();

		globals.load(new JseBaseLib());
		globals.load(new PackageLib());
		globals.load(new LuajavaLib());

		LuaAPI.defineFunction("fill", new FourArgFunction() {
			@Override
			public LuaValue call(LuaValue r, LuaValue g, LuaValue b, LuaValue a) {
				Graphics.fillColor = new Color((r == NIL) ? 0 : r.toint(),
						(g == NIL) ? 0 : g.toint(), (b == NIL) ? 0 : b.toint(),
						(a == NIL) ? 1f : a.toint());

				Assets.font.setColor(Graphics.fillColor.cpy());
				Graphics.fill = true;

				// TODO: translate 0-255 into 0.0f-1.0f

				return NIL;
			}
		});

		LuaAPI.defineFunction("no_fill", new ZeroArgFunction() {
			@Override
			public LuaValue call() {
				Graphics.fill = false;
				return NIL;
			}
		});

		/* LuaAPI.defineFunction("stroke", new FourArgFunction() {
			@Override
			public LuaValue call(LuaValue r, LuaValue g, LuaValue b, LuaValue a) {
				Graphics.strokeColor = new Color((r == NIL) ? 0 : r.toint(),
						(g == NIL) ? 0 : g.toint(), (b == NIL) ? 0 : b.toint(),
						(a == NIL) ? 1f : a.toint());

				// TODO: translate 0-255 into 0.0f-1.0f

				return NIL;
			}
		});*/

		/* LuaAPI.defineFunction("stroke_weight", new OneArgFunction() {
			@Override
			public LuaValue call(LuaValue weight) {
				Graphics.strokeWeight = (weight == NIL) ? 0 : weight.toint();
				return NIL;
			}
		}); */

		/* LuaAPI.defineFunction("no_stroke", new ZeroArgFunction() {
			@Override
			public LuaValue call() {
				Graphics.strokeWeight = 0;
				return NIL;
			}
		}); */

		LuaAPI.defineFunction("rect", new FourArgFunction() {
			@Override
			public LuaValue call(LuaValue x, LuaValue y, LuaValue width, LuaValue height) {
				Monitor.batch.end();


				Graphics.shapeRenderer.begin((Graphics.fill) ? ShapeRenderer.ShapeType.Filled : ShapeRenderer.ShapeType.Line);
				Graphics.shapeRenderer.rect((x == NIL) ? 0 : x.tofloat(),
					(y == NIL) ? 0 : y.tofloat(), (width == NIL) ? 100 : width.tofloat(),
					(height == NIL) ? 100 : height.tofloat());

				Graphics.shapeRenderer.end();
				Monitor.batch.begin();

				return NIL;
			}
		});

		LuaAPI.defineFunction("circle", new ThreeArgFunction() {
			@Override
			public LuaValue call(LuaValue x, LuaValue y, LuaValue r) {
				Monitor.batch.end();


				Graphics.shapeRenderer.begin((Graphics.fill) ? ShapeRenderer.ShapeType.Filled : ShapeRenderer.ShapeType.Line);
				Graphics.shapeRenderer.circle((x == NIL) ? 0 : x.tofloat(),
					(y == NIL) ? 0 : y.tofloat(), (r == NIL) ? 100 : r.tofloat());

				Graphics.shapeRenderer.end();
				Monitor.batch.begin();

				return NIL;
			}
		});

		LuaAPI.defineFunction("point", new TwoArgFunction() {
			@Override
			public LuaValue call(LuaValue x, LuaValue y) {
				Monitor.batch.end();

				Graphics.shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
				Graphics.shapeRenderer.point((x == NIL) ? 0 : x.tofloat(),
						(y == NIL) ? 0 : y.tofloat(), 0);

				Graphics.shapeRenderer.end();
				Monitor.batch.begin();

				return NIL;
			}
		});

		LuaAPI.defineFunction("line", new FourArgFunction() {
			@Override
			public LuaValue call(LuaValue x1, LuaValue y1, LuaValue x2, LuaValue y2) {
				Monitor.batch.end();


				Graphics.shapeRenderer.begin((Graphics.fill) ? ShapeRenderer.ShapeType.Filled : ShapeRenderer.ShapeType.Line);
				Graphics.shapeRenderer.line((x1 == NIL) ? 0 : x1.tofloat(),
						(y1 == NIL) ? 0 : y1.tofloat(), (x2 == NIL) ? 10 : x2.tofloat(),
						(y2 == NIL) ? 10 : y2.tofloat()); // TODO: width

				Graphics.shapeRenderer.end();
				Monitor.batch.begin();

				return NIL;
			}
		});

		LuaAPI.defineFunction("text", new ThreeArgFunction() {
			@Override
			public LuaValue call(LuaValue text, LuaValue x, LuaValue y) {
				Assets.font.draw(Monitor.batch, (text == NIL) ? "Where is your text?" : text.tojstring(),
					(x == NIL) ? 0 : x.tofloat(), ((y == NIL) ?  0 : y.tofloat()) + 20);

				return NIL;
			}
		});

		LuaAPI.defineFunction("set_color", new OneArgFunction() {
			@Override
			public LuaValue call(LuaValue color) {
				int value = (color == NIL) ? 0 : color.toint();

				switch (value) {
					case 0: Drivers.tty.setColor(Tty.WHITE); break;
					case 1: Drivers.tty.setColor(Tty.BLACK); break;
					case 2: Drivers.tty.setColor(Tty.DARK_BLUE); break;
					case 3: Drivers.tty.setColor(Tty.DARK_PURPLE); break;
					case 4: Drivers.tty.setColor(Tty.DARK_GREEN); break;
					case 5: Drivers.tty.setColor(Tty.DARK_GRAY); break;
					case 6: Drivers.tty.setColor(Tty.LIGHT_GRAY); break;
					case 7: Drivers.tty.setColor(Tty.BLUE); break;
					case 8: Drivers.tty.setColor(Tty.GREEN); break;
					case 9: Drivers.tty.setColor(Tty.INDIGO); break;
					case 10: Drivers.tty.setColor(Tty.PEACH); break;
					case 11: Drivers.tty.setColor(Tty.PINK); break;
					case 12: Drivers.tty.setColor(Tty.BROWN); break;
					case 13: Drivers.tty.setColor(Tty.ORANGE); break;
					case 14: Drivers.tty.setColor(Tty.YELLOW); break;
					case 15: Drivers.tty.setColor(Tty.RED); break;
				}

				return NIL;
			}
		});

		LuaAPI.defineFunction("load_texture", new OneArgFunction() {
			@Override
			public LuaValue call(LuaValue path) {
				if (path == NIL) {
					return NIL;
				}

				try {
					Gdx.app.postRunnable(new Runnable() {
						@Override
						public void run() {
							try {
								texture = new Texture(Drivers.fileSystem.root + Drivers.fileSystem.workingDirectory + path);
							} catch (RuntimeException exception) {
								Drivers.tty.println(Tty.RED, "Failed to load texture.");
								error = true;
							}
						}
					});

					while (texture == null) {
						if (error) {
							error = false;
							return NIL;
						}

						try {
							TimeUnit.MICROSECONDS.sleep(10);
						} catch (InterruptedException exception) {
							Log.error("Failed to sleep");
						}
					}

					return CoerceJavaToLua.coerce(texture);
				} catch (RuntimeException exception) {
					Drivers.tty.println(Tty.RED, "Failed to load texture.");
					return NIL;
				}
			}
		});

		LuaAPI.defineFunction("load_sound", new OneArgFunction() {
			@Override
			public LuaValue call(LuaValue path) {
				if (path == NIL) {
					return NIL;
				}

				try {
					Gdx.app.postRunnable(new Runnable() {
						@Override
						public void run() {
							try {
								sound = Gdx.audio.newSound(new FileHandle(Drivers.fileSystem.root + Drivers.fileSystem.workingDirectory + path));
							} catch (RuntimeException exception) {
								Drivers.tty.println(Tty.RED, "Failed to load sound.");
								error = true;
							}
						}
					});

					while (sound == null) {
						if (error) {
							error = false;
							return NIL;
						}

						try {
							TimeUnit.MICROSECONDS.sleep(10);
						} catch (InterruptedException exception) {
							Log.error("Failed to sleep");
						}
					}

					return CoerceJavaToLua.coerce(sound);
				} catch (RuntimeException exception) {
					Drivers.tty.println(Tty.RED, "Failed to load sound.");
					return NIL;
				}
			}
		});

		LuaAPI.defineFunction("load_music", new OneArgFunction() {
			@Override
			public LuaValue call(LuaValue path) {
				if (path == NIL) {
					return NIL;
				}

				try {
					Gdx.app.postRunnable(new Runnable() {
						@Override
						public void run() {
							try {
								music = Gdx.audio.newMusic(new FileHandle(Drivers.fileSystem.root + Drivers.fileSystem.workingDirectory + path));
							} catch (RuntimeException exception) {
								Drivers.tty.println(Tty.RED, "Failed to load music.");
								error = true;
							}
						}
					});

					while (music == null) {
						if (error) {
							error = false;
							return NIL;
						}

						try {
							TimeUnit.MICROSECONDS.sleep(10);
						} catch (InterruptedException exception) {
							Log.error("Failed to sleep");
						}
					}

					return CoerceJavaToLua.coerce(music);
				} catch (RuntimeException exception) {
					Drivers.tty.println(Tty.RED, "Failed to load music.");
					return NIL;
				}
			}
		});

		defineFunction("image", new FiveArgFunction() {
			@Override
			public LuaValue call(LuaValue texture, LuaValue x, LuaValue y, LuaValue w, LuaValue h) {
				if (texture == NIL) {
					return NIL;
				}

				boolean drawing = Graphics.shapeRenderer.isDrawing();

				if (drawing) {
					Graphics.shapeRenderer.end();
					Monitor.batch.begin();
				}

				Texture t = (Texture) CoerceLuaToJava.coerce(texture, Texture.class);

				Monitor.batch.draw(t, (x == NIL) ? 0 : x.tofloat(),
					(y == NIL) ? 0 : y.tofloat(), (w == NIL) ? t.getWidth() : w.tofloat(), (h == NIL) ? t.getHeight() : h.tofloat());

				if (drawing) {
					Monitor.batch.end();
					Graphics.shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
				}

				return NIL;
			}
		});

		defineFunction("play_sound", new OneArgFunction() {
			@Override
			public LuaValue call(LuaValue sound) {
				if (sound == NIL) {
					return NIL;
				}

				Sound s = (Sound) CoerceLuaToJava.coerce(sound, Sound.class);
				s.play();

				return NIL;
			}
		});

		defineFunction("play_music", new TwoArgFunction() {
			@Override
			public LuaValue call(LuaValue music, LuaValue looped) {
				if (music == NIL) {
					return NIL;
				}

				Music m = (Music) CoerceLuaToJava.coerce(music, Music.class);

				if (looped != NIL && looped.toboolean()) {
					m.setLooping(true);
				}

				m.play();

				return NIL;
			}
		});

		LuaAPI.defineFunction("get_mouse_x", new ZeroArgFunction() {
			@Override
			public LuaValue call() {
				return LuaValue.valueOf(Gdx.input.getX());
			}
		});

		LuaAPI.defineFunction("get_mouse_y", new ZeroArgFunction() {
			@Override
			public LuaValue call() {
				return LuaValue.valueOf(Gdx.input.getY());
			}
		});

		bindClass("texture", "com.badlogic.gdx.graphics.Texture");
		bindClass("sound", "com.badlogic.gdx.audio.Sound");
		bindClass("music", "com.badlogic.gdx.audio.Music");

		return globals;
	}
}