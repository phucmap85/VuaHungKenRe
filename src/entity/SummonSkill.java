package entity;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import static utilz.LoadSave.loadSummonedEntityAnimation;
import static utilz.HelpMethods.canMoveHere;
import static utilz.Constants.GameConstants.GAME_WIDTH;
import static utilz.Constants.PlayerConstants.*;
public class SummonSkill extends Entity {
    private final float speed = 3.0f; // speed of the summoned entity
    private int direction; // 0: right, 1: left
    private boolean isActive = false;
    private boolean collision = false;
    private BufferedImage[][] image;
    private String characterName;
    private int framesCounter, aniSpeed = 20, framesIndex, maxFrames, startFrames = 0;
    private int aniSpeedDisappearTornado = 15, maxFramesDisappearTornado = 17;
    public SummonSkill(float x, float y,
                       float x_OffSetHitBox, float y_OffSetHitBox, float widthHitBox, float heightHitBox,
                       int direction, String characterName) {
        super(x, y, x_OffSetHitBox, y_OffSetHitBox, widthHitBox, heightHitBox);
        this.direction = direction;
        this.isActive = true;

        this.characterName = characterName;
        // Load animations based on character name
        this.image = loadSummonedEntityAnimation(characterName);
        this.maxFrames = image[0].length;

        if(characterName.equals("ThuyTinh")){
            startFrames = 9;
            framesIndex = startFrames;
            maxFrames = 11; 
        }
    }

    public void update(){
        if(isActive){
            if(characterName.equals("SonTinh") || !collision){
                
            if(direction == RIGHT){
                if(hitBox.x + speed > GAME_WIDTH) isActive = false;
                else x += speed;
            }
            else{
                if(hitBox.x + hitBox.width - speed < 0) isActive = false;
                else x -= speed;
            }
        }
            updateHitBox();
            updateAnimationTick();
        } 
    }

    public void updateAnimationTick(){
        if(characterName.equals("SonTinh") || !collision){
        framesCounter++;
        if(framesCounter >= aniSpeed){
            framesCounter = 0;
            framesIndex++;
            if(framesIndex >= maxFrames){
                framesIndex = startFrames;
            }
        }
    }
    else if(collision){
        framesCounter++;
        if(framesIndex < maxFrames) framesIndex = maxFrames;
        aniSpeed = aniSpeedDisappearTornado;
        if(framesCounter >= aniSpeed){
            framesCounter = 0;
            framesIndex++;
            if(framesIndex >= maxFramesDisappearTornado){
                isActive = false;
            }
        }
    }
        
    }
    public void render(Graphics g){
        if(isActive){
            Graphics2D g2 = (Graphics2D) g;
            if(characterName.equals("ThuyTinh")){
                g2.drawImage(image[direction][framesIndex], (int)x, (int)y - 20, 150, 150, null);
                return;
            }
            
            g2.drawImage(image[direction][framesIndex], (int)x, (int)y, 128, 128, null);
        }
    }
    
    public boolean isActive(){
        return isActive;
    }
    public int getDirection(){
        return direction;
    }
    public boolean setCollision(boolean collision){
        framesCounter = 0;
        return this.collision = collision;
        
    }

}