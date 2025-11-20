package gamestates;

import static utilz.Constants.GameConstants.*;
import static utilz.Constants.UI.MenuButton.*;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import main.Game;
import sound.SoundManager;
import ui.GameOption;
import ui.MenuButton;
import utilz.LoadSave;

public class Menu extends State implements Statemethods {
	private MenuButton[] buttons = new MenuButton[4];
	private int menuX, menuY, menuWidth, menuHeight;
	
	// Game option
	private GameOption gameOption;
	private boolean option = false;

	// Animation frames
	private BufferedImage[] animationFrames;
	private int currentFrame = 0;
	private int animationSpeed = 10;
	private int animationTick = 0;

	public Menu(Game game) {
		super(game);
		loadButtons();
		loadFrames();

		gameOption = new GameOption(this);
	}
	
	private void loadFrames() {
		animationFrames = new BufferedImage[TOTAL_FRAMES];
		for (int i = 0; i < TOTAL_FRAMES; i++) {
			String frameName = String.format(LoadSave.MenuBackground + "out-%03d.png", i + 1);
			animationFrames[i] = LoadSave.GetSpriteAtlas(frameName);
		}

		menuWidth = animationFrames[0].getWidth();
		menuHeight = animationFrames[0].getHeight();
		menuX = GAME_WIDTH / 2 - menuWidth / 2 - 80;
		menuY = 0;
	}

	private void loadButtons() {
		buttons[0] = new MenuButton(GAME_WIDTH - 180, 295 + 200, 0, Gamestate.MATCH_SETUP);
		buttons[1] = new MenuButton(GAME_WIDTH - 180, 365 + 200, 3, Gamestate.MANUAL);
		buttons[2] = new MenuButton(GAME_WIDTH - 180, 435 + 200, 1, Gamestate.MENU);
		buttons[3] = new MenuButton(GAME_WIDTH - 180, 505 + 200, 2, Gamestate.QUIT);
	}

	@Override
	public void update() {
		for (MenuButton mb : buttons) mb.update();
		updateAnimation();

		if (option) gameOption.update();
	}
	
	private void updateAnimation() {
		animationTick++;
		if (animationTick >= animationSpeed) {
			animationTick = 0;
			currentFrame++;
			if (currentFrame >= TOTAL_FRAMES) currentFrame = 0;
		}
	}

	@Override
	public void draw(Graphics g) {
		if (animationFrames != null && animationFrames[currentFrame] != null) {
			g.drawImage(animationFrames[currentFrame], menuX, menuY, menuWidth, menuHeight, null);
		}

		if (option) gameOption.draw(g);
		else {
			for (MenuButton mb : buttons) mb.draw(g);
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (option) gameOption.mousePressed(e);
	}

	@Override
	public void mousePressed(MouseEvent e) {
		for (MenuButton mb : buttons) {
			if (isIn(e, mb)) mb.setMousePressed(true);
		}

		if (option) gameOption.mousePressed(e);
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		for (MenuButton mb : buttons) {
			if (isIn(e, mb)) {
				if (mb.isMousePressed()) {
					if (mb == buttons[2]) {
						Game.soundPlayer.play(SoundManager.CLICKBUTTON);
						option = true;
					} else {
						mb.applyGamestate();
					}
				}
				break;
			}
		}

		resetButtons();

		if (option) gameOption.mouseReleased(e);
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

		if (option) gameOption.mouseMoved(e);
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		if (option) gameOption.mouseDragged(e);
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
	}

	public void setOption(boolean status) {
		option = status;
	}
}
