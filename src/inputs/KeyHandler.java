package inputs;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import main.Game;
import main.GamePanel;

public class KeyHandler implements KeyListener {
    private GamePanel gamePanel;


    public KeyHandler(GamePanel gamePanel) {this.gamePanel = gamePanel;}
    @Override
    public void keyTyped(KeyEvent e) {
        // Không cần xử lý ở đây
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_A:
            gamePanel.getGame().getPlayer1().setLeft(true);
                break;
            case KeyEvent.VK_D:
            gamePanel.getGame().getPlayer1().setRight(true);
                break;
            case KeyEvent.VK_S:
            gamePanel.getGame().getPlayer1().setDefense(true);
                break;
            case KeyEvent.VK_K:
            gamePanel.getGame().getPlayer1().setJump(true);
                break;


            
            default:
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
       switch (e.getKeyCode()) {
            case KeyEvent.VK_A:
            gamePanel.getGame().getPlayer1().setLeft(false);
                break;
            case KeyEvent.VK_D:
            gamePanel.getGame().getPlayer1().setRight(false);
                break;
            case KeyEvent.VK_S:
            gamePanel.getGame().getPlayer1().setDefense(false);
                break;
            case KeyEvent.VK_K:
            gamePanel.getGame().getPlayer1().setJump(false);
                break;


            
            default:
                break;
        }
    }
}
