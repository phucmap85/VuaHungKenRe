package sound;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineEvent;

public class Sound {
    private Clip currentClip;
    private final Map<Integer, Clip[]> soundCache;
    private static final int CACHE_SIZE = 3; // Number of clips to cache for each sound
    private final Map<Integer, SoundEffect> indexToEffect = new HashMap<>();

    public Sound() {
        soundCache = new HashMap<>();
        buildIndexMap();
        preloadSounds();
    }

    /**
     * Preloads all sound effects into the cache
     */
    private void preloadSounds() {
        System.out.println("Preloading sounds...");
        for (SoundEffect se : SoundEffect.values()) {
            int idx = se.getIndex();
            if (!soundCache.containsKey(idx)) {
                loadSound(se);
            }
        }
        System.out.println("Sound preloading complete!");
    }

    /**
     * Loads multiple instances of a sound into the cache
     */
    private void loadSound(SoundEffect se) {
        if (se == null) return;

        try {
            String path = se.getPath();
            URL soundURL = getClass().getResource(path);
            if (soundURL == null) {
                System.err.println("Could not find sound file: " + path);
                return;
            }

            Clip[] clips = new Clip[CACHE_SIZE];
            for (int i = 0; i < CACHE_SIZE; i++) {
                AudioInputStream ais = AudioSystem.getAudioInputStream(soundURL);
                Clip clip = AudioSystem.getClip();
                clip.open(ais);
                
                // Add listener to handle clip completion
                clip.addLineListener(event -> {
                    if (event.getType() == LineEvent.Type.STOP) {
                        clip.setFramePosition(0); // Reset clip to beginning
                    }
                });
                
                clips[i] = clip;
            }
            soundCache.put(se.getIndex(), clips);
            
        } catch (Exception e) {
            System.err.println("Error loading sound " + (se != null ? se.getIndex() : "?") + ": " + e.getMessage());
        }
    }

    /**
     * Gets the next available clip for the given sound index
     */
    private Clip getNextClip(int soundIndex) {
        Clip[] clips = soundCache.get(soundIndex);
        if (clips == null) {
            return null;
        }

        // Find a clip that's not playing
        for (Clip clip : clips) {
            if (!clip.isRunning()) {
                return clip;
            }
        }

        // If all clips are playing, use the first one
        return clips[0];
    }

    public void setFile(int i) {
        if (!indexToEffect.containsKey(i)) {
            System.err.println("Invalid sound index: " + i);
            return;
        }

        currentClip = getNextClip(i);
        if (currentClip != null) {
            currentClip.setFramePosition(0);
        }
    }

    /**
     * Set the current clip by SoundEffect enum
     */
    public void setFile(SoundEffect se) {
        if (se == null) return;
        setFile(se.getIndex());
    }

    /**
     * Play a sound directly by SoundEffect enum (helper)
     */
    public void play(SoundEffect se) {
        if (se == null) return;
        setFile(se);
        play();
    }

    private void buildIndexMap() {
        for (SoundEffect se : SoundEffect.values()) {
            indexToEffect.put(se.getIndex(), se);
        }
    }

    public void play() {
        if (currentClip != null && !currentClip.isRunning()) {
            currentClip.start();
        }
    }

    public void loop() {
        if (currentClip != null) {
            currentClip.loop(Clip.LOOP_CONTINUOUSLY);
        }
    }

    public void stop() {
        if (currentClip != null) {
            currentClip.stop();
        }
    }
    
    /**
     * Clean up resources when the sound system is no longer needed
     */
    public void cleanup() {
        for (Clip[] clips : soundCache.values()) {
            for (Clip clip : clips) {
                if (clip.isRunning()) {
                    clip.stop();
                }
                clip.close();
            }
        }
        soundCache.clear();
    }
}
