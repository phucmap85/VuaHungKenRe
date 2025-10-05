package main;
import static utilz.Constants.GameConstants.*;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JPanel;

import inputs.KeyHandler;


public class GamePanel extends JPanel {

	private Game game;

	public GamePanel(Game game) {
		this.game = game;
		setPanelSize();
		addKeyListener(new KeyHandler(this));
	}

	private void setPanelSize() {
		Dimension size = new Dimension(GAME_WIDTH, GAME_HEIGHT);
		setPreferredSize(size);
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);
	}

	public void updateGame() {
        
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		game.render(g);
	}

	public Game getGame() {
		return game;
	}

}