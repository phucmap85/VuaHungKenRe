package gamestates;

public enum Gamestate {
    PLAYING, MENU, OPTIONS, QUIT, MANUAL, MATCH_SETUP;

    public static Gamestate state = MENU;
}
