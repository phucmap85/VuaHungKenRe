package utilz;


import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;



public class LoadSave {
    public static String BattleMap = "/image/map.png";



    public static BufferedImage GetMap(String name){
        BufferedImage img = null;
        InputStream is = LoadSave.class.getResourceAsStream(name);
        try{
            img = ImageIO.read(is);

        }catch(IOException e){
            e.printStackTrace();
        }finally{
            try{
                is.close();
            }catch(IOException e){
                e.printStackTrace();
            }
        }
        return img;
    }
    public static BufferedImage[][] GetAnimation() {
    BufferedImage[][] animations = new BufferedImage[6][9];
    
    String[][] animConfig = {
        {"IDLE_right", "8"},   // animations[0] - 8 frames
        {"IDLE_left", "8"},    // animations[1] - 8 frames
        {"MOVE_right", "4"},   // animations[2] - 4 frames
        {"MOVE_left", "4"},    // animations[3] - 4 frames
        {"JUMP_right", "9"},   // animations[4] - 9 frames
        {"JUMP_left", "9"}     // animations[5] - 9 frames
    };
    
    try {
        for (int i = 0; i < animConfig.length; i++) {
            String animName = animConfig[i][0];
            int frameCount = Integer.parseInt(animConfig[i][1]);
            
            for (int j = 0; j < frameCount; j++) {
                String path = String.format("/image/%s_%04d.png", animName, j + 1);
                animations[i][j] = ImageIO.read(LoadSave.class.getResourceAsStream(path));
            }
        }
    } catch (IOException e) {
        e.printStackTrace();
    }
    
    return animations;
}

}
