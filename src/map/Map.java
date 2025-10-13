package map;

import static utilz.Constants.GameConstants.*;
import static utilz.LoadSave.*;

import java.awt.Graphics;
import java.awt.Graphics2D;

import java.awt.image.BufferedImage;

import main.Game;

public class Map {
    private Game game;
    private BufferedImage img;

    public Map(Game game) {
        this.game = game;
        importMap();
    }

    private void importMap() {
        img = GetSpriteAtlas(BattleMap);
    }

    public void draw(Graphics g) {
        Graphics g2 = (Graphics2D) g;
        g2.drawImage(img, 0, 0, GAME_WIDTH, GAME_HEIGHT, null); 
    }
}
