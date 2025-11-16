package utilz;

public class Constants {
    public static class UI {
		public static class MenuButton {
			public static final int B_WIDTH = 140;
			public static final int B_HEIGHT = 56;

            public static final int TOTAL_FRAMES = 141;
		}

        public static class KeyButton {
            public static final int K_WIDTH = 19;
			public static final int K_HEIGHT = 21;
            public static final int K_YPOS = 295;
            public static final int K_XPOS = 80;
            public static final int K_OFFSET = 60;
        }

        public static class PauseButton {
            public static final int SOUND_SIZE_DEFAULT = 42;
            public static final double SOUND_SCALE = 1.5;
            public static final int SOUND_SIZE = (int) (SOUND_SIZE_DEFAULT * SOUND_SCALE);
        }

        public static class URMButton {
            public static final int URM_DEFAULT_SIZE = 56;
            public static final double URM_SCALE = 1.5;
            public static final int URM_SIZE = (int) (URM_DEFAULT_SIZE * URM_SCALE);
        }

        public static class VolumeButton {
            public static final int VOLUME_DEFAULT_WIDTH = 28;
            public static final int VOLUME_DEFAULT_HEIGHT = 44;
            public static final int SLIDER_DEFAULT_WIDTH = 215;
            public static final double VOLUME_SCALE = 1.5;

            public static final int VOLUME_WIDTH = (int) (VOLUME_DEFAULT_WIDTH * VOLUME_SCALE);
            public static final int VOLUME_HEIGHT = (int) (VOLUME_DEFAULT_HEIGHT * VOLUME_SCALE);
            public static final int SLIDER_WIDTH = (int) (SLIDER_DEFAULT_WIDTH * VOLUME_SCALE);
        }
	}

    public static class PlayerConstants {
        // directions
        public static final int RIGHT = 0;
        public static final int LEFT = 1;

        // moving
        public static final int IDLE_RIGHT = 0;
        public static final int IDLE_LEFT = 1;
        public static final int MOVE_RIGHT = 2;
        public static final int MOVE_LEFT = 3;
        public static final int JUMP_RIGHT= 4;
        public static final int JUMP_LEFT = 5;
        public static final int PUNCH_RIGHT = 6;
        public static final int PUNCH_LEFT = 7;
        public static final int DEFEND_RIGHT = 8;
        public static final int DEFEND_LEFT = 9;

        public static final int TAKING_HIT_RIGHT = 10;
        public static final int TAKING_HIT_LEFT = 11;

        public static final int FALLING_RIGHT = 12;
        public static final int FALLING_LEFT = 13;

        public static final int SUMMONSKILL_RIGHT = 14;
        public static final int SUMMONSKILL_LEFT = 15;

        public static final int DASH_RIGHT = 16;
        public static final int DASH_LEFT = 17;

        public static final int SUMMONULTI_RIGHT = 18;
        public static final int SUMMONULTI_LEFT = 19;

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
                case PUNCH_LEFT:
                case PUNCH_RIGHT:
                    return 19;
                case DEFEND_LEFT:
                case DEFEND_RIGHT:
                    return 3;
                case TAKING_HIT_LEFT:
                case TAKING_HIT_RIGHT:
                    return 3;            
                case FALLING_LEFT:
                case FALLING_RIGHT:
                    return 12;
                case SUMMONSKILL_LEFT:
                case SUMMONSKILL_RIGHT:
                    return 6;
                case DASH_LEFT:
                case DASH_RIGHT:
                    return 1;
                case SUMMONULTI_LEFT:
                case SUMMONULTI_RIGHT:
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

        /*// đây là thông số của cái bậc và thông số map
        public final static float platFormX1 = 320f, platFormX2 = 755f, platFormY = 470f;
        public final static float groundY = 640f;*/
    }

    public static class EffectConstants {
        // Effect types with right (0) and left (1) directions
        public static final int IMPACT1_RIGHT = 0;
        public static final int IMPACT1_LEFT = 1;
        
        public static final int IMPACT2_RIGHT = 2;
        public static final int IMPACT2_LEFT = 3;
        
        public static final int PUNCHSLASH_RIGHT = 4;
        public static final int PUNCHSLASH_LEFT = 5;
        
        public static final int SHIELD_RIGHT = 6;
        public static final int SHIELD_LEFT = 7;
        
        public static final int SLASH_RIGHT = 8;
        public static final int SLASH_LEFT = 9;
        
        public static final int SMEAR_RIGHT = 10;
        public static final int SMEAR_LEFT = 11;
        
        public static final int SMOKE_RIGHT = 12;
        public static final int SMOKE_LEFT = 13;
        
        public static final int LANDING_RIGHT = 14;
        public static final int LANDING_LEFT = 15;
        public static int getFramesAmount(int effectType) {
            switch (effectType) {
                case IMPACT1_LEFT:
                case IMPACT1_RIGHT:
                    return 3;
                case IMPACT2_LEFT:
                case IMPACT2_RIGHT:
                    return 9;
                case PUNCHSLASH_LEFT:
                case PUNCHSLASH_RIGHT:
                    return 5;
                case SHIELD_LEFT:
                case SHIELD_RIGHT:
                    return 2;
                case SLASH_LEFT:
                case SLASH_RIGHT:
                    return 4;
                case SMEAR_LEFT:
                case SMEAR_RIGHT:
                    return 5;
                case SMOKE_LEFT:
                case SMOKE_RIGHT:
                    return 9;
                case LANDING_LEFT:
                case LANDING_RIGHT:
                    return 9;
                default:
                    return 0;
            }
        }
    }
}