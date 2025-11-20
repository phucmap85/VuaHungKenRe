package ui;

import static utilz.Constants.GameConstants.*;
import static utilz.Constants.UI.URMButton.*;

import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import gamestates.Gamestate;
import gamestates.Playing;
import main.Game;
import sound.SoundManager;
import utilz.LoadSave;

public class PauseOverlay {

    private Playing playing;
    private BufferedImage backgroundImg;
    private int bgX, bgY, bgW, bgH;
    
    private UrmButton menuB, replayB, unpauseB;
    private AudioOption audioOption;

    public PauseOverlay(Playing playing) {
        this.playing = playing;

        loadBackground();
        createUrmButtons();

        audioOption = playing.getGame().getAudioOption();
    }

    private void createUrmButtons() {
        int menuX = 402;
        int replayX = menuX + URM_SIZE + 20;
        int unpauseX = replayX + URM_SIZE + 20;
        int bY = 545;

        menuB = new UrmButton(menuX, bY, URM_SIZE, URM_SIZE, 2);
        replayB = new UrmButton(replayX, bY, URM_SIZE, URM_SIZE, 1);
        unpauseB = new UrmButton(unpauseX, bY, URM_SIZE, URM_SIZE, 0);
    }

    private void loadBackground() {
        backgroundImg = LoadSave.GetSpriteAtlas(LoadSave.PauseBackground);
        bgW = (int) (backgroundImg.getWidth() * 1.5);
        bgH = (int) (backgroundImg.getHeight() * 1.5);
        bgX = GAME_WIDTH / 2 - bgW / 2;
        bgY = GAME_HEIGHT / 2 - bgH / 2;
    }

    public void update() {
        menuB.update();
        replayB.update();
        unpauseB.update();
        audioOption.update();
    }

    public void draw(Graphics g) {
        // Background
        g.drawImage(backgroundImg, bgX, bgY, bgW, bgH, null);

        // Urm buttons
        menuB.draw(g);
        replayB.draw(g);
        unpauseB.draw(g);

        // Audio options
        audioOption.draw(g);
    }

    public void mouseDragged(MouseEvent e) {
        audioOption.mouseDragged(e);
    }

    public void mousePressed(MouseEvent e) {
        if(isIn(e, menuB)) {
            menuB.setMousePressed(true);
            Game.soundPlayer.play(SoundManager.CLICKBUTTON);
        }
        else if(isIn(e, replayB)) {
            replayB.setMousePressed(true);
            Game.soundPlayer.play(SoundManager.CLICKBUTTON);
        }
        else if(isIn(e, unpauseB)) {
            unpauseB.setMousePressed(true);
            Game.soundPlayer.play(SoundManager.CLICKBUTTON);
        }
        else {
            audioOption.mousePressed(e);
        }
    }

    public void mouseReleased(MouseEvent e) {
        if(isIn(e, menuB)) {
            if(menuB.isMousePressed()) {
                Gamestate.state = Gamestate.MENU;
                playing.unpauseGame();
                playing.resetAllStates();
            }
            menuB.setMousePressed(false);
        }
        else if(isIn(e, replayB)) {
            if(replayB.isMousePressed()) {
                playing.resetAllStates();
                playing.unpauseGame();
                
            }
            replayB.setMousePressed(false);
        }
        else if(isIn(e, unpauseB)) {
            if(unpauseB.isMousePressed()) {
                playing.unpauseGame();
            }
            unpauseB.setMousePressed(false);
        }
        else {
            audioOption.mouseReleased(e);
        }

        menuB.resetBools();
        replayB.resetBools();
        unpauseB.resetBools();
    }

    public void mouseMoved(MouseEvent e) {
        menuB.setMouseOver(isIn(e, menuB));
        replayB.setMouseOver(isIn(e, replayB));
        unpauseB.setMouseOver(isIn(e, unpauseB));

        if(isIn(e, menuB)) {
            menuB.setMouseOver(true);
        }
        else if(isIn(e, replayB)) {
            replayB.setMouseOver(true);
        }
        else if(isIn(e, unpauseB)) {
            unpauseB.setMouseOver(true);
        }
        else {
            audioOption.mouseMoved(e);
        }
    }

    private boolean isIn(MouseEvent e, PauseButton pb) {
        return pb.getBounds().contains(e.getX(), e.getY());
    }

}
