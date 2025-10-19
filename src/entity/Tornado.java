package entity;

import static utilz.Constants.GameConstants.GAME_WIDTH;
import static utilz.Constants.PlayerConstants.LEFT;
import static utilz.Constants.PlayerConstants.RIGHT;
import static utilz.Constants.PlayerConstants.TORNADO;
import static utilz.Constants.PlayerConstants.TORNADO_LEFT;
import static utilz.Constants.PlayerConstants.TORNADO_RIGHT;
import static utilz.Constants.PlayerConstants.getFramesAmount;
import static utilz.HelpMethods.Collision;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import ui.PlayerUI;

public class Tornado {
    Player1 thuytinh; Player2 sontinh;
    float x,y;
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
            if(!takeDamage && Collision(hitbox, sontinh.hitbox)){
                if(!sontinh.defending) {
                    playerUI.takeDamage(20);
                    takeDamage = true;
                    sontinh.takingHit = true;
                }

                
        }
            if(x < -128 || x + speed  > GAME_WIDTH){
                isRender = false;
                doneTornado = true;
                takeDamage =  false;
            }
            else{
                x += speed;
                updateHitbox();
            }
        }

    }
    

    public void render(Graphics g){
        Graphics2D g2 = (Graphics2D) g;
        if(isRender){
            g2.drawImage(thuytinh.animations[TORNADO_RIGHT][framesIndex], (int)x, (int)y, 128,128,null);
            drawHitbox(g);
            

            }
        
        
        
    }

}
