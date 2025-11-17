package entity;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import static utilz.HelpMethods.*;
import static utilz.Constants.PlayerConstants.*;
import static utilz.Constants.PlayerConstants.getFramesAmount;
import static utilz.Constants.EffectConstants.*;
import static utilz.LoadSave.*;

import main.Game;
import sound.SoundManager;

public class Character extends Entity {
    // Character info
    private String name;
    private int direction;
    
    // Input flags
    private boolean left, right, jump, defend, punch, summon, ulti, dash;

    // State flags
    private boolean moving, defending, jumping, punching, summoning, dashing, ulting, inAir;
    private boolean takingHit, falling;
    private boolean callSummonedEntity, callUltiEntity;
    private boolean defendDamageSignal = false;
    
    // Animation
    private int playerAction = (direction == RIGHT) ? IDLE_RIGHT : IDLE_LEFT;
    private BufferedImage[][] animations;
    private int framesCounter, framesIndex;
    private int DelayForGettingUp = 100, DelayForTakingHit = 50;
    private int normalAniSpeed = 18, punchAniSpeed = 18, summonAniSpeed = 15;

    // Physics
    private float speed = 2.0f, punchSpeed = 0.2f;
    private float jumpSpeed = -6.5f, gravity = 0.1f, velocityY = 0;
    private float gravityFalling = 0.025f, fallStrength = 1.0f;
    
    // Combat
    private int directionTakenHit, healthTakenPerCombo;
    private int healthThresholdForFalling = 100;
    private int healthDefend, healthThresholdForDefend = 200;
    private long lastPunchTime, PUNCH_RESET_TIME = 300;
    private long lastTimeFalling;

    // Dash
    private int dashCounter = 0, maxDashCount = 50;
    private float dashSpeed = 5.0f;
    private long lastTimeDash = 0, DASH_RESET_TIME = 200;
    
    // Resources
    private int mana, maxMana = 2000, summonManaCost = 400;
    private map.Map map;
    private EffectManager effectManager;

    public Character(float x, float y,
                     float x_OffSetHitBox, float y_OffSetHitBox, float widthHitBox, float heightHitBox,
                     float x_OffSetHurtBox, float y_OffSetHurtBox, float widthHurtBox, float heightHurtBox,
                     String name, int direction, map.Map map) {
        super(x, y, x_OffSetHitBox, y_OffSetHitBox, widthHitBox, heightHitBox,
              x_OffSetHurtBox, y_OffSetHurtBox, widthHurtBox, heightHurtBox);
        this.name = name;
        this.direction = direction;
        this.map = map;
        loadAnimations(name);
        effectManager = new EffectManager(5);
    }

    public void update() {
        updateFalling();
        updateTakingHit();
        updateSummon();
        updateUlti();
        updateDash();
        updatePunch();
        updateDefend();
        updateMoving();
        updateJumping();
        updatePosition();
        setAnimations();
        updateAnimationTick();
        effectManager.update();
    }

    // === UPDATE METHODS ===
    
    public void updateFalling() {
        if (!falling && takingHit && healthTakenPerCombo >= healthThresholdForFalling) {
            resetAllStates();
            falling = true;
            playSoundForCharacter(SoundManager.SONTINHFALL, SoundManager.THUYTINHFALL);
        }
    }

    public void updateTakingHit() {
        if (takingHit) {
            resetAllStates();
            takingHit = true;
            direction = (directionTakenHit == RIGHT) ? LEFT : RIGHT;
        }
        if (!takingHit) {
            healthTakenPerCombo = 0;
            setThressholdForFalling(100);
        }
    }
    
    public void updateUlti() {
        if (ulti && !ulting && !jumping && !takingHit && !falling && !inAir && mana == maxMana) {
            ulting = true;
            callUltiEntity = true;
        } else if (ulting && framesIndex == getFramesAmount(playerAction) - 1 && framesCounter >= normalAniSpeed - 1) {
            ulting = false;
        }
    }

    public void updateSummon() {
        if (summon && !summoning && !jumping && !takingHit && !falling && !inAir && mana >= summonManaCost) {
            resetAllStates();
            summoning = true;
        } else if (summoning && framesIndex == getFramesAmount(playerAction) - 2 && framesCounter == 0) {
            callSummonedEntity = true;
        } else if (summoning && framesIndex == getFramesAmount(playerAction) - 1 && framesCounter >= summonAniSpeed - 1) {
            summoning = false;
        }
    }

    public void updateDash() {
        if (dash && !dashing && !takingHit && !falling && System.currentTimeMillis() - lastTimeDash >= DASH_RESET_TIME) {
            dashing = true;
            punching = false;
            dashCounter = 0;
            int effectType = (direction == RIGHT) ? SMOKE_RIGHT : SMOKE_LEFT;
            int effectX = (direction == RIGHT) ? (int)(x - 100) : (int)(x + 100);
            effectManager.addEffect(effectX, y, effectType);
            playSoundForCharacter(SoundManager.SONTINHDASH, SoundManager.THUYTINHDASH);
        }
        if (dashing) {
            dashCounter++;
            if (dashCounter >= maxDashCount) {
                dashing = false;
                lastTimeDash = System.currentTimeMillis();
            }
        }
    }
    
    public void updateDefend() {
        if (defend && !defending && !jumping && !summoning && !takingHit && !falling) {
            defending = true;
            playSoundForCharacter(SoundManager.SONTINHBLOCK, SoundManager.THUYTINHBLOCK);
        } else if (!defend || (defending && healthDefend >= healthThresholdForDefend)) {
            defending = false;
            healthDefend = 0;
            defendDamageSignal = false;
        }
    }

    public void updatePunch() {
        if (punch && !jumping && !summoning && !takingHit && !falling && !inAir) {
            resetAllStates();
            punching = true;
        }
        if (punching && !punch && System.currentTimeMillis() - lastPunchTime >= PUNCH_RESET_TIME) {
            punching = false;
        }
    }

    public void updateAttackBox() {
        if (punching && punch) {
            x_OffSetHitBox = (direction == RIGHT) ? 65f : 30f;
            updateHitBox();
        }
    }

    public void updateJumping() {
        if (jump && !jumping && !takingHit && !summoning && !falling) {
            jumping = true;
            playSoundForCharacter(SoundManager.SONTINHJUMP, SoundManager.THUYTINHJUMP);
        }
    }

    public void updateMoving() {
        moving = false;
        if (!takingHit && !summoning && !falling) {
            if (left && !right) {
                direction = LEFT;
                moving = true;
            } else if (right && !left) {
                direction = RIGHT;
                moving = true;
            }
        }
    }

    public void updatePosition() {
        updateHorizontalMovement();
        updateBoxes();
        updateVerticalMovement();
        updateBoxes();
    }
    
    private void updateHorizontalMovement() {
        if (falling) {
            if (framesIndex <= 5) {
                float moveSpeed = 10 * punchSpeed;
                moveHorizontally(directionTakenHit, moveSpeed);
                y -= fallStrength;
            }
        } else if (takingHit) {
            moveHorizontally(directionTakenHit, punchSpeed);
        } else if (ulting) {
            // No movement
        } else if (punching) {
            moveHorizontally(direction, punchSpeed);
        } else if (dashing) {
            moveHorizontally(direction, dashSpeed);
        } else if (defending) {
            if (defendDamageSignal) {
                moveHorizontally(directionTakenHit, punchSpeed);
                defendDamageSignal = false;
            }
        } else if (moving) {
            moveHorizontally(direction, speed);
        }
    }
    
    private void moveHorizontally(int dir, float moveSpeed) {
        float delta = (dir == RIGHT) ? moveSpeed : -moveSpeed;
        if (canMoveHere(hurtBox, delta)) {
            x += delta;
        }
    }
    
    private void updateVerticalMovement() {
        float groundY = map.getGroundY();
        var platforms = map.getPlatforms();

        if ((jumping || isInAir(hurtBox, groundY, platforms)) && !inAir) {
            if (jump && !takingHit && !falling) {
                velocityY = jumpSpeed;
            }
            inAir = true;
        }

        if (inAir) {
            velocityY += falling ? gravityFalling : gravity;

            if (willHitPlatform(hurtBox, velocityY, platforms)) {
                handlePlatformLanding(platforms);
            } else if (willHitGround(hurtBox, velocityY, groundY)) {
                handleGroundLanding(groundY);
            } else {
                y += velocityY;
            }
        }
    }
    
    private void handlePlatformLanding(java.util.List<map.Platform> platforms) {
        for (map.Platform p : platforms) {
            float boxBottom = hurtBox.y + hurtBox.height;
            boolean withinX = (hurtBox.x + hurtBox.width >= p.x1) && (hurtBox.x <= p.x2);
            boolean movingDown = (velocityY > 0);
            boolean above = (boxBottom <= p.y);
            boolean willCross = (boxBottom + velocityY >= p.y);
            
            if (withinX && movingDown && above && willCross) {
                y = p.y - hurtBox.height - y_OffSetHurtBox;
                velocityY = 0;
                inAir = false;
                jumping = false;
                playLandingEffectAndSound();
                break;
            }
        }
    }
    
    private void handleGroundLanding(float groundY) {
        y = groundY - hurtBox.height - y_OffSetHurtBox;
        velocityY = 0;
        inAir = false;
        jumping = false;
        playLandingEffectAndSound();
    }
    
    private void playLandingEffectAndSound() {
        effectManager.addEffect(x, y + 50, LANDING_RIGHT);
        playSoundForCharacter(SoundManager.SONTINHLANDING, SoundManager.THUYTINHLANDING);
    }
    
    // === HELPER METHODS ===
    
    private void playSoundForCharacter(SoundManager sonTinhSound, SoundManager thuyTinhSound) {
        Game.soundPlayer.play("SonTinh".equals(name) ? sonTinhSound : thuyTinhSound);
    }

    public void setMap(map.Map map) {
        this.map = map;
    }
    
    // === ANIMATION METHODS ===
    public void setAnimations() {
        int startAnim = playerAction;

        if (falling) {
            playerAction = (directionTakenHit == LEFT) ? FALLING_RIGHT : FALLING_LEFT;
        } else if (takingHit) {
            playerAction = (directionTakenHit == LEFT) ? TAKING_HIT_RIGHT : TAKING_HIT_LEFT;
        } else if (summoning) {
            playerAction = (direction == RIGHT) ? SUMMONSKILL_RIGHT : SUMMONSKILL_LEFT;
        } else if(ulting){
            playerAction = (direction == RIGHT) ? SUMMONULTI_RIGHT : SUMMONULTI_LEFT;
        }
        else if(dashing){
            playerAction = (direction == RIGHT) ? DASH_RIGHT : DASH_LEFT;
        }
        else if (punching) {
            playerAction = (direction == RIGHT) ? PUNCH_RIGHT : PUNCH_LEFT;
        } else if (jumping) {
            playerAction = (direction == RIGHT) ? JUMP_RIGHT : JUMP_LEFT;
        } else if (defending) {
            playerAction = (direction == RIGHT) ? DEFEND_RIGHT : DEFEND_LEFT;
        } else if (moving) {
            playerAction = (direction == RIGHT) ? MOVE_RIGHT : MOVE_LEFT;
        } else {
            playerAction = (direction == RIGHT) ? IDLE_RIGHT : IDLE_LEFT;
        }

        if (startAnim != playerAction) {
            resetAnimationTick();
        }
    }

    public void updateAnimationTick() {
        int currentSpeed = normalAniSpeed;
        framesCounter++;

        if (isFallingAnimation()) {
            updateFallingAnimation(currentSpeed);
        } else if (isTakingHitAnimation()) {
            updateTakingHitAnimation(currentSpeed);
        } else if (isDefendAnimation()) {
            updateDefendAnimation(currentSpeed);
        } else {
            updateNormalAnimation();
        }
    }
    
    private boolean isFallingAnimation() {
        return playerAction == FALLING_LEFT || playerAction == FALLING_RIGHT;
    }
    
    private boolean isTakingHitAnimation() {
        return playerAction == TAKING_HIT_LEFT || playerAction == TAKING_HIT_RIGHT;
    }
    
    private boolean isDefendAnimation() {
        return playerAction == DEFEND_LEFT || playerAction == DEFEND_RIGHT;
    }
    
    private void updateFallingAnimation(int currentSpeed) {
        if (framesIndex < 2) {
            framesIndex = 2;
        } else if (framesIndex != 6 && framesCounter >= currentSpeed) {
            framesCounter = 0;
            framesIndex++;
            if (framesIndex >= getFramesAmount(playerAction)) {
                framesIndex = 0;
                falling = false;
                lastTimeFalling = System.currentTimeMillis();
            }
        } else if (framesCounter >= DelayForGettingUp) {
            framesCounter = 0;
            framesIndex++;
        }
    }
    
    private void updateTakingHitAnimation(int currentSpeed) {
        if (framesIndex < 2 && framesCounter >= currentSpeed) {
            framesCounter = 0;
            framesIndex++;
        } else if (framesCounter >= DelayForTakingHit) {
            resetAnimationTick();
            takingHit = false;
        }
    }
    
    private void updateDefendAnimation(int currentSpeed) {
        if (framesCounter >= currentSpeed) {
            framesCounter = 0;
            framesIndex++;
            if (framesIndex >= getFramesAmount(playerAction)) {
                framesIndex = getFramesAmount(playerAction) - 1;
            }
        }
    }
    
    private void updateNormalAnimation() {
        int currentSpeed = (playerAction == PUNCH_LEFT || playerAction == PUNCH_RIGHT) ? punchAniSpeed :
                          (playerAction == SUMMONSKILL_LEFT || playerAction == SUMMONSKILL_RIGHT) ? summonAniSpeed :
                          normalAniSpeed;
        
        if (framesCounter >= currentSpeed) {
            framesCounter = 0;
            framesIndex++;
            
            playAnimationSounds();
            
            if (framesIndex >= getFramesAmount(playerAction)) {
                framesIndex = 0;
            }
        }
    }
    
    private void playAnimationSounds() {
        boolean isPunch = (playerAction == PUNCH_LEFT || playerAction == PUNCH_RIGHT);
        boolean isMove = (playerAction == MOVE_LEFT || playerAction == MOVE_RIGHT);
        
        if (isPunch && framesIndex == 2) {
            playSoundForCharacter(SoundManager.SONTINHPUNCH1, SoundManager.THUYTINHPUNCH1);
        } else if (isPunch && framesIndex == 12) {
            playSoundForCharacter(SoundManager.SONTINHPUNCH2, SoundManager.THUYTINHPUNCH2);
        } else if (isMove && (framesIndex == 1 || framesIndex == 3)) {
            playSoundForCharacter(SoundManager.SONTINHMOVING, SoundManager.THUYTINHMOVING);
        }
    }

    public void render(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        int offsetY = 0;
        
        if ((playerAction == FALLING_LEFT || playerAction == FALLING_RIGHT) && framesIndex <= 6) {
            offsetY = ("SonTinh".equals(name) ? 5 : 2) * framesIndex;
        }
        
        g2.drawImage(animations[playerAction][framesIndex], (int)x, (int)y + offsetY, 128, 128, null);
        effectManager.draw(g);
    }

    public void loadAnimations(String characterName) {
        this.animations = "SonTinh".equals(characterName) ? getSonTinhAnimations() : getThuyTinhAnimations();
    }

    public void resetAnimationTick() {
        framesCounter = 0;
        framesIndex = 0;
    }
    
    // === SETTERS ===
    public void setLeft(boolean left) {
        this.left = left;
    }

    public void setRight(boolean right) {
        this.right = right;
    }

    public void setJump(boolean jump) {
        this.jump = jump;
    }

    public void setDefend(boolean defend) {
        this.defend = defend;
    }

    public void setSummon(boolean summon) {
        this.summon = summon;
    }

    public void setTakingHit(boolean takingHit) {
        this.takingHit = takingHit;
        if (this.takingHit && framesIndex == 2) {
            framesCounter = 0;
        }
    }
    
    public void setPunch(boolean punch) {
        this.punch = punch;
        lastPunchTime = System.currentTimeMillis();
    }

    public void setDash(boolean dash) {
        this.dash = dash;
    }

    public void setUlti(boolean ulti) {
        this.ulti = ulti;
    }

    
    public void setDirectionTakenHit(int directionEnemy) {
        this.directionTakenHit = directionEnemy;
    }

    public void setHealthTakenPerCombo(int damage) {
        this.healthTakenPerCombo += damage;
    }

    public void setHealthDefend(int damage) {
        this.healthDefend += damage;
    }

    public void setDefendDamageSignal(boolean signal) {
        this.defendDamageSignal = signal;
    }

    public void setCallSummonedEntity(boolean callSummonedEntity) {
        this.callSummonedEntity = callSummonedEntity;
    }
    
    public void setCallUltiEntity(boolean callUltiEntity) {
        this.callUltiEntity = callUltiEntity;
    }
    
    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
        updateBoxes();
    }
    
    public void setMana(int mana) {
        this.mana = mana;
    }
    
    public void setThressholdForFalling(int threshold) {
        this.healthThresholdForFalling = threshold;
    }
    
    public void setFalling(boolean falling) {
        this.falling = falling;
    }
    
    public void setDirection(int direction) {
        this.direction = direction;
    }
    
    // === GETTERS ===
    
    public void resetAllBools() {
        left = false;
        right = false;
        jump = false;
        defend = false;
        punch = false;
        summon = false;
        takingHit = false;
        falling = false;
    }

    public void resetAllStates() {
        moving = false;
        defending = false;
        jumping = false;
        punching = false;
        summoning = false;
        takingHit = false;
        falling = false;
        dashing = false;
        ulting = false;
    }
    
    public boolean isLeft() {
        return left;
    }

    public boolean isRight() {
        return right;
    }

    public boolean isJump() {
        return jump;
    }

    public boolean isDefend() {
        return defend;
    }

    public boolean isPunch() {
        return punch;
    }

    public boolean isSummon() {
        return summon;
    }

    public boolean moving() {
        return moving;
    }

    public boolean jumping() {
        return jumping;
    }

    public boolean punching() {
        return punching;
    }

    public boolean punch() {
        return punch;
    }
    
    public boolean summoning() {
        return summoning;
    }

    public boolean takingHit() {
        return takingHit;
    }

    public boolean falling() {
        return falling;
    }

    public boolean defending() {
        return defending;
    }
    
    public boolean ulting() {
        return ulting;
    }

    public boolean dashing() {
        return dashing;
    }

    public boolean callSummonedEntity() {
        return callSummonedEntity;
    }

    public boolean callUltiEntity() {
        return callUltiEntity;
    }

    public int getDirection() {
        return direction;
    }

    public String getCharacterName() {
        return name;
    }

    public long getLastTimeFalling() {
        return lastTimeFalling;
    }

    public String getName() {
        return name;
    }
}

