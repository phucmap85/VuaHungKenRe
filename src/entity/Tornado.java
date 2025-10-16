package entity;

import static utilz.Constants.GameConstants.GAME_WIDTH;
import static utilz.Constants.PlayerConstants.LEFT;
import static utilz.Constants.PlayerConstants.RIGHT;
import static utilz.Constants.PlayerConstants.TORNADO;
import static utilz.Constants.PlayerConstants.getFramesAmount;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class Tornado {
    Player1 thuytinh;
    float x,y;
    float speed;
    BufferedImage tornadoImage[];
    int framesIndex = 0;
    int framesCounter = 0;
    int animationSpeed = 10;
    public Tornado(Player1 thuytinh) {
        this.thuytinh = thuytinh;
        initClasses();
    }
    private boolean isRender = false;

    void initClasses(){
        tornadoImage = new BufferedImage[2];
        tornadoImage[0] = thuytinh.animations[12][0];
        tornadoImage[1] = thuytinh.animations[12][1];
    }
    void updateAnimationTick(){
        framesCounter++;
        if(framesCounter >= animationSpeed){
            framesCounter = 0;
            framesIndex++;
            if(framesIndex >= getFramesAmount(TORNADO)){
                framesIndex = 0;
            }
        }
    }
    public void update() {
        if(!isRender){
            if(thuytinh.tornadoing && thuytinh.tornadoFrameIndex == thuytinh.MAX_TORNADO_FRAMES-1 && thuytinh.direction == RIGHT && !thuytinh.inAir){
                x = thuytinh.x + thuytinh.xOffSet + thuytinh.width;
                y = thuytinh.y;
                speed = 3.5f;
                isRender = true;
            }
            else if(thuytinh.tornadoing && thuytinh.tornadoFrameIndex == thuytinh.MAX_TORNADO_FRAMES-1 && thuytinh.direction == LEFT && !thuytinh.inAir){
                x = thuytinh.x- 120 + thuytinh.xOffSet; // 120 để chiêu phát ra sát người
                y = thuytinh.y;
                speed = -3.5f;
                isRender = true;
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
            g2.drawImage(thuytinh.animations[TORNADO][framesIndex], (int)x, (int)y, 128,128,null);
        }
    }

}
