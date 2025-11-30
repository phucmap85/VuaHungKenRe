package inputs;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import gamestates.Gamestate;
import main.GamePanel;

public class MouseHandler implements MouseListener, MouseMotionListener {
	private GamePanel gamePanel;

	public MouseHandler(GamePanel gamePanel) {
		this.gamePanel = gamePanel;
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		switch (Gamestate.state) {
			case MENU:
				gamePanel.getGame().getMenu().mouseDragged(e);
				break;
			case PLAYING:
				gamePanel.getGame().getPlaying().mouseDragged(e);
				break;
			case ENDING:
				gamePanel.getGame().getEnding().mouseDragged(e);
				break;
			default:
				break;
		}
	}

	@Override
	public void mouseMoved(MouseEvent e) {
        switch (Gamestate.state) {
			case MENU:
				gamePanel.getGame().getMenu().mouseMoved(e);
				break;
			case PLAYING:
				gamePanel.getGame().getPlaying().mouseMoved(e);
				break;
			case MATCH_SETUP:
				gamePanel.getGame().getMatchSetup().mouseMoved(e);
				break;
			case ENDING:
				gamePanel.getGame().getEnding().mouseMoved(e);
				break;
			default:
				break;
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		switch (Gamestate.state) {
			case PLAYING:
				gamePanel.getGame().getPlaying().mouseClicked(e);
				break;
			case MATCH_SETUP:
				gamePanel.getGame().getMatchSetup().mouseClicked(e);
				break;
			default:
				break;
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
		switch (Gamestate.state) {
			case MENU:
				gamePanel.getGame().getMenu().mousePressed(e);
				break;
			case PLAYING:
				gamePanel.getGame().getPlaying().mousePressed(e);
				break;
			case MATCH_SETUP:
				gamePanel.getGame().getMatchSetup().mousePressed(e);
				break;
			case ENDING:
				gamePanel.getGame().getEnding().mousePressed(e);
				break;
			default:
				break;
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		switch (Gamestate.state) {
			case MENU:
				gamePanel.getGame().getMenu().mouseReleased(e);
				break;
			case PLAYING:
				gamePanel.getGame().getPlaying().mouseReleased(e);
				break;
			case MATCH_SETUP:
				gamePanel.getGame().getMatchSetup().mouseReleased(e);
				break;
			case ENDING:
				gamePanel.getGame().getEnding().mouseReleased(e);
				break;
			default:
				break;
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mouseExited(MouseEvent e) {
        // TODO Auto-generated method stub
	}
}