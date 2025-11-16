package entity;

import java.awt.Graphics;

public class EffectManager {
    private Effect[] effects;
    private int currentEffectIndex = 0;
    private int maxEffects;

    public EffectManager(int maxEffects) {
        this.maxEffects = maxEffects;
        effects = new Effect[maxEffects];
        for (int i = 0; i < maxEffects; i++) {
            effects[i] = new Effect();
        }
    }

    public void addEffect(float x, float y, int effectType) {
        Effect current = effects[currentEffectIndex];
        
        if (!current.isActive()) {
            current.setRender(x, y, effectType);
        } else {
            int nextIndex = (currentEffectIndex + 1) % maxEffects;
            if (!effects[nextIndex].isActive()) {
                currentEffectIndex = nextIndex;
                effects[currentEffectIndex].setRender(x, y, effectType);
            }
        }
    }

    public void update() {
        for (Effect effect : effects) {
            if (effect.isActive()) {
                effect.update();
            }
        }
    }

    public void draw(Graphics g) {
        for (Effect effect : effects) {
            if (effect.isActive()) {
                effect.draw(g);
            }
        }
    }
}

