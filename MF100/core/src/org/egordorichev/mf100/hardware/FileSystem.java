package org.egordorichev.mf100.hardware;

import com.badlogic.gdx.Gdx;
import org.egordorichev.mf100.MF100;
import org.egordorichev.mf100.util.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class FileSystem extends Driver {
	public static String root;
	public static String workingDirectory = "";

	public static String TEMPLATE = "function init()\n" +
			"\n" +
			"end\n" +
			"\n" +
			"function draw()\n" +
			"  if state == 1 then\n" +
			"    fill(0, 1, 0)\n" +
			"  else\n" +
			"    fill(1, 0, 0)\n" +
			"  end\n" +
			"\n" +
			"  rect(10, 10, 460, 460)\n" +
			"end\n" +
			"\n" +
			"function update()\n" +
			"\n" +
			"end\n" +
			"\n" +
			"function key_pressed(key)\n" +
			"\n" +
			"end\n" +
			"\n" +
			"function mouse_pressed()\n" +
			"  state = 1\n" +
			"end\n" +
			"\n" +
			"function mouse_released()\n" +
			"  state = 0\n" +
			"end\n";

	public FileSystem() {
		root = System.getProperty("user.home") + "/.MF100/";

		if (!fileExists("")) {
			MF100.showStory = true;
			Log.debug("Creating save directory at " + root);

			if (!createDirectory("")) {
				Log.error("Failed to create save directory!");
				Gdx.app.exit();
			} else {
				File file = new File(root + "main.lua");

				try {
					file.createNewFile();
					BufferedWriter br = new BufferedWriter(new FileWriter(root + "main.lua"));
					br.write(TEMPLATE);
					br.close();
				} catch(IOException exception) {
					exception.printStackTrace();
				}
			}
		}

		System.setProperty("user.dir", root);
		Log.debug(System.getProperty("user.dir"));
	}

	public File[] listFiles(String directory) {
		return new File(root + workingDirectory + directory).listFiles();
	}

	public boolean deleteFile(String name) {
		File file = new File(root + workingDirectory + name);

		if (!file.exists()) {
			return false;
		}

		return file.delete();
	}

	public boolean createDirectory(String name) {
		name = root + workingDirectory + name;
		File dir = new File(name);
		return dir.mkdir();
	}

	public File open(String path) {
		return new File(root + workingDirectory + path);
	}

	public boolean fileExists(String name) {
		File file = new File(root + workingDirectory + name);
		return file.exists();
	}

	public boolean cd(String path) {
		try {
			workingDirectory = new URI(path).normalize().toString();

			if (workingDirectory.equals("..") || workingDirectory.equals("../")) {
				workingDirectory = "";
			}
		} catch (URISyntaxException exception) {
			exception.printStackTrace();
			return false;
		}

		File file = new File(root + workingDirectory);

		if (!file.exists() || !file.isDirectory()) {
			return false;
		}

		System.setProperty("user.dir", root + workingDirectory);

		return true;
	}
}