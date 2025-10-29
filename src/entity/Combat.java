package entity;

import ui.PlayerUI;

import static utilz.Constants.PlayerConstants.*;
import static utilz.HelpMethods.Collision;

import java.awt.Graphics;


public class Combat {
    private Character sonTinh;
    private Character thuyTinh;
    private PlayerUI sonTinhUI;
    private PlayerUI thuyTinhUI;
    private SummonSkill[] hog;
    private UltiSkill ulti;
    private long TimeInVulnerable = 500;
    public Combat(Character sonTinh, Character thuyTinh, PlayerUI sonTinhUI, PlayerUI thuyTinhUI) {
        this.sonTinh = sonTinh;
        this.thuyTinh = thuyTinh;
        this.sonTinhUI = sonTinhUI;
        this.thuyTinhUI = thuyTinhUI;
        hog = new SummonSkill[5];
    }

    public void update() {
        sonTinh.setMana(sonTinhUI.getMana());
        for (int i = 0; i < 5; i++) {
            if (sonTinh.callSummonedEntity() && hog[i] == null) {
                if (sonTinh.getDirection() == RIGHT) {
                    hog[i] = new SummonSkill(sonTinh.getX() + 40, sonTinh.getY(),
                            40f, 20f, 60f, 80f,
                            RIGHT, sonTinh.getCharacterName());
                } else {
                    hog[i] = new SummonSkill(sonTinh.getX() - 40, sonTinh.getY(),
                            40f, 20f, 60f, 80f,
                            LEFT, sonTinh.getCharacterName());
                }
                sonTinhUI.takeMana(25);
                sonTinh.setCallSummonedEntity(false);
            }
            if (hog[i] != null) {
                hog[i].update();
                if (!hog[i].isActive()) {
                    hog[i] = null;
                }
            }
            if (!thuyTinh.falling() && System.currentTimeMillis() - thuyTinh.getLastTimeFalling() > TimeInVulnerable) {
                if (hog[i] != null) {
                    if (Collision(hog[i].getHitBox(), thuyTinh.getHurtBox()) && !thuyTinh.dashing()) {
        
                        if(!hog[i].getCollision()) hog[i].setCollision(true);
                        if (thuyTinh.defending() && thuyTinh.getDirection() != hog[i].getDirection()) {
                            thuyTinh.setDefendDamageSignal(true);
                            thuyTinh.setHealthDefend(2);
                            thuyTinh.setDirectionTakenHit(hog[i].getDirection());
                        } else {
                            thuyTinh.setTakingHit(true);
                            thuyTinh.setHealthTakenPerCombo(2);
                            thuyTinhUI.takeDamage(2);
                            thuyTinh.setDirectionTakenHit(hog[i].getDirection());
                        }
                    }
                }
            }
        }

        if (!thuyTinh.falling() && System.currentTimeMillis() - thuyTinh.getLastTimeFalling() > TimeInVulnerable) {
            if (sonTinh.punching() && sonTinh.punch()) {
                sonTinh.updateAttackBox();
                if (Collision(sonTinh.getHitBox(), thuyTinh.getHurtBox()) && !thuyTinh.dashing()) {
                    
                    if (thuyTinh.defending() && thuyTinh.getDirection() != sonTinh.getDirection()) {
                        thuyTinh.setDefendDamageSignal(true);
                        thuyTinh.setHealthDefend(1);
                        thuyTinh.setDirectionTakenHit(sonTinh.getDirection());
                    } else {
                        thuyTinh.setTakingHit(true);
                        thuyTinh.setHealthTakenPerCombo(1);
                        thuyTinhUI.takeDamage(1);
                        thuyTinh.setDirectionTakenHit(sonTinh.getDirection());
                    }
                }
            }
        }
        
        if(sonTinh.callUltiEntity() && ulti == null) {
            ulti = new UltiSkill(0,0,0,0,0,0, sonTinh.getCharacterName());
            sonTinh.setCallUltiEntity(false);
        }
        if(ulti !=null && !thuyTinh.falling()) {
            ulti.update(thuyTinh.getX(), thuyTinh.getY());
            thuyTinh.setTakingHit(true); 
            thuyTinhUI.takeDamage(1);
            if(!ulti.isActive()) {
                ulti = null;
                thuyTinh.setTakingHit(false);
                thuyTinh.setFalling(true);
            }
        }
    }



    public void render(Graphics g) {
        for(int i = 0 ; i < 5 ; i++) {
            if (hog[i] != null) {
                hog[i].render(g);
                hog[i].drawHitBox(g);
            }
        }
        if(ulti != null) {
            ulti.render(g);
        }
    }
}
