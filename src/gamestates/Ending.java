package gamestates;

import main.Game;
import map.Map;
import ui.EndingOverlay;
import utilz.LoadSave;

import static utilz.Constants.PlayerConstants.LEFT;
import static utilz.Constants.PlayerConstants.RIGHT;
import static utilz.LoadSave.GetSpriteAtlas;
import static utilz.LoadSave.loadAllAnimations;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import entity.Character;
import entity.Combat;

public class Ending extends State implements Statemethods {
    Map map;
    Character sonTinh, thuyTinh, vuaHung, myNuong;
    EndingOverlay endingOverlay;

    int selectedMapIndex;
    BufferedImage[] dialogue;
    
    public Ending(Game game) {
        super(game);
        initClasses();
    }

    void initClasses() {
        loadAllAnimations();
		map = new Map(game, selectedMapIndex);
		thuyTinh = new Character(200f, 535f, 80f, 40f, 30f, 50f, 35f, 20f, 55f, 85f, "ThuyTinh", RIGHT, map);
        sonTinh = new Character(800f, 535f, 15f, 40f, 30f, 50f, 35f, 20f, 55f, 85f, "SonTinh", LEFT, map);
        vuaHung = new Character(440f, 535f, 50f, 40f, 30f, 50f, 35f, 20f, 55f, 85f, "VuaHung", LEFT, map);
        myNuong = new Character(540f, 535f, 40f, 40f, 30f, 50f, 35f, 20f, 55f, 85f, "MyNuong", RIGHT, map);
        endingOverlay = new EndingOverlay(this);
        loadDialogue();
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

    private void loadDialogue() {
        BufferedImage fullImg = LoadSave.GetSpriteAtlas(LoadSave.Dialogue);
        int frameWidth = fullImg.getWidth() / 2;
        int frameHeight = fullImg.getHeight();


        dialogue = new BufferedImage[2];
        dialogue[0] = fullImg.getSubimage(0, 0, frameWidth, frameHeight); // SonTinh
        dialogue[1] = fullImg.getSubimage(frameWidth, 0, frameWidth, frameHeight); //ThuyTinh


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
        endingOverlay.update();
    }

    @Override
    public void draw(Graphics g) {
        map.draw(g);
        thuyTinh.render(g);
        sonTinh.render(g);
        vuaHung.render(g);
        myNuong.render(g);
        endingOverlay.draw(g);
        int scaledW = (int) (dialogue[0].getWidth() * 0.6);
        int scaledH = (int) (dialogue[0].getHeight() * 0.6);
        int dialogueX = 440;
       
        if(this.selectedMapIndex == 3){
             g.drawImage(dialogue[1], dialogueX, 410, scaledW, scaledH, null);
        }else if(this.selectedMapIndex == 4){
            g.drawImage(dialogue[0], dialogueX, 600, scaledW, scaledH, null);
        }  
    }

    @Override
    public void mouseClicked(MouseEvent e) {}

    @Override
    public void mousePressed(MouseEvent e) {
        endingOverlay.mousePressed(e);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        endingOverlay.mouseReleased(e);
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        endingOverlay.mouseMoved(e);
    }

    @Override
    public void mouseDragged(MouseEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {}

    @Override
    public void keyReleased(KeyEvent e) {}
}
