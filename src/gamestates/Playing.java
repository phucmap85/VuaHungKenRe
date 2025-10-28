package gamestates;

import static utilz.Constants.GameConstants.*;
import static utilz.Constants.PlayerConstants.*;

import main.Game;
import map.Map;
import ui.PlayerUI;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import entity.SummonSkill;
import entity.UltiSkill;
import entity.Character;
import entity.Combat;





public class Playing extends State implements Statemethods {
    private boolean[] keysPressed = new boolean[256];

    private Map map;
    private Character sonTinh, thuyTinh;
    private PlayerUI playerUI1, playerUI2;
    private Combat combat1;
    private Combat combat2;

    public Playing(Game game) {
        super(game);
        initClasses();
    }
    
    private void initClasses() {
		map = new Map(game);
		thuyTinh = new Character(200f, 535f, 80f, 40f, 30f, 50f, 35f, 20f, 55f, 85f, "ThuyTinh", RIGHT);
        sonTinh = new Character(235f, 535f, 15f, 40f, 30f, 50f, 35f, 20f, 55f, 85f, "SonTinh", LEFT);
        playerUI1 = new PlayerUI(1000, true);
        playerUI2 = new PlayerUI(1000, false);
        combat1 = new Combat(sonTinh, thuyTinh, playerUI2, playerUI1);
        combat2 = new Combat(thuyTinh, sonTinh, playerUI1, playerUI2);
        
        // TEST: Initialize lightning animations ở giữa màn hình
      
	}

    public void windowFocusLost() {
		sonTinh.resetAllBools();
        thuyTinh.resetAllBools();
	}

    @Override
    public void update() {
        sonTinh.update();
        thuyTinh.update();
        combat1.update();
        combat2.update();
        playerUI1.update();
        playerUI2.update();
       
    }

    @Override
    public void draw(Graphics g) {
        map.draw(g);
        // layer rendering based on punching state
        if(sonTinh.punching()){
            thuyTinh.render(g);
            sonTinh.render(g);


        }
        else if(thuyTinh.punching()){
            sonTinh.render(g);
            thuyTinh.render(g);
        }
        else{
            sonTinh.render(g);
            thuyTinh.render(g);
        }
        playerUI1.draw(g, GAME_WIDTH);
        playerUI2.draw(g, GAME_WIDTH);
        combat1.render(g);
        combat2.render(g);
        
        // TEST: Render lightning animations on top
        // lightningTestSonTinh.render(g);
        // lightningTestThuyTinh.render(g);

    }  
    @Override
    public void mouseClicked(MouseEvent e) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'mouseClicked'");
    }

    @Override
    public void mousePressed(MouseEvent e) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'mousePressed'");
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'mouseReleased'");
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'mouseMoved'");
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
                case KeyEvent.VK_ESCAPE:
                    Gamestate.state = Gamestate.MENU;
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
            case KeyEvent.VK_LEFT:
                sonTinh.setLeft(false); 
                break;
            case KeyEvent.VK_RIGHT:
                sonTinh.setRight(false); 
                break;
            case KeyEvent.VK_DOWN:
                sonTinh.setDefend(false);  
                break;
            case KeyEvent.VK_I:
                thuyTinh.setUlti(false);
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
            default:
                break;
        }
    }
}