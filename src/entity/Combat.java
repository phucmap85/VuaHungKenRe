package entity;

import static utilz.Constants.PlayerConstants.*;
import static utilz.HelpMethods.Collision;

import java.awt.Graphics;

public class Combat {
    private Character sonTinh;
    private Character thuyTinh;
    private SummonSkill hog = null;

    public Combat(Character sonTinh, Character thuyTinh) {
        this.sonTinh = sonTinh;
        this.thuyTinh = thuyTinh;
    }

    public void update() {
        if(sonTinh.callSummonedEntity() && hog == null) {
            sonTinh.setCallSummonedEntity(false);
            if(sonTinh.getDirection() == RIGHT) {
                hog = new SummonSkill(sonTinh.getX() + 100, sonTinh.getY(),
                        20f, 20f, 50f, 80f,
                        RIGHT, sonTinh.getCharacterName());
            } else {
                hog = new SummonSkill(sonTinh.getX() - 100, sonTinh.getY(),
                        20f, 20f, 50f, 80f,
                        LEFT, sonTinh.getCharacterName());
            }
        }
        if(hog != null){
            hog.update();
            if(!hog.isActive()){
                hog = null;
            }
        }
        if(!thuyTinh.falling()){
            if(hog != null){
            if(Collision(hog.getHitBox(), thuyTinh.getHurtBox())) {
                thuyTinh.setTakingHit(true);
                thuyTinh.setHealthTakenPerCombo(2);
                thuyTinh.setDirectionTakenHit(hog.getDirection());
            }
        }
            if(sonTinh.punching() && sonTinh.punch()){
                sonTinh.updateAttackBox();
                if(Collision(sonTinh.getHitBox(), thuyTinh.getHurtBox())) {
                    thuyTinh.setTakingHit(true);
                    thuyTinh.setHealthTakenPerCombo(1);
                    thuyTinh.setDirectionTakenHit(sonTinh.getDirection());
                }
            }
        }
       
    }

    public void render(Graphics g) {
        if (hog != null) {
            hog.render(g);
            hog.drawHitBox(g);
        }
    }
}
