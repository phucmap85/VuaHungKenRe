package gamestates;

import main.Game;
import map.Map;
import ui.EndingOverlay;
import utilz.LoadSave;

import static utilz.Constants.PlayerConstants.IDLE_LEFT;
import static utilz.Constants.PlayerConstants.IDLE_RIGHT;
import static utilz.Constants.PlayerConstants.IDLE_SOUTH;
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
		thuyTinh = new Character(550f, 535f, 80f, 40f, 30f, 50f, 35f, 20f, 55f, 85f, "ThuyTinh", RIGHT, map);
        sonTinh = new Character(550f, 535f, 15f, 40f, 30f, 50f, 35f, 20f, 55f, 85f, "SonTinh", LEFT, map);
        vuaHung = new Character(400f, 535f, 50f, 40f, 30f, 50f, 35f, 20f, 55f, 85f, "VuaHung", LEFT, map);
        myNuong = new Character(500f, 535f, 40f, 40f, 30f, 50f, 35f, 20f, 55f, 85f, "MyNuong", RIGHT, map);
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
        thuyTinh.setPlayerAction(IDLE_SOUTH);
        sonTinh.setPlayerAction(IDLE_SOUTH);
        vuaHung.setPlayerAction(IDLE_SOUTH);   // Giờ VuaHung cũng có IDLE_SOUTH
        myNuong.setPlayerAction(IDLE_SOUTH);   // Giờ MyNuong cũng có IDLE_SOUTH
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
        sonTinh.updateForEnding();
        thuyTinh.updateForEnding();
        vuaHung.updateForEnding();
        myNuong.updateForEnding();
        endingOverlay.update();
    }

    @Override
    public void draw(Graphics g) {
        map.draw(g);
        if(selectedMapIndex == 3) thuyTinh.renderforEnding(g);
        else sonTinh.renderforEnding(g);
        vuaHung.render(g);
        myNuong.render(g);
        endingOverlay.draw(g);
        int scaledW = (int) (dialogue[0].getWidth() * 0.6);
        int scaledH = (int) (dialogue[0].getHeight() * 0.6);
        int dialogueX = 390;
       
        if(this.selectedMapIndex == 3){
             g.drawImage(dialogue[1], dialogueX, 450, scaledW, scaledH, null);
        }else if(this.selectedMapIndex == 2){
            g.drawImage(dialogue[0], dialogueX, 450, scaledW, scaledH, null);
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
