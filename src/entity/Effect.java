package entity;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import static utilz.Constants.EffectConstants.*;
import static utilz.LoadSave.getEffectSprites;
public class Effect {
    private static BufferedImage[][] animations = getEffectSprites();
    private int effectType, framesIndex, framesCounter, aniSpeedforSmoke = 10, aniSpeedforSlash = 15;
    private float x, y;
    private boolean isActive;
    
    public Effect(){
        
    }

    public void setRender(float x, float y, int effectType){
        this.x = x;
        this.y = y;
        this.effectType = effectType;
        isActive = true;
}
    public void update(){
        framesCounter++;
        int currentSpeed = (effectType == SMOKE_RIGHT || effectType == SMOKE_LEFT) ? aniSpeedforSmoke : aniSpeedforSlash;
        if(framesCounter >= currentSpeed){
            framesCounter = 0;
            framesIndex++;
            if(framesIndex >= getFramesAmount(effectType)){
                framesIndex = 0;
                isActive = false;
            }
        }
    }
    
    public void draw(Graphics g){
    Graphics2D g2 = (Graphics2D) g;
       if(isActive) {
        if(effectType == SLASH_RIGHT || effectType == SLASH_LEFT) {
            g2.drawImage(animations[effectType][framesIndex], (int)x, (int)y, 87, 87, null);
        }
        else
        g2.drawImage(animations[effectType][framesIndex], (int)x, (int)y, 160,128 ,null);
       }
    }
    public boolean isActive() {
        return isActive;
    }
}