package entity;
import java.awt.Color;
import java.awt.Graphics;

import java.awt.geom.Rectangle2D;

public abstract class Entity {
	protected float x, y, xOffSet, yOffSet;
	protected float width, height;
	protected Rectangle2D.Float hitbox;

	public Entity(float x, float y, float width, float height,float xOffSet,float yOffSet) {
		this.x = x;
		this.y = y;
		this.xOffSet = xOffSet;
		this.yOffSet = yOffSet;
		this.width = width;
		this.height = height;
	}

	protected void drawHitbox(Graphics g) {
		// For debugging the hitbox
		g.setColor(Color.PINK);
		g.drawRect((int) hitbox.x, (int) hitbox.y, (int) hitbox.width, (int) hitbox.height);
	}

	protected void initHitbox() {
		hitbox = new Rectangle2D.Float(x + xOffSet, y + yOffSet, width, height);
	}

	protected void updateHitbox() {
	 	hitbox.x =  x + xOffSet;
	 	hitbox.y =  y + yOffSet;
 }

	public Rectangle2D.Float getHitbox() {
		return hitbox;
	}
}
