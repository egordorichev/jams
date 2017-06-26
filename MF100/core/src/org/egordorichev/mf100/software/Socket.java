package org.egordorichev.mf100.software;

import org.egordorichev.mf100.MF100;
import org.egordorichev.mf100.hardware.Drivers;
import org.egordorichev.mf100.hardware.Operation;
import org.egordorichev.mf100.hardware.monitor.Tty;
import org.egordorichev.mf100.util.Util;

public class Socket extends Operation {
	private int serverHp = 100;
	private int playerHp = 80;
	private boolean exit = false;

	public Socket() {

	}

	@Override
	public void onRun() {
		Drivers.tty.println(Tty.PEACH, "Finally, we got connected\nto the server. now,\nlets hack it!\n");

		while (!this.exit) {
			do {
				Drivers.tty.print("> ");
				String response = Drivers.keyboard.getLine();

				response = response.trim();

				if (response.equals("attack")) {
					int amount = Util.randomInt(5, 15);
					this.serverHp -= amount;

					Drivers.tty.println("you damage server by " + amount + " hp");

					amount = Util.randomInt(5, 15);
					this.playerHp -= amount;

					Drivers.tty.println("server damaged you by " + amount + " hp");
					break;
				} else if (response.equals("defend")) {
					Drivers.tty.println("you are trying to defend");
					Drivers.tty.println("server is attacking");

					if (Drivers.random.nextBoolean()) {
						Drivers.tty.println("but you were in safely");
					} else {
						Drivers.tty.println("and you failed to defend");

						int amount = Util.randomInt(5, 15);
						this.playerHp -= amount;

						Drivers.tty.println("server damaged you by " + amount + " hp");
					}

					break;
				} else if (response.equals("heal")) {
					int amount = Util.randomInt(5, 25);
					this.playerHp += amount;

					Drivers.tty.println("you healed your self\n by " + amount + " hp");

					amount = Util.randomInt(5, 15);
					this.playerHp -= amount;

					Drivers.tty.println("server damaged you by " + amount + " hp");
					break;
				} else if (response.equals("help")) {
					Drivers.tty.println("kill the server to hack it");
					Drivers.tty.println("commands:\n");
					Drivers.tty.println("attack");
					Drivers.tty.println("heal");
					Drivers.tty.println("defend");
					Drivers.tty.println("hp");
					Drivers.tty.println("cheat");
				} else if (response.equals("cheat")) {
					Drivers.tty.println("you are trying to cheat");

					if (Drivers.random.nextBoolean()) {
						int amount = Util.randomInt(15, 25);
						this.serverHp -= amount;

						Drivers.tty.println("you avoided server attack");
						Drivers.tty.println("and damaged him by " + amount + "hp");
					} else {
						Drivers.tty.println("and you failed to cheat!");

						int amount = Util.randomInt(5, 30);
						this.playerHp -= amount;

						Drivers.tty.println("server damaged you by " + amount + " hp");
					}
				} else if (response.equals("hp")) {
					int amount = Util.randomInt(5, 15);
					this.playerHp -= amount;

					Drivers.tty.println("server damaged you by " + amount + " hp");

					Drivers.tty.println("\nyour hp: " + this.playerHp);
					Drivers.tty.println("server hp: " + serverHp);
				} else {
					Drivers.tty.println(Tty.RED, "unknown option");
				}
			} while (true);

			if (this.serverHp <= 0) {
				Drivers.tty.println(Tty.GREEN, "you won!");

				new java.util.Timer().schedule(
					new java.util.TimerTask() {
						@Override
						public void run() {
							kill();
							MF100.currentOperation = new EndStory();
							MF100.currentOperation.start();
						}
					}, 5000);
				return;
			} else if (this.playerHp <= 0) {
				Drivers.tty.println(Tty.RED, "game over!");

				new java.util.Timer().schedule(
					new java.util.TimerTask() {
						@Override
						public void run() {
							kill();
						}
					}, 5000);
				return;
			} else {
				Drivers.tty.println(Tty.PEACH, "\nyour hp: " + this.playerHp);
			}
		}
	}
}