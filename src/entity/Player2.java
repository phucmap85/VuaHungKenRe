package entity;

import java.awt.Graphics;
import java.awt.Graphics2D;

import java.awt.image.BufferedImage;
import static utilz.Constants.PlayerConstants.*;
import ui.HealthMana;

import utilz.LoadSave;

public class Player2 extends Entity {
    // Movement states
    private boolean left, right, jump, defense;
    private boolean moving = false;
    private boolean inAir = false;
    private int direction = LEFT; // Lưu hướng mặt cuối cùng

    //health
    private HealthMana healthMana;
    private int health = 100;
    private int maxHealth = 100;

    // Animation
    private int playerAction = IDLE_LEFT;
    private BufferedImage[][] animations = null;
    private int framesCounter = 0, framesIndex = 0, animationSpeed = 15;
    private int jumpAnimationSpeed = 15; // Animation nhảy chậm hơn để thấy rõ

    // Physics
    private float speed = 2.0f;
    private float jumpStrength = -6.0f; //
    private float gravity = 0.1f;
    private float velocityY = 0;
    private float groundY; // Vị trí mặt đất

    public Player2(float x, float y, float width, float height) {
        super(x, y, width, height);
        this.groundY = y;
        this.healthMana = new HealthMana(100, false);
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
    }

    // Cập nhật vị trí dựa trên input
    private void updatePos() {
        moving = false;

        // Di chuyển ngang
        if (left && !right) {
            x -= speed;
            moving = true;
            direction = LEFT; // Cập nhật hướng sang trái
        } else if (right && !left) {
            x += speed;
            moving = true;
            direction = RIGHT; // Cập nhật hướng sang phải
        }

        // Nhảy
        if (jump && !inAir) {
            velocityY = jumpStrength;
            inAir = true;
        }

        // Áp dụng trọng lực
        if (inAir) {
            velocityY += gravity;
            y += velocityY;

            // Kiểm tra chạm đất
            if (y >= groundY) {
                y = groundY;
                velocityY = 0;
                inAir = false;
            }
        }
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

    public int getHealth() {
        return health;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    // Gây sát thương
    public void takeDamage(int dmg) {
        health -= dmg;
        if (health < 0) health = 0;
    }

    // Hồi máu
    public void heal(int amount) {
        health += amount;
        if (health > maxHealth) health = maxHealth;
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