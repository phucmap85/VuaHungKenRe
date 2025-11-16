package entity;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import javax.sound.sampled.Clip;
import sound.SoundManager;

import static utilz.Constants.GameConstants.GAME_WIDTH;
import static utilz.Constants.PlayerConstants.*;
import static utilz.Constants.EffectConstants.*;
import static utilz.LoadSave.getAvailableSfxClip;
import static utilz.LoadSave.loadSummonedEntityAnimation;

public class SummonSkill extends Entity {
    private EffectManager effectManager;
    private final float speed = 3.0f;
    private int direction;
    private boolean isActive = false;
    private boolean collision = false;
    private boolean doneTakeHealth = false;
    private BufferedImage[][] image;
    private String characterName;
    
    // Animation
    private int framesCounter, framesIndex;
    private int aniSpeed = 20, maxFrames, startFrames = 0;
    private int aniSpeedDisappearTornado = 15, maxFramesDisappearTornado = 17;
    
    // Sound
    private Clip summonClip;
    private boolean soundPlaying = false;
    
    // Effect update
    private int updateCounter = 0, updateSpeed = 50;
    
    private boolean isSonTinh;

    public SummonSkill(float x, float y,
                       float x_OffSetHitBox, float y_OffSetHitBox, float widthHitBox, float heightHitBox,
                       int direction, String characterName) {
        super(x, y, x_OffSetHitBox, y_OffSetHitBox, widthHitBox, heightHitBox);
        this.direction = direction;
        this.characterName = characterName;
        this.isActive = true;
        this.isSonTinh = "SonTinh".equals(characterName);
        
        loadCharacterAnimations();
        initializeEffects();
        playSummonSound();
    }
    
    private void loadCharacterAnimations() {
        image = loadSummonedEntityAnimation(characterName);
        maxFrames = image[0].length;
        
        if (!isSonTinh) {
            startFrames = 9;
            framesIndex = startFrames;
            maxFrames = 11;
        }
    }
    
    private void initializeEffects() {
        if (isSonTinh) {
            effectManager = new EffectManager(10);
            int effectX = (direction == RIGHT) ? (int)(x - 100) : (int)(x + 110);
            int effectType = (direction == RIGHT) ? SMOKE_RIGHT : SMOKE_LEFT;
            effectManager.addEffect(effectX, y, effectType);
        }
    }

    public void update() {
        if (isActive) {
            updateMovement();
            updateHitBox();
            updateAnimationTick();
            updateEffectsIfNeeded();
        } else {
            stopSummonSound();
        }
    }
    
    private void updateMovement() {
        if (!isSonTinh && collision) return;
        
        if (direction == RIGHT) {
            if (hitBox.x + speed > GAME_WIDTH) {
                isActive = false;
                stopSummonSound();
            } else {
                x += speed;
            }
        } else {
            if (hitBox.x + hitBox.width - speed < 0) {
                isActive = false;
                stopSummonSound();
            } else {
                x -= speed;
            }
        }
    }
    
    private void updateEffectsIfNeeded() {
        if (!isSonTinh) return;
        
        updateCounter++;
        if (updateCounter >= updateSpeed) {
            updateCounter = 0;
            int effectX = (direction == RIGHT) ? (int)(x - 100) : (int)(x + 110);
            int effectType = (direction == RIGHT) ? SMOKE_RIGHT : SMOKE_LEFT;
            effectManager.addEffect(effectX, y, effectType);
        }
        effectManager.update();
    }

    public void updateAnimationTick() {
        if (isSonTinh || !collision) {
            updateNormalAnimation();
        } else {
            updateCollisionAnimation();
        }
    }
    
    private void updateNormalAnimation() {
        framesCounter++;
        if (framesCounter >= aniSpeed) {
            framesCounter = 0;
            framesIndex++;
            if (framesIndex >= maxFrames) {
                framesIndex = startFrames;
            }
        }
    }
    
    private void updateCollisionAnimation() {
        framesCounter++;
        if (framesIndex < maxFrames) framesIndex = maxFrames;
        aniSpeed = aniSpeedDisappearTornado;
        
        if (framesCounter >= aniSpeed) {
            framesCounter = 0;
            framesIndex++;
            if (framesIndex >= maxFramesDisappearTornado) {
                isActive = false;
                collision = false;
                stopSummonSound();
            }
        }
    }
    
    public void render(Graphics g) {
        if (isActive) {
            Graphics2D g2 = (Graphics2D) g;
            if (!isSonTinh) {
                g2.drawImage(image[direction][framesIndex], (int)x, (int)y - 20, 150, 150, null);
            } else {
                g2.drawImage(image[direction][framesIndex], (int)x, (int)y, 128, 128, null);
            }
        }
        if (effectManager != null) {
            effectManager.draw(g);
        }
    }
    
    // Sound management
    private void playSummonSound() {
        if (soundPlaying) return;
        
        SoundManager sound = isSonTinh ? SoundManager.SONTINHSUMMON : SoundManager.THUYTINHSUMMON;
        summonClip = getAvailableSfxClip(sound.getIndex());
        
        if (summonClip != null) {
            summonClip.setFramePosition(0);
            summonClip.start();
            soundPlaying = true;
        }
    }
    
    private void stopSummonSound() {
        if (soundPlaying && summonClip != null && summonClip.isRunning()) {
            summonClip.stop();
            summonClip.setFramePosition(0);
            soundPlaying = false;
        }
    }
    
    // Getters/Setters
    public boolean isActive() {
        return isActive;
    }
    
    public int getDirection() {
        return direction;
    }
    
    public boolean setCollision(boolean collision) {
        framesCounter = 0;
        return this.collision = collision;
    }
    
    public boolean getCollision() {
        return collision;
    }
    
    public boolean getdoneTakeHealth() {
        return doneTakeHealth;
    }
    
    public void setDoneTakeHealth(boolean val) {
        this.doneTakeHealth = val;
    }
}

