package ui;

import static utilz.Constants.GameConstants.*;
import static utilz.Constants.UI.URMButton.*;

import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import gamestates.Menu;
import main.Game;
import sound.SoundManager;
import utilz.LoadSave;

public class GameOption {

    private Menu menu;
    private AudioOption audioOption;
    private int bgX, bgY, bgW, bgH;
    private BufferedImage backgroundImg;
	private UrmButton menuB;

    public GameOption(Menu menu) {
        this.menu = menu;

        loadButton();
        loadBackground();
        audioOption = menu.getGame().getAudioOption();
    }

    private void loadButton() {
		int menuX = 505;
        int bY = 550;

        menuB = new UrmButton(menuX, bY, URM_SIZE, URM_SIZE, 2);
	}

    private void loadBackground() {
        backgroundImg = LoadSave.GetSpriteAtlas(LoadSave.OptionBackground);
        bgW = (int) (backgroundImg.getWidth() * 1.5);
        bgH = (int) (backgroundImg.getHeight() * 1.5);
        bgX = GAME_WIDTH / 2 - bgW / 2;
        bgY = GAME_HEIGHT / 2 - bgH / 2 + 15;
    }

    public void update() {
        // Urm button
        menuB.update();

        // Audio options
        audioOption.update();
    }

    public void draw(Graphics g) {
        // Background
        g.drawImage(backgroundImg, bgX, bgY, bgW, bgH, null);

        // Urm buttons
        menuB.draw(g);

        // Audio options
        audioOption.draw(g);
    }

    public void mouseDragged(MouseEvent e) {
        audioOption.mouseDragged(e);
    }

    public void mousePressed(MouseEvent e) {
        if (isIn(e, menuB)) {
            menuB.setMousePressed(true);
            Game.soundPlayer.play(SoundManager.CLICKBUTTON);
        } else {
            audioOption.mousePressed(e);
        }
    }

    public void mouseReleased(MouseEvent e) {
        if (isIn(e, menuB)) {
            if (menuB.isMousePressed()) menu.setOption(false);
            menuB.setMousePressed(false);
        } else {
            audioOption.mouseReleased(e);
        }

        menuB.resetBools();
    }

    public void mouseMoved(MouseEvent e) {
        menuB.setMouseOver(isIn(e, menuB));

        if (isIn(e, menuB)) {
            menuB.setMouseOver(true);
        } else {
            audioOption.mouseMoved(e);
        }
    }

    private boolean isIn(MouseEvent e, PauseButton pb) {
        return pb.getBounds().contains(e.getX(), e.getY());
    }

}
