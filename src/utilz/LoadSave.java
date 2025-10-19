package utilz;

import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;

public class LoadSave {
    public static String BattleMap = "map.png";
    public static String MenuButton = "menu_button_atlas.png";
    public static String MenuBackground = "menu_background.png";

    public static BufferedImage GetSpriteAtlas(String fileName) {
		BufferedImage img = null;
		InputStream is = LoadSave.class.getResourceAsStream("/image/" + fileName);

		try {
			img = ImageIO.read(is);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return img;
	}

    public static BufferedImage flipHorizontally(BufferedImage img){
        AffineTransform tx = AffineTransform.getScaleInstance(-1, 1);
        tx.translate(-img.getWidth(), 0);
        AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
        BufferedImage flippedHorizontally = op.filter(img, null);
        return flippedHorizontally;
    }
  

    public static BufferedImage[][] GetAnimation(String player) {
        BufferedImage[][] animations = new BufferedImage[20][20];
        String[][] animConfig = null;

        // SỬA LỖI: Dùng .equals() để so sánh chuỗi, không dùng ==
        if ("SonTinh".equals(player)) {
            // Cấu hình cho nhân vật SonTinh
            animConfig = new String[][] {
                // { Tên Animation, Số Frames, Loại }
                {"IDLE", "8", "NORMAL"},      // -> animations[0] (Phải), animations[1] (Trái)
                {"MOVE", "8", "NORMAL"},      // -> animations[2] (Phải), animations[3] (Trái)
                {"JUMP", "9", "NORMAL"},      // -> animations[4] (Phải), animations[5] (Trái)
                {"PUNCH", "19", "NORMAL"},     // -> animations[6] (Phải), animations[7] (Trái)
                {"DEFENSE", "3", "NORMAL"},   // -> animations[8] (Phải), animations[9] (Trái)
                {"SUMMONHOG", "6", "NORMAL"}, // Hoạt ảnh "triệu hồi" nên có 2 chiều
                {"HOG", "8", "NORMAL"},
                {"TAKINGHIT","3","NORMAL"}       // Con lợn chỉ có 1 chiều -> animations[12]
            };
        } else { // Mặc định là ThuyTinh hoặc nhân vật khác
            // Cấu hình cho nhân vật ThuyTinh
            animConfig = new String[][] {
                // { Tên Animation, Số Frames, Loại }
                {"IDLE", "8", "NORMAL"},          // -> animations[0] (Phải), animations[1] (Trái)
                {"MOVE", "4", "NORMAL"},          // -> animations[2] (Phải), animations[3] (Trái)
                {"JUMP", "9", "NORMAL"},          // -> animations[4] (Phải), animations[5] (Trái)
                {"PUNCH", "19", "NORMAL"},         // -> animations[6] (Phải), animations[7] (Trái)
                {"DEFENSE", "3", "NORMAL"},       // -> animations[8] (Phải), animations[9] (Trái)
                {"SUMMONTORNADO", "6", "NORMAL"}, // Hoạt ảnh "tung chiêu" -> animations[10], [11]
                {"TORNADO", "2", "NORMAL"},
                {"TAKINGHIT","3","NORMAL"}        // Lốc xoáy chỉ có 1 chiều -> animations[12]
            };
        }

        try {
            int currentRow = 0; // Biến theo dõi hàng hiện tại trong mảng animations[][]

            for (int i = 0; i < animConfig.length; i++) {
                String animName = animConfig[i][0];
                int frameCount = Integer.parseInt(animConfig[i][1]);
                String animType = animConfig[i][2];

                System.out.println("Loading: " + animName + " (" + frameCount + " frames, type: " + animType + ")");

                for (int j = 0; j < frameCount; j++) {
                    String path = String.format("/image/%s/%s_%04d.png", player, animName, j + 1);
                    InputStream is = LoadSave.class.getResourceAsStream(path);

                    if (is == null) {
                        System.err.println("ERROR: File not found: " + path);
                        throw new RuntimeException("Missing animation file: " + path);
                    }
                    BufferedImage img = ImageIO.read(is);
                    
                    // Logic tải ảnh dựa trên loại animation
                    if ("NORMAL".equals(animType)) {
                        // Tải vào 2 hàng: một hàng cho ảnh gốc, một hàng cho ảnh lật
                        animations[currentRow][j] = img;
                        animations[currentRow + 1][j] = flipHorizontally(img);
                    } else if ("SINGLE".equals(animType)) {
                        // Chỉ tải vào 1 hàng, không lật
                        animations[currentRow][j] = img;
                    }

                    is.close();
                }

                // Cập nhật chỉ số hàng cho animation tiếp theo
                if ("NORMAL".equals(animType)) {
                    currentRow += 2;
                } else { // animType là "SINGLE"
                    currentRow += 1;
                }

                System.out.println("✓ Loaded successfully into row(s) starting from " + (currentRow - ("NORMAL".equals(animType) ? 2 : 1)));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return animations;
    }

}
