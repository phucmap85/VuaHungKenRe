package ui;

import java.awt.*;

public class HealthBar {
    private int health, maxHealth;
    private int barWidth, barHeight;
    private boolean leftSide;

    public HealthBar(int maxHealth, boolean leftSide) {
        this.maxHealth = maxHealth;
        this.health = maxHealth;
        this.barWidth = 250;
        this.barHeight = 25;
        this.leftSide = leftSide;
    }

    public void draw(Graphics2D g2, int x, int y) {
        g2.setColor(new Color(40, 40, 40));
        g2.fillRoundRect(x - 3, y - 3, barWidth + 6, barHeight + 6, 15, 15);

        g2.setColor(new Color(80, 0, 0));
        g2.fillRoundRect(x, y, barWidth, barHeight, 15, 15);

        float percent = (float) health / maxHealth;
        int currentWidth = (int) (barWidth * percent);

        Color startColor = percent > 0.5 ? new Color(50, 200, 50)
                : percent > 0.25 ? new Color(255, 180, 0)
                : new Color(255, 50, 50);
        Color endColor = startColor.darker();

        GradientPaint gradient = leftSide
                ? new GradientPaint(x, y, startColor, x + currentWidth, y, endColor)
                : new GradientPaint(x + barWidth, y, startColor, x + barWidth - currentWidth, y, endColor);

        g2.setPaint(gradient);
        if (leftSide)
            g2.fillRoundRect(x, y, currentWidth, barHeight, 15, 15);
        else
            g2.fillRoundRect(x + (barWidth - currentWidth), y, currentWidth, barHeight, 15, 15);

        g2.setColor(Color.WHITE);
        g2.setStroke(new BasicStroke(2f));
        g2.drawRoundRect(x, y, barWidth, barHeight, 15, 15);
    }

    public void takeDamage(int amount) {
        health = Math.max(0, health - amount);
    }

    public void setHealth(int health) {
        this.health = Math.max(0, Math.min(maxHealth, health));
    }

    public int getHealth() { return health; }
}