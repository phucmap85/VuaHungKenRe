package main;

import entity.*;
import static utilz.Constants.GameConstants.*;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JPanel;

import inputs.KeyHandler;
import inputs.MouseHandler;

public class GamePanel extends JPanel {
	private Game game;
	private MouseHandler mouseHandler;

	public GamePanel(Game game) {
		this.game = game;
		setPanelSize();
		
		// Keyboard
		addKeyListener(new KeyHandler(this));
		// Mouse
		mouseHandler = new MouseHandler(this);
		addMouseListener(mouseHandler);
		addMouseMotionListener(mouseHandler);
		
		
		setFocusable(true);
        requestFocusInWindow();
	}

	private void setPanelSize() {
		Dimension size = new Dimension(GAME_WIDTH, GAME_HEIGHT);
		setPreferredSize(size);
        setBackground(Color.black);
        setDoubleBuffered(true);
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