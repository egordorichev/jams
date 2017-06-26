package org.egordorichev.mf100.software;

import com.badlogic.gdx.Gdx;
import org.egordorichev.mf100.MF100;
import org.egordorichev.mf100.Version;
import org.egordorichev.mf100.hardware.Driver;
import org.egordorichev.mf100.hardware.Drivers;
import org.egordorichev.mf100.hardware.FileSystem;
import org.egordorichev.mf100.hardware.Operation;
import org.egordorichev.mf100.hardware.monitor.Monitor;
import org.egordorichev.mf100.hardware.monitor.Tty;
import org.egordorichev.mf100.lua.LuaRunner;
import org.egordorichev.mf100.util.Log;
import org.egordorichev.mf100.util.Util;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class Shell extends Operation {
	public static Shell instace;
	private ArrayList<ShellCommand> commands = new ArrayList<>();

	public Shell() {
		instace = this;

		this.commands.add(new ShellCommand("help") {
			@Override
			public void run(String[] args) {
				Drivers.tty.println(Tty.GREEN, "mkdir: creates a dir");
				Drivers.tty.println(Tty.GREEN, "dir: lists all files in the current dir");
				Drivers.tty.println(Tty.GREEN, "rm: deletes file or folder");
				Drivers.tty.println(Tty.BLUE, "edit: opens file for edit");
				Drivers.tty.println(Tty.PEACH, "woman: manual");
				Drivers.tty.println(Tty.PEACH, "tig: tig is not git");
				Drivers.tty.println(Tty.BLUE, "run: runs script");
				Drivers.tty.print(Tty.GREEN, "cd: change working directory");
				Drivers.tty.print(Tty.GREEN, "pwd: print working directory");
				Drivers.tty.println(Tty.BLUE, "gimp: image editor");
				Drivers.tty.println(Tty.PEACH, "ping [site]: ping-pongs\na server");
				Drivers.tty.println(Tty.PINK, "shutdown");
				Drivers.tty.println(Tty.PINK, "clear");
				Drivers.tty.println(Tty.GREEN, "api: lua api");
			}
		});

		this.commands.add(new ShellCommand("api") {
			@Override
			public void run(String[] args) {
				Drivers.tty.println("fill(r, g, b, a)");
				Drivers.tty.println("no_fill()");
				Drivers.tty.println("rect(x, y, w, h)");
				Drivers.tty.println("circle(x, y, r)");
				Drivers.tty.println("point(x, y)");
				Drivers.tty.println("line(x1, y1, x2, y2)");
				Drivers.tty.println("text(t, x, y)");
				Drivers.tty.println("set_color(r, g, b, a)");
				Drivers.tty.println("load_texture(name)");
				Drivers.tty.println("load_sound(name)");
				Drivers.tty.println("load_music(name)");
				Drivers.tty.println("image(t, x, y)");
				Drivers.tty.println("play_sound(s)");
				Drivers.tty.println("play_music(m)");
			}
		});

		this.commands.add(new ShellCommand("mkdir") {
			@Override
			public void run(String[] args) {
				if (args.length != 2) {
					Drivers.tty.println("mkdir [dir]: creates \na directory with given name");
				} else {
					String name = args[1];

					if (!name.endsWith("/")) {
						name += "/";
					}

					if (args[1].equals("muffin")) {
						Drivers.tty.println("Man, the muffin can be a directory!");
					} else if (Drivers.fileSystem.fileExists(name)) {
						Drivers.tty.println("File with given name already exists.");
					} else if (Drivers.fileSystem.createDirectory(name)) {
						Drivers.tty.println("Created new empty folder.");
					} else {
						Drivers.tty.println(Tty.RED, "Failed to create folder.");
					}
				}
			}
		});

		this.commands.add(new ShellCommand("clear") {
			@Override
			public void run(String[] args) {
				Drivers.tty.clear();
			}
		});

		this.commands.add(new ShellCommand("ping") {
			@Override
			public void run(String[] args) {
				if (args.length != 2) {
					Drivers.tty.println("ping [site]: ping-pongs\na server");
				} else if (args[1].startsWith("ldjam.com")|| args[1].startsWith("https://ldjam.com") || args[1].startsWith("http://ldjam.com")) {
					MF100.currentOperation = new Ping();
					MF100.currentOperation.start();
				} else {
					try {
						URL url = new URL(args[1]);
						url.toURI();

						Drivers.tty.println("Hm, man, do you need that\nsite? Psh... We are\nhacking ludum dare!");
					} catch (Exception exception) {
						if (args[1].equals("muffin")) {
							Drivers.tty.println(Tty.RED, "Man, can we ping a muffin?");
						} else {
							Drivers.tty.println(Tty.RED, "Invalid url.");
						}
					}
				}
			}
		});

		this.commands.add(new ShellCommand("tig") {
			@Override
			public void run(String[] args) {
				if (args.length != 2) {
					Drivers.tty.println("tig: is not git");
				} else {
					Drivers.tty.println("tig is not git, and\nthat is why it is\nnot working");
				}
			}
		});

		this.commands.add(new ShellCommand("woman") {
			@Override
			public void run(String[] args) {
				if (args.length != 1) {
					if (Drivers.random.nextBoolean()) {
						Drivers.tty.print("did you search it in google?");
					} else {
						Drivers.tty.println("try to search it in google.");
					}
				} else {
					Drivers.tty.println("woman: manual");
				}
			}
		});

		this.commands.add(new ShellCommand("rm") {
			@Override
			public void run(String[] args) {
				if (args.length != 2) {
					Drivers.tty.println("rm [file or dir]: removes \nfile with given name");
				} else {
					if (!Drivers.fileSystem.fileExists(args[1])) {
						Drivers.tty.println("File with given name doesn't exist");
					} else if (!Drivers.fileSystem.deleteFile(args[1])) {
						Drivers.tty.println(Tty.RED, "Failed to delete " + args[1] + ".");
					}
				}
			}
		});

		this.commands.add(new ShellCommand("dir") {
			@Override
			public void run(String[] args) {
				if (args.length != 1) {
					Drivers.tty.println("dir: list all files in\nthe current directory");
				} else {
					File[] files = Drivers.fileSystem.listFiles(""); // TODO: working dir

					if (files.length == 0) {
						Drivers.tty.println("this directory is empty.");
					} else {
						for (File file : files) {
							if (file.isDirectory()) {
								Drivers.tty.println(file.getName() + "/");
							} else {
								Drivers.tty.println(file.getName());
							}
						}
					}
				}
			}
		});

		this.commands.add(new ShellCommand("edit") {
			@Override
			public void run(String[] args) {
				if (args.length != 2) {
					Drivers.tty.println("edit [file]: opens file editor");
				} else {
					if (args[1].charAt(args[1].length() - 1) == '/') {
						Drivers.tty.println(Tty.RED, "You can't edit a directory.");
						return;
					}

					File file = Drivers.fileSystem.open(args[1]);

					if (!file.exists()) {
						try {
							file.createNewFile();
						} catch(IOException exception) {
							Drivers.tty.println(Tty.RED, "Failed to create new file.");
							return;
						}
					}

					MF100.currentOperation = new CodeEditor(FileSystem.root + args[1]);
					MF100.currentOperation.start();
				}
			}
		});

		this.commands.add(new ShellCommand("run") {
			@Override
			public void run(String[] args) {
				if (args.length != 2) {
					Drivers.tty.println("run [script]: runs lua\nscript with given name");
				} else {
					File file = Drivers.fileSystem.open(args[1]);

					if (!file.exists()) {
						Drivers.tty.println(Tty.RED, "Failed to open file.");
					} else if (file.isDirectory()) {
						Drivers.tty.println(Tty.RED, "Man, I don't know, can\nyou edit a folder?");
					} else {
						MF100.currentOperation = new LuaRunner(FileSystem.root + args[1]);
						MF100.currentOperation.start();
					}
				}
			}
		});

		this.commands.add(new ShellCommand("pwd") {
			@Override
			public void run(String[] args) {
				Drivers.tty.println((FileSystem.workingDirectory.equals("")) ? "/" : FileSystem.workingDirectory);
			}
		});

		this.commands.add(new ShellCommand("cd") {
			@Override
			public void run(String[] args) {
				if (args.length != 2) {
					Drivers.tty.println("cd [dir]: change working\ndirectory");
				} else {
					if (!Drivers.fileSystem.cd(args[1])) {
						Drivers.tty.println(Tty.RED, "Failed to change working\ndirectory.");
					}
				}
			}
		});

		this.commands.add(new ShellCommand("gimp") {
			@Override
			public void run(String[] args) {
				if (args.length != 2) {
					Drivers.tty.println("gimp [file]: image editor");
				} else {
					if (args[1].charAt(args[1].length() - 1) == '/') {
						Drivers.tty.println(Tty.RED, "Man, can you draw a directory?");
						return;
					}

					if (!args[1].endsWith(".png")) {
						Drivers.tty.println(Tty.RED, "you can edit only\n.png files.");
						return;
					}

					File file = Drivers.fileSystem.open(args[1]);

					if (!file.exists()) {
						try {
							file.createNewFile();
						} catch(IOException exception) {
							Drivers.tty.println(Tty.RED, "Failed to create new file.");
							return;
						}
					}

					MF100.currentOperation = new ImageEditor(FileSystem.root + args[1]);
					MF100.currentOperation.start();
				}
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
		Util.delay();
		Drivers.tty.println("MF100 ready");
		Util.delay();
		Drivers.tty.println("\nMF100-" + Version.getAsString());
		Util.delay();
		Drivers.tty.println("written for ld #38");
		Util.delay();
		Drivers.tty.println("\ntype help for more");

		while (true) {
		    Drivers.tty.setColor(Tty.LIGHT_GRAY);
		    Drivers.tty.print("\n> ");

			String response = Drivers.keyboard.getLine().toLowerCase();
			Drivers.tty.setColor(Tty.WHITE);

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
