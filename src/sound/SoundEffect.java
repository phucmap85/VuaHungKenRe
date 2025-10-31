package sound;

public enum SoundEffect {
    MENU(0),
    PLAYING(1),
    ENDING(2),

    SONTINHATTACK(3),
    SONTINHSUMMON(4),
    SONTINHULTI(5),
    SONTINHBLOCK(6),
    SONTINHDASH(7),
    SONTINHJUMP(8),
    SONTINHFALL(9),

    THUYTINHATTACK(10),
    THUYTINHSUMMON(11),
    THUYTINHULTI(12),
    THUYTINHBLOCK(13),
    THUYTINHDASH(14),
    THUYTINHJUMP(15),
    THUYTINHFALL(16);

    private final int index;

    SoundEffect(int idx) {
        this.index = idx;
    }

    public int getIndex() {
        return index;
    }
}
