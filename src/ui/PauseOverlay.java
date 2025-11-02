package ui;

import static utilz.Constants.GameConstants.*;

import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import gamestates.Gamestate;
import gamestates.Playing;
import utilz.LoadSave;
import static utilz.Constants.UI.PauseButton.*;
import static utilz.Constants.UI.URMButton.*;
import static utilz.Constants.UI.VolumeButton.*;

public class PauseOverlay {
    private Playing playing;
    private BufferedImage backgroundImg;
    private int bgX, bgY, bgW, bgH;
    private SoundButton musicButton, sfxButton;
    private UrmButton menuB, replayB, unpauseB;
    private VolumeButton volumeButton;

    public PauseOverlay(Playing playing) {
        this.playing = playing;

        loadBackground();
        createSoundButtons();
        createUrmButtons();
        createVolumeButton();
    }

    private void createSoundButtons() {
        int soundX = 600;
        int musicY = 265;
        int sfxY = 265 + SOUND_SIZE + 8;

        musicButton = new SoundButton(soundX, musicY, SOUND_SIZE, SOUND_SIZE);
        sfxButton = new SoundButton(soundX, sfxY, SOUND_SIZE, SOUND_SIZE);
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

    private void createVolumeButton() {
        int vX = 387;
        int vY = 470;

        volumeButton = new VolumeButton(vX, vY, SLIDER_WIDTH, VOLUME_HEIGHT);
    }

    private void loadBackground() {
        backgroundImg = LoadSave.GetSpriteAtlas(LoadSave.PauseBackground);
        bgW = (int) (backgroundImg.getWidth() * 1.5);
        bgH = (int) (backgroundImg.getHeight() * 1.5);
        bgX = GAME_WIDTH / 2 - bgW / 2;
        bgY = GAME_HEIGHT / 2 - bgH / 2;
    }

    public void update() {
        musicButton.update();
        sfxButton.update();

        menuB.update();
        replayB.update();
        unpauseB.update();

        volumeButton.update();
    }

    public void draw(Graphics g) {
        // Background
        g.drawImage(backgroundImg, bgX, bgY, bgW, bgH, null);

        // Sound buttons
        musicButton.draw(g);
        sfxButton.draw(g);

        // Urm buttons
        menuB.draw(g);
        replayB.draw(g);
        unpauseB.draw(g);

        // Volume button
        volumeButton.draw(g);
    }

    public void mouseDragged(MouseEvent e) {
        if(volumeButton.isMousePressed()) {
            volumeButton.changeVolume(e.getX());
        }
    }

    public void mousePressed(MouseEvent e) {
        if(isIn(e, musicButton)) {
            musicButton.setMousePressed(true);
        } 
        else if(isIn(e, sfxButton)) {
            sfxButton.setMousePressed(true);
        }
        else if(isIn(e, menuB)) {
            menuB.setMousePressed(true);
        }
        else if(isIn(e, replayB)) {
            replayB.setMousePressed(true);
        }
        else if(isIn(e, unpauseB)) {
            unpauseB.setMousePressed(true);
        }
        else if(isIn(e, volumeButton)) {
            volumeButton.setMousePressed(true);
        }
    }

    public void mouseReleased(MouseEvent e) {
        if(isIn(e, musicButton)) {
            if(musicButton.isMousePressed()) {
                musicButton.setMuted(!musicButton.isMuted());
            }
            musicButton.setMousePressed(false);
        } 
        else if(isIn(e, sfxButton)) {
            if(sfxButton.isMousePressed()) {
                sfxButton.setMuted(!sfxButton.isMuted());
            }
            sfxButton.setMousePressed(false);
        }
        else if(isIn(e, menuB)) {
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

        musicButton.resetBools();
        sfxButton.resetBools();
        menuB.resetBools();
        replayB.resetBools();
        unpauseB.resetBools();
        volumeButton.resetBools();
    }

    public void mouseMoved(MouseEvent e) {
        musicButton.setMouseOver(isIn(e, musicButton));
        sfxButton.setMouseOver(isIn(e, sfxButton));
        menuB.setMouseOver(isIn(e, menuB));
        replayB.setMouseOver(isIn(e, replayB));
        unpauseB.setMouseOver(isIn(e, unpauseB));
        volumeButton.setMouseOver(isIn(e, volumeButton));

        if(isIn(e, musicButton)) {
            musicButton.setMouseOver(true);
        } 
        else if(isIn(e, sfxButton)) {
            sfxButton.setMouseOver(true);
        }
        else if(isIn(e, menuB)) {
            menuB.setMouseOver(true);
        }
        else if(isIn(e, replayB)) {
            replayB.setMouseOver(true);
        }
        else if(isIn(e, unpauseB)) {
            unpauseB.setMouseOver(true);
        }
        else if(isIn(e, volumeButton)) {
            volumeButton.setMouseOver(true);
        }
    }

    private boolean isIn(MouseEvent e, PauseButton pb) {
        return pb.getBounds().contains(e.getX(), e.getY());
    }

}
