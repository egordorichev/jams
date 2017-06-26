package org.egordorichev.mf100.software;

import org.egordorichev.mf100.MF100;
import org.egordorichev.mf100.hardware.Operation;

public class BootProcess extends Operation {
	@Override
	public void onRun() {
		MF100.booted = true;

		if (MF100.showStory) {
			new Story().start();
		} else {
			new Menu().start();
		}
	}
}