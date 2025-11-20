package gamestates;

public enum Gamestate {
    PLAYING, MENU, QUIT, MANUAL, MATCH_SETUP, ENDING;

    public static Gamestate state = MENU;
}
