package ui;

import static utilz.Constants.UI.PauseButton.*;
import static utilz.Constants.UI.VolumeButton.*;

import java.awt.Graphics;
import java.awt.event.MouseEvent;

import main.Game;
import sound.SoundManager;

public class AudioOption {
    private SoundButton musicButton, sfxButton;
    private VolumeButton volumeButton;

    private float lastMusicVolume = 1.0f;
    private float lastSfxVolume = 1.0f;
    
    public AudioOption() {
        createSoundButtons();
        createVolumeButton();
    }

    private void createSoundButtons() {
        int soundX = 600;
        int musicY = 265;
        int sfxY = 265 + SOUND_SIZE + 8;

        musicButton = new SoundButton(soundX, musicY, SOUND_SIZE, SOUND_SIZE);
        sfxButton = new SoundButton(soundX, sfxY, SOUND_SIZE, SOUND_SIZE);
    }

    private void createVolumeButton() {
        int vX = 387;
        int vY = 470;

        volumeButton = new VolumeButton(vX, vY, SLIDER_WIDTH, VOLUME_HEIGHT);
    }

    public void update() {
        musicButton.update();
        sfxButton.update();

        volumeButton.update();
    }

    public void draw(Graphics g) {
        // Sound buttons
        musicButton.draw(g);
        sfxButton.draw(g);

        // Volume button
        volumeButton.draw(g);
    }

    public void mouseDragged(MouseEvent e) {
        if(volumeButton.isMousePressed()) {
            volumeButton.changeVolume(e.getX());
            float newVolume = volumeButton.getVolumeLevel();
            if(musicButton.isMuted()) {
                lastMusicVolume = newVolume;
            }
            else {
                Game.soundPlayer.setMusicVolume(newVolume);
            }
            
        }
    }

    public void mousePressed(MouseEvent e) {
        if(isIn(e, musicButton)) {
            musicButton.setMousePressed(true);
            Game.soundPlayer.play(SoundManager.CLICKBUTTON);
        } 
        else if(isIn(e, sfxButton)) {
            sfxButton.setMousePressed(true);
            Game.soundPlayer.play(SoundManager.CLICKBUTTON);
        }
        else if(isIn(e, volumeButton)) {
            volumeButton.setMousePressed(true);
            Game.soundPlayer.play(SoundManager.CLICKBUTTON);
            volumeButton.changeVolume(e.getX());
            lastMusicVolume = volumeButton.getVolumeLevel();
        }
    }

    public void mouseReleased(MouseEvent e) {
        if(isIn(e, musicButton)) {
            if(musicButton.isMousePressed()) {
                musicButton.setMuted(!musicButton.isMuted());
                Game.soundPlayer.setMusicVolume(musicButton.isMuted() ? 0 : lastMusicVolume);
            }
            musicButton.setMousePressed(false);
        } 
        else if(isIn(e, sfxButton)) {
            if(sfxButton.isMousePressed()) {
                sfxButton.setMuted(!sfxButton.isMuted());
                Game.soundPlayer.setSfxVolume(sfxButton.isMuted() ? 0 : lastSfxVolume);
            }
            sfxButton.setMousePressed(false);
        }

        musicButton.resetBools();
        sfxButton.resetBools();
        volumeButton.resetBools();
    }

    public void mouseMoved(MouseEvent e) {
        musicButton.setMouseOver(isIn(e, musicButton));
        sfxButton.setMouseOver(isIn(e, sfxButton));
        volumeButton.setMouseOver(isIn(e, volumeButton));

        if(isIn(e, musicButton)) {
            musicButton.setMouseOver(true);
        } 
        else if(isIn(e, sfxButton)) {
            sfxButton.setMouseOver(true);
        }
        else if(isIn(e, volumeButton)) {
            volumeButton.setMouseOver(true);
        }
    }

    private boolean isIn(MouseEvent e, PauseButton pb) {
        return pb.getBounds().contains(e.getX(), e.getY());
    }

}
