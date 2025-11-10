package sound;

public enum SoundManager {
    MENU(0, "/sound/audio/menu.wav"),
    PLAYING(1, "/sound/audio/playing.wav"),
    ENDING(2, "/sound/audio/ending.wav"),

    SONTINHPUNCH1(3, "/sound/audio/sontinh_punch1.wav"),
    SONTINHPUNCH2(4, "/sound/audio/sontinh_punch2.wav"),
    SONTINHSUMMON(5, "/sound/audio/sontinh_summon.wav"),
    SONTINHULTI(6, "/sound/audio/sontinh_ulti.wav"),
    SONTINHBLOCK(7, "/sound/audio/sontinh_block.wav"),
    SONTINHDASH(8, "/sound/audio/sontinh_dash.wav"),
    SONTINHJUMP(9, "/sound/audio/sontinh_jump.wav"),
    SONTINHFALL(10, "/sound/audio/sontinh_fall.wav"),
    SONTINHMOVING(11, "/sound/audio/sontinh_moving.wav"),
    SONTINHLANDING(12, "/sound/audio/sontinh_landing.wav"),
    SQUID(13, "/sound/audio/squid.wav"),

    THUYTINHPUNCH1(14, "/sound/audio/thuytinh_punch1.wav"),
    THUYTINHPUNCH2(15, "/sound/audio/thuytinh_punch2.wav"),
    THUYTINHSUMMON(16, "/sound/audio/thuytinh_summon.wav"),
    THUYTINHULTI(17, "/sound/audio/thuytinh_ulti.wav"),
    THUYTINHBLOCK(18, "/sound/audio/thuytinh_block.wav"),
    THUYTINHDASH(19, "/sound/audio/thuytinh_dash.wav"),
    THUYTINHJUMP(20, "/sound/audio/thuytinh_jump.wav"),
    THUYTINHFALL(21, "/sound/audio/thuytinh_fall.wav"),
    THUYTINHMOVING(22, "/sound/audio/thuytinh_moving.wav"),
    THUYTINHLANDING(23, "/sound/audio/thuytinh_landing.wav"),
    PHOENIX(24, "/sound/audio/phoenix.wav"),

    LIGHTNING(25, "/sound/audio/lightning.wav"),
    BOMB(26, "/sound/audio/bomb.wav"),

    CLICKBUTTON(27, "/sound/audio/click_button.wav");

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
