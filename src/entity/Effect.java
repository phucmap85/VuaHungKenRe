package entity;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import static utilz.Constants.EffectConstants.*;
import static utilz.LoadSave.getEffectSprites;

public class Effect {
    private static BufferedImage[][] animations = getEffectSprites();
    
    private int effectType, framesIndex, framesCounter, currentSpeed;   
    private float x, y; 
    private boolean isActive;
    
    // Animation speeds
    private static final int ANI_SPEED_SMOKE = 8;
    private static final int ANI_SPEED_SLASH = 15;
    private static final int ANI_SPEED_LANDING = 15;
    private static final int ANI_SPEED_PUNCHING = 20;

    public void setRender(float x, float y, int effectType) {
        this.x = x;
        this.y = y;
        this.effectType = effectType;
        this.isActive = true;
        this.currentSpeed = getAnimationSpeed(effectType);
    }
    
    private int getAnimationSpeed(int effectType) {
        if (effectType == LANDING_RIGHT || effectType == LANDING_LEFT) return ANI_SPEED_LANDING;
        if (effectType == SLASH_RIGHT || effectType == SLASH_LEFT) return ANI_SPEED_SLASH;
        if (effectType == IMPACT1_RIGHT || effectType == IMPACT1_LEFT) return ANI_SPEED_PUNCHING;
        return ANI_SPEED_SMOKE;
    }

    public void update() {
        framesCounter++;
        if (framesCounter >= currentSpeed) {
            framesCounter = 0;
            framesIndex++;
            if (framesIndex >= getFramesAmount(effectType)) {
                framesIndex = 0;
                isActive = false;
            }
        }
    }
    
    public void draw(Graphics g) {
        if (!isActive) return;
        
        Graphics2D g2 = (Graphics2D) g;
        int width = 160, height = 128;
        
        if (effectType == SLASH_RIGHT || effectType == SLASH_LEFT) {
            width = height = 87;
        } else if (effectType == LANDING_RIGHT || effectType == LANDING_LEFT) {
            width = height = 128;
        } else if (effectType == IMPACT1_RIGHT || effectType == IMPACT1_LEFT) {
            width = 96;
            height = 180;
        }
        
        g2.drawImage(animations[effectType][framesIndex], (int)x, (int)y, width, height, null);
    }
    
    public boolean isActive() {
        return isActive;
    }
}

