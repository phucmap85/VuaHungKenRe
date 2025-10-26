package entity;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import static utilz.HelpMethods.*;
import static utilz.Constants.PlayerConstants.*;
import static utilz.Constants.GameConstants.*;
import static utilz.LoadSave.*;

public class Character extends Entity {
    private String name;
    private int direction;
    
    // Get from key input
    private boolean left, right, jump, defend, punch, summon;

    // States of character
    private boolean moving, defending, jumping, punching, summoning, inAir; // Internal states
    private boolean takingHit, falling; // States from outside

    // Signals calling skills
    private boolean callSummonedEntity; // Hog and tornado
    private boolean defendDamageSignal = false;
    
    // Animations
    private int playerAction = (direction == RIGHT) ? IDLE_RIGHT : IDLE_LEFT;
    private BufferedImage[][] animations;
    private int framesCounter, framesIndex;
    private int DelayForGettingUp = 100, DelayForTakingHit = 50;
    private int normalAniSpeed = 20, punchAniSpeed = 22, summonAniSpeed = 25;

    // Physics
    private float speed = 2.0f, punchSpeed = 0.2f;
    private float jumpSpeed = -6.5f, gravity = 0.1f, velocityY = 0;
    private float velocityX = 0, punchStrength = 1.5f, gravityX = -0.3f;
    
    // Timings
    private long lastPunchTime, PUNCH_RESET_TIME = 300;

    // Combat
    private int directionTakenHit, healthTakenPerCombo;
    private int healthThresholdForFalling = 250;
    private boolean vulnerable = true;
    private int healthDefend, healthThresholdForDefend = 200;

    // Update
    private int updateCounter = 0, updateSpeed = 5;

    private int mana;

    public Character(float x, float y,
                     float x_OffSetHitBox, float y_OffSetHitBox, float widthHitBox, float heightHitBox,
                     float x_OffSetHurtBox, float y_OffSetHurtBox, float widthHurtBox, float heightHurtBox,
                     String name, int direction) {
        super(x, y, x_OffSetHitBox, y_OffSetHitBox, widthHitBox, heightHitBox,
              x_OffSetHurtBox, y_OffSetHurtBox, widthHurtBox, heightHurtBox);
        this.name = name;
        this.direction = direction;
        loadAnimations(name);
    }

    public void update() {
        updateFalling();
        updateTakingHit();
        updateSummon();
        updatePunch();
        updateDefend();
        updateMoving();
        updateJumping();
        updatePosition();
        setAnimations();
        updateAnimationTick();
    }

    public void updateFalling() {
        if (!falling && takingHit && healthTakenPerCombo >= healthThresholdForFalling) {
            resetAllStates();
            falling = true;

        }
    }

    public void updateTakingHit() {
        if (takingHit) {
            resetAllStates();
            takingHit = true;
            direction = (directionTakenHit == RIGHT) ? LEFT : RIGHT;
        }
        if(!takingHit) {
            healthTakenPerCombo = 0;
        }
    }

    public void updateSummon() {
        if (summon && !summoning && !jumping && !takingHit && !falling && mana >= 25) {
            resetAllStates();
            summoning = true;
        } else if (summoning == true && framesIndex == getFramesAmount(playerAction) - 1
                && framesCounter == normalAniSpeed - 1) {
            callSummonedEntity = true;
            summoning = false;
        }
    }

    public void updateDefend() {
        if (defend && !defending && !jumping && !summoning && !takingHit && !falling) {
            defending = true;
        } else if (!defend || (defending && healthDefend >= healthThresholdForDefend)) {
            defending = false;
            healthDefend = 0;
            defendDamageSignal = false;
        }
    }

    public void updatePunch() {
        if (punching && !punch && System.currentTimeMillis() - lastPunchTime >= PUNCH_RESET_TIME) {
            punching = false;
        }
    }

    public void updateAttackBox() {
        if (punching && punch) {
            x_OffSetHitBox = (direction == RIGHT) ? 80 : 15f;
            updateHitBox();
        }
    }

    public void updateJumping() {
        if (jump && !jumping && !takingHit && !summoning && !falling) {
            jumping = true;
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
        // Handle horizontal movement based on character state
        if (falling) {
            if (framesIndex <= 5) {
                if (directionTakenHit == RIGHT) {
                    if (canMoveHere(hurtBox, punchSpeed)) {
                        x += punchSpeed;
                    }
                } else {
                    if (canMoveHere(hurtBox, -punchSpeed)) {
                        x -= punchSpeed;
                    }
                }
            }
        } else if (takingHit) {
            if (directionTakenHit == RIGHT) {
                if (canMoveHere(hurtBox, punchSpeed/2)) {
                    x += punchSpeed/2;
                }
            } else {
                if (canMoveHere(hurtBox, -punchSpeed/2)) {
                    x -= punchSpeed/2;
                }
            }
        } else if (summoning) {
            return;
        } else if (punching) {
            if (direction == RIGHT) {
                if (canMoveHere(hurtBox, punchSpeed)) {
                    x += punchSpeed;
                }
            } else {
                if (canMoveHere(hurtBox, -punchSpeed)) {
                    x -= punchSpeed;
                }
            }
        } else if (defending) {
            if (defendDamageSignal) {
                if (directionTakenHit == RIGHT) {
                    if (canMoveHere(hurtBox, punchSpeed)) {
                        x += punchSpeed;
                    }
                } else {
                    if (canMoveHere(hurtBox, -punchSpeed)) {
                        x -= punchSpeed;
                    }
                }
                defendDamageSignal = false;
            }
        } else {
            if (moving) {
                if (direction == RIGHT) {
                    if (canMoveHere(hurtBox, speed)) {
                        x += speed;
                    }
                } else {
                    if (canMoveHere(hurtBox, -speed)) {
                        x -= speed;
                    }
                }
            }
        }

        updateBoxes();

        // Handle vertical movement and jumping
        if ((jumping || isInAir(hurtBox)) && !inAir) {
            if (jump) {
                velocityY = jumpSpeed;
            }
            inAir = true;
        }

        if (inAir) {
            velocityY += gravity;

            if (willHitPlatForm(hurtBox, velocityY)) {
                y = platFormY - hurtBox.height - y_OffSetHurtBox;
                velocityY = 0;
                inAir = false;
                jumping = false;
            } else {
                y += velocityY;
            }

            if (willHitGround(hurtBox, velocityY)) {
                y = groundY - hurtBox.height - y_OffSetHurtBox;
                velocityY = 0;
                inAir = false;
                jumping = false;
            }
        }

        updateBoxes();
    }

    public void setAnimations() {
        int startAnim = playerAction;

        if (falling) {
            playerAction = (directionTakenHit == LEFT) ? FALLING_RIGHT : FALLING_LEFT;
        } else if (takingHit) {
            playerAction = (directionTakenHit == LEFT) ? TAKING_HIT_RIGHT : TAKING_HIT_LEFT;
        } else if (summoning) {
            playerAction = (direction == RIGHT) ? SUMMONSKILL_RIGHT : SUMMONSKILL_LEFT;
        } else if (punching) {
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

        if (playerAction == FALLING_LEFT || playerAction == FALLING_RIGHT) {
            if (framesIndex < 2) {
                framesIndex = 2; // Bắt đầu từ frame thứ 3
            } else if (framesIndex != 6 && framesCounter >= currentSpeed) {
                framesCounter = 0;
                framesIndex++;
                if (framesIndex >= getFramesAmount(playerAction)) {
                    framesIndex = 0;
                    falling = false;
                }
            } else {
                if (framesCounter >= DelayForGettingUp) {
                    framesCounter = 0;
                    framesIndex++;
                }
            }
        } else if (playerAction == TAKING_HIT_LEFT || playerAction == TAKING_HIT_RIGHT) {
            if (framesIndex < 2 && framesCounter >= currentSpeed) { // 2 is last frame of taking hit
                framesCounter = 0;
                framesIndex++;
            } else {
                if (framesCounter >= DelayForTakingHit) {
                    resetAnimationTick();
                    takingHit = false; // Done taking hit
                }
            }
        } else if (playerAction == DEFEND_LEFT || playerAction == DEFEND_RIGHT) {
            if (framesCounter >= currentSpeed) {
                framesCounter = 0;
                framesIndex++;
                if (framesIndex >= getFramesAmount(playerAction)) {
                    framesIndex = getFramesAmount(playerAction) - 1; // Giữ khung cuối cùng
                }
            }
        } else {
            if (playerAction == PUNCH_LEFT || playerAction == PUNCH_RIGHT) {
                currentSpeed = punchAniSpeed;
            }
            else if(playerAction == SUMMONSKILL_LEFT || playerAction == SUMMONSKILL_RIGHT){
                currentSpeed = summonAniSpeed;
            }
            if (framesCounter >= currentSpeed) {
                framesCounter = 0;
                framesIndex++;
                if (framesIndex >= getFramesAmount(playerAction)) {
                    framesIndex = 0;
                }
            }
        }
    }

    public void render(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        if(playerAction == FALLING_LEFT || playerAction == FALLING_RIGHT) {
            if(framesIndex <= 6){
                int a;
                if(name == "SonTinh"){
                     a = 5;
                }
                else a = 2;

                g2.drawImage(animations[playerAction][framesIndex],(int) x, (int) y + a*framesIndex, 128, 128, null);
                drawBoxes(g);
                return;
            }
        }
        g2.drawImage(animations[playerAction][framesIndex], (int) x, (int) y, 128, 128, null);
        drawBoxes(g);
    }

    public void loadAnimations(String name) {
        this.animations = getAnimations(name);
    }

    public void resetAnimationTick() {
        framesCounter = 0;
        framesIndex = 0;
    }

    // Getter and setters for key inputs
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
        if(this.takingHit && takingHit && framesIndex == 2){
            framesCounter = 0;
        }
    }
    public void setPunch(boolean punch) {
        this.punch = punch;
        if (punch && !jumping && !summoning && !takingHit && !falling) {
            resetAllStates();
            punching = true;
            lastPunchTime = System.currentTimeMillis();
        }
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

    public boolean callSummonedEntity() {
        return callSummonedEntity;
    }

    public int getDirection() {
        return direction;
    }

    public String getCharacterName() {
        return name;
    }

    public void setMana(int mana){
        this.mana = mana;
    }
}