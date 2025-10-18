package entity;

import static utilz.Constants.GameConstants.GAME_WIDTH;
import static utilz.Constants.PlayerConstants.LEFT;
import static utilz.Constants.PlayerConstants.RIGHT;
import static utilz.Constants.PlayerConstants.TORNADO;
import static utilz.Constants.PlayerConstants.getFramesAmount;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

public class Tornado {
    Player1 thuytinh;
    float x,y;
    private Rectangle2D.Float attackBox;
    float speed;
    BufferedImage tornadoImage[];
    int framesIndex = 0;
    int framesCounter = 0;
    int animationSpeed = 10;
    public Tornado(Player1 thuytinh) {
        this.thuytinh = thuytinh;
        initClasses();
        this.attackBox = new Rectangle2D.Float(x, y, 100, 128);
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
                updateAttackBox();
            }
            else if(thuytinh.tornadoing && thuytinh.tornadoFrameIndex == thuytinh.MAX_TORNADO_FRAMES-1 && thuytinh.direction == LEFT && !thuytinh.inAir){
                x = thuytinh.x- 120 + thuytinh.xOffSet; // 120 để chiêu phát ra sát người
                y = thuytinh.y;
                speed = -3.5f;
                isRender = true;
                updateAttackBox();
            }
            

        }
        else if (isRender){
            if(x + speed < -128 || x + speed > GAME_WIDTH){
                isRender = false;
            }
            else{
                x += speed;
                updateAttackBox();
            }
        }

    }

    private void updateAttackBox() {
        attackBox.x = x+14;
        attackBox.y = y;
    }

    public Rectangle2D.Float getAttackBox() {
        return attackBox;
    }

    private void drawAttackBox(Graphics g) {
        g.setColor(Color.RED);
        g.drawRect((int)attackBox.x, (int)attackBox.y, (int)attackBox.width, (int)attackBox.height);
    }
    

    public void render(Graphics g){
        Graphics2D g2 = (Graphics2D) g;
        if(isRender){
            updateAnimationTick();
            g2.drawImage(thuytinh.animations[TORNADO][framesIndex], (int)x, (int)y, 128,128,null);
            drawAttackBox(g);
        }
    }

}
