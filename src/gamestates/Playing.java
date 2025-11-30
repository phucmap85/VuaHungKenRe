package gamestates;

import static utilz.Constants.GameConstants.*;
import static utilz.Constants.PlayerConstants.*;
import static utilz.LoadSave.loadAllAnimations;

import main.Game;
import map.Map;
import sound.SoundManager;
import ui.PauseOverlay;
import ui.PlayerUI;
import utilz.LoadSave;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import entity.Character;
import entity.Combat;

public class Playing extends State implements Statemethods {
    boolean[] keysPressed = new boolean[256];
    boolean paused = false;
    PauseOverlay pauseOverlay;

    Map map;
    Character sonTinh, thuyTinh;
    PlayerUI playerUI1, playerUI2;
    Combat combat1;
    Combat combat2;

    int selectedMapIndex;

    int framesIndex = 0, framesCounter = 0, maxFrames = 11, framesSpeed = 20, delayForLastFrame = 450;
    boolean switchEnding = false;
    BufferedImage[] ko = null;
    int countUpdate = 0;
    int thresholdUpdate = 2;

    public Playing(Game game) {
        super(game);
        initClasses();
    }

    void initClasses() {
        loadAllAnimations();
		map = new Map(game, selectedMapIndex);
		thuyTinh = new Character(200f, 535f, 80f, 40f, 30f, 50f, 35f, 20f, 55f, 85f, "ThuyTinh", RIGHT, map);
        sonTinh = new Character(800f, 535f, 15f, 40f, 30f, 50f, 35f, 20f, 55f, 85f, "SonTinh", LEFT, map);
        playerUI1 = new PlayerUI(50000, true);
        playerUI2 = new PlayerUI(50000, false);
        combat1 = new Combat(sonTinh, thuyTinh, playerUI2, playerUI1);
        combat2 = new Combat(thuyTinh, sonTinh, playerUI1, playerUI2);
        pauseOverlay = new PauseOverlay(this);
        ko = LoadSave.getKOAnimation();
        switchEnding = false;
	}

    public void setMatchSettings(int mapID) {
        this.selectedMapIndex = mapID;
        map = new Map(game, selectedMapIndex);
        thuyTinh.setMap(map);
        sonTinh.setMap(map);
        resetAllStates();
        /* init lai vi playing van dang o default map */
    }

    public void resetAllStates(){
        thuyTinh.setPosition(200f, 535f);
        sonTinh.setPosition(800f, 535f);
        thuyTinh.resetAllBools();
        sonTinh.resetAllBools();
        thuyTinh.resetAllStates();
        sonTinh.resetAllStates();
        thuyTinh.setDirection(RIGHT);
        sonTinh.setDirection(LEFT);
        playerUI1.resetAll();
        playerUI2.resetAll();
        combat1.resetCombat(sonTinh, thuyTinh, playerUI2, playerUI1);
        combat2.resetCombat(thuyTinh, sonTinh, playerUI1, playerUI2);
        switchEnding = false;
        framesIndex = 0;
        framesCounter = 0;
        framesSpeed = 20;
    }

    public void windowFocusLost() {
		sonTinh.resetAllBools();
        thuyTinh.resetAllBools();
	}

    @Override
    public void update() {
        if (!paused) {
            if (playerUI1.getHealth() <= 0) {
                if (!switchEnding) {
                    Game.soundPlayer.play(SoundManager.KO);
                    switchEnding = true;
                }
                
                // Chỉ cho ngã khi không đang dính ulti và chưa ngã
                if(!thuyTinh.falling() && combat1.ultiIsNull()){
                    thuyTinh.resetAnimationTick();
                    thuyTinh.setFalling(true);
                    thuyTinh.setPlayerAction(thuyTinh.getDirection() == RIGHT ?  FALLING_RIGHT : FALLING_LEFT );
                    Game.soundPlayer.play(SoundManager.THUYTINHFALL);
                }
                
                countUpdate++;
                if (countUpdate >= thresholdUpdate) {
                    countUpdate = 0;
                    thuyTinh.updateForEnding();
                    sonTinh.update();
                    combat1.update();
                    combat2.update();
                }
            
                
                
                framesCounter++;
                if(framesIndex == maxFrames - 1) {
                    framesSpeed = delayForLastFrame;
                }
                if (framesCounter >= framesSpeed) {
                    framesCounter = 0;
                    framesIndex++;
                    if (framesIndex >= maxFrames) {
                        framesIndex = 0;
                        switchEnding = false;
                        framesSpeed = 20;
                        game.getEnding().setMap(0);
                        Gamestate.state = Gamestate.ENDING;
                    }
                }
            } else if (playerUI2.getHealth() <= 0) {
                if (!switchEnding) {
                    Game.soundPlayer.play(SoundManager.KO);
                    switchEnding = true;
                }
                
                // Chỉ cho ngã khi không đang dính ulti và chưa ngã
                if(!sonTinh.falling() && combat2.ultiIsNull()){
                    sonTinh.resetAnimationTick();
                    sonTinh.setFalling(true);
                    sonTinh.setPlayerAction(sonTinh.getDirection() == RIGHT ?  FALLING_RIGHT : FALLING_LEFT );
                    Game.soundPlayer.play(SoundManager.SONTINHFALL);
                }
               
               
                countUpdate++;
                if (countUpdate >= thresholdUpdate) {
                    countUpdate = 0;
                    sonTinh.updateForEnding();
                    thuyTinh.update();
                    combat1.update();
                    combat2.update();
                }
                
                
                framesCounter++;
                 if(framesIndex == maxFrames - 1) {
                    framesSpeed = delayForLastFrame;
                }
                if (framesCounter >= framesSpeed) {
                    framesCounter = 0;
                    framesIndex++;
                    if (framesIndex >= maxFrames) {
                        framesIndex = 0;
                        switchEnding = false;
                        framesSpeed = 20;
                        game.getEnding().setMap(1);
                    
                        Gamestate.state = Gamestate.ENDING;
                    }
                }
            } else {
                sonTinh.update();
                thuyTinh.update();
                combat1.update();
                combat2.update();
                playerUI1.update();
                playerUI2.update();
            }
        } else {
            pauseOverlay.update();
        }
    }

    public void renderKo(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.drawImage(ko[framesIndex], -70, 0, 1280, 720, null);
    }
    @Override
    public void draw(Graphics g) {
        map.draw(g);
        if(sonTinh.punching()) {
            thuyTinh.render(g);
            sonTinh.render(g);
        }
        else if(thuyTinh.punching()) {
            sonTinh.render(g);
            thuyTinh.render(g);
        } else {
            sonTinh.render(g);
            thuyTinh.render(g);
        }
        playerUI1.draw(g, GAME_WIDTH);
        playerUI2.draw(g, GAME_WIDTH);
        combat1.render(g);
        combat2.render(g);
        if(switchEnding) renderKo(g);
        if(paused) pauseOverlay.draw(g);
    }

    public void unpauseGame() {
        paused = false;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if(paused) {
            pauseOverlay.mouseDragged(e);
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if(paused) {
            pauseOverlay.mousePressed(e);
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if(paused) {
            pauseOverlay.mouseReleased(e);
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if(paused) {
            pauseOverlay.mouseMoved(e);
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();

        if (keyCode < keysPressed.length && !keysPressed[keyCode]) {
            keysPressed[keyCode] = true;

            switch (keyCode) {
                case KeyEvent.VK_A:
                    thuyTinh.setLeft(true);
                    break;
                case KeyEvent.VK_D:
                    thuyTinh.setRight(true);
                    break;
                case KeyEvent.VK_S:
                    thuyTinh.setDefend(true);
                    break;
                case KeyEvent.VK_K:
                    thuyTinh.setJump(true);
                    break;
                case KeyEvent.VK_J:
                    thuyTinh.setPunch(true);
                    break;
                case KeyEvent.VK_U:
                    thuyTinh.setSummon(true);
                    break;
                case KeyEvent.VK_I:
                    thuyTinh.setUlti(true);
                    break;
                case KeyEvent.VK_L:
                    thuyTinh.setDash(true);
                    break;
                case KeyEvent.VK_LEFT:
                    sonTinh.setLeft(true);
                    break;
                case KeyEvent.VK_RIGHT:
                    sonTinh.setRight(true); 
                    break;
                case KeyEvent.VK_DOWN:
                    sonTinh.setDefend(true);
                    break;
                case KeyEvent.VK_NUMPAD2:
                    sonTinh.setJump(true);
                    break;
                case KeyEvent.VK_NUMPAD1:
                    sonTinh.setPunch(true); 
                    break;
                case KeyEvent.VK_NUMPAD4:
                    sonTinh.setSummon(true);
                    break;
                case KeyEvent.VK_NUMPAD5:
                    sonTinh.setUlti(true);
                    break;
                case KeyEvent.VK_NUMPAD3:
                    sonTinh.setDash(true);
                    break;
                case KeyEvent.VK_ESCAPE:
                    paused = !paused;
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int keyCode = e.getKeyCode();
        
        if (keyCode < keysPressed.length) keysPressed[keyCode] = false;

        switch (keyCode) {
            case KeyEvent.VK_A:
                thuyTinh.setLeft(false);
                break;
            case KeyEvent.VK_D:
                thuyTinh.setRight(false);
                break;
            case KeyEvent.VK_S:
                thuyTinh.setDefend(false);
                break;
            case KeyEvent.VK_K:
                thuyTinh.setJump(false);
                break;
            case KeyEvent.VK_J:
                thuyTinh.setPunch(false);
                break;
            case KeyEvent.VK_U:
                thuyTinh.setSummon(false);  
                break;
            case KeyEvent.VK_L:
                thuyTinh.setDash(false);
                break;
            case KeyEvent.VK_I:
                thuyTinh.setUlti(false);
                break;
            case KeyEvent.VK_LEFT:
                sonTinh.setLeft(false); 
                break;
            case KeyEvent.VK_RIGHT:
                sonTinh.setRight(false); 
                break;
            case KeyEvent.VK_DOWN:
                sonTinh.setDefend(false);  
                break;
            case KeyEvent.VK_NUMPAD2:
                sonTinh.setJump(false); 
                break;      
            case KeyEvent.VK_NUMPAD1:
                sonTinh.setPunch(false);
                break;
            case KeyEvent.VK_NUMPAD4:
                sonTinh.setSummon(false);  
                break;
            case KeyEvent.VK_NUMPAD5:
                sonTinh.setUlti(false);
                break;
            case KeyEvent.VK_NUMPAD3:
                sonTinh.setDash(false);
                break;
            default:
                break;
        }
    }
}