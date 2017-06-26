package org.egordorichev.mf100.hardware;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import org.egordorichev.mf100.MF100;
import org.egordorichev.mf100.hardware.monitor.Monitor;
import org.egordorichev.mf100.software.Shell;

public abstract class Operation {
	private Thread thread;

	public void start() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				onRun();
			}
		}) {
			public void set() {
				thread = this;
			}
		}.set();

		this.thread.start();
	}

	public abstract void onRun();

	public void render(){
		if(Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE) && !(MF100.currentOperation instanceof Shell)){
			Drivers.monitor.state = Monitor.State.TTY;
			//this.returnLine = true;

			MF100.currentOperation.kill();

			Drivers.tty.clear();
		}

	}

	public void kill() {
		MF100.currentOperation = null;
		this.thread.interrupt();
		Drivers.monitor.state = Monitor.State.TTY;
	}
}