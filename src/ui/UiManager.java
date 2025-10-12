package ui;

import java.awt.*;

public class UiManager {
    private HealthBar healthBar;
    private ManaBar manaBar;
    private boolean leftSide;

    public UiManager(int maxHealth, boolean leftSide) {
        this.leftSide = leftSide;
        this.healthBar = new HealthBar(maxHealth, leftSide);
        this.manaBar = new ManaBar(leftSide);
    }

    public void draw(Graphics g, int screenWidth) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int x = leftSide ? 30 : screenWidth - 280;
        int y = 30;

        healthBar.draw(g2, x, y);
        manaBar.draw(g2, x, y + 35);
    }

    public void takeDamage(int dmg) { healthBar.takeDamage(dmg); }
    public void setHealth(int hp) { healthBar.setHealth(hp); }
    public void setMana(int mp) { manaBar.setMana(mp); }

    public int getHealth() { return healthBar.getHealth(); }
    public int getMana() { return manaBar.getMana(); }
}