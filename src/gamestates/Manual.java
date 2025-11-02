package gamestates;

import static utilz.Constants.GameConstants.*;

import static utilz.Constants.UI.KeyButton.*;


import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;


import main.Game;
import ui.KeyButton;






public class Manual extends Playing{

//    private boolean[] keysPressed = new boolean[256];
    private KeyButton[] keyButtons = new KeyButton[19];


    public Manual(Game game) {
        super(game);
        loadKeyButtons();
        setMatchSettings(2);
    }



    void loadKeyButtons(){
        keyButtons[0] = new KeyButton(K_XPOS, K_YPOS, 0); // "A"
        keyButtons[3] = new KeyButton(K_XPOS + K_OFFSET, K_YPOS, 1); // "S"
        keyButtons[2] = new KeyButton(K_XPOS + K_OFFSET * 2, K_YPOS, 2); // "D"
        keyButtons[1] = new KeyButton(K_XPOS + K_OFFSET, K_YPOS - K_OFFSET, 3); // "W"

        keyButtons[4] = new KeyButton(K_XPOS + K_OFFSET * 4, K_YPOS - K_OFFSET, 4); // "U"
        keyButtons[5] = new KeyButton(K_XPOS + K_OFFSET * 5, K_YPOS - K_OFFSET, 5); // "I"
        keyButtons[6] = new KeyButton(K_XPOS + K_OFFSET * 4, K_YPOS, 6); // "J"
        keyButtons[7] = new KeyButton(K_XPOS + K_OFFSET * 5, K_YPOS, 7); // "K"
        keyButtons[8] = new KeyButton(K_XPOS + K_OFFSET * 6, K_YPOS, 8); // "L"

        keyButtons[9] = new KeyButton(GAME_WIDTH / 2 + K_XPOS, K_YPOS, 9); // "LEFT"
        keyButtons[12] = new KeyButton(GAME_WIDTH / 2 + K_XPOS + K_OFFSET, K_YPOS, 10); // "DOWN"
        keyButtons[11] = new KeyButton(GAME_WIDTH / 2 + K_XPOS + K_OFFSET * 2, K_YPOS, 11); // "RIGHT"
        keyButtons[10] = new KeyButton(GAME_WIDTH / 2 + K_XPOS + K_OFFSET, K_YPOS - K_OFFSET, 12); // "UP"

        keyButtons[16] = new KeyButton(GAME_WIDTH / 2 + K_XPOS + K_OFFSET * 4, K_YPOS, 13); // "1"
        keyButtons[17] = new KeyButton(GAME_WIDTH / 2 + K_XPOS + K_OFFSET * 5, K_YPOS, 14); // "2"
        keyButtons[15] = new KeyButton(GAME_WIDTH / 2 + K_XPOS + K_OFFSET * 6, K_YPOS, 15); // "3"
        keyButtons[13] = new KeyButton(GAME_WIDTH / 2 + K_XPOS + K_OFFSET * 4, K_YPOS - K_OFFSET, 16); // "4"
        keyButtons[14] = new KeyButton(GAME_WIDTH / 2 + K_XPOS + K_OFFSET * 5, K_YPOS - K_OFFSET, 17); // "5"

        keyButtons[18] = new KeyButton(K_XPOS, K_YPOS - K_OFFSET * 3, 0, 21, 41, "KeyButton/esc.png"); // "ESC"
    }
    private void resetHealthMana(){
        playerUI1.setHealth((int)1e5 * 2);
        playerUI2.setHealth((int)1e5 * 2);
        playerUI1.setMana((int) 2e3);
        playerUI2.setMana((int) 2e3);
    }

    @Override
    public void update() {
        sonTinh.update();
        thuyTinh.update();
        combat1.update();
        combat2.update();
        resetHealthMana();
        for (KeyButton kb : keyButtons){
            kb.update();
        }
    }

    @Override
    public void draw(Graphics g) {
        //map.draw(g);
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
        //playerUI1.draw(g, GAME_WIDTH);
        //playerUI2.draw(g, GAME_WIDTH);
        combat1.render(g);
        combat2.render(g);

        if(paused) pauseOverlay.draw(g);
        for (KeyButton kb : keyButtons) {
            kb.draw(g);
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    
    }

    @Override
    public void mousePressed(MouseEvent e) {
        
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();

        if (keyCode < keysPressed.length && !keysPressed[keyCode]) {
            keysPressed[keyCode] = true;

            switch (keyCode) {
                case KeyEvent.VK_A:
                    thuyTinh.setLeft(true);
                    keyButtons[0].setKeyPressed(true);
                    break;
                case KeyEvent.VK_D:
                    thuyTinh.setRight(true);
                    keyButtons[2].setKeyPressed(true);
                    break;
                case KeyEvent.VK_S:
                    thuyTinh.setDefend(true);
                    keyButtons[3].setKeyPressed(true);
                    break;
                case KeyEvent.VK_K:
                    thuyTinh.setJump(true);
                    keyButtons[7].setKeyPressed(true);
                    break;
                case KeyEvent.VK_J:
                    thuyTinh.setPunch(true);
                    keyButtons[6].setKeyPressed(true);
                    break;
                case KeyEvent.VK_U:
                    thuyTinh.setSummon(true);
                    keyButtons[4].setKeyPressed(true);
                    break;
                case KeyEvent.VK_I:
                    thuyTinh.setUlti(true);
                    keyButtons[5].setKeyPressed(true);
                    break;
                case KeyEvent.VK_L:
                    thuyTinh.setDash(true);
                    keyButtons[8].setKeyPressed(true);
                    break;
                case KeyEvent.VK_LEFT:
                    sonTinh.setLeft(true);
                    keyButtons[9].setKeyPressed(true);
                    break;
                case KeyEvent.VK_RIGHT:
                    sonTinh.setRight(true); 
                    keyButtons[11].setKeyPressed(true);
                    break;
                case KeyEvent.VK_DOWN:
                    sonTinh.setDefend(true);
                    keyButtons[12].setKeyPressed(true);
                    break;
                case KeyEvent.VK_NUMPAD2:
                    sonTinh.setJump(true);
                    keyButtons[17].setKeyPressed(true);
                    break;
                case KeyEvent.VK_NUMPAD1:
                    sonTinh.setPunch(true); 
                    keyButtons[16].setKeyPressed(true);
                    break;
                case KeyEvent.VK_NUMPAD4:
                    sonTinh.setSummon(true);
                    keyButtons[13].setKeyPressed(true);
                    break;
                case KeyEvent.VK_NUMPAD3:
                    sonTinh.setDash(true);
                    keyButtons[15].setKeyPressed(true);
                    break;
                case KeyEvent.VK_NUMPAD5:
                    sonTinh.setUlti(true);
                    keyButtons[14].setKeyPressed(true);
                    break;
                case KeyEvent.VK_ESCAPE:
                    Gamestate.state = Gamestate.MENU;
                    resetAllStates();
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
                keyButtons[0].setKeyPressed(false);
                break;
            case KeyEvent.VK_D:
                thuyTinh.setRight(false);
                keyButtons[2].setKeyPressed(false);
                break;
            case KeyEvent.VK_S:
                thuyTinh.setDefend(false);
                keyButtons[3].setKeyPressed(false);
                break;
            case KeyEvent.VK_K:
                thuyTinh.setJump(false);
                keyButtons[7].setKeyPressed(false);
                break;
            case KeyEvent.VK_J:
                thuyTinh.setPunch(false);
                keyButtons[6].setKeyPressed(false);
                break;
            case KeyEvent.VK_U:
                thuyTinh.setSummon(false); 
                keyButtons[4].setKeyPressed(false); 
                break;
            case KeyEvent.VK_I:
                thuyTinh.setUlti(false);
                keyButtons[5].setKeyPressed(false);
                break;
            case KeyEvent.VK_L:
                thuyTinh.setDash(false);
                keyButtons[8].setKeyPressed(false);
                break;
            case KeyEvent.VK_LEFT:
                sonTinh.setLeft(false); 
                keyButtons[9].setKeyPressed(false);
                break;
            case KeyEvent.VK_RIGHT:
                sonTinh.setRight(false); 
                keyButtons[11].setKeyPressed(false);
                break;
            case KeyEvent.VK_DOWN:
                sonTinh.setDefend(false);  
                keyButtons[12].setKeyPressed(false);
                break;
            case KeyEvent.VK_NUMPAD2:
                sonTinh.setJump(false); 
                keyButtons[17].setKeyPressed(false);
                break;      
            case KeyEvent.VK_NUMPAD1:
                sonTinh.setPunch(false);
                keyButtons[16].setKeyPressed(false);
                break;
            case KeyEvent.VK_NUMPAD4:
                sonTinh.setSummon(false);  
                keyButtons[13].setKeyPressed(false);
                break;
            case KeyEvent.VK_NUMPAD3:
                sonTinh.setDash(false);
                keyButtons[15].setKeyPressed(false);
                break;
            case KeyEvent.VK_NUMPAD5:
                sonTinh.setUlti(false);
                keyButtons[14].setKeyPressed(false);
                break;
            default:
                break;
        }
    }
}
