package gamestates;

import static utilz.Constants.GameConstants.*;

import main.Game;
import map.Map;
import ui.PlayerUI;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import entity.Hog;
import entity.Player1;
import entity.Player2;
import entity.Tornado;

public class Playing extends State implements Statemethods {
    private boolean[] keysPressed = new boolean[256];

    private Map map;
	private Player1 player1;
    private PlayerUI uiPlayer1;
    private PlayerUI uiPlayer2;
    private Tornado tornado;
    private Player2 player2;
    private Hog hog;
    public Playing(Game game) {
        super(game);
        initClasses();
    }
    
    private void initClasses() {
		map = new Map(game);
		player1 = new Player1(200f, 530f, 55f, 95f, 35f, 20f);
        uiPlayer1 = new PlayerUI(10000, true);
        uiPlayer2 = new PlayerUI(10000, false);
        player2 = new Player2(800f, 530f, 55f, 95f, 35f, 20f, uiPlayer2);

        tornado = new Tornado(player1,player2,uiPlayer2);
        hog = new Hog(player2);

	}

    public void windowFocusLost() {
		player1.resetDirBooleans();
	}

	public Player1 getPlayer1() {
        return player1;
	}
    public Player2 getPlayer2() {
        return player2;
    }

    @Override
    public void update() {
        player1.update();
        uiPlayer1.update();
        tornado.update();
        player2.update();
        uiPlayer2.update();     
        hog.update();
    }

    @Override
    public void draw(Graphics g) {
        map.draw(g);
		player1.render(g);
        uiPlayer1.draw(g, GAME_WIDTH);
        tornado.render(g);
        player2.render(g);
        uiPlayer2.draw(g, GAME_WIDTH); 
        hog.render(g);      
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
                    player1.setLeft(true);
                    break;
                case KeyEvent.VK_D:
                    player1.setRight(true);
                    break;
                case KeyEvent.VK_S:
                    player1.setDefense(true);
                    break;
                case KeyEvent.VK_K:
                    player1.setJump(true);
                    break;
                case KeyEvent.VK_J:
                    player1.setPunch(true);
                    break;
                case KeyEvent.VK_U:
                    player1.setTornado(true);
                    break;
                case KeyEvent.VK_LEFT:
                    player2.setLeft(true);
                    break;
                case KeyEvent.VK_RIGHT:
                    player2.setRight(true); 
                    break;
                case KeyEvent.VK_DOWN:
                    player2.setDefense(true);
                    break;
                case KeyEvent.VK_NUMPAD2:
                    player2.setJump(true);
                    break;
                case KeyEvent.VK_NUMPAD1:
                    player2.setPunch(true); 
                    break;
                case KeyEvent.VK_NUMPAD4:
                    player2.setTornado(true);
             
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
                player1.setLeft(false);
                break;
            case KeyEvent.VK_D:
                player1.setRight(false);
                break;
            case KeyEvent.VK_S:
                player1.setDefense(false);
                break;
            case KeyEvent.VK_K:
                player1.setJump(false);
                break;
            case KeyEvent.VK_J:
                player1.setPunch(false);
                break;
            case KeyEvent.VK_U:
                player1.setTornado(false);  
                break;
            case KeyEvent.VK_LEFT:
                player2.setLeft(false); 
                break;
            case KeyEvent.VK_RIGHT:
                player2.setRight(false); 
                break;
            case KeyEvent.VK_DOWN:
                player2.setDefense(false);  
                break;
            case KeyEvent.VK_NUMPAD2:
                player2.setJump(false); 
                break;      
            case KeyEvent.VK_NUMPAD1:
                player2.setPunch(false);
                break;
            case KeyEvent.VK_NUMPAD4:
                player2.setTornado(false);  
                break;
            default:
                break;
        }
    }
}