package sound;

public enum SoundEffect {
    MENU(0, "/sound/audio/menu.wav"),
    PLAYING(1, "/sound/audio/playing.wav"),
    ENDING(2, "/sound/audio/ending.wav"),

    SONTINHPUNCH(3, "/sound/audio/sonTinh_punch.wav"),
    SONTINHSUMMON(4, "/sound/audio/sonTinh_summon.wav"),
    SONTINHULTI(5, "/sound/audio/sonTinh_ulti.wav"),
    SONTINHBLOCK(6, "/sound/audio/sonTinh_block.wav"),
    SONTINHDASH(7, "/sound/audio/sonTinh_dash.wav"),
    SONTINHJUMP(8, "/sound/audio/sonTinh_jump.wav"),
    SONTINHFALL(9, "/sound/audio/sonTinh_fall.wav"),

    THUYTINHPUNCH(10, "/sound/audio/thuyTinh_punch.wav"),
    THUYTINHSUMMON(11, "/sound/audio/thuyTinh_summon.wav"),
    THUYTINHULTI(12, "/sound/audio/thuyTinh_ulti.wav"),
    THUYTINHBLOCK(13, "/sound/audio/thuyTinh_block.wav"),
    THUYTINHDASH(14, "/sound/audio/thuyTinh_dash.wav"),
    THUYTINHJUMP(15, "/sound/audio/thuyTinh_jump.wav"),
    THUYTINHFALL(16, "/sound/audio/thuyTinh_fall.wav");

    private final int index;
    private final String path;

    SoundEffect(int idx, String path) {
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
