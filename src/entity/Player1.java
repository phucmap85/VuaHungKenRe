package entity;

import java.awt.Graphics;
import java.awt.Graphics2D;

import java.awt.image.BufferedImage;

import ui.HealthMana;

import static utilz.HelpMethods.*;

import static utilz.Constants.PlayerConstants.*;
import static utilz.Constants.GameConstants.*;

import utilz.LoadSave;

public class Player1 extends Entity {
    // Movement states
    private boolean left, right, jump, defense; 
    private boolean moving = false;
    private boolean inAir = false;
    private int direction = RIGHT; // Lưu hướng mặt cuối cùng
    
    // Animation
    private int playerAction = IDLE_RIGHT;
    private BufferedImage[][] animations = null;
    private int framesCounter = 0, framesIndex = 0, animationSpeed = 20;
    private int jumpAnimationSpeed = 15; // Animation nhảy chậm hơn để thấy rõ
    
    // Physics
    private float speed = 2.0f;
    private float jumpStrength = -6.5f; // 
    private float gravity = 0.1f;
    private float velocityY = 0;
    private float groundY;

    // Health And Mana
    private HealthMana healthMana;
    private int health = 100;
    private int maxHealth = 100;

    public Player1(float x, float y, float width, float height,float xOffSet,float yOffSet) {
        super(x, y, width, height, xOffSet, yOffSet);
        initHitbox();
        groundY = y;
        this.healthMana = new HealthMana(100, true);
        loadAnimation();
    }
    
    // Hàm chính: cập nhật logic
    public void update() { 
        updateAnimationTick();
        setAnimation();
        updatePos();
    }
    
    // Hàm chính: vẽ nhân vật
    public void render(Graphics g) { 
        Graphics2D g2 = (Graphics2D) g;
        g2.drawImage(animations[playerAction][framesIndex], 
                    (int) x, (int) y, 128, 128, null);
       // drawHitbox(g); de fix bug 
       // renderPlatForm(g); ham de fix bug 
        
    }

    public void renderPlatForm(Graphics g){
        Graphics2D g2 = (Graphics2D) g;
        g2.drawRect((int)platFormX1,(int)platFormY,(int)(platFormX2-platFormX1),10);
     
    }

    // Cập nhật vị trí dựa trên input
    private void updatePos() {
        moving = false;
        
        // Di chuyển ngang
        if (left && !right) {
            if(hitbox.x - speed >=0) x-=speed;
            moving = true;
            direction = LEFT; // Cập nhật hướng sang trái
        } else if (right && !left) {
            if(hitbox.x + hitbox.width + speed <= GAME_WIDTH) x+=speed;
            moving = true;
            direction = RIGHT; // Cập nhật hướng sang phải
        }
        updateHitbox();
        
        // Nhảy
        if(isInAir(y,hitbox,groundY)){
            inAir = true;
        }
        if (jump && !inAir) {
            velocityY = jumpStrength;
            inAir = true;
        }
       
        
        // Áp dụng trọng lực
        if (inAir) {
            velocityY += gravity;
            if(isOnPlatForm(hitbox, velocityY)){
                y = platFormY - yOffSet - height;
                velocityY = 0;
                inAir = false;
            }
            else y+=velocityY;
            // Kiểm tra chạm đất
            if (y >= groundY) {
                y = groundY;
                velocityY = 0;
                inAir = false;
            }
        }
        updateHitbox();
    }

    // Cập nhật frame animation
    private void updateAnimationTick() {
        framesCounter++;
        
        // Animation nhảy chậm hơn các animation khác
        int currentSpeed = (playerAction == JUMP_LEFT || playerAction == JUMP_RIGHT) 
                          ? jumpAnimationSpeed 
                          : animationSpeed;
        
        if (framesCounter >= currentSpeed) {
            framesCounter = 0;
            framesIndex++;
            
            // Reset về frame 0 khi hết animation
            if (framesIndex >= getFramesAmount(playerAction)) {
                framesIndex = 0;
            }
        }
    }

    // Xác định animation nào sẽ chạy
    private void setAnimation() {
        int startAnim = playerAction;
        
        if (inAir) {
            playerAction = (direction == LEFT) ? JUMP_LEFT : JUMP_RIGHT;
        } else if (moving) {
            playerAction = (direction == LEFT) ? MOVE_LEFT : MOVE_RIGHT;
        } else {
            playerAction = (direction == LEFT) ? IDLE_LEFT : IDLE_RIGHT;
        }
        
        // Reset frame khi đổi animation
        if (startAnim != playerAction) {
            resetAnimationTick();
        }
    }

    // Reset animation về frame đầu
    private void resetAnimationTick() {
        framesCounter = 0;
        framesIndex = 0;
    }

    // Load animation từ file
    private void loadAnimation() {
        animations = LoadSave.GetAnimation();
    }

    // Dừng tất cả khi mất focus window
    public void resetDirBooleans() {
        left = false;
        right = false;
        jump = false;
        defense = false;
    }

    // Setters
    public void setLeft(boolean left) { this.left = left; }
    public void setRight(boolean right) { this.right = right; }
    public void setJump(boolean jump) { this.jump = jump; }
    public void setDefense(boolean defense) { this.defense = defense; }
    
    // Getters
    public boolean isLeft() { return left; }
    public boolean isRight() { return right; }
    public boolean isJump() { return jump; }
    public boolean isDefense() { return defense; }
}