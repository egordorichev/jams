package org.egordorichev.mf100.hardware;

import org.egordorichev.mf100.hardware.monitor.Monitor;
import org.egordorichev.mf100.hardware.monitor.Tty;
import org.egordorichev.mf100.lua.Lua;

import java.util.Random;

public class Drivers {
	public static Monitor monitor;
	public static Tty tty;
	public static Graphics graphics;
	public static FileSystem fileSystem;
	public static Keyboard keyboard;
	public static Lua lua;
	public static Random random;

	public static void init() {
		random = new Random();
		monitor = new Monitor();
		monitor.clear();
		tty = new Tty();
		graphics = new Graphics();
		fileSystem = new FileSystem();
		keyboard = new Keyboard();
		lua = new Lua();
	}
}