package entity;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import static utilz.LoadSave.loadSummonedEntityAnimation;
import static utilz.HelpMethods.canMoveHere;
import static utilz.Constants.GameConstants.GAME_WIDTH;
import static utilz.Constants.PlayerConstants.*;
public class SummonSkill extends Entity {
    private final float speed = 3.5f; // speed of the summoned entity
    private int direction; // 0: right, 1: left
    private boolean isActive = false;

    private BufferedImage[][] image;
    private int framesCounter, aniSpeed = 15, framesIndex, maxFrames;
    
    public SummonSkill(float x, float y,
                       float x_OffSetHitBox, float y_OffSetHitBox, float widthHitBox, float heightHitBox,
                       int direction, String characterName) {
        super(x, y, x_OffSetHitBox, y_OffSetHitBox, widthHitBox, heightHitBox);
        this.direction = direction;
        this.isActive = true;

        // Load animations based on character name
        this.image = loadSummonedEntityAnimation(characterName);
        this.maxFrames = image[0].length;
    }

    public void update(){
        if(isActive){
            if(direction == RIGHT){
                if(hitBox.x + speed > GAME_WIDTH) isActive = false;
                else x += speed;
            }
            else{
                if(hitBox.x + hitBox.width - speed < 0) isActive = false;
                else x -= speed;
            }
            updateHitBox();
        } 
    }

    public void render(Graphics g){
        if(isActive){
            Graphics2D g2 = (Graphics2D) g;
            g2.drawImage(image[direction][framesIndex], (int)x, (int)y, 128, 128, null);
        }
    }
    
    public boolean isActive(){
        return isActive;
    }
    public int getDirection(){
        return direction;
    }


}