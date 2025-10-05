package ui;

import java.awt.*;

public class UIManager {
    private int health;
    private int maxHealth;
    private int mana;
    private int maxMana;

    private int barWidth = 250;
    private int barHeight = 25;
    private int manaBarHeight = 10;

    private boolean leftSide;
    private long lastManaRegenTime;
    private long blinkTimer;
    private boolean blinkState;

    public UIManager(int maxHealth, boolean leftSide) {
        this.maxHealth = maxHealth;
        this.health = maxHealth;
        this.maxMana = 100;
        this.mana = maxMana / 4; // bắt đầu với 25% mana
        this.leftSide = leftSide;
        this.lastManaRegenTime = System.currentTimeMillis();
        this.blinkTimer = System.currentTimeMillis();
        this.blinkState = false;
    }

    public void draw(Graphics g, int screenWidth) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // === Vị trí hiển thị ===
        int x = leftSide ? 30 : screenWidth - barWidth - 30;
        int y = 30;

        // === Vẽ thanh máu ===
        drawHealthBar(g2, x, y);

        // === Vẽ thanh mana ===
        drawManaBar(g2, x, y + barHeight + 8);

        // === Hồi mana ===
        regenMana();
    }

    private void drawHealthBar(Graphics2D g2, int x, int y) {
        // Viền ngoài
        g2.setColor(new Color(40, 40, 40));
        g2.fillRoundRect(x - 3, y - 3, barWidth + 6, barHeight + 6, 15, 15);

        // Nền tối
        g2.setColor(new Color(80, 0, 0));
        g2.fillRoundRect(x, y, barWidth, barHeight, 15, 15);

        // Phần máu còn lại
        float healthPercent = (float) health / maxHealth;
        int currentWidth = (int) (barWidth * healthPercent);

        Color startColor;
        if (healthPercent > 0.5)
            startColor = new Color(50, 200, 50);
        else if (healthPercent > 0.25)
            startColor = new Color(255, 180, 0);
        else
            startColor = new Color(255, 50, 50);

        Color endColor = startColor.darker();

        GradientPaint gradient = leftSide
                ? new GradientPaint(x, y, startColor, x + currentWidth, y, endColor)
                : new GradientPaint(x + barWidth, y, startColor, x + barWidth - currentWidth, y, endColor);

        g2.setPaint(gradient);
        if (leftSide)
            g2.fillRoundRect(x, y, currentWidth, barHeight, 15, 15);
        else
            g2.fillRoundRect(x + (barWidth - currentWidth), y, currentWidth, barHeight, 15, 15);

        // Viền trắng
        g2.setColor(Color.WHITE);
        g2.setStroke(new BasicStroke(2f));
        g2.drawRoundRect(x, y, barWidth, barHeight, 15, 15);
    }

    private void drawManaBar(Graphics2D g2, int x, int y) {
        // Nền mana
        g2.setColor(new Color(20, 20, 60));
        g2.fillRoundRect(x, y, barWidth, manaBarHeight, 10, 10);

        float manaPercent = (float) mana / maxMana;
        int manaWidth = (int) (barWidth * manaPercent);

        // Hiệu ứng nhấp nháy khi đầy mana
        if (mana == maxMana) {
            long now = System.currentTimeMillis();
            if (now - blinkTimer >= 400) { // đổi trạng thái mỗi 0.4s
                blinkState = !blinkState;
                blinkTimer = now;
            }
        } else {
            blinkState = false;
        }

        // Chọn màu gradient
        Color startColor, endColor;
        if (mana == maxMana) {
            if (blinkState) {
                startColor = new Color(160, 224, 236, 255); // sáng hơn
                endColor = new Color(0, 80, 200);
            } else {
                startColor = new Color(0, 100, 255); // đậm hơn
                endColor = new Color(0, 50, 150);
            }
        } else {
            startColor = new Color(0, 100, 255);
            endColor = new Color(0, 50, 150);
        }

        GradientPaint manaGradient = new GradientPaint(
                x, y, startColor,
                x + barWidth, y, endColor
        );

        g2.setPaint(manaGradient);
        if (leftSide)
            g2.fillRoundRect(x, y, manaWidth, manaBarHeight, 10, 10);
        else
            g2.fillRoundRect(x + (barWidth - manaWidth), y, manaWidth, manaBarHeight, 10, 10);

        // Viền trắng
        g2.setColor(Color.WHITE);
        g2.setStroke(new BasicStroke(1.5f));
        g2.drawRoundRect(x, y, barWidth, manaBarHeight, 10, 10);
    }

    private void regenMana() {
        long now = System.currentTimeMillis();
        if (now - lastManaRegenTime >= 1000) { // +5 mỗi giây
            mana = Math.min(maxMana, mana + 5);
            lastManaRegenTime = now;
        }
    }

    // === Khi dùng ulti ===
    public void useUlti() {
        if(this.getMana() == this.maxMana) {
            mana = 0;
        }
    }

    // ===== Setter / Getter =====
    public void takeDamage(int amount) {
        health -= amount;
        if (health < 0) health = 0;
    }

    public void setHealth(int health) {
        this.health = Math.max(0, Math.min(maxHealth, health));
    }

    public void setMana(int mana) {
        this.mana = Math.max(0, Math.min(maxMana, mana));
    }

    public int getHealth() { return health; }
    public int getMana() { return mana; }
}
