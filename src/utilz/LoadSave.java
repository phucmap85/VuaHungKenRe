package utilz;

import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineEvent;
import sound.SoundManager;

public class LoadSave {
    // ===== IMAGE PATH CONSTANTS =====
    public static String BattleMap0 = "Maps/map_0001.png";
    public static String BattleMap1 = "Maps/map_0002.png";
    public static String BattleMap2 = "Maps/ending_1.png";
    public static String BattleMap3 = "Maps/ending_2.png";
    public static String MenuButton = "MainMenu/menu_button_atlas.png";
    public static String MenuBackground = "MainMenu/frames/";
    public static String KeyButton = "KeyButton/keys.png";
    public static String OptionBackground = "OptionMenu/options_background.png";
    public static String PauseBackground = "PauseMenu/pause_menu.png";
    public static String SoundButton = "PauseMenu/sound_button.png";
    public static String UrmButton = "PauseMenu/urm_button.png";
    public static String VolumeButton = "PauseMenu/volume_button.png";
    public static String MatchSetupBackground = "MatchSetup/MatchSetupBackground.png";
    public static String MatchSetupTitle = "MatchSetup/mapselection.png";
    public static String NextButton = "MatchSetup/nextbutton.png";
    
    private static final int SPRITE_SIZE = 64;
    private static final int DIRECTION_COUNT = 2;

    // ===== ANIMATION CACHE =====
    private static BufferedImage[][] sonTinhAnimations = null;
    private static BufferedImage[][] thuyTinhAnimations = null;
    private static BufferedImage[][] vuaHungAnimations = null;
    private static BufferedImage[][] myNuongAnimations = null;
    private static BufferedImage[][] sonTinhSummonedEntity = null;
    private static BufferedImage[][] thuyTinhSummonedEntity = null;
    private static BufferedImage[][] sonTinhLightning = null;
    private static BufferedImage[][] thuyTinhLightning = null;
    private static BufferedImage[][] sonTinhUltiCreature = null;
    private static BufferedImage[][] thuyTinhUltiCreature = null;
    private static BufferedImage[][] effectSprites = null;
    private static boolean allAnimationsLoaded = false;

    // ===== SOUND CACHE =====
    private static final Map<Integer, List<Clip>> sfxPool = new HashMap<>();
    private static final int SFX_POOL_SIZE = 4;
    private static boolean soundsPreloaded = false;

    // ===== ANIMATION LOADING =====
    
    public static void loadAllAnimations() {
        if (allAnimationsLoaded) return;
        
        System.out.println("Loading all animations...");
        sonTinhAnimations = getAnimations("SonTinh");
        thuyTinhAnimations = getAnimations("ThuyTinh");
        vuaHungAnimations = getAnimations("VuaHung");
        myNuongAnimations = getAnimations("MyNuong");
        loadSpecialAnimations();
        allAnimationsLoaded = true;
        System.out.println("All animations loaded!");
    }

    private static void loadSpecialAnimations() {
        sonTinhSummonedEntity = loadFrames("SonTinh", "HOG", 8);
        sonTinhLightning = loadFrames("SonTinh", "LIGHTNING", 27);
        sonTinhUltiCreature = loadFrames("SonTinh", "PHOENIX", 19);
        
        thuyTinhSummonedEntity = loadFrames("ThuyTinh", "TORNADO", 17);
        thuyTinhLightning = loadFrames("ThuyTinh", "LIGHTNING", 20);
        thuyTinhUltiCreature = loadFrames("ThuyTinh", "SQUID", 20);
        
        loadEffects();
    }

    private static BufferedImage[][] loadFrames(String character, String animation, int frameCount) {
        BufferedImage[][] frames = new BufferedImage[DIRECTION_COUNT][frameCount];
        
        for (int i = 0; i < frameCount; i++) {
            String path = String.format("/image/%s/%s_%04d.png", character, animation, i + 1);
            try (InputStream is = LoadSave.class.getResourceAsStream(path)) {
                if (is != null) {
                    BufferedImage img = ImageIO.read(is);
                    frames[0][i] = img;
                    frames[1][i] = flipHorizontally(img);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return frames;
    }

    private static void loadEffects() {
        String[][] effectConfigs = {
            {"IMPACT1", "3"}, {"IMPACT2", "9"}, {"PUNCHSLASH", "5"}, {"SHIELD", "2"},
            {"SLASH", "4"}, {"SMEAR", "5"}, {"SMOKE", "9"}, {"LANDING", "9"}
        };
        
        effectSprites = new BufferedImage[16][];
        int row = 0;
        
        for (String[] config : effectConfigs) {
            String name = config[0];
            int frameCount = Integer.parseInt(config[1]);
            
            effectSprites[row] = new BufferedImage[frameCount];
            effectSprites[row + 1] = new BufferedImage[frameCount];
            
            for (int i = 0; i < frameCount; i++) {
                String path = String.format("/image/Effects/%s_%04d.png", name, i + 1);
                try (InputStream is = LoadSave.class.getResourceAsStream(path)) {
                    if (is != null) {
                        BufferedImage img = ImageIO.read(is);
                        effectSprites[row][i] = img;
                        effectSprites[row + 1][i] = flipHorizontally(img);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            row += 2;
        }
    }

    // ===== ANIMATION GETTERS =====
    
    public static BufferedImage[][] getSonTinhAnimations() {
        if (sonTinhAnimations == null) loadAllAnimations();
        return sonTinhAnimations;
    }

    public static BufferedImage[][] getThuyTinhAnimations() {
        if (thuyTinhAnimations == null) loadAllAnimations();
        return thuyTinhAnimations;
    }

    public static BufferedImage[][] getVuaHungAnimations() {
        if (vuaHungAnimations == null) loadAllAnimations();
        return vuaHungAnimations;
    }

    public static BufferedImage[][] getMyNuongAnimations() {
        if (myNuongAnimations == null) loadAllAnimations();
        return myNuongAnimations;
    }

    public static BufferedImage[][] loadSummonedEntityAnimation(String characterName) {
        if (!allAnimationsLoaded) loadAllAnimations();
        return "SonTinh".equals(characterName) ? sonTinhSummonedEntity : thuyTinhSummonedEntity;
    }

    public static BufferedImage[][] loadLightningAnimation(String characterName) {
        if (!allAnimationsLoaded) loadAllAnimations();
        return "SonTinh".equals(characterName) ? sonTinhLightning : thuyTinhLightning;
    }

    public static BufferedImage[][] loadUltiCreatureAnimation(String characterName) {
        if (!allAnimationsLoaded) loadAllAnimations();
        return "SonTinh".equals(characterName) ? sonTinhUltiCreature : thuyTinhUltiCreature;
    }

    public static BufferedImage[][] getEffectSprites() {
        if (!allAnimationsLoaded) loadAllAnimations();
        return effectSprites;
    }

    // ===== IMAGE UTILITIES =====
    
    public static BufferedImage GetSpriteAtlas(String fileName) {
        String path = "/image/" + fileName;
        try (InputStream is = LoadSave.class.getResourceAsStream(path)) {
            if (is != null) {
                return ImageIO.read(is);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static BufferedImage flipHorizontally(BufferedImage img) {
        AffineTransform tx = AffineTransform.getScaleInstance(-1, 1);
        tx.translate(-img.getWidth(), 0);
        AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
        return op.filter(img, null);
    }

    public static BufferedImage[][] getAnimations(String character) {
        BufferedImage[][] animations = new BufferedImage[22][20];
        String[][] animConfig = getAnimationConfig(character);
        
        String atlasPath = String.format("/image/%s/%sSpriteAtlas.png", character, character);
        try (InputStream is = LoadSave.class.getResourceAsStream(atlasPath)) {
            if (is == null) {
                throw new RuntimeException("Missing animation file: " + atlasPath);
            }
            
            BufferedImage atlas = ImageIO.read(is);
            loadAnimationsFromAtlas(animations, atlas, animConfig);
            System.out.println("Successfully loaded for " + character);
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return animations;
    }
    
    private static String[][] getAnimationConfig(String character) {
        if ("SonTinh".equals(character)) {
            return new String[][] {
                {"IDLE", "8"}, {"MOVE", "8"}, {"JUMP", "9"},
                {"PUNCH", "19"}, {"DEFENSE", "3"}, {"TAKINGHIT", "3"},
                {"FALLINGBACKDEATH", "12"}, {"SUMMONSKILL", "6"}, {"DASH", "1"},
                {"SUMMONULTI", "6"}
            };
        } else if ("ThuyTinh".equals(character)) {
            return new String[][] {
                {"IDLE", "8"}, {"MOVE", "4"}, {"JUMP", "9"},
                {"PUNCH", "19"}, {"DEFENSE", "3"}, {"TAKINGHIT", "3"},
                {"FALLINGBACKDEATH", "12"}, {"SUMMONSKILL", "6"}, {"DASH", "1"},
                {"SUMMONULTI", "6"}
            };
        } else {
            return new String[][] {
                {"IDLE", "4"}, {"MOVE", "8"}
            };
        }
    }
    
    private static void loadAnimationsFromAtlas(BufferedImage[][] animations, BufferedImage atlas, String[][] config) {
        int currentRow = 0;
        
        for (int i = 0; i < config.length; i++) {
            int frameCount = Integer.parseInt(config[i][1]);
            
            for (int j = 0; j < frameCount; j++) {
                BufferedImage subImg = atlas.getSubimage(j * SPRITE_SIZE, i * SPRITE_SIZE, SPRITE_SIZE, SPRITE_SIZE);
                animations[currentRow][j] = subImg;
                animations[currentRow + 1][j] = flipHorizontally(subImg);
            }
            
            currentRow += 2;
        }
    }

    public static BufferedImage mergeImageGrid(BufferedImage[][] images) {
        int rows = 10, cols = 19;
        int tileWidth = images[0][0].getWidth();
        int tileHeight = images[0][0].getHeight();
        
        BufferedImage merged = new BufferedImage(cols * tileWidth, rows * tileHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = merged.createGraphics();
        
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2d.setComposite(AlphaComposite.SrcOver);
        
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                g2d.drawImage(images[i * 2][j], j * tileWidth, i * tileHeight, null);
            }
        }
        
        g2d.dispose();
        return merged;
    }

    // ===== SOUND LOADING =====
    
    public static void preloadSounds() {
        if (soundsPreloaded) return;
        
        System.out.println("Preloading sounds...");
        for (SoundManager se : SoundManager.values()) {
            int poolSize = getSoundPoolSize(se.getIndex());
            loadSfxPool(se, poolSize);
        }
        soundsPreloaded = true;
        System.out.println("Sounds preloaded successfully!");
    }
    
    private static int getSoundPoolSize(int index) {
        if (index < 4) return 1; // Music
        if (index == 12 || index == 23) return 1; // Special sounds
        return SFX_POOL_SIZE; // SFX
    }

    private static void loadSfxPool(SoundManager se, int poolSize) {
        if (se == null) return;
        
        try {
            URL soundURL = LoadSave.class.getResource(se.getPath());
            if (soundURL == null) {
                System.out.println("Can't load sound pool: " + se.getPath());
                return;
            }
            
            List<Clip> pool = new ArrayList<>();
            for (int i = 0; i < poolSize; i++) {
                Clip clip = createClip(soundURL);
                if (clip != null) pool.add(clip);
            }
            
            sfxPool.put(se.getIndex(), pool);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private static Clip createClip(URL soundURL) {
        try {
            AudioInputStream ais = AudioSystem.getAudioInputStream(soundURL);
            Clip clip = AudioSystem.getClip();
            clip.open(ais);
            clip.addLineListener(event -> {
                if (event.getType() == LineEvent.Type.STOP) {
                    clip.setFramePosition(0);
                }
            });
            return clip;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // ===== SOUND GETTERS =====
    
    public static Clip getAvailableSfxClip(int soundIndex) {
        List<Clip> pool = sfxPool.get(soundIndex);
        if (pool == null || pool.isEmpty()) return null;
        
        for (Clip clip : pool) {
            if (!clip.isRunning()) return clip;
        }
        
        return pool.get(0);
    }

    public static Clip getSoundClip(int soundIndex) {
        List<Clip> pool = sfxPool.get(soundIndex);
        return (pool != null && !pool.isEmpty()) ? pool.get(0) : null;
    }

    public static void stopAllInstancesOfSound(int soundIndex) {
        List<Clip> pool = sfxPool.get(soundIndex);
        if (pool == null) return;
        
        for (Clip clip : pool) {
            if (clip != null && clip.isRunning()) {
                clip.stop();
                clip.setFramePosition(0);
            }
        }
    }

    public static void stopAllInstancesOfSound(SoundManager se) {
        if (se != null) {
            stopAllInstancesOfSound(se.getIndex());
        }
    }
}