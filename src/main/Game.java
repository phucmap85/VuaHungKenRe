package main;

import java.awt.Graphics;

import entity.Player2;
import ui.UIManager;
import map.Map;

import entity.Player1;

public class Game implements Runnable {

	private GameWindow gameWindow;
	private GamePanel gamePanel;
	private Thread gameThread;
    private UIManager uiPlayer1;
    private UIManager uiPlayer2;
    private long lastDamageTime = 0;

	private final int FPS_SET = 120;
	private final int UPS_SET = 200;
	
	private Map map;
	private Player1 player1;
    private Player2 player2;


	public final static int GAME_WIDTH = 1097;
	public final static int GAME_HEIGHT = 768;

	public Game() {
		initClasses();

		gamePanel = new GamePanel(this);
		gameWindow = new GameWindow(gamePanel);
		gamePanel.requestFocus();

		startGameLoop();
	}

	private void initClasses() {
		map = new Map(this);
		player1 = new Player1(200f, 485f, 128f, 128f);
        player2 = new Player2(200f, 485f, 128f, 128f);
        uiPlayer1 = new UIManager(100, true);
        uiPlayer2 = new UIManager(100, false);
	}

	private void startGameLoop() {
		gameThread = new Thread(this);
		gameThread.start();
	}

	public void update() { // hàm update về thông số
		player1.update();
        player2.update();
    }

	public void render(Graphics g) {
		map.draw(g);
		player1.render(g);
        player2.render(g);
        uiPlayer1.draw(g,GAME_WIDTH);
        uiPlayer2.draw(g,GAME_WIDTH);
	}

	@Override
	public void run() {
		double timePerFrame = 1000000000.0 / FPS_SET;
		double timePerUpdate = 1000000000.0 / UPS_SET;

		long previousTime = System.nanoTime();

		int frames = 0;
		int updates = 0;
		long lastCheck = System.currentTimeMillis();

		double deltaU = 0;
		double deltaF = 0;

		while (true) {
			long currentTime = System.nanoTime();

			deltaU += (currentTime - previousTime) / timePerUpdate;
			deltaF += (currentTime - previousTime) / timePerFrame;
			previousTime = currentTime;

			if (deltaU >= 1) {
				update();
				updates++;
				deltaU--;
			}

			if (deltaF >= 1) {
				gamePanel.repaint();
				frames++;
				deltaF--;
			}

			if (System.currentTimeMillis() - lastCheck >= 1000) {
				lastCheck = System.currentTimeMillis();
				System.out.println("FPS: " + frames + " | UPS: " + updates);
				frames = 0;
				updates = 0;
			}
		}

	}

	public void windowFocusLost() {
		player1.resetDirBooleans();
	}

	public Player1 getPlayer1() {
        return player1;
	}
}