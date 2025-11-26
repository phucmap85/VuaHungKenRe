package map;

import main.Game;
import utilz.LoadSave;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import static utilz.Constants.GameConstants.*;

public class Map {
    private Game game;
    private int mapID;
    private BufferedImage img;
    private float groundY;
    private List<Platform> platforms = new ArrayList<>();

    public Map(Game game, int mapID) {
        this.game = game;
        this.mapID = mapID;
        importMap();
        initLayout();
    }

    private void importMap() {
        switch (mapID) {
            case 1:
                img = LoadSave.GetSpriteAtlas(LoadSave.BattleMap1);
                break;
            case 2:
                img = LoadSave.GetSpriteAtlas(LoadSave.BattleMap2);
                break;
            case 3:
                img = LoadSave.GetSpriteAtlas(LoadSave.BattleMap3);
                break;
            default:
                img = LoadSave.GetSpriteAtlas(LoadSave.BattleMap0);
                break;
        }
    }

    private void initLayout() {
        platforms.clear();

        switch (mapID) {
            case 1: // Map 2 - có nhiều tầng
                groundY = 660f;
                platforms.add(new Platform(346f, 800f, 465f));
                platforms.add(new Platform(118f, 268f, 540f));
                platforms.add(new Platform(886f, 1030f, 401f));
                break;
            case 2:
                groundY = 660f;
                break;
            case 3:
                groundY = 640f;
                break;
            case 4:
                groundY = 660f;
                break;
            default: // Map 1
                groundY = 640f;
                platforms.add(new Platform(320f, 755f, 470f));
                break;
        }
    }

    public List<Platform> getPlatforms() {
        return platforms;
    }

    public int getMapID() {
        return mapID;
    }
    public float getGroundY() {
        return groundY;
    }

    public void draw(Graphics g) {
        g.drawImage(img, 0, 0, GAME_WIDTH, GAME_HEIGHT, null);

        // platform debug
        
        /*g.setColor(Color.YELLOW);
        for (Platform p : platforms) {
            g.drawLine((int)p.x1, (int)p.y, (int)p.x2, (int)p.y);
        }*/
        
    }
}
