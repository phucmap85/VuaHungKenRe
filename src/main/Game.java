package main;

import java.awt.Graphics;

import static utilz.Constants.GameConstants.*;
import static utilz.LoadSave.preloadSounds;


import gamestates.Gamestate;
import gamestates.Manual;
import gamestates.MatchSetup;
import gamestates.Menu;
import gamestates.Playing;
import sound.SoundPlayer;
import sound.SoundManager;


public class Game implements Runnable {
	private GameWindow gameWindow;
	private GamePanel gamePanel;
	private Thread gameThread;
	private Playing playing;
	private Menu menu;
	private Manual manual;
	private MatchSetup matchSetup;

	public static SoundPlayer soundPlayer;
    private Gamestate oldState = Gamestate.MENU;
	public Game() {
		preloadSounds();
		soundPlayer = new SoundPlayer();
		initClasses();

		gamePanel = new GamePanel(this);
		gameWindow = new GameWindow(gamePanel);
		gamePanel.requestFocus();
		soundPlayer.loop(SoundManager.MENU);
		startGameLoop();
	}

	private void initClasses() {
		menu = new Menu(this);
		playing = new Playing(this);
		manual = new Manual(this);
		matchSetup = new MatchSetup(this);
	}

	private void startGameLoop() {
		gameThread = new Thread(this);
		gameThread.start();
	}

	public void update() {
		if (oldState != Gamestate.state) {
			Game.soundPlayer.play(SoundManager.CLICKBUTTON);
            soundPlayer.stopMusic(); // Dừng nhạc cũ
            switch (Gamestate.state) {
                case MENU:
                    soundPlayer.loop(SoundManager.MENU);
                    break;
                case PLAYING:
                    soundPlayer.loop(SoundManager.PLAYING);
                    break;
				case MANUAL:
                    soundPlayer.loop(SoundManager.PLAYING);
                    break;
				case MATCH_SETUP:
					soundPlayer.loop(SoundManager.SELECTION);
					break;
				default:
					break;
            }
            oldState = Gamestate.state; // Cập nhật state cũ
        }
		switch (Gamestate.state) {
			case MENU:
				menu.update();
				break;
			case PLAYING:
				playing.update();
				break;
			case MANUAL:
				manual.update();
				break;
			case MATCH_SETUP:
				matchSetup.update();
				break;
			case OPTIONS:
			case QUIT:
			default:
				System.exit(0);
				break;
		}
	}

	public void render(Graphics g) {
		switch (Gamestate.state) {
			case MENU:
				menu.draw(g);
				break;
			case PLAYING:
				playing.draw(g);
				break;
			case MANUAL:
				manual.draw(g);
				break;
			case MATCH_SETUP:
				matchSetup.draw(g);	
				break;
			default:
				break;
		}
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
		if (Gamestate.state == Gamestate.PLAYING) {
			playing.windowFocusLost();
		}
	}

	public Menu getMenu() {
		return menu;
	}

	public Playing getPlaying() {
		return playing;
	}
	public Manual getManual(){
		return manual;
	}
	public MatchSetup getMatchSetup() {
        return matchSetup;
    }
	public static SoundPlayer getSoundPlayer() {
        return soundPlayer;
    }
}