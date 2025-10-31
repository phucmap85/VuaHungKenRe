package utilz;

import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;

public class LoadSave {
    public static String BattleMap0 = "map_0001.png";
    public static String BattleMap1 = "map_0002.png";
    public static String MenuButton = "MainMenu/menu_button_atlas.png";
    public static String MenuBackground = "MainMenu/menu_background.png";
    public static String KeyButton = "KeyButton/keys.png";
    public static String PauseBackground = "PauseMenu/pause_menu.png";
    public static String SoundButton = "PauseMenu/sound_button.png";
    public static String UrmButton = "PauseMenu/urm_button.png";
    public static String VolumeButton = "PauseMenu/volume_button.png";
    public static String MatchSetupBackground = "MatchSetup/MatchSetupBackground.png";
    public static String MatchSetupTitle = "MatchSetup/mapselection.png";
    public static String NextButton = "MatchSetup/nextbutton.png";
    // Static cache cho animations
    private static BufferedImage[][] sonTinhSummonedEntity = null;
    private static BufferedImage[][] thuyTinhSummonedEntity = null;
    private static BufferedImage[][] sonTinhLightning = null;
    private static BufferedImage[][] thuyTinhLightning = null;
    private static BufferedImage[][] sonTinhUltiCreature = null;
    private static BufferedImage[][] thuyTinhUltiCreature = null;
    private static BufferedImage[][] effectSprites = null;
    
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
  

    public static BufferedImage[][] getAnimations(String player) {
        BufferedImage[][] animations = new BufferedImage[22][20];
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
                {"TAKINGHIT","3","NORMAL"},
                {"FALLINGBACKDEATH","12","NORMAL"},
                {"SUMMONSKILL","6","NORMAL"},
                {"DASH","1","NORMAL"},
                {"SUMMONULTI","6","NORMAL"}      // Con lợn chỉ có 1 chiều -> animations[12]
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
                {"TAKINGHIT","3","NORMAL"},
                {"FALLINGBACKDEATH","12","NORMAL"},
                {"SUMMONSKILL","6","NORMAL"},
                {"SUMMONULTI","6","NORMAL"}        // Con lốc xoáy chỉ có 1 chiều -> animations[12]
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
   
    
    /**
     * Loads animation frames for summoned entities (HOG, TORNADO) based on character name
     * @param characterName Name of the character ("SonTinh" or "ThuyTinh")
     * @return 2D array of animation frames [direction][frame]
     */
    public static BufferedImage[][] loadSummonedEntityAnimation(String characterName) {
        // Kiểm tra cache trước
        if ("SonTinh".equals(characterName)) {
            if (sonTinhSummonedEntity != null) return sonTinhSummonedEntity;
        } else if ("ThuyTinh".equals(characterName)) {
            if (thuyTinhSummonedEntity != null) return thuyTinhSummonedEntity;
        }

        String entityName;
        int frameCount;
        
        // Get entity name and frame count based on character
        if ("SonTinh".equals(characterName)) {
            entityName = "HOG";
            frameCount = 8;  // 8 frames for HOG
        } else if ("ThuyTinh".equals(characterName)) {
            entityName = "TORNADO";
            frameCount = 17;  // 17 frames for TORNADO
        } else {
            System.err.println("Unknown character for summoned entity: " + characterName);
            return new BufferedImage[2][1];
        }
        
        BufferedImage[][] animations = new BufferedImage[2][frameCount];
        
        try {
            for (int j = 0; j < frameCount; j++) {
                String path = String.format("/image/%s/%s_%04d.png", characterName, entityName, j + 1);
                InputStream is = LoadSave.class.getResourceAsStream(path);
                
                if (is == null) {
                    System.err.println("ERROR: Summoned entity file not found: " + path);
                    continue;
                }
                
                BufferedImage img = ImageIO.read(is);
                animations[0][j] = img;
                animations[1][j] = flipHorizontally(img);
                
                is.close();
            }
            
            // Lưu vào cache
            if ("SonTinh".equals(characterName)) {
                sonTinhSummonedEntity = animations;
            } else if ("ThuyTinh".equals(characterName)) {
                thuyTinhSummonedEntity = animations;
            }
            
            System.out.println("Loaded summoned entity animations for " + characterName + " (" + entityName + ", " + frameCount + " frames)");
            
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return animations;
    }

    /**
     * Loads animation frames for LIGHTNING attack based on character name
     * @param characterName Name of the character ("SonTinh" or "ThuyTinh")
     * @return 2D array of animation frames [direction][frame]
     */
    public static BufferedImage[][] loadLightningAnimation(String characterName) {
        // Kiểm tra cache trước
        if ("SonTinh".equals(characterName)) {
            if (sonTinhLightning != null) return sonTinhLightning;
        } else if ("ThuyTinh".equals(characterName)) {
            if (thuyTinhLightning != null) return thuyTinhLightning;
        }

        int frameCount;
        
        if ("SonTinh".equals(characterName)) {
            frameCount = 27;  // 27 frames for SonTinh's LIGHTNING
        } else if ("ThuyTinh".equals(characterName)) {
            frameCount = 20;  // 20 frames for ThuyTinh's LIGHTNING
        } else {
            System.err.println("Unknown character for lightning animation: " + characterName);
            return new BufferedImage[2][1];
        }
        
        BufferedImage[][] animations = new BufferedImage[2][frameCount];
        
        try {
            for (int j = 0; j < frameCount; j++) {
                String path = String.format("/image/%s/LIGHTNING_%04d.png", characterName, j + 1);
                InputStream is = LoadSave.class.getResourceAsStream(path);
                
                if (is == null) {
                    System.err.println("ERROR: Lightning file not found: " + path);
                    continue;
                }
                
                BufferedImage img = ImageIO.read(is);
                animations[0][j] = img;
                animations[1][j] = flipHorizontally(img);
                
                is.close();
            }
            
            // Lưu vào cache
            if ("SonTinh".equals(characterName)) {
                sonTinhLightning = animations;
            } else if ("ThuyTinh".equals(characterName)) {
                thuyTinhLightning = animations;
            }
            
            System.out.println("Loaded lightning animations for " + characterName + " (" + frameCount + " frames)");
            
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return animations;
    }

    /**
     * Loads animation frames for ULTI summoned creatures (PHOENIX, SQUID) based on character name
     * @param characterName Name of the character ("SonTinh" or "ThuyTinh")
     * @return 2D array of animation frames [direction][frame]
     */
    public static BufferedImage[][] loadUltiCreatureAnimation(String characterName) {
        // Kiểm tra cache trước
        if ("SonTinh".equals(characterName)) {
            if (sonTinhUltiCreature != null) return sonTinhUltiCreature;
        } else if ("ThuyTinh".equals(characterName)) {
            if (thuyTinhUltiCreature != null) return thuyTinhUltiCreature;
        }

        String creatureName;
        int frameCount;
        
        if ("SonTinh".equals(characterName)) {
            creatureName = "PHOENIX";
            frameCount = 19;  // 19 frames for PHOENIX
        } else if ("ThuyTinh".equals(characterName)) {
            creatureName = "SQUID";
            frameCount = 20;  // 20 frames for SQUID
        } else {
            System.err.println("Unknown character for ulti creature: " + characterName);
            return new BufferedImage[2][1];
        }
        
        BufferedImage[][] animations = new BufferedImage[2][frameCount];
        
        try {
            for (int j = 0; j < frameCount; j++) {
                String path = String.format("/image/%s/%s_%04d.png", characterName, creatureName, j + 1);
                InputStream is = LoadSave.class.getResourceAsStream(path);
                
                if (is == null) {
                    System.err.println("ERROR: Ulti creature file not found: " + path);
                    continue;
                }
                
                BufferedImage img = ImageIO.read(is);
                animations[0][j] = img;
                animations[1][j] = flipHorizontally(img);
                
                is.close();
            }
            
            // Lưu vào cache
            if ("SonTinh".equals(characterName)) {
                sonTinhUltiCreature = animations;
            } else if ("ThuyTinh".equals(characterName)) {
                thuyTinhUltiCreature = animations;
            }
            
            System.out.println("Loaded ulti creature animations for " + characterName + " (" + creatureName + ", " + frameCount + " frames)");
            
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return animations;
    }

    /**
     * Loads all effect animations from Effects folder
     * Returns 2D array where each row corresponds to an effect direction constant
     * @return 2D array of effect animations [effectConstant][frame]
     *         Row indices match EffectConstants (0=IMPACT1_RIGHT, 1=IMPACT1_LEFT, etc.)
     */
    public static BufferedImage[][] getEffectSprites() {
        // Kiểm tra cache trước
        if (effectSprites != null) return effectSprites;
        // Effect configuration: name, frame count
        String[][] effectConfig = {
            {"IMPACT1", "3"},      // Row 0-1: IMPACT1_RIGHT, IMPACT1_LEFT
            {"IMPACT2", "9"},      // Row 2-3: IMPACT2_RIGHT, IMPACT2_LEFT
            {"PUNCHSLASH", "5"},   // Row 4-5: PUNCHSLASH_RIGHT, PUNCHSLASH_LEFT
            {"SHIELD", "2"},       // Row 6-7: SHIELD_RIGHT, SHIELD_LEFT
            {"SLASH", "4"},        // Row 8-9: SLASH_RIGHT, SLASH_LEFT
            {"SMEAR", "5"},        // Row 10-11: SMEAR_RIGHT, SMEAR_LEFT
            {"SMOKE", "9"},        // Row 12-13: SMOKE_RIGHT, SMOKE_LEFT
            {"LANDING", "9"}       // Row 14-15: LANDING_RIGHT, LANDING_LEFT
        };
        
        // Create array: [14 rows for all effect directions][max frames]
        BufferedImage[][] effects = new BufferedImage[16][];
        
        try {
            int currentRow = 0;
            
            for (int i = 0; i < effectConfig.length; i++) {
                String effectName = effectConfig[i][0];
                int frameCount = Integer.parseInt(effectConfig[i][1]);
                
                System.out.println("Loading effect: " + effectName + " (" + frameCount + " frames)");
                
                // Initialize arrays for right and left directions
                effects[currentRow] = new BufferedImage[frameCount];     // Right direction
                effects[currentRow + 1] = new BufferedImage[frameCount]; // Left direction
                
                for (int j = 0; j < frameCount; j++) {
                    String path = String.format("/image/Effects/%s_%04d.png", effectName, j + 1);
                    InputStream is = LoadSave.class.getResourceAsStream(path);
                    
                    if (is == null) {
                        System.err.println("ERROR: Effect file not found: " + path);
                        continue;
                    }
                    
                    BufferedImage img = ImageIO.read(is);
                    
                    // Store original for right direction
                    effects[currentRow][j] = img;
                    
                    // Store flipped for left direction
                    effects[currentRow + 1][j] = flipHorizontally(img);
                    
                    is.close();
                }
                
                System.out.println("✓ Loaded effect: " + effectName + " into rows " + currentRow + "-" + (currentRow + 1));
                currentRow += 2;
            }

            // Lưu vào cache
            effectSprites = effects;
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return effects;
    }

}