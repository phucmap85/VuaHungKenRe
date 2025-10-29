package entity;

public class EffectManager {
    private Effect[] effects;
    private int currentEffectIndex = 0;
    private static final int MAX_EFFECTS = 10;  // Số lượng effect tối đa có thể chạy đồng thời

    public EffectManager() {
        effects = new Effect[MAX_EFFECTS];
        // Khởi tạo mảng effects
        for (int i = 0; i < MAX_EFFECTS; i++) {
            effects[i] = new Effect();
        }
    }

    public void addEffect(float x, float y, int effectType) {
        // Tìm effect không active hoặc dùng effect tiếp theo trong mảng
        if (!effects[currentEffectIndex].isActive()) {
            effects[currentEffectIndex].setRender(x, y, effectType);
        } else {
            // Nếu effect hiện tại đang active, chuyển sang effect tiếp theo
            currentEffectIndex = (currentEffectIndex + 1) % MAX_EFFECTS;
            effects[currentEffectIndex].setRender(x, y, effectType);
        }
    }

    public void update() {
        // Update tất cả effects đang active
        for (Effect effect : effects) {
            if (effect.isActive()) {
                effect.update();
            }
        }
    }

    public void draw(java.awt.Graphics g) {
        // Vẽ tất cả effects đang active
        for (Effect effect : effects) {
            if (effect.isActive()) {
                effect.draw(g);
            }
        }
    }
}