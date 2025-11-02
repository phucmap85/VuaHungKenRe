package utilz;

import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineEvent;
import sound.SoundManager;

public class LoadSave {
    // ============= CONSTANTS =============
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

    // ============= ANIMATION CACHE =============
    private static BufferedImage[][] sonTinhAnimations = null;
    private static BufferedImage[][] thuyTinhAnimations = null;
    private static BufferedImage[][] sonTinhSummonedEntity = null;
    private static BufferedImage[][] thuyTinhSummonedEntity = null;
    private static BufferedImage[][] sonTinhLightning = null;
    private static BufferedImage[][] thuyTinhLightning = null;
    private static BufferedImage[][] sonTinhUltiCreature = null;
    private static BufferedImage[][] thuyTinhUltiCreature = null;
    private static BufferedImage[][] effectSprites = null;
    private static boolean allAnimationsLoaded = false;

    // ============= SOUND CACHE =============
    private static final Map<Integer, Clip[]> soundCache = new HashMap<>();
    private static final int CACHE_SIZE = 3;
    private static boolean soundsPreloaded = false;

    // ============= MAIN LOADING METHODS =============
    
    /**
     * Load tất cả animations một lần - gọi ở đầu game
     */
    public static void loadAllAnimations() {
        if (allAnimationsLoaded) return;
        
        System.out.println("Loading all animations...");
        if(sonTinhAnimations == null) sonTinhAnimations = getAnimations("SonTinh");
        if(thuyTinhAnimations == null) thuyTinhAnimations = getAnimations("ThuyTinh");
        loadSpecialAnimations();
        allAnimationsLoaded = true;
        System.out.println("All animations loaded!");
    }

    /**
     * Load special animations (summoned, lightning, ulti, effects)
     */
    private static void loadSpecialAnimations() {
        // SonTinh special animations
        sonTinhSummonedEntity = loadFrames("SonTinh", "HOG", 8);
        sonTinhLightning = loadFrames("SonTinh", "LIGHTNING", 27);
        sonTinhUltiCreature = loadFrames("SonTinh", "PHOENIX", 19);
        
        // ThuyTinh special animations
        thuyTinhSummonedEntity = loadFrames("ThuyTinh", "TORNADO", 17);
        thuyTinhLightning = loadFrames("ThuyTinh", "LIGHTNING", 20);
        thuyTinhUltiCreature = loadFrames("ThuyTinh", "SQUID", 20);
        
        // Effects
        loadEffects();
    }

    /**
     * Helper method để load animation frames
     */
    private static BufferedImage[][] loadFrames(String character, String animation, int frameCount) {
        BufferedImage[][] frames = new BufferedImage[2][frameCount];
        try {
            for (int i = 0; i < frameCount; i++) {
                String path = String.format("/image/%s/%s_%04d.png", character, animation, i + 1);
                InputStream is = LoadSave.class.getResourceAsStream(path);
                if (is != null) {
                    BufferedImage img = ImageIO.read(is);
                    frames[0][i] = img;
                    frames[1][i] = flipHorizontally(img);
                    is.close();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return frames;
    }

    /**
     * Load effect animations
     */
    private static void loadEffects() {
        String[][] effects = {
            {"IMPACT1", "3"}, {"IMPACT2", "9"}, {"PUNCHSLASH", "5"}, {"SHIELD", "2"},
            {"SLASH", "4"}, {"SMEAR", "5"}, {"SMOKE", "9"}, {"LANDING", "9"}
        };
        
        effectSprites = new BufferedImage[16][];
        try {
            int row = 0;
            for (String[] effect : effects) {
                String name = effect[0];
                int frameCount = Integer.parseInt(effect[1]);
                
                effectSprites[row] = new BufferedImage[frameCount];
                effectSprites[row + 1] = new BufferedImage[frameCount];
                
                for (int i = 0; i < frameCount; i++) {
                    String path = String.format("/image/Effects/%s_%04d.png", name, i + 1);
                    InputStream is = LoadSave.class.getResourceAsStream(path);
                    if (is != null) {
                        BufferedImage img = ImageIO.read(is);
                        effectSprites[row][i] = img;
                        effectSprites[row + 1][i] = flipHorizontally(img);
                        is.close();
                    }
                }
                row += 2;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // ============= PUBLIC GETTER METHODS =============
    
    /**
     * Trả về animations của Sơn Tinh
     */
    public static BufferedImage[][] getSonTinhAnimations() {
        if (sonTinhAnimations == null) loadAllAnimations();
        return sonTinhAnimations;
    }

    /**
     * Trả về animations của Thủy Tinh
     */
    public static BufferedImage[][] getThuyTinhAnimations() {
        if (thuyTinhAnimations == null) loadAllAnimations();
        return thuyTinhAnimations;
    }

    /**
     * Load summoned entity animation
     */
    public static BufferedImage[][] loadSummonedEntityAnimation(String characterName) {
        if (!allAnimationsLoaded) loadAllAnimations();
        if ("SonTinh".equals(characterName)) return sonTinhSummonedEntity;
        if ("ThuyTinh".equals(characterName)) return thuyTinhSummonedEntity;
        return new BufferedImage[2][1];
    }

    /**
     * Load lightning animation
     */
    public static BufferedImage[][] loadLightningAnimation(String characterName) {
        if (!allAnimationsLoaded) loadAllAnimations();
        if ("SonTinh".equals(characterName)) return sonTinhLightning;
        if ("ThuyTinh".equals(characterName)) return thuyTinhLightning;
        return new BufferedImage[2][1];
    }

    /**
     * Load ulti creature animation
     */
    public static BufferedImage[][] loadUltiCreatureAnimation(String characterName) {
        if (!allAnimationsLoaded) loadAllAnimations();
        if ("SonTinh".equals(characterName)) return sonTinhUltiCreature;
        if ("ThuyTinh".equals(characterName)) return thuyTinhUltiCreature;
        return new BufferedImage[2][1];
    }

    /**
     * Load effect sprites
     */
    public static BufferedImage[][] getEffectSprites() {
        if (!allAnimationsLoaded) loadAllAnimations();
        return effectSprites;
    }

    // ============= LEGACY METHODS =============
    
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

    public static BufferedImage flipHorizontally(BufferedImage img) {
        AffineTransform tx = AffineTransform.getScaleInstance(-1, 1);
        tx.translate(-img.getWidth(), 0);
        AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
        return op.filter(img, null);
    }

    public static BufferedImage[][] getAnimations(String character) {
        BufferedImage[][] animations = new BufferedImage[22][20];
        String[][] animConfig = null;

        if ("SonTinh".equals(character)) {
            animConfig = new String[][] {
                {"IDLE", "8", "NORMAL"}, {"MOVE", "8", "NORMAL"}, {"JUMP", "9", "NORMAL"},
                {"PUNCH", "19", "NORMAL"}, {"DEFENSE", "3", "NORMAL"}, {"TAKINGHIT","3","NORMAL"},
                {"FALLINGBACKDEATH","12","NORMAL"}, {"SUMMONSKILL","6","NORMAL"}, {"DASH","1","NORMAL"},
                {"SUMMONULTI","6","NORMAL"}
            };
        } else {
            animConfig = new String[][] {
                {"IDLE", "8", "NORMAL"}, {"MOVE", "4", "NORMAL"}, {"JUMP", "9", "NORMAL"},
                {"PUNCH", "19", "NORMAL"}, {"DEFENSE", "3", "NORMAL"}, {"TAKINGHIT","3","NORMAL"},
                {"FALLINGBACKDEATH","12","NORMAL"}, {"SUMMONSKILL","6","NORMAL"}, {"DASH", "1","NORMAL"},
                {"SUMMONULTI","6","NORMAL"}
            };
        }

        try {
            int currentRow = 0; // Biến theo dõi hàng hiện tại trong mảng animations[][]
            String path = String.format("/image/%s/%sSpriteAtlas.png", character, character);
            InputStream is = LoadSave.class.getResourceAsStream(path);
            if (is == null) {
                System.err.println("ERROR: File not found: " + path);
                throw new RuntimeException("Missing animation file: " + path);
            }
            BufferedImage img = ImageIO.read(is);
            for (int i = 0; i < animConfig.length; i++) {
                String animName = animConfig[i][0];
                int frameCount = Integer.parseInt(animConfig[i][1]);
                String animType = animConfig[i][2];

                //System.out.println("Loading: " + animName + " (" + frameCount + " frames, type: " + animType + ")");
                
                for (int j = 0; j < frameCount; j++) {
                    //String path = String.format("/image/%s/%s_%04d.png", player, animName, j + 1);
                    
                    
                    // Logic tải ảnh dựa trên loại animation
                    if ("NORMAL".equals(animType)) {
                        // Tải vào 2 hàng: một hàng cho ảnh gốc, một hàng cho ảnh lật
                        BufferedImage subImg = img.getSubimage(j * 64, i * 64, 64, 64);
                        animations[currentRow][j] = subImg;
                        animations[currentRow + 1][j] = flipHorizontally(subImg);
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
            System.out.println("Succesfully loaded for " + character);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // try {
        //     BufferedImage merged = mergeImageGrid(animations);
        //     ImageIO.write(merged, "png", new File(character + ".png"));
        //     System.out.println("Merged image saved as merged_output.png");
        // } catch (IOException e){
        //     e.printStackTrace();
        // }
        
        return animations;
    }
   
    public static BufferedImage mergeImageGrid(BufferedImage[][] images) {
        int rows = 10;
        int cols = 19;

        int tileWidth = 0, tileHeight = 0;


        tileWidth = Math.max(tileWidth, images[0][0].getWidth());
        tileHeight = Math.max(tileHeight, images[0][0].getHeight());

        // Total output dimensions
        int totalWidth = cols * tileWidth;
        int totalHeight = rows * tileHeight;

        // Create output image with alpha (transparency) support
        BufferedImage merged = new BufferedImage(totalWidth, totalHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = merged.createGraphics();

        // Optional: smoother scaling or background fill
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2d.setComposite(AlphaComposite.SrcOver);

        // Draw each image in grid
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                int x = j * tileWidth;
                int y = i * tileHeight;
                g2d.drawImage(images[i * 2][j], x, y, null);
            }
        }

        g2d.dispose();
        return merged;
    }

    // ============= SOUND METHODS =============
    
    public static void preloadSounds() {
        if (soundsPreloaded) return;
        System.out.println("Preloading sounds...");
        for (SoundManager se : SoundManager.values()) {
            if (!soundCache.containsKey(se.getIndex())) {
                loadSoundIntoCache(se);
            }
        }
        soundsPreloaded = true;
    }

    private static void loadSoundIntoCache(SoundManager se) {
        if (se == null) return;
        try {
            String path = se.getPath();
            URL soundURL = LoadSave.class.getResource(path);
            if (soundURL == null) {
                System.out.println("Can't load sound: " + path);
                return;
            }
            Clip[] clips = new Clip[CACHE_SIZE];
            for (int i = 0; i < CACHE_SIZE; i++) {
                AudioInputStream ais = AudioSystem.getAudioInputStream(soundURL);
                Clip clip = AudioSystem.getClip();
                clip.open(ais);
                clip.addLineListener(event -> {
                    if (event.getType() == LineEvent.Type.STOP) {
                        clip.setFramePosition(0);
                    }
                });
                clips[i] = clip;
            }
            soundCache.put(se.getIndex(), clips);
        } catch (Exception e) {
            // Silent fail
        }
    }

    public static Clip getSoundClip(int soundIndex) {
        Clip[] clips = soundCache.get(soundIndex);
        if (clips == null) return null;
        for (Clip clip : clips) {
            if (!clip.isRunning()) return clip;
        }
        return clips[0];
    }
    
    public static void cleanupSounds() {
        for (Clip[] clips : soundCache.values()) {
            for (Clip clip : clips) {
                try {
                    if (clip.isRunning()) clip.stop();
                    clip.close();
                } catch (Exception e) {
                    // Silent fail
                }
            }
        }
        soundCache.clear();
        soundsPreloaded = false;
    }
}