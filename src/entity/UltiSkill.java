package entity;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfigTemplate;
import java.awt.image.BufferedImage;
import static utilz.LoadSave.loadLightningAnimation;
import static utilz.LoadSave.loadUltiCreatureAnimation;
import static utilz.Constants.GameConstants.GAME_WIDTH;
import static utilz.Constants.PlayerConstants.*;

public class UltiSkill extends Entity {
    private String characterName;
    // image
    private BufferedImage[][] lightningAnimations;
    private BufferedImage[][] monsterAnimations;
    private int framesCounterforLightning, framesCounterforMonster, 
    framesIndexforLightning, framesIndexforMonster = 5,
    aniSpeedForMonster = 14, aniSpeedForLightning = 10, 
    maxFramesLightning, maxFramesMonster,
    delayForMonsterAppearing = 50, framesDelayForSquid = 12, framesDelayForPhoenix = 13;
    private int framesDelay;
    private boolean isActive = false, collision = false, 
    monsterAppeared = true, lightningAppeared = false;

    private int direction;
    private float enemyX, enemyY;

    public UltiSkill(float x, float y, float x_OffSetHitBox, float y_OffSetHitBox,
                     float widthHitBox, float heightHitBox,String characterName) {
        super(x, y, x_OffSetHitBox, y_OffSetHitBox, widthHitBox, heightHitBox);
        this.characterName = characterName;
        lightningAnimations = loadLightningAnimation(characterName);
        monsterAnimations = loadUltiCreatureAnimation(characterName);
        maxFramesLightning = lightningAnimations[0].length;
        maxFramesMonster = monsterAnimations[0].length;
        isActive = true;
        if(characterName.equals("SonTinh")){
            framesDelay = framesDelayForPhoenix;
        }
        else if(characterName.equals("ThuyTinh")){
            framesDelay = framesDelayForSquid;
        }
    }

    public void update(float enemyX, float enemyY){
        setX_Y (enemyX, enemyY);
        updatePerFrame();
        updateAnimationTick();

    }

    public void updatePerFrame(){
        if(characterName.equals("ThuyTinh")){
            if(framesIndexforLightning <= 9) {
                if(enemyX > GAME_WIDTH/2 ) direction = LEFT;
                else direction = RIGHT; 
                x = direction == RIGHT ? -110 : 125;
                y = enemyY - 425;
            }
            else{
                if(direction == RIGHT){
                    x = enemyX - 460;
                }
                else{
                    x = enemyX - 505;
                }

                y = 0;
            }
        }
        else if(characterName.equals("SonTinh")){
            if(framesIndexforLightning <= 6){
                x = enemyX - 480;
                y = enemyY - 480;
            }
            else if(framesIndexforLightning <= 16){
                x = enemyX - 490;
                y = 0;
            }
            else{
                x = enemyX - 500;
                y = enemyY - 320;
            }
        }
    }

    public void updateAnimationTick(){
        if(monsterAppeared){
        framesCounterforMonster++;
        if(framesIndexforMonster == framesDelay){
            if(framesCounterforMonster >= delayForMonsterAppearing){
                framesIndexforMonster++;
                framesCounterforMonster = 0;
                lightningAppeared = true;
            }
        }
        else {
            if(framesCounterforMonster >= aniSpeedForMonster){
                framesCounterforMonster = 0;
                framesIndexforMonster++;
                if(framesIndexforMonster >= maxFramesMonster){
                    monsterAppeared = false;
                    framesIndexforMonster = 5;
                }
            }
    }
}       
        if(lightningAppeared){
            framesCounterforLightning++;
            if(framesCounterforLightning >= aniSpeedForLightning){
                framesCounterforLightning = 0;
                framesIndexforLightning++;
                if(framesIndexforLightning >= maxFramesLightning){
                    lightningAppeared = false;
                    framesIndexforLightning = 0;
                    isActive = false;
                }
        }
    }
    }

    public void render(Graphics g){
        Graphics2D g2 = (Graphics2D) g;
        if(isActive){
        if(monsterAppeared) g2.drawImage(monsterAnimations[direction][framesIndexforMonster], 306, -30, 484, 484, null);
        if(lightningAppeared) g2.drawImage(lightningAnimations[direction][framesIndexforLightning], (int) x,(int) y, 1089, 822, null);
    }
}

    public boolean isActive(){
        return isActive;
    }
    public void setMonsterAppeared(boolean val){
        monsterAppeared = val;
    }
    public void setX_Y(float enemyX, float enemyY){
        this.enemyX = enemyX;
        this.enemyY = enemyY;
}
    public boolean lightningAppeared(){
        return lightningAppeared;
    }
    public boolean setActive(boolean val){
        return this.isActive = val;
    }
    public int getDirection(){
        return direction;
    }   
}