package entity;

import static utilz.Constants.GameConstants.GAME_WIDTH;
import static utilz.Constants.PlayerConstants.LEFT;
import static utilz.Constants.PlayerConstants.RIGHT;

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
    int animationSpeed = 20;
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
            if(framesIndex >= 2){
                framesIndex = 0;
            }
        }
    }
    public void update() {
        if(!isRender){
            if(thuytinh.tornado && thuytinh.direction == RIGHT && !thuytinh.inAir){
                x = thuytinh.x;
                y = thuytinh.y;
                speed = 3.0f;
                isRender = true;
            }
            else if(thuytinh.tornado && thuytinh.direction == LEFT && !thuytinh.inAir){
                x = thuytinh.x;
                y = thuytinh.y;
                speed = -3.0f;
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
            g2.drawImage(tornadoImage[framesIndex], (int)x, (int)y, 128,128,null);
        }
    }

}
