package utilz;

public class Constants {
    public static class UI {
		public static class MenuButton {
			public static final int B_WIDTH = 140;
			public static final int B_HEIGHT = 56;
		}
	}

    public static class PlayerConstants {
        public static final int RIGHT = 0;
        public static final int LEFT = 1;

        // moving
        public static final int IDLE_RIGHT = 0;
        public static final int IDLE_LEFT = 1;
        public static final int MOVE_RIGHT = 2;
        public static final int MOVE_LEFT = 3;
        public static final int JUMP_RIGHT= 4;
        public static final int JUMP_LEFT = 5;
        
        // combat
        public static final int PUNCHING_RIGHT = 6;
        public static final int PUNCHING_LEFT = 7;
        public static final int ROUNDHOUSE_RIGHT = 8;
        public static final int ROUNDHOUSE_LEFT = 9;
        public static final int FLYING_KICK_RIGHT = 10;
        public static final int FLYING_KICK_LEFT = 11;
        
        public static int getFramesAmount(int player_action) {
            switch (player_action) {
                case IDLE_LEFT:
                case IDLE_RIGHT:
                    return 8;
                case MOVE_LEFT:
                case MOVE_RIGHT:
                    return 4;
                case JUMP_LEFT:
                case JUMP_RIGHT:
                    return 9;
                case PUNCHING_LEFT:
                case PUNCHING_RIGHT:
                    return 6;
                case ROUNDHOUSE_LEFT:
                case ROUNDHOUSE_RIGHT:
                    return 7;
                case FLYING_KICK_LEFT:
                case FLYING_KICK_RIGHT:
                    return 6;
                default:
                    return 0;
            }
        }
    }
    public static class GameConstants {
        public final static int FPS_SET = 120;
	    public final static int UPS_SET = 200;
        public final static int GAME_WIDTH = 1097;
	    public final static int GAME_HEIGHT = 768;

        // đây là thông số của cái bậc và thông số map
        public final static float platFormX1 = 320f, platFormX2 = 755f, platFormY = 465f;
    }
}