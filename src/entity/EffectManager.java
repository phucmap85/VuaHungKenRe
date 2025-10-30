package entity;

public class EffectManager {
    private Effect[] effects;
    private int currentEffectIndex = 0;
    private int maxEffects;  // Số lượng effect tối đa có thể chạy đồng thời

    public EffectManager(int maxEffects) {
        this.maxEffects = maxEffects;
        effects = new Effect[maxEffects];
        // Khởi tạo mảng effects
        for (int i = 0; i < maxEffects; i++) {
            effects[i] = new Effect();
        }
    }

    public void addEffect(float x, float y, int effectType) {
        // Tìm effect không active hoặc dùng effect tiếp theo trong mảng
        if (!effects[currentEffectIndex].isActive()) {
            effects[currentEffectIndex].setRender(x, y, effectType);
        } else {
            if(!effects[(currentEffectIndex +1) % maxEffects].isActive()){
                currentEffectIndex = (currentEffectIndex + 1) % maxEffects;
                effects[currentEffectIndex].setRender(x, y, effectType);
            }
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