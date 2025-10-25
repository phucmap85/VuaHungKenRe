package entity;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

// for all entities including characters and skills
public abstract class Entity {

    protected float x, y; // position (top-left point)
    protected float x_OffSetHurtBox, y_OffSetHurtBox, x_OffSetHitBox, y_OffSetHitBox; 
    protected Rectangle2D.Float hurtBox; // only for characters
    protected Rectangle2D.Float hitBox; // for skills and punches of characters

    public Entity(float x, float y,
                  float x_OffSetHitBox, float y_OffSetHitBox, float widthHitBox, float heightHitBox) {
        this.x = x;
        this.y = y;
        this.x_OffSetHitBox = x_OffSetHitBox;
        this.y_OffSetHitBox = y_OffSetHitBox;
        this.hitBox = new Rectangle2D.Float(x + x_OffSetHitBox, y + y_OffSetHitBox, widthHitBox, heightHitBox);
    }

    public Entity(float x, float y,
                  float x_OffSetHitBox, float y_OffSetHitBox, float widthHitBox, float heightHitBox,
                  float x_OffSetHurtBox, float y_OffSetHurtBox, float widthHurtBox, float heightHurtBox) {
        this(x,y,x_OffSetHitBox,y_OffSetHitBox,widthHitBox,heightHitBox);
        this.x_OffSetHurtBox = x_OffSetHurtBox;
        this.y_OffSetHurtBox = y_OffSetHurtBox;
        this.hurtBox = new Rectangle2D.Float(x + x_OffSetHurtBox, y + y_OffSetHurtBox, widthHurtBox, heightHurtBox);
    }
    
    

    protected void drawHurtBox(Graphics g) { // for debugging the hurtbox
        if (hurtBox != null) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setColor(Color.PINK);
            g2.draw(hurtBox); 
        }
        else {
            System.err.println("HurtBox is null, cannot draw.");
        }
    }
    
    protected void drawHitBox(Graphics g) { // for debugging the hitbox
        if (hitBox != null) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setColor(Color.RED);
            g2.draw(hitBox); 
        }
        else {
            System.err.println("HitBox is null, cannot draw.");
        }
    }
    
    protected void updateHurtBox() {
        if (hurtBox != null) {
            hurtBox.x = x + x_OffSetHurtBox;
            hurtBox.y = y + y_OffSetHurtBox;
        }
        else {
            System.err.println("HurtBox is null, cannot update.");
        }
    }

    protected void updateHitBox() {
        if (hitBox != null) {
            hitBox.x = x + x_OffSetHitBox;
            hitBox.y = y + y_OffSetHitBox;
        }
        else {
            System.err.println("HitBox is null, cannot update.");
        }
    }
    
  
    protected void updateBoxes() {
        updateHitBox();
        updateHurtBox();
    }

	protected void drawBoxes(Graphics g) {
        drawHitBox(g);
        drawHurtBox(g);
	}
    
    //getters
    public Rectangle2D.Float getHitBox() { return hitBox; }
    public Rectangle2D.Float getHurtBox() { return hurtBox; }
    public float getX() { return x; }
    public float getY() { return y; }
}
