package entity;

import static utilz.Constants.GameConstants.GAME_WIDTH;
import static utilz.Constants.PlayerConstants.*;

import java.awt.Graphics;
import java.awt.Graphics2D;


public class Hog {
    Player2 sontinh;
    float x,y;
    float speed;
    int ani;
    int framesIndex = 0;
    int framesCounter = 0;
    int animationSpeed = 20;
    public Hog(Player2 sontinh) {
        this.sontinh = sontinh;

    }
    private boolean isRender = false;


    void updateAnimationTick(){
        framesCounter++;
        if(framesCounter >= animationSpeed){
            framesCounter = 0;
            framesIndex++;
            if(framesIndex >= 6){
                framesIndex = 0;
            }
        }
    }
 public void update() {
    if (!isRender) {
        // Kiểm tra điều kiện tung chiêu khi SƠN TINH quay mặt sang PHẢI
        if (sontinh.tornadoing && sontinh.tornadoFrameIndex == sontinh.MAX_TORNADO_FRAMES - 1 && sontinh.direction == RIGHT && !sontinh.inAir) {
            x = sontinh.x + sontinh.xOffSet + sontinh.width;
            y = sontinh.y;
            speed = 3.5f;
            isRender = true;
            ani = HOG_RIGHT;
        }
        // Kiểm tra điều kiện tung chiêu khi SƠN TINH quay mặt sang TRÁI
        else if (sontinh.tornadoing && sontinh.tornadoFrameIndex == sontinh.MAX_TORNADO_FRAMES - 1 && sontinh.direction == LEFT && !sontinh.inAir) {
            x = sontinh.x - 120 + sontinh.xOffSet; // 120 để chiêu phát ra sát người
            y = sontinh.y;
            speed = -3.5f;
            isRender = true;
            ani = HOG_LEFT;
        }
    }
     else if (isRender){
            if(x + speed < -128 || x + speed > GAME_WIDTH){
                isRender = false;
            }
            else{
                x += speed;
            }
        }
}
        


    

    public void render(Graphics g){
        Graphics2D g2 = (Graphics2D) g;
        if(isRender){
            updateAnimationTick();
            g2.drawImage(sontinh.animations[ani][framesIndex], (int)x, (int)y, 128,128,null);
        }
    }
}
