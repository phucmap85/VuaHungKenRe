package ui;

import static utilz.Constants.UI.URMButton.*;

import java.awt.Graphics;
import java.awt.event.MouseEvent;

import gamestates.Gamestate;
import gamestates.Ending;
import main.Game;
import sound.SoundManager;

public class EndingOverlay {
    private Ending ending;
    
    private UrmButton menuB;

    public EndingOverlay(Ending ending) {
        this.ending = ending;

        createUrmButtons();
    }

    private void createUrmButtons() {
        int menuX = 30;
        int bY = 30;

        menuB = new UrmButton(menuX, bY, URM_SIZE - 10, URM_SIZE - 10, 2);
    }

    public void update() {
        menuB.update();
    }

    public void draw(Graphics g) {
        menuB.draw(g);
    }

    public void mousePressed(MouseEvent e) {
        if (isIn(e, menuB)) {
            menuB.setMousePressed(true);
            Game.soundPlayer.play(SoundManager.CLICKBUTTON);
        }
    }

    public void mouseReleased(MouseEvent e) {
        if (isIn(e, menuB)) {
            if (menuB.isMousePressed()) {
                Gamestate.state = Gamestate.MENU;
                ending.resetAllStates();
            }
            menuB.setMousePressed(false);
        }

        menuB.resetBools();
    }

    public void mouseMoved(MouseEvent e) {
        menuB.setMouseOver(isIn(e, menuB));

        if (isIn(e, menuB)) {
            menuB.setMouseOver(true);
        }
    }

    private boolean isIn(MouseEvent e, PauseButton pb) {
        return pb.getBounds().contains(e.getX(), e.getY());
    }
}
