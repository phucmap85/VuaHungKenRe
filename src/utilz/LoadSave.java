package utilz;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;

public class LoadSave {
    public static String BattleMap = "/image/map.png";

    public static BufferedImage GetMap(String name) {
        BufferedImage img = null;
        InputStream is = LoadSave.class.getResourceAsStream(name);
        try {
            img = ImageIO.read(is);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (is != null) { // Kiểm tra null trước khi close
                    is.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return img;
    }

    public static BufferedImage[][] GetAnimation() {
        // FIXED: Mảng có 10 animations, không phải 12
        BufferedImage[][] animations = new BufferedImage[10][20];
        
        String[][] animConfig = {
            {"IDLE_right", "8"},      // animations[0]
            {"IDLE_left", "8"},       // animations[1]
            {"MOVE_right", "4"},      // animations[2]
            {"MOVE_left", "4"},       // animations[3]
            {"JUMP_right", "9"},      // animations[4]
            {"JUMP_left", "9"},       // animations[5]
            {"PUNCH_right", "19"},    // animations[6]
            {"PUNCH_left", "19"},     // animations[7]
            {"DEFENSE_right", "3"},   // animations[8]
            {"DEFENSE_left", "3"}     // animations[9]
            // Tổng: 10 animations
        };
        
        try {
            for (int i = 0; i < animConfig.length; i++) {
                String animName = animConfig[i][0];
                int frameCount = Integer.parseInt(animConfig[i][1]);
                
                System.out.println("Loading: " + animName + " (" + frameCount + " frames)");
                
                for (int j = 0; j < frameCount; j++) {
                    String path = String.format("/image/%s_%04d.png", animName, j + 1);
                    
                    InputStream is = LoadSave.class.getResourceAsStream(path);
                    
                    // Kiểm tra file có tồn tại không
                    if (is == null) {
                        System.err.println("ERROR: File not found: " + path);
                        System.err.println("Please check:");
                        System.err.println("  1. File exists in /res/image/ or /resources/image/");
                        System.err.println("  2. File name is EXACT: " + animName + "_" + String.format("%04d", j + 1) + ".png");
                        System.err.println("  3. Check uppercase/lowercase");
                        throw new RuntimeException("Missing animation file: " + path);
                    }
                    
                    animations[i][j] = ImageIO.read(is);
                    is.close(); // Đóng stream sau khi đọc
                }
                
                System.out.println("✓ Loaded successfully");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return animations;
    }
}
