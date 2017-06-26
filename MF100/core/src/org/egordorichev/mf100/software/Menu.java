package org.egordorichev.mf100.software;

import com.badlogic.gdx.Gdx;
import org.egordorichev.mf100.Version;
import org.egordorichev.mf100.hardware.Drivers;
import org.egordorichev.mf100.hardware.Operation;
import org.egordorichev.mf100.hardware.monitor.Monitor;
import org.egordorichev.mf100.hardware.monitor.Tty;
import org.egordorichev.mf100.util.Util;

import java.util.ArrayList;

public class Menu extends Operation {
	private ArrayList<ShellCommand> commands = new ArrayList<>();
	private boolean exit = false;

	public Menu() {
		this.commands.add(new ShellCommand("help") {
			@Override
			public void run(String[] args) {
				Drivers.tty.println(Tty.GREEN, "boot: start the game");
				Drivers.tty.println(Tty.PEACH, "about: print info");
				Drivers.tty.println(Tty.PINK, "shutdown: exit the game");
			}
		});

		this.commands.add(new ShellCommand("boot") {
			@Override
			public void run(String[] args) {
				kill();
				exit = true;
				Drivers.tty.clear();
				Drivers.monitor.state = Monitor.State.GRAPHIC;
				Drivers.monitor.state = Monitor.State.TTY;
				new Shell().start();
			}
		});

		this.commands.add(new ShellCommand("about") {
			@Override
			public void run(String[] args) {
				Drivers.tty.println("the game made for the\nLudum Dare #38\nby Egor and Vito in april\n2017. The goal is to hack\n"
					+ "Ludum Dare's servers.\nalso it is possible to write your\nown game!");

				Drivers.tty.println("\nhttps://github.com/egordorichev/ldjam38");
			}
		});

		this.commands.add(new ShellCommand("shutdown") {
			@Override
			public void run(String[] args) {
				Gdx.app.exit();
			}
		});
	}

	@Override
	public void onRun() {
		Drivers.tty.println("BIOS ready");
		Util.delay();
		Drivers.tty.println("type help for more\n");
		Util.delay();

		while (!this.exit) {
			Drivers.tty.setColor(Tty.LIGHT_GRAY);
			Drivers.tty.print("> ");

			String response = Drivers.keyboard.getLine().toLowerCase();
			Drivers.tty.setColor(Tty.DEFAULT_COLOR);

			if (response.isEmpty()) {
				continue;
			}

			String[] parts = response.split(" ");
			boolean commandFound = false;

			for (int i = 0; i < this.commands.size(); i++) {
				ShellCommand command = this.commands.get(i);

				if (command.getName().equals(parts[0])) {
					command.run(parts);
					commandFound = true;
					break;
				}
			}

			if (!commandFound) {
				Drivers.tty.println(Tty.RED, "unknown command " + parts[0] + ".\ntype help for more.");
			}
		}
	}
}