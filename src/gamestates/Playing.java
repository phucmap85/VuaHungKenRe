package gamestates;

import static utilz.Constants.GameConstants.*;
import static utilz.Constants.PlayerConstants.*;
import static utilz.LoadSave.loadAllAnimations;

import main.Game;
import map.Map;
import ui.PauseOverlay;
import ui.PlayerUI;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;


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

    public Playing(Game game) {
        super(game);
        initClasses();
    }

    void initClasses() {
        loadAllAnimations();
		map = new Map(game, selectedMapIndex);
		thuyTinh = new Character(200f, 535f, 80f, 40f, 30f, 50f, 35f, 20f, 55f, 85f, "ThuyTinh", RIGHT, map);
        sonTinh = new Character(800f, 535f, 15f, 40f, 30f, 50f, 35f, 20f, 55f, 85f, "SonTinh", LEFT, map);
        playerUI1 = new PlayerUI(200000, true);
        playerUI2 = new PlayerUI(200000, false);
        combat1 = new Combat(sonTinh, thuyTinh, playerUI2, playerUI1);
        combat2 = new Combat(thuyTinh, sonTinh, playerUI1, playerUI2);
        pauseOverlay = new PauseOverlay(this);
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
    }

    public void windowFocusLost() {
		sonTinh.resetAllBools();
        thuyTinh.resetAllBools();
	}

    @Override
    public void update() {
        if (!paused) {
            sonTinh.update();
            thuyTinh.update();
            combat1.update();
            combat2.update();
            playerUI1.update();
            playerUI2.update();
        }
        else {
            pauseOverlay.update();
        }
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

        if(paused) pauseOverlay.draw(g);
    }

    public void unpauseGame() {
        paused = false;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        
    }

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