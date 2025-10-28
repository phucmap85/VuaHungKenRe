package gamestates;

import static utilz.Constants.GameConstants.*;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import main.Game;
import ui.MenuButton;
import utilz.LoadSave;

public class Menu extends State implements Statemethods {
	private MenuButton[] buttons = new MenuButton[3];
	private BufferedImage backgroundImg;
	private int menuX, menuY, menuWidth, menuHeight;

	public Menu(Game game) {
		super(game);
		loadButtons();
		loadBackground();
	}

	private void loadBackground() {
		backgroundImg = LoadSave.GetSpriteAtlas(LoadSave.MenuBackground);
		menuWidth = backgroundImg.getWidth() + 50;
		menuHeight = backgroundImg.getHeight() + 50;
		menuX = GAME_WIDTH / 2 - menuWidth / 2;
		menuY = 160;
	}

	private void loadButtons() {
		buttons[0] = new MenuButton(GAME_WIDTH / 2, 295, 0, Gamestate.PLAYING);
		buttons[2] = new MenuButton(GAME_WIDTH / 2, 435, 2, Gamestate.QUIT);
		buttons[1] = new MenuButton(GAME_WIDTH / 2, 365, 3, Gamestate.MANUAL);		
	}

	@Override
	public void update() {
		for (MenuButton mb : buttons) mb.update();
	}

	@Override
	public void draw(Graphics g) {
		g.drawImage(backgroundImg, menuX, menuY, menuWidth, menuHeight, null);

		for (MenuButton mb : buttons) mb.draw(g);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mousePressed(MouseEvent e) {
		for (MenuButton mb : buttons) {
			if (isIn(e, mb)) mb.setMousePressed(true);
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		for (MenuButton mb : buttons) {
			if (isIn(e, mb)) {
				if (mb.isMousePressed()) mb.applyGamestate();
				break;
			}
		}

		resetButtons();
	}

	private void resetButtons() {
		for (MenuButton mb : buttons) mb.resetBools();
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		for (MenuButton mb : buttons) mb.setMouseOver(false);

		for (MenuButton mb : buttons) {
			if (isIn(e, mb)) {
				mb.setMouseOver(true);
				break;
			}
        }
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
	}
}
