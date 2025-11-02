package sound;

public enum SoundManager {
    MENU(0, "/sound/audio/menu.wav"),
    PLAYING(1, "/sound/audio/playing.wav"),
    ENDING(2, "/sound/audio/ending.wav"),

    SONTINHPUNCH(3, "/sound/audio/sontinh_punch.wav"),
    SONTINHSUMMON(4, "/sound/audio/sontinh_summon.wav"),
    SONTINHULTI(5, "/sound/audio/sontinh_ulti.wav"),
    SONTINHBLOCK(6, "/sound/audio/sontinh_block.wav"),
    SONTINHDASH(7, "/sound/audio/sontinh_dash.wav"),
    SONTINHJUMP(8, "/sound/audio/sontinh_jump.wav"),
    SONTINHFALL(9, "/sound/audio/sontinh_fall.wav"),

    THUYTINHPUNCH(10, "/sound/audio/thuytinh_punch.wav"),
    THUYTINHSUMMON(11, "/sound/audio/thuytinh_summon.wav"),
    THUYTINHULTI(12, "/sound/audio/thuytinh_ulti.wav"),
    THUYTINHBLOCK(13, "/sound/audio/thuytinh_block.wav"),
    THUYTINHDASH(14, "/sound/audio/thuytinh_dash.wav"),
    THUYTINHJUMP(15, "/sound/audio/thuytinh_jump.wav"),
    THUYTINHFALL(16, "/sound/audio/thuytinh_fall.wav"),

    CLICKBUTTON(17, "/sound/audio/click_button.wav");

    private final int index;
    private final String path;

    SoundManager(int idx, String path) {
        this.index = idx;
        this.path = path;
    }

    public int getIndex() {
        return index;
    }

    public String getPath() {
        return path;
    }
}
