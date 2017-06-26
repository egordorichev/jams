package org.egordorichev.mf100.software;

public abstract class ShellCommand {
	private String name;

	public ShellCommand(String name) {
		this.name = name;
	}

	public abstract void run(String[] args);

	public String getName() {
		return this.name;
	}
}