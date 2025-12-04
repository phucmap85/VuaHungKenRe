package utilz;
import static utilz.Constants.GameConstants.*;
import java.util.List;

import map.Platform;

import java.awt.geom.Rectangle2D;

public class HelpMethods {
	public static boolean isInAir(Rectangle2D.Float box, float groundY, List<Platform> platforms) {
		float boxBottom = box.y + box.height;
		float boxLeft = box.x;
		float boxRight = box.x + box.width;

		//Nếu đang đứng trên mặt đất → không ở trên không
		if (boxBottom >= groundY)
			return false;

		//Kiểm tra nếu đang đứng trên bất kỳ platform nào
		for (Platform p : platforms) {
			boolean withinX = (boxRight >= p.x1) && (boxLeft <= p.x2);
			boolean atPlatformHeight = Math.abs(boxBottom - p.y) <= 1f;

			// Nếu player đang đúng độ cao platform và trong phạm vi X của nó
			if (atPlatformHeight && withinX) {
				return false; // đứng trên platform
			}
		}

		for (Platform p : platforms) {
			boolean atPlatformHeight = Math.abs(boxBottom - p.y) <= 1f;
			boolean outsidePlatformRange = (boxRight < p.x1) || (boxLeft > p.x2);

			if (atPlatformHeight && outsidePlatformRange) {
				return true;
			}
		}

		return true;
	}

	public static boolean willHitPlatform(Rectangle2D.Float box, float speedY, List<Platform> platforms) {
		float boxBottom = box.y + box.height;
		float nextBottom = boxBottom + speedY;
		float boxLeft = box.x;
		float boxRight = box.x + box.width;

		for (Platform p : platforms) {
			boolean withinX = (boxRight >= p.x1) && (boxLeft <= p.x2);
			boolean movingDown = (speedY > 0);
			boolean abovePlatform = (boxBottom <= p.y);
			boolean willCross = (nextBottom >= p.y);
			if (withinX && movingDown && abovePlatform && willCross)
				return true;
		}

		return false;
	}

	public static boolean willHitGround(Rectangle2D.Float box, float speedY, float groundY) {
		float boxBottom = box.y + box.height;
		boolean aboveGround = (boxBottom <= groundY);
		boolean willHit = (boxBottom + speedY > groundY);
		return aboveGround && willHit;
	}

	public static boolean canMoveHere(Rectangle2D.Float box, float speedX) {
		float boxLeft = box.x + speedX;
		float boxRight = box.x + box.width + speedX;
		if (boxLeft <= 0 || boxRight >= GAME_WIDTH) {
			return false;
		}
		return true;
	}

	public static boolean Collision(Rectangle2D.Float box1, Rectangle2D.Float box2) {
		return box1.intersects(box2);
	}
}