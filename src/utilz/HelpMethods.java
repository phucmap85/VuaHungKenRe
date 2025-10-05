package utilz;

import java.awt.Rectangle;

public class HelpMethods {
    public static boolean canMoveHere(Rectangle r){
        if(r.x  < 0 || r.x + r.width > 1143 || r.y  + r.height > 600 || r.y  < 0) return false;
        return true;
    }
}
