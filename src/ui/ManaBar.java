package ui;

import java.awt.*;

public class ManaBar {
    private int mana, maxMana;
    private int barWidth, barHeight;
    private boolean leftSide;

    private long lastRegenTime;
    private long blinkTimer;
    private boolean blinkState;

    public ManaBar(boolean leftSide) {
        this.maxMana = 2000;
        this.mana = 0;
        this.barWidth = 250;
        this.barHeight = 10;
        this.leftSide = leftSide;
        this.lastRegenTime = System.currentTimeMillis();
        this.blinkTimer = System.currentTimeMillis();
        this.blinkState = false;
    }

    public void draw(Graphics2D g2, int x, int y) {
        g2.setColor(new Color(20, 20, 60));
        g2.fillRoundRect(x, y, barWidth, barHeight, 10, 10);

        float manaPercent = (float) mana / maxMana;
        int manaWidth = (int) (barWidth * manaPercent);

        handleBlink();

        Color startColor, endColor;
        if (mana == maxMana && blinkState) {
            startColor = new Color(160, 224, 236, 255);
            endColor = new Color(0, 80, 200);
        } else {
            startColor = new Color(0, 100, 255);
            endColor = new Color(0, 50, 150);
        }

        GradientPaint manaGradient = new GradientPaint(x, y, startColor, x + barWidth, y, endColor);
        g2.setPaint(manaGradient);
        if (leftSide)
            g2.fillRoundRect(x, y, manaWidth, barHeight, 10, 10);
        else
            g2.fillRoundRect(x + (barWidth - manaWidth), y, manaWidth, barHeight, 10, 10);

        g2.setColor(Color.WHITE);
        g2.setStroke(new BasicStroke(1.5f));
        g2.drawRoundRect(x, y, barWidth, barHeight, 10, 10);

    }

    private void handleBlink() {
        if (mana == maxMana) {
            long now = System.currentTimeMillis();
            if (now - blinkTimer >= 400) {
                blinkState = !blinkState;
                blinkTimer = now;
            }
        } else {
            blinkState = false;
        }
    }

    public void update() {
        long now = System.currentTimeMillis();
        if (now - lastRegenTime >= 1000) {
            mana = Math.min(maxMana, mana + 30);
            lastRegenTime = now;
        }
    }

    public void takeMana(int amount) {
        mana = Math.max(0, mana - amount);
    }

    public void resetMana(){ mana = 0;}
    public void setMana(int amount) {
        mana = Math.min(maxMana, Math.max(0, amount));
    }
    public int getMana() { return mana; }

    public void plusMana(int amount) {
        mana = Math.min(maxMana, mana + amount);
    }
}
