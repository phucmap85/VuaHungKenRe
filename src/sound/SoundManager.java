package sound;

public enum SoundManager {
    MENU(0, "/sound/audio/menu.wav"),
    PLAYING(1, "/sound/audio/playing.wav"),
    SELECTION(2, "/sound/audio/selection.wav"),
    ENDING(3, "/sound/audio/ending.wav"),

    SONTINHPUNCH1(4, "/sound/audio/sontinh_punch1.wav"),
    SONTINHPUNCH2(5, "/sound/audio/sontinh_punch2.wav"),
    SONTINHSUMMON(6, "/sound/audio/sontinh_summon.wav"),
    SONTINHULTI(7, "/sound/audio/sontinh_ulti.wav"),
    SONTINHBLOCK(8, "/sound/audio/sontinh_block.wav"),
    SONTINHDASH(9, "/sound/audio/sontinh_dash.wav"),
    SONTINHJUMP(10, "/sound/audio/sontinh_jump.wav"),
    SONTINHFALL(11, "/sound/audio/sontinh_fall.wav"),
    SONTINHMOVING(12, "/sound/audio/sontinh_moving.wav"),
    SONTINHLANDING(13, "/sound/audio/sontinh_landing.wav"),
    SQUID(14, "/sound/audio/squid.wav"),

    THUYTINHPUNCH1(15, "/sound/audio/thuytinh_punch1.wav"),
    THUYTINHPUNCH2(16, "/sound/audio/thuytinh_punch2.wav"),
    THUYTINHSUMMON(17, "/sound/audio/thuytinh_summon.wav"),
    THUYTINHULTI(18, "/sound/audio/thuytinh_ulti.wav"),
    THUYTINHBLOCK(19, "/sound/audio/thuytinh_block.wav"),
    THUYTINHDASH(20, "/sound/audio/thuytinh_dash.wav"),
    THUYTINHJUMP(21, "/sound/audio/thuytinh_jump.wav"),
    THUYTINHFALL(22, "/sound/audio/thuytinh_fall.wav"),
    THUYTINHMOVING(23, "/sound/audio/thuytinh_moving.wav"),
    THUYTINHLANDING(24, "/sound/audio/thuytinh_landing.wav"),
    PHOENIX(25, "/sound/audio/phoenix.wav"),

    LIGHTNING(26, "/sound/audio/lightning.wav"),
    BOMB(27, "/sound/audio/bomb.wav"),

    CLICKBUTTON(28, "/sound/audio/button.wav"),

    KO(29, "/sound/audio/ko.wav");
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
