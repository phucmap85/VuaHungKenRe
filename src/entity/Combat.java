package entity;

import ui.PlayerUI;
import java.awt.Graphics;

import main.Game;
import sound.SoundManager;

import static utilz.Constants.PlayerConstants.*;
import static utilz.Constants.EffectConstants.*;
import static utilz.HelpMethods.Collision;

public class Combat {
    private Character sonTinh, thuyTinh;
    private PlayerUI sonTinhUI, thuyTinhUI;
    private SummonSkill[] hog;
    private UltiSkill ulti;
    private EffectManager effectManager;
    private final long TimeInVulnerable = 500;

    public Combat(Character sonTinh, Character thuyTinh, PlayerUI sonTinhUI, PlayerUI thuyTinhUI) {
        this.sonTinh = sonTinh;
        this.thuyTinh = thuyTinh;
        this.sonTinhUI = sonTinhUI;
        this.thuyTinhUI = thuyTinhUI;
        hog = new SummonSkill[6];
        effectManager = new EffectManager(1);
    }

    public void update() {
        sonTinh.setMana(sonTinhUI.getMana());
        updateSummonedEntities();
        updatePunchAttack();
        updateUltiAttack();
        effectManager.update();
    }
    
    private void updateSummonedEntities() {
        for (int i = 0; i < 5; i++) {
            spawnSummonIfNeeded(i);
            updateSummonEntity(i);
            checkSummonCollision(i);
        }
    }
    
    private void spawnSummonIfNeeded(int index) {
        if (!sonTinh.callSummonedEntity() || hog[index] != null) return;
        
        float offsetX = (sonTinh.getDirection() == RIGHT) ? 40 : -40;
        int direction = sonTinh.getDirection();
        
        hog[index] = new SummonSkill(
            sonTinh.getX() + offsetX, sonTinh.getY(),
            40f, 20f, 60f, 80f,
            direction, sonTinh.getCharacterName()
        );
        
        sonTinhUI.takeMana(400);
        sonTinh.setCallSummonedEntity(false);
    }
    
    private void updateSummonEntity(int index) {
        if (hog[index] == null) return;
        
        hog[index].update();
        if (!hog[index].isActive()) {
            hog[index] = null;
        }
    }
    
    private void checkSummonCollision(int index) {
        if (hog[index] == null) return;
        if (!canTakeDamage(thuyTinh)) return;
        if (!Collision(hog[index].getHitBox(), thuyTinh.getHurtBox()) || thuyTinh.dashing()) return;
        
        if (!hog[index].getCollision()) {
            hog[index].setCollision(true);
        }
        
        if (isDefendingAgainst(thuyTinh, hog[index].getDirection())) {
            applyDefendDamage(thuyTinh, hog[index].getDirection(), 2);
        } else {
            applySummonDamage(index);
            
        }
    }
    
    private void applySummonDamage(int index) {
        thuyTinh.setTakingHit(true);
        thuyTinh.setHealthTakenPerCombo(2);
        thuyTinh.setDirectionTakenHit(hog[index].getDirection());
        
        if (!hog[index].getdoneTakeHealth()) {
            thuyTinhUI.takeDamage(8000);
            hog[index].setDoneTakeHealth(true);
            sonTinhUI.regenMana(400);
        }
    }
    
    private void updatePunchAttack() {
        if (!canTakeDamage(thuyTinh)) return;
        if (!sonTinh.punching() || !sonTinh.punch()) return;
        
        sonTinh.updateAttackBox();
        
        if (!Collision(sonTinh.getHitBox(), thuyTinh.getHurtBox()) || thuyTinh.dashing()) return;
        

        
        if (isDefendingAgainst(thuyTinh, sonTinh.getDirection())) {
            applyDefendDamage(thuyTinh, sonTinh.getDirection(), 1);
        } else {
            applyPunchDamage();
            
        }
    }
    
    private void applyPunchDamage() {
        thuyTinh.setTakingHit(true);
        thuyTinh.setHealthTakenPerCombo(1);
        thuyTinh.setDirectionTakenHit(sonTinh.getDirection());
        thuyTinhUI.takeDamage(150);
        sonTinhUI.regenMana(3);
        
        if (thuyTinh.getDirection() != sonTinh.getDirection()) {
            addImpactEffect(thuyTinh);
        }
    }
    
    private void updateUltiAttack() {
        spawnUltiIfNeeded();
        
        if (ulti == null) return;
        
        ulti.update(thuyTinh.getX(), thuyTinh.getY());
        
        if (ulti.lightningAppeared() && !thuyTinh.falling()) {
            applyUltiDamage();
        }
        
        if (!ulti.isActive()) {
            deactivateUlti();
        }
    }
    
    private void spawnUltiIfNeeded() {
        if (!sonTinh.callUltiEntity() || ulti != null) return;
        
        ulti = new UltiSkill(0, 0, 0, 0, 0, 0, sonTinh.getCharacterName());
        sonTinh.setCallUltiEntity(false);
        sonTinhUI.takeMana(2000);
    }
    
    private void applyUltiDamage() {
        thuyTinh.setTakingHit(true);
        thuyTinh.setDirectionTakenHit(ulti.getDirection());
        
        int damage = "SonTinh".equals(sonTinh.getCharacterName()) ? 199 : 269;
        thuyTinhUI.takeDamage(damage);
        
        if ("SonTinh".equals(sonTinh.getName())) {
            thuyTinh.setDirectionTakenHit(sonTinh.getDirection());
        }
    }
    
    private void deactivateUlti() {
        ulti = null;
        thuyTinh.setTakingHit(false);
        thuyTinh.setFalling(true);
        Game.soundPlayer.play(SoundManager.THUYTINHFALL);
    }
    
    // Helper methods
    private boolean canTakeDamage(Character character) {
        return !character.falling() && 
               (System.currentTimeMillis() - character.getLastTimeFalling() > TimeInVulnerable);
    }
    
    private boolean isDefendingAgainst(Character defender, int attackDirection) {
        return defender.defending() && defender.getDirection() != attackDirection;
    }
    
    private void applyDefendDamage(Character defender, int attackDirection, int damageAmount) {
        defender.setDefendDamageSignal(true);
        defender.setHealthDefend(damageAmount);
        defender.setDirectionTakenHit(attackDirection);
        addSlashEffect(defender);
    }
    
    private void addSlashEffect(Character character) {
        int effectType = (character.getDirection() == RIGHT) ? SLASH_RIGHT : SLASH_LEFT;
        float effectX = (character.getDirection() == RIGHT) ? character.getX() + 50 : character.getX();
        effectManager.addEffect(effectX, character.getY() - 30, effectType);
    }
    
    private void addImpactEffect(Character character) {
        int effectType = (character.getDirection() == RIGHT) ? IMPACT1_RIGHT : IMPACT1_LEFT;
        float effectX = (character.getDirection() == RIGHT) ? character.getX() + 45 : character.getX() - 15;
        effectManager.addEffect(effectX, character.getY() - 85, effectType);
    }

    public void render(Graphics g) {
        for (int i = 0; i < 5; i++) {
            if (hog[i] != null) {
                hog[i].render(g);
            }
        }
        
        if (ulti != null) {
            ulti.render(g);
        }
        
        if (effectManager != null) {
            effectManager.draw(g);
        }
    }

    public void resetCombat(Character sonTinh, Character thuyTinh, PlayerUI sonTinhUI, PlayerUI thuyTinhUI) {
        this.sonTinh = sonTinh;
        this.thuyTinh = thuyTinh;
        this.sonTinhUI = sonTinhUI;
        this.thuyTinhUI = thuyTinhUI;
        
        ulti = null;
        for (int i = 0; i < 5; i++) {
            hog[i] = null;
        }
    }
}


