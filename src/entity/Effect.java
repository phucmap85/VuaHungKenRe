package entity;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import static utilz.Constants.EffectConstants.*;
import static utilz.LoadSave.getEffectSprites;
public class Effect {
    private static BufferedImage[][] animations = getEffectSprites();
    private int effectType, framesIndex, framesCounter, 
    aniSpeedforSmoke = 8, aniSpeedforSlash = 15, aniSpeedForLanding = 15, aniSpeedforPunching = 20,
    currentSpeed;   
    private float x, y; 
    private boolean isActive;
    

    public void setRender(float x, float y, int effectType){
        this.x = x;
        this.y = y;
        this.effectType = effectType;
        isActive = true;
        
        if(effectType == LANDING_RIGHT || effectType == LANDING_LEFT){
            currentSpeed = aniSpeedForLanding;
        }
        else if (effectType == SLASH_RIGHT || effectType == SLASH_LEFT){
            currentSpeed = aniSpeedforSlash;
        }
        else if(effectType == IMPACT1_RIGHT || effectType == IMPACT1_LEFT){
            currentSpeed = aniSpeedforPunching;
            
        }
        else currentSpeed = aniSpeedforSmoke;
    }

    public void update(){
        framesCounter++;
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
        else if(effectType == LANDING_RIGHT || effectType == LANDING_LEFT) {
            g2.drawImage(animations[effectType][framesIndex], (int)x, (int)y, 128, 128, null);
        }
        else if(effectType == IMPACT1_RIGHT || effectType == IMPACT1_LEFT) {
            g2.drawImage(animations[effectType][framesIndex], (int)x, (int)y, 96, 180, null);
        }
        else
        g2.drawImage(animations[effectType][framesIndex], (int)x, (int)y, 160,128 ,null);
       }
    }
    public boolean isActive() {
        return isActive;
    }
}