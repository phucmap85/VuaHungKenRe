package utilz;

public class SoundConstants {
    //Background music
    public static final int MENU = 0;
    public static final int PLAYING = 1;
    public static final int ENDING = 2;
    // Sontinh
    public static final int SONTINHATTACK = 3;
    public static final int SONTINHSUMMON = 4;
    public static final int SONTINHULTI = 5;
    public static final int SONTINHBLOCK = 6;
    public static final int SONTINHDASH = 7;
    public static final int SONTINHJUMP = 8;
    public static final int SONTINHFALL = 9;

    //Thuytinh
    public static final int THUYTINHATTACK = 10;
    public static final int THUYTINHSUMMON = 11;
    public static final int THUYTINHULTI = 12;
    public static final int THUYTINHBLOCK = 13;
    public static final int THUYTINHDASH = 14;
    public static final int THUYTINHJUMP = 15;
    public static final int THUYTINHFALL = 16;

    // Sound file paths relative to resources
    public static final String MENU_PATH = "/sound/audio/menu.wav";
    public static final String PLAYING_PATH = "/sound/audio/playing.wav";
    public static final String ENDING_PATH = "/sound/audio/ending.wav";
    public static final String SONTINHATTACK_PATH = "/sound/audio/sonTinh_attack.wav";
    public static final String SONTINHSUMMON_PATH = "/sound/audio/sonTinh_summon.wav";
    public static final String SONTINHULTI_PATH = "/sound/audio/sonTinh_ulti.wav";
    public static final String SONTINHBLOCK_PATH = "/sound/audio/sonTinh_block.wav";
    public static final String SONTINHDASH_PATH = "/sound/audio/sonTinh_dash.wav";
    public static final String SONTINHJUMP_PATH = "/sound/audio/sonTinh_jump.wav";
    public static final String SONTINHFALL_PATH = "/sound/audio/sonTinh_fall.wav";
    public static final String THUYTINHATTACK_PATH = "/sound/audio/thuyTinh_attack.wav";
    public static final String THUYTINHSUMMON_PATH = "/sound/audio/thuyTinh_summon.wav";
    public static final String THUYTINHULTI_PATH = "/sound/audio/thuyTinh_ulti.wav";
    public static final String THUYTINHBLOCK_PATH = "/sound/audio/thuyTinh_block.wav";
    public static final String THUYTINHDASH_PATH = "/sound/audio/thuyTinh_dash.wav";
    public static final String THUYTINHJUMP_PATH = "/sound/audio/thuyTinh_jump.wav";
    public static final String THUYTINHFALL_PATH = "/sound/audio/thuyTinh_fall.wav";

    // Total number of sounds
    public static final int TOTAL_SOUNDS = 17;

    /**
     * Get the file path for a given sound index
     * @param soundIndex the index of the sound
     * @return the resource path for the sound file
     */
    public static String getPathForSound(int soundIndex) {
        switch (soundIndex) {
            case MENU: return MENU_PATH;
            case PLAYING: return PLAYING_PATH;
            case ENDING: return ENDING_PATH;
            case SONTINHATTACK: return SONTINHATTACK_PATH;
            case SONTINHSUMMON: return SONTINHSUMMON_PATH;
            case SONTINHULTI: return SONTINHULTI_PATH;
            case SONTINHBLOCK: return SONTINHBLOCK_PATH;
            case SONTINHDASH: return SONTINHDASH_PATH;
            case SONTINHJUMP: return SONTINHJUMP_PATH;
            case SONTINHFALL: return SONTINHFALL_PATH;
            case THUYTINHATTACK: return THUYTINHATTACK_PATH;
            case THUYTINHSUMMON: return THUYTINHSUMMON_PATH;
            case THUYTINHULTI: return THUYTINHULTI_PATH;
            case THUYTINHBLOCK: return THUYTINHBLOCK_PATH;
            case THUYTINHDASH: return THUYTINHDASH_PATH;
            case THUYTINHJUMP: return THUYTINHJUMP_PATH;
            case THUYTINHFALL: return THUYTINHFALL_PATH;
            default: throw new IllegalArgumentException("Invalid sound index: " + soundIndex);
        }
    }
    
    /**
     * Check if the given index is a valid sound index
     * @param index the index to check
     * @return true if the index is valid, false otherwise
     */
    public static boolean isValidSoundIndex(int index) {
        return index >= 0 && index < TOTAL_SOUNDS;
    }
}