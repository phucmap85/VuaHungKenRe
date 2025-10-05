package utilz;

public class Constants {
    public static class PlayerConstants{
        public static final int RIGHT = 0;
        public static final int LEFT = 1;

        public static final int IDLE_RIGHT = 0;
        public static final int IDLE_LEFT = 1;
        public static final int MOVE_RIGHT = 2;
        public static final int MOVE_LEFT = 3;
        public static final int JUMP_RIGHT= 4;
        public static final int JUMP_LEFT = 5;
        
        public static int getFramesAmount(int player_action){
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
                
            
                default:
                    return 0;
            }
        }
    }
}
