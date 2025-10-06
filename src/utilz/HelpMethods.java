package utilz;
import static utilz.Constants.GameConstants.*;

import java.awt.geom.Rectangle2D;

public class HelpMethods {
    public static boolean isInAir(float y, Rectangle2D.Float hitbox, float groundY) {
        float hitboxBottom = hitbox.y + hitbox.height;  // Cạnh dưới của hitbox
        float hitboxRight = hitbox.x + hitbox.width;    // Cạnh phải của hitbox
        float hitboxLeft = hitbox.x;                     // Cạnh trái của hitbox
        
        // Player đang ở trên mặt đất và không ở trên platform
        if (y > groundY && hitboxBottom != platFormY) {
            return true;
        }
        
        // Player đang ở độ cao của platform nhưng nằm ngoài vùng X của platform
        boolean atPlatformHeight = (hitboxBottom == platFormY);
        boolean outsidePlatformRange = (hitboxRight < platFormX1) || (hitboxLeft > platFormX2);
        
        if (atPlatformHeight && outsidePlatformRange) {
            return true;
        }
        
        return false;
    }
    

    public static boolean isOnPlatForm(Rectangle2D.Float hitbox, float speed) {
        float hitboxRight = hitbox.x + hitbox.width;    // Cạnh phải của hitbox
        float hitboxLeft = hitbox.x;                     // Cạnh trái của hitbox
        float hitboxBottom = hitbox.y + hitbox.height;  // Cạnh dưới của hitbox
        
        // Kiểm tra player có trong phạm vi X của platform không
        boolean inPlatformXRange = (hitboxRight >= platFormX1) && (hitboxLeft <= platFormX2);
        
        if (inPlatformXRange) {
            // Kiểm tra player đang ở trên platform và sắp chạm vào nó trong frame tiếp theo
            boolean abovePlatform = (hitboxBottom <= platFormY);
            boolean willHitPlatform = (hitboxBottom + speed > platFormY);
            
            if (abovePlatform && willHitPlatform) return true;
        }
        
        return false;
    }
}