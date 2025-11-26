package gamestates;

import main.Game;
import map.Map;

import static utilz.Constants.PlayerConstants.LEFT;
import static utilz.Constants.PlayerConstants.RIGHT;
import static utilz.LoadSave.loadAllAnimations;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import entity.Character;
import entity.Combat;

public class Ending extends State implements Statemethods {
    Map map;
    Character sonTinh, thuyTinh, vuaHung, myNuong;

    int selectedMapIndex;
    
    public Ending(Game game) {
        super(game);
        initClasses();
    }

    void initClasses() {
        loadAllAnimations();
		map = new Map(game, selectedMapIndex);
		thuyTinh = new Character(200f, 535f, 80f, 40f, 30f, 50f, 35f, 20f, 55f, 85f, "ThuyTinh", RIGHT, map);
        sonTinh = new Character(800f, 535f, 15f, 40f, 30f, 50f, 35f, 20f, 55f, 85f, "SonTinh", LEFT, map);
        vuaHung = new Character(500f, 535f, 50f, 40f, 30f, 50f, 35f, 20f, 55f, 85f, "VuaHung", LEFT, map);
        myNuong = new Character(600f, 535f, 40f, 40f, 30f, 50f, 35f, 20f, 55f, 85f, "MyNuong", RIGHT, map);
	}

    public void setMap(int mapID) {
        this.selectedMapIndex = mapID + 2;
        map = new Map(game, selectedMapIndex);
        thuyTinh.setMap(map);
        sonTinh.setMap(map);
        vuaHung.setMap(map);
        myNuong.setMap(map);
        resetAllStates();
    }

    public void resetAllStates(){
        thuyTinh.setPosition(200f, 535f);
        sonTinh.setPosition(800f, 535f);
        vuaHung.setPosition(500f, 535f);
        myNuong.setPosition(600f, 535f);
        thuyTinh.resetAllBools();
        sonTinh.resetAllBools();
        vuaHung.resetAllBools();
        myNuong.resetAllBools();
        thuyTinh.resetAllStates();
        sonTinh.resetAllStates();
        vuaHung.resetAllStates();
        myNuong.resetAllStates();
        thuyTinh.setDirection(RIGHT);
        sonTinh.setDirection(LEFT);
    }

    public void windowFocusLost() {
		sonTinh.resetAllBools();
        thuyTinh.resetAllBools();
        vuaHung.resetAllBools();
        myNuong.resetAllBools();
	}

    @Override
    public void update() {
        sonTinh.update();
        thuyTinh.update();
        vuaHung.update();
        myNuong.update();
    }

    @Override
    public void draw(Graphics g) {
        map.draw(g);
        thuyTinh.render(g);
        sonTinh.render(g);
        vuaHung.render(g);
        myNuong.render(g);
    }

    @Override
    public void mouseClicked(MouseEvent e) {}

    @Override
    public void mousePressed(MouseEvent e) {}

    @Override
    public void mouseReleased(MouseEvent e) {}

    @Override
    public void mouseMoved(MouseEvent e) {}

    @Override
    public void mouseDragged(MouseEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {}

    @Override
    public void keyReleased(KeyEvent e) {}
}
