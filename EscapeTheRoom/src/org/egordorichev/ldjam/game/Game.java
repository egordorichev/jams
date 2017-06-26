package org.egordorichev.ldjam.game;

import asciiPanel.*;
import org.egordorichev.ldjam.AudioManager;
import org.egordorichev.ldjam.player.Player;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class Game extends JFrame implements KeyListener {
	private AsciiPanel terminal;
	private GameState state;
	private Player player;
	private java.util.Timer timer;
	private int level;
	public static AudioManager audioManager = new AudioManager();
	public static int[] keys = new int[200];

	public Game(){
		super("Escape the Room");

		this.terminal = new AsciiPanel(80, 30);
		this.state = new MenuState(this);
		this.player = new Player(null, 3, 4);
		this.level = 1;

		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLayout(new BorderLayout());
		this.add(this.terminal, BorderLayout.CENTER);
		this.pack();
		this.setResizable(false);
		this.setLocationRelativeTo(null);
		this.setVisible(true);
		this.addKeyListener(this);

		this.repaint();

		try {
			this.setIconImage(ImageIO.read(this.getClass().getResource("icon.png")));
		} catch (IOException exception) {
			exception.printStackTrace();
		}

		this.audioManager.load("background", "assets/music.wav");
		this.audioManager.playLooped("background");

		this.audioManager.load("pickup", "assets/pickup.wav");
		this.audioManager.load("keyfinder", "assets/keyfinder.wav");
		this.audioManager.load("hit", "assets/hit.wav");

		this.run();
	}

	public void run() {
		while(true) {
			this.state.update();

			try {
				Thread.sleep(1000 / 5);
			} catch(InterruptedException exception) {
				System.out.println("Error");
				Thread.currentThread().interrupt();
			}
		}
	}

	public void setState(GameState state) {
		this.state = state;
		this.repaint();
	}

	@Override
	public void repaint(){
		this.terminal.clear();
		this.state.render(this.terminal);

		super.repaint();
	}

	@Override
	public void keyPressed(KeyEvent event) {
		if(event.getKeyCode() > 200) {
			return;
		}

		if(keys[event.getKeyCode()] == 0) {
			this.state.handleInput(event);
			this.repaint();
		}

		keys[event.getKeyCode()] = 1;
	}

	@Override
	public void keyReleased(KeyEvent event) {
		if(event.getKeyCode() > 200) {
			return;
		}

		keys[event.getKeyCode()] = 0;
	}

	@Override
	public void keyTyped(KeyEvent event) {

	}

	public void nextLevel() {
		this.level++;
	}

	public int getCurrentLevel() {
		return this.level;
	}

	public AsciiPanel getTerminal() {
		return this.terminal;
	}

	public static void main(String[] args) {
		Game game = new Game();
	}

	public Player getPlayer() {
		return this.player;
	}
}