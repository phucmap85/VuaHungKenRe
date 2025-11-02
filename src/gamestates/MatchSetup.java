package gamestates;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import utilz.LoadSave;
import static utilz.Constants.GameConstants.*;
import main.Game;
import map.Map;

public class MatchSetup extends State implements Statemethods {

    private BufferedImage[] nextButtonFrames;
    private BufferedImage[] prevButtonFrames;
    private boolean rightPressed = false, leftPressed = false;
    private long lastPressTimeRight = 0, lastPressTimeLeft = 0;
    private static final int PRESS_DURATION = 150; // ms
    private BufferedImage title;
    
    // Fade transition
    private float fadeAlpha = 1.0f;       // độ trong suốt (1 = bình thường, 0 = tối đen)
    private boolean fadingOut = false;    // đang làm mờ
    private boolean fadingIn = false;     // đang sáng dần
    private int nextMapIndex = -1;        // lưu map sắp chuyển
    private static final float FADE_SPEED = 0.05f; // tốc độ mờ (0.05f = nhanh, giảm xuống để chậm hơn)
    private Map map;
    private BufferedImage[] mapPreviews;

    private String[] mapOptions = { "Map 1 (Default)", "Map 2 (New)"};
    
    
    private int[] mapValues = { 0, 1 };

    // State variables
    private int selectedMapIndex = 0;

    public MatchSetup(Game game) {
        super(game);
        loadMapPreviews();
        loadButtons();
        loadTitle();
    }

    private void loadMapPreviews() {
        mapPreviews = new BufferedImage[mapOptions.length];
        mapPreviews[0] = utilz.LoadSave.GetSpriteAtlas(utilz.LoadSave.BattleMap0);
        mapPreviews[1] = utilz.LoadSave.GetSpriteAtlas(utilz.LoadSave.BattleMap1);
    }

    private void loadTitle() {
        title = LoadSave.GetSpriteAtlas(LoadSave.MatchSetupTitle);
    }

    private void loadButtons() {
        BufferedImage fullImg = LoadSave.GetSpriteAtlas(LoadSave.NextButton);
        int frameWidth = fullImg.getWidth() / 2;
        int frameHeight = fullImg.getHeight();

        // Right arrow
        nextButtonFrames = new BufferedImage[2];
        nextButtonFrames[0] = fullImg.getSubimage(0, 0, frameWidth, frameHeight);
        nextButtonFrames[1] = fullImg.getSubimage(frameWidth, 0, frameWidth, frameHeight);

        // Left arrow
        prevButtonFrames = new BufferedImage[2];
        prevButtonFrames[0] = LoadSave.flipHorizontally(nextButtonFrames[0]);
        prevButtonFrames[1] = LoadSave.flipHorizontally(nextButtonFrames[1]);
}

    @Override
public void draw(Graphics g) {
    // BACKGROUND
    BufferedImage background = LoadSave.GetSpriteAtlas(LoadSave.MatchSetupBackground);
    g.drawImage(background, 0, 0, GAME_WIDTH, GAME_HEIGHT, null);

    // DRAW TITLE

    if (title != null) {
            int scaledW = (int) (title.getWidth() * 0.3);
            int scaledH = (int) (title.getHeight() * 0.3);
            int titleX = 395;
            g.drawImage(title, titleX, -90, scaledW, scaledH, null);
        }
    
    // --- MAP PREVIEW ---
    if (mapPreviews[selectedMapIndex] != null) {
        int previewWidth = 710;
        int previewHeight = 463;
        int previewX = 194;
        int previewY = 152;

        g.drawImage(mapPreviews[selectedMapIndex], previewX, previewY, previewWidth, previewHeight, null);
        g.setColor(Color.GRAY);
        g.drawRect(previewX, previewY, previewWidth, previewHeight);
        g.setFont(new Font("Arial", Font.BOLD, 18));
    }

    // --- DRAW ARROWS ---
    int arrowY = 307;
    int arrowW = 64, arrowH = 128;

    int leftX = (GAME_WIDTH / 2) - 500;
    int leftFrame = leftPressed ? 1 : 0;
    g.drawImage(prevButtonFrames[leftFrame], leftX, arrowY, arrowW, arrowH, null);

    int rightX = GAME_WIDTH / 2 + 436;
    int rightFrame = rightPressed ? 1 : 0;
    g.drawImage(nextButtonFrames[rightFrame], rightX, arrowY, arrowW, arrowH, null);

    // --- FADE EFFECT ---
    if (fadeAlpha < 1.0f) {
    Graphics g2 = g.create();
    g2.setColor(new Color(0, 0, 0, 1f - fadeAlpha)); // lớp mờ
    
    // Chỉ phủ lên vùng map preview
    int previewWidth = 710;
    int previewHeight = 463;
    int previewX = 194;
    int previewY = 152;
    g2.fillRect(previewX, previewY, previewWidth, previewHeight);
    g2.dispose();
}
}


    @Override
    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT:
                if (!fadingOut && !fadingIn) { // chỉ cho fade nếu không đang hiệu ứng
                    nextMapIndex = (selectedMapIndex - 1 + mapOptions.length) % mapOptions.length;
                    fadingOut = true;
                }
                leftPressed = true;
                lastPressTimeLeft = System.currentTimeMillis();
                break;

            case KeyEvent.VK_RIGHT:
                if (!fadingOut && !fadingIn) {
                    nextMapIndex = (selectedMapIndex + 1) % mapOptions.length;
                    fadingOut = true;
                }
                rightPressed = true;
                lastPressTimeRight = System.currentTimeMillis();
                break;

            case KeyEvent.VK_ENTER:
                game.getPlaying().setMatchSettings(mapValues[selectedMapIndex]);
                Gamestate.state = Gamestate.PLAYING;
                break;
            case KeyEvent.VK_ESCAPE:
                Gamestate.state = Gamestate.MENU;
                break;
        }
    }


public void update() {
    long now = System.currentTimeMillis();
    if (rightPressed && now - lastPressTimeRight > PRESS_DURATION) rightPressed = false;
    if (leftPressed && now - lastPressTimeLeft > PRESS_DURATION) leftPressed = false;

    // --- Hiệu ứng mờ ---
    if (fadingOut) {
        fadeAlpha -= FADE_SPEED;
        if (fadeAlpha <= 0f) {
            fadeAlpha = 0f;
            fadingOut = false;
            selectedMapIndex = nextMapIndex; // đổi map
            fadingIn = true; // bắt đầu sáng lại
        }
    } else if (fadingIn) {
        fadeAlpha += FADE_SPEED;
        if (fadeAlpha >= 1f) {
            fadeAlpha = 1f;
            fadingIn = false;
        }
    }
}

    @Override public void keyPressed(KeyEvent e) {}
    @Override public void mouseClicked(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();
        System.out.println("[DEBUG] Mouse clicked at: X=" + x + ", Y=" + y);
    }
    @Override public void mousePressed(MouseEvent e) {}
    @Override public void mouseReleased(MouseEvent e) {}
    @Override public void mouseMoved(MouseEvent e) {}
}
