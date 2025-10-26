package ui;

import java.awt.*;

public class PlayerUI {
    public HealthBar healthBar;
    private ManaBar manaBar;
    private boolean leftSide;

    public PlayerUI(int maxHealth, boolean leftSide) {
        this.leftSide = leftSide;
        this.healthBar = new HealthBar(maxHealth, leftSide);
        this.manaBar = new ManaBar(leftSide);
    }

    public void update() {
        healthBar.update();
        manaBar.update();
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
    public void takeMana(int mp) { manaBar.takeMana(mp); }

    public int getHealth() { return healthBar.getHealth(); }
    public int getMana() { return manaBar.getMana(); }
}