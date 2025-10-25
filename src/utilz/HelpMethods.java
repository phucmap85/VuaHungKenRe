package utilz;
import static utilz.Constants.GameConstants.*;

import java.awt.geom.Rectangle2D;

public class HelpMethods {
    public static boolean isInAir(Rectangle2D.Float box) {
        float boxBottom = box.y + box.height;  // Cạnh dưới của box
        float boxRight = box.x + box.width;    // Cạnh phải của box
        float boxLeft = box.x;                     // Cạnh trái của box
        
        // Player đang ở trên mặt đất và không ở trên platform
        if (boxBottom < groundY && boxBottom != platFormY) {
            return true;
        }
        
        // Player đang ở độ cao của platform nhưng nằm ngoài vùng X của platform
        boolean atPlatformHeight = (boxBottom == platFormY);
        boolean outsidePlatformRange = (boxRight < platFormX1) || (boxLeft > platFormX2);
        
        if (atPlatformHeight && outsidePlatformRange) {
            return true;
        }
        
        return false;
    }
    

    public static boolean willHitPlatForm(Rectangle2D.Float box, float speedY) {
        float boxRight = box.x + box.width;    // Cạnh phải của box
        float boxLeft = box.x;                     // Cạnh trái của box
        float boxBottom = box.y + box.height;  // Cạnh dưới của box
        
        // Kiểm tra player có trong phạm vi X của platform không
        boolean inPlatformXRange = (boxRight >= platFormX1) && (boxLeft <= platFormX2);
        
        if (inPlatformXRange) {
            // Kiểm tra player đang ở trên platform và sắp chạm vào nó trong frame tiếp theo
            boolean abovePlatform = (boxBottom <= platFormY);
            boolean willHitPlatform = (boxBottom + speedY > platFormY);
            
            if (abovePlatform && willHitPlatform) return true;
        }
        
        return false;
    }

    public static boolean willHitGround(Rectangle2D.Float box, float speedY) {
        float boxBottom = box.y + box.height;  // Cạnh dưới của box
        
        // Kiểm tra player đang ở trên mặt đất và sắp chạm vào nó trong frame tiếp theo
        boolean aboveGround = (boxBottom <= groundY);
        boolean willHitGround = (boxBottom + speedY > groundY);
        
        if (aboveGround && willHitGround) return true;
        
        return false;
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