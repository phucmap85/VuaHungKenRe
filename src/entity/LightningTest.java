package entity;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import utilz.LoadSave;

public class LightningTest {
    private BufferedImage[][] animations;
    private int framesCounter = 0;
    private int framesIndex = 0;
    private int aniSpeed = 10; // Tốc độ animation (càng nhỏ càng nhanh)
    private int direction = 0; // 0 = RIGHT, 1 = LEFT
    private int maxFrames;
    
    private float x, y; // Vị trí hiển thị
    private String characterName;
    
    public LightningTest(float x, float y, String characterName) {
        this.x = x;
        this.y = y;
        this.characterName = characterName;
        loadAnimations();
    }
    
    private void loadAnimations() {
        animations = LoadSave.loadLightningAnimation(characterName);
        maxFrames = animations[0].length;
        System.out.println("LightningTest: Loaded " + maxFrames + " frames for " + characterName);
    }
    
    public void update() {
        updateAnimationTick();
    }
    
    private void updateAnimationTick() {
        framesCounter++;
        
        if (framesCounter >= aniSpeed) {
            framesCounter = 0;
            framesIndex++;
            
            if (framesIndex >= maxFrames) {
                framesIndex = 0; // Loop animation
            }
        }
    }
    
    public void render(Graphics g) {
        if (animations != null && animations[direction] != null) {
            g.drawImage(animations[direction][framesIndex], (int) x, (int) y, 1089, 822, null);
            
            // Hiển thị thông tin debug
            g.drawString("Lightning: " + characterName, (int) x, (int) y - 10);
            g.drawString("Frame: " + (framesIndex + 1) + "/" + maxFrames, (int) x, (int) y - 25);
        }
    }
    
    public void setDirection(int direction) {
        this.direction = direction;
    }
    
    public void reset() {
        framesIndex = 0;
        framesCounter = 0;
    }
}
