package gamestates;

import main.Game;
import map.Map;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import entity.Player1;

public class Playing extends State implements Statemethods {
    private Map map;
	private Player1 player1;
    
    public Playing(Game game) {
        super(game);
        initClasses();
    }
    
    private void initClasses() {
		map = new Map(game);
		player1 = new Player1(200f, 485f, 128f, 128f);
	}

    public void windowFocusLost() {
		player1.resetDirBooleans();
	}

	public Player1 getPlayer1() {
        return player1;
	}

    @Override
    public void update() {
        player1.update();
    }

    @Override
    public void draw(Graphics g) {
        map.draw(g);
		player1.render(g);
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
        switch (e.getKeyCode()) {
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
            case KeyEvent.VK_ESCAPE:
                Gamestate.state = Gamestate.MENU;
                break;
            default:
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {
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
            default:
                break;
        }
    }
}
