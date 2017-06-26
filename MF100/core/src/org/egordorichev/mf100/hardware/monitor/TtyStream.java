package org.egordorichev.mf100.hardware.monitor;

import org.egordorichev.mf100.hardware.Drivers;

import java.io.*;

public class TtyStream extends PrintStream {
	public TtyStream() {
		super(new OutputStream() {
			@Override
			public void write(int i) throws IOException {
			}
		});
	}


	@Override
	public void println(String s) {
		Drivers.tty.println(s);
	}

	@Override
	public void print(String string) {
		Drivers.tty.print(string);
	}
}