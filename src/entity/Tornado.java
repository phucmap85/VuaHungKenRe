package entity;

import static utilz.Constants.GameConstants.GAME_WIDTH;
import static utilz.Constants.PlayerConstants.*;
import static utilz.HelpMethods.Collision;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import ui.PlayerUI;

public class Tornado {
    Player1 thuytinh; Player2 sontinh;
    float x,y;
    private Rectangle2D.Float attackBox;
    float speed;
    BufferedImage tornadoImage[];
    int framesIndex = 0;
    int framesCounter = 0;
    int animationSpeed = 20;
    PlayerUI playerUI;

    protected Rectangle2D.Float hitbox;
    private boolean isRender = false;
    private boolean doneTornado = false;
    private boolean takeDamage = false;
    public Tornado(Player1 thuytinh,Player2 sontinh,PlayerUI playerUI) {
        this.thuytinh = thuytinh;
        this.sontinh = sontinh;
        this.playerUI = playerUI;
        initClasses();
        this.attackBox = new Rectangle2D.Float(x, y, 100, 128);
    }

    protected void drawHitbox(Graphics g) {
		// For debugging the hitbox
		g.setColor(Color.PINK);
		g.drawRect((int) hitbox.x, (int) hitbox.y, (int) hitbox.width, (int) hitbox.height);
	}

	protected void initHitbox() {
		hitbox = new Rectangle2D.Float(x+30 , y+5 , 70, 115);
	}

	protected void updateHitbox() {
	 	hitbox.x =  x + 30;
	 	hitbox.y =  y + 5;
 }


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
        if(thuytinh.tornadoing == false){
            doneTornado = false;
        }
        else if(!doneTornado && !isRender){
            if(thuytinh.tornadoing && thuytinh.tornadoFrameIndex == thuytinh.MAX_TORNADO_FRAMES-1 && thuytinh.direction == RIGHT && !thuytinh.inAir){
                x = thuytinh.x + thuytinh.xOffSet + thuytinh.width;
                y = thuytinh.y;
                speed = 3.5f;
                isRender = true;
                initHitbox();
                doneTornado = true;

            }
            else if(thuytinh.tornadoing && thuytinh.tornadoFrameIndex == thuytinh.MAX_TORNADO_FRAMES-1 && thuytinh.direction == LEFT && !thuytinh.inAir){
                x = thuytinh.x- 120 + thuytinh.xOffSet; // 120 để chiêu phát ra sát người
                y = thuytinh.y;
                speed = -3.5f;
                isRender = true;
                initHitbox();
                doneTornado = true;

            }
            
            

        }
        if (isRender){
            updateAnimationTick();
            if(sontinh.vulnerable && Collision(hitbox, sontinh.hitbox)){
                if(!sontinh.defending) {
                    sontinh.takingHit = true;
                    playerUI.takeDamage(20);
                    
                   
                }
            }
            

                
        
            if(x < -128 || x + speed  > GAME_WIDTH){
                isRender = false;
                doneTornado = true;

            }
            else{
                x += speed;
                updateHitbox();
            }
        }
        if(sontinh.vulnerable && !sontinh.defending && Collision(thuytinh.getAttackBox(), sontinh.hitbox)){
                    playerUI.takeDamage(1);
                    sontinh.takingHit = true;
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
            g2.drawImage(thuytinh.animations[TORNADOING_RIGHT][framesIndex], (int)x, (int)y, 128,128,null);
            drawHitbox(g);
            

            }
        
        
        
    }

}
