package ui;

import java.awt.*;

public class HealthBar {
    public int health, maxHealth;
   public  float animatedHealth;
    private int barWidth, barHeight;
    private boolean leftSide;

    private long lastHitTime;
    private static final long DAMAGE_ANIMATION_DELAY = 200; // Độ trễ 500ms (nửa giây)
    private static final float HEALTH_DROP_SPEED = 100f;

    public HealthBar(int maxHealth, boolean leftSide) {
        this.maxHealth = maxHealth;
        this.animatedHealth = maxHealth;
        this.health = maxHealth;
        this.barWidth = 250;
        this.barHeight = 25;
        this.leftSide = leftSide;
        this.lastHitTime = 0;
    }

    public void update() {
        if (animatedHealth > health) {
            long timeSinceHit = System.currentTimeMillis() - lastHitTime;
            if (timeSinceHit > DAMAGE_ANIMATION_DELAY) {
                animatedHealth = Math.max(health, animatedHealth - HEALTH_DROP_SPEED); // Giảm về 'health'
            }
        }
    }

    public void draw(Graphics2D g2, int x, int y) {
        g2.setColor(new Color(40, 40, 40));
        g2.fillRoundRect(x - 3, y - 3, barWidth + 6, barHeight + 6, 15, 15);
        g2.setColor(new Color(80, 0, 0));
        g2.fillRoundRect(x, y, barWidth, barHeight, 15, 15);

        float animatedPercent = animatedHealth / maxHealth;
        int animatedWidth = (int) (barWidth * animatedPercent);

        float healthPercent = (float) health / maxHealth;
        int healthWidth = (int) (barWidth * healthPercent);

        drawHealthGradient(g2, x, y, animatedWidth, animatedPercent);

        g2.setColor(new Color(180, 0, 0));
        int damageWidth = animatedWidth - healthWidth;
        if (leftSide) {
            g2.fillRoundRect(x + healthWidth, y, damageWidth, barHeight, 15, 15);
        } else {
            g2.fillRoundRect(x + (barWidth - animatedWidth), y, damageWidth, barHeight, 15, 15);
        }

        drawHealthGradient(g2, x, y, healthWidth, healthPercent);

        g2.setColor(Color.WHITE);
        g2.setStroke(new BasicStroke(2f));
        g2.drawRoundRect(x, y, barWidth, barHeight, 15, 15);
    }

    private void drawHealthGradient(Graphics2D g2, int x, int y, int width, float percent) {
        if (width <= 0) return;
        Color startColor = new Color(50, 200, 50);
        Color endColor = startColor.darker();
        GradientPaint gradient = leftSide ? new GradientPaint(x, y, startColor, x + width, y, endColor) : new GradientPaint(x + barWidth, y, startColor, x + barWidth - width, y, endColor);
        g2.setPaint(gradient);
        if (leftSide) g2.fillRoundRect(x, y, width, barHeight, 15, 15);
        else g2.fillRoundRect(x + (barWidth - width), y, width, barHeight, 15, 15);
    }

    public void takeDamage(int amount) {
        health = Math.max(0, health - amount);
        lastHitTime = System.currentTimeMillis();
    }

    public void setHealth(int health) {
        this.health = Math.max(0, Math.min(maxHealth, health));
        this.animatedHealth = this.health;
    }

    public int getHealth() {
        return health;
    }
}