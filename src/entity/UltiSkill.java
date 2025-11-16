package entity;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import main.Game;
import sound.SoundManager;

import static utilz.LoadSave.loadLightningAnimation;
import static utilz.LoadSave.loadUltiCreatureAnimation;
import static utilz.Constants.GameConstants.GAME_WIDTH;
import static utilz.Constants.PlayerConstants.*;

public class UltiSkill extends Entity {
    private String characterName;
    
    // Animation
    private BufferedImage[][] lightningAnimations, monsterAnimations;
    private int framesCounterforLightning, framesCounterforMonster;
    private int framesIndexforLightning, framesIndexforMonster = 5;
    private int aniSpeedForMonster = 14, aniSpeedForLightning = 10;
    private int maxFramesLightning, maxFramesMonster;
    private int delayForMonsterAppearing = 50;
    private int framesDelayForSquid = 12, framesDelayForPhoenix = 13;
    private int framesDelay;
    
    // State
    private boolean isActive = false;
    private boolean monsterAppeared = true, lightningAppeared = false;
    private int direction;
    
    // Position
    private float enemyX, enemyY;
    
    private final boolean isSonTinh;

    public UltiSkill(float x, float y, float x_OffSetHitBox, float y_OffSetHitBox,
                     float widthHitBox, float heightHitBox, String characterName) {
        super(x, y, x_OffSetHitBox, y_OffSetHitBox, widthHitBox, heightHitBox);
        this.characterName = characterName;
        this.isSonTinh = "SonTinh".equals(characterName);
        this.isActive = true;
        
        loadAnimations();
        setFrameDelay();
    }
    
    private void loadAnimations() {
        lightningAnimations = loadLightningAnimation(characterName);
        monsterAnimations = loadUltiCreatureAnimation(characterName);
        maxFramesLightning = lightningAnimations[0].length;
        maxFramesMonster = monsterAnimations[0].length;
    }
    
    private void setFrameDelay() {
        framesDelay = isSonTinh ? framesDelayForPhoenix : framesDelayForSquid;
    }

    public void update(float enemyX, float enemyY) {
        setX_Y(enemyX, enemyY);
        updatePerFrame();
        updateAnimationTick();
    }

    public void updatePerFrame() {
        if (isSonTinh) {
            updateSonTinhPosition();
        } else {
            updateThuyTinhPosition();
        }
    }
    
    private void updateSonTinhPosition() {
        if (framesIndexforLightning <= 6) {
            x = enemyX - 480;
            y = enemyY - 480;
        } else if (framesIndexforLightning <= 16) {
            x = enemyX - 490;
            y = 0;
        } else {
            x = enemyX - 500;
            y = enemyY - 320;
        }
    }
    
    private void updateThuyTinhPosition() {
        if (framesIndexforLightning <= 9) {
            direction = (enemyX > GAME_WIDTH / 2) ? LEFT : RIGHT;
            x = (direction == RIGHT) ? -110 : 125;
            y = enemyY - 425;
        } else {
            x = (direction == RIGHT) ? enemyX - 460 : enemyX - 505;
            y = 0;
        }
    }

    public void updateAnimationTick() {
        updateMonsterAnimation();
        updateLightningAnimation();
    }
    
    private void updateMonsterAnimation() {
        if (!monsterAppeared) return;
        
        framesCounterforMonster++;
        
        if (framesIndexforMonster == framesDelay) {
            if (framesCounterforMonster >= delayForMonsterAppearing) {
                framesIndexforMonster++;
                framesCounterforMonster = 0;
                lightningAppeared = true;
            }
        } else {
            if (framesCounterforMonster >= aniSpeedForMonster) {
                framesCounterforMonster = 0;
                
                if (framesIndexforMonster == 5) {
                    playMonsterSound();
                }
                
                framesIndexforMonster++;
                if (framesIndexforMonster >= maxFramesMonster) {
                    monsterAppeared = false;
                    framesIndexforMonster = 5;
                }
            }
        }
    }
    
    private void playMonsterSound() {
        SoundManager sound = isSonTinh ? SoundManager.PHOENIX : SoundManager.SQUID;
        Game.soundPlayer.play(sound);
    }
    
    private void updateLightningAnimation() {
        if (!lightningAppeared) return;
        
        framesCounterforLightning++;
        if (framesCounterforLightning >= aniSpeedForLightning) {
            framesCounterforLightning = 0;
            
            if (framesIndexforLightning == 1) {
                Game.soundPlayer.play(SoundManager.LIGHTNING);
                Game.soundPlayer.play(SoundManager.BOMB);
            }
            if (framesIndexforLightning == 14) {
                Game.soundPlayer.play(SoundManager.BOMB);
            }
            
            framesIndexforLightning++;
            if (framesIndexforLightning >= maxFramesLightning) {
                lightningAppeared = false;
                framesIndexforLightning = 0;
                isActive = false;
            }
        }
    }

    public void render(Graphics g) {
        if (!isActive) return;
        
        Graphics2D g2 = (Graphics2D) g;
        if (monsterAppeared) {
            g2.drawImage(monsterAnimations[direction][framesIndexforMonster], 
                        306, -30, 484, 484, null);
        }
        if (lightningAppeared) {
            g2.drawImage(lightningAnimations[direction][framesIndexforLightning], 
                        (int)x, (int)y, 1089, 822, null);
        }
    }
    
    // Getters/Setters
    public boolean isActive() {
        return isActive;
    }
    
    public void setMonsterAppeared(boolean val) {
        monsterAppeared = val;
    }
    
    public void setX_Y(float enemyX, float enemyY) {
        this.enemyX = enemyX;
        this.enemyY = enemyY;
    }
    
    public boolean lightningAppeared() {
        return lightningAppeared;
    }
    
    public boolean setActive(boolean val) {
        return this.isActive = val;
    }
    
    public int getDirection() {
        return direction;
    }
}

