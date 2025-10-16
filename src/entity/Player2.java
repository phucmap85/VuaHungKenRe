package entity;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import ui.PlayerUI;

import static utilz.HelpMethods.*;
import static utilz.Constants.PlayerConstants.*;
import static utilz.Constants.GameConstants.*;
import utilz.LoadSave;

public class Player2 extends Entity {
    // Movement states
    protected boolean left, right, jump, defense, punch, tornado;
    private boolean moving = false;
    protected boolean inAir = false;
    private boolean defending = false;
    protected boolean tornadoing = false;
    protected int direction = LEFT;
    
    // ===== PUNCH STATE (Tách riêng) =====
    private boolean punching = false;
    protected int punchFrameIndex = 0;        // Frame hiện tại của punch animation
    protected int punchFrameCounter = 0;       // Counter để control tốc độ
    private long lastPunchTime = 0;          // Thời điểm punch cuối
    private final long PUNCH_RESET_TIME = 200; // 0.3 giây = 300ms
    private final int MAX_PUNCH_FRAMES = 19;   // Tổng 19 frames
    
    protected int tornadoFrameIndex = 0;       // Frame hiện tại của tornado animation
    protected int tornadoFrameCounter = 0;     // Counter để control tốc độ
    protected final int MAX_TORNADO_FRAMES = 6;  // TỔNG SỐ FRAME CỦA TORNADO
    private int tornadoAnimationSpeed = 25;  // Tốc độ animation (càng lớn càng chậm)

    // Animation (cho các action khác)
    private int playerAction = IDLE_RIGHT;
    protected BufferedImage[][] animations = null;
    private int framesCounter = 0, framesIndex = 0;
    private int animationSpeed = 20;
    private int jumpAnimationSpeed = 15;
    private int punchAnimationSpeed = 20; // Tốc độ animation punch (2 = nhanh)
    
    // Physics
    private float speed = 2.0f;
    private float punchSpeed = 0.3f;
    private float jumpStrength = -6.5f;
    private float gravity = 0.1f;
    private float velocityY = 0;
    private float groundY;

    public Player2(float x, float y, float width, float height,float xOffSet,float yOffSet) {
        super(x, y, width, height, xOffSet, yOffSet);
        initHitbox();
        groundY = y;
        loadAnimation();
    }
    
    public void update() { 
        updatePunchState();    // Update punch state TRƯỚC
        updateTornadoState();
        updateAnimationTick();
        setAnimation();
        updatePos();
    }
    
    public void render(Graphics g) { 
        Graphics2D g2 = (Graphics2D) g;
        
        // Nếu đang punch, vẽ punch animation
        if (punching) {
            int punchAction = (direction == LEFT) ? PUNCH_LEFT : PUNCH_RIGHT;
            g2.drawImage(animations[punchAction][punchFrameIndex], 
                        (int) x, (int) y, 128, 128, null);
        } 
        // Không punch, vẽ animation bình thường
        else if(tornadoing){
            int tornadoAction = (direction == LEFT) ? TORNADO_LEFT : TORNADO_RIGHT;
            g2.drawImage(animations[tornadoAction][tornadoFrameIndex], 
                        (int) x, (int) y, 128, 128, null);      
        }
        else {
            g2.drawImage(animations[playerAction][framesIndex], 
                        (int) x, (int) y, 128, 128, null);
        }
    }
    private void updateTornadoState() {
        // Chỉ chạy khi đang trong trạng thái tornadoing
        if (!tornadoing) return;
        
        tornadoFrameCounter++;
        if (tornadoFrameCounter >= tornadoAnimationSpeed) {
            tornadoFrameCounter = 0;
            tornadoFrameIndex++;
            
            // KHI ANIMATION CHẠY XONG -> TỰ ĐỘNG RESET
            if (tornadoFrameIndex >= MAX_TORNADO_FRAMES) {
                resetTornadoState(); // Tự động kết thúc chiêu
            }
        }
    }

    private void resetTornadoState() {
        tornadoing = false;
        tornadoFrameIndex = 0;
        tornadoFrameCounter = 0;
    }
    // ===== PUNCH STATE MANAGEMENT =====
    private void updatePunchState() {
        long currentTime = System.currentTimeMillis();
        
        // Nếu đang punch
        if (punching) {
            // Update frame animation
            punchFrameCounter++;
            if (punchFrameCounter >= punchAnimationSpeed) {
                punchFrameCounter = 0;
                punchFrameIndex++;
       
                
                // Đã chạy hết animation
                if (punchFrameIndex >= MAX_PUNCH_FRAMES) {
                    punchFrameIndex = 0; // Giữ ở frame cuối
                }
            }
            
            // Kiểm tra timeout - Reset về IDLE nếu quá lâu không nhấn J
            if ((currentTime - lastPunchTime > PUNCH_RESET_TIME) && !punch) {
                System.out.println("Punch timeout - Reset to IDLE");
                resetPunchState();
            }
        }
    }
    
    // Reset punch state về đầu
    private void resetPunchState() {
        punching = false;
        punchFrameIndex = 0;
        punchFrameCounter = 0;
    }
    
    // Setter cho punch - QUAN TRỌNG
    public void setPunch(boolean punch) {
        this.punch = punch;
        System.out.println(punch);
        if (punch && !inAir && !tornadoing) { // Chỉ punch khi không trong không khí
            // Nếu chưa đang punch → Bắt đầu mới
            if (!punching) {
                punching = true;
                punchFrameIndex = 0;
                punchFrameCounter = 0;
                System.out.println("Start punching from frame 0");
            } 
            // Nếu đang punch → Tiếp tục combo (không reset frame)
            else {
                System.out.println("Continue combo at frame " + punchFrameIndex);
            }
            
            // Cập nhật thời gian punch cuối
            lastPunchTime = System.currentTimeMillis();
        }
    }

    private void updatePos() {
        moving = false;
        if(tornado && !inAir)  {
            tornadoing = true;
        }
        else if(tornadoing == true) return;
        // ===== XỬ LÝ PUNCH =====
        else if (punching) {
            defending = false;
            
             if(punchFrameIndex % 3 == 0){ // update theo frames cho nó mượt hơn 
 
            if (direction == RIGHT) {
                if (hitbox.x + hitbox.width + punchSpeed <= GAME_WIDTH) 
                    x += punchSpeed;
            } else {
                if (hitbox.x - punchSpeed >= 0) 
                    x -= punchSpeed;
            }
            updateHitbox();
            
                }
            if (isInAir(y, hitbox, groundY)) {
                inAir = true;
            }
            
            // Áp dụng trọng lực
            if (inAir) {
                velocityY += gravity;
                if (isOnPlatForm(hitbox, velocityY)) {
                    y = platFormY - yOffSet - height;
                    velocityY = 0;
                    inAir = false;
                } else {
                    y += velocityY;
                }
                
                if (y >= groundY) {
                    y = groundY;
                    velocityY = 0;
                    inAir = false;
                }
                
                // Hủy punch khi rơi
                resetPunchState();
            }
        }
        // ===== XỬ LÝ DEFENSE =====
        else if (defense && !punch && !inAir) {
            defending = true;
        }
        // ===== XỬ LÝ DI CHUYỂN BÌNH THƯỜNG =====
        else {
            defending = false;
            
            if (left && !right) {
                if (hitbox.x - speed >= 0) {
                    x -= speed;
                    moving = true;
                }
                direction = LEFT;
            } else if (right && !left) {
                if (hitbox.x + hitbox.width + speed <= GAME_WIDTH) {
                    x += speed;
                    moving = true;
                }
                direction = RIGHT;
            }
            
            updateHitbox();
            
            // Kiểm tra rơi
            if (isInAir(y, hitbox, groundY)) {
                inAir = true;
            }
            
            // Nhảy
            if (jump && !inAir) {
                velocityY = jumpStrength;
                inAir = true;
            }
            
            // Áp dụng trọng lực
            if (inAir) {
                defending = false;

                velocityY += gravity;
                if (isOnPlatForm(hitbox, velocityY)) {
                    y = platFormY - yOffSet - height;
                    velocityY = 0;
                    inAir = false;
                } else {
                    y += velocityY;
                }
                
                if (y >= groundY) {
                    y = groundY;
                    velocityY = 0;
                    inAir = false;
                }
            }
        }
        
        updateHitbox();
    }

    private void updateAnimationTick() {
        // Không update animation tick nếu đang punch
        // (Punch có animation riêng)
        if (punching) return;
        if(tornadoing) return;
        framesCounter++;
        
        int currentSpeed = animationSpeed;
        if (playerAction == JUMP_LEFT || playerAction == JUMP_RIGHT) {
            currentSpeed = jumpAnimationSpeed;
        }
        
        if (framesCounter >= currentSpeed) {
            framesCounter = 0;
            
            // Defense animation (giữ frame cuối)
            if (playerAction == DEFEND_LEFT || playerAction == DEFEND_RIGHT) {
                if (framesIndex < getFramesAmount(playerAction) - 1) {
                    framesIndex++;
                }
            } 
            // Các animation khác (loop)
            else {
                framesIndex++;
                if (framesIndex >= getFramesAmount(playerAction)) {
                    framesIndex = 0;
                }
            }
        }
    }

    private void setAnimation() {
        // Không set animation nếu đang punch
        if (punching) return;
        if(tornadoing) return;
        int startAnim = playerAction;
        
        if (inAir) {
            playerAction = (direction == LEFT) ? JUMP_LEFT : JUMP_RIGHT;
        } else if (defending) {
            playerAction = (direction == LEFT) ? DEFEND_LEFT : DEFEND_RIGHT;
        } else if (moving) {
            playerAction = (direction == LEFT) ? MOVE_LEFT : MOVE_RIGHT;
        } else {
            playerAction = (direction == LEFT) ? IDLE_LEFT : IDLE_RIGHT;
        }
        
        if (startAnim != playerAction) {
            resetAnimationTick();
        }
    }

    private void resetAnimationTick() {
        framesCounter = 0;
        framesIndex = 0;
    }

    private void loadAnimation() {
        animations = LoadSave.GetAnimation("SonTinh");
    }
    public void resetDirBooleans() {
        left = false;
        right = false;
        jump = false;
        defense = false;
        punch = false;
    }

    // Setters
    public void setLeft(boolean left) { this.left = left; }
    public void setRight(boolean right) { this.right = right; }
    public void setJump(boolean jump) { this.jump = jump; }
    public void setDefense(boolean defense) { this.defense = defense; }
    public void setTornado(boolean tornado) { this.tornado = tornado; }
    // setPunch đã được định nghĩa ở trên
    
    // Getters
    public boolean isLeft() { return left; }
    public boolean isRight() { return right; }
    public boolean isJump() { return jump; }
    public boolean isDefense() { return defense; }
    public boolean isPunch() { return punch; }
    public boolean isPunching() { return punching; }
    public boolean isTornadoing() { return tornadoing; }    
    public int getPunchFrame() { return punchFrameIndex; }

}

// ============================================
// HƯỚNG DẪN ĐIỀU CHỈNH
// ============================================

/*
 * 1. THAY ĐỔI THỜI GIAN RESET:
 * -----------------------------
 * private final long PUNCH_RESET_TIME = 300; // 0.3 giây
 * 
 * - Nhỏ hơn (200-250ms) = Phải nhấn nhanh hơn
 * - Lớn hơn (400-500ms) = Dễ combo hơn
 * 
 * 
 * 2. THAY ĐỔI TỐC ĐỘ ANIMATION:
 * ------------------------------
 * private int punchAnimationSpeed = 2;
 * 
 * - Nhỏ hơn (1) = Animation rất nhanh
 * - Lớn hơn (3-4) = Animation chậm hơn
 * 
 * 
 * 3. CHIA COMBO THEO FRAME:
 * -------------------------
 * Trong setPunch(), có thể thêm:
 * 
 * if (punchFrameIndex >= 0 && punchFrameIndex < 6) {
 *     // Đang ở combo 1 (đấm)
 * } else if (punchFrameIndex >= 6 && punchFrameIndex < 12) {
 *     // Đang ở combo 2 (đá)
 * } else {
 *     // Đang ở combo 3 (đạp)
 * }
 * 
 * 
 * 4. FORCE RESET KHI NHẤN DEFENSE:
 * --------------------------------
 * public void setDefense(boolean defense) {
 *     this.defense = defense;
 *     if (defense && punching) {
 *         resetPunchState(); // Cancel punch
 *     }
 * }
 */