package sound;

import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import utilz.LoadSave;

public class SoundPlayer {
    private Clip currentMusicClip;
    

    private float musicVolume = 0.5f;
    private float sfxVolume = 0.5f; // (0.0 đến 1.0)

    public void setMusicVolume(float volume) {
        this.musicVolume = Math.max(0.0f, Math.min(1.0f, volume));
        
        // Áp dụng ngay lập tức cho nhạc nền đang phát
        if (currentMusicClip != null) {
            setClipVolume(currentMusicClip, this.musicVolume);
        }
    }

    public void setSfxVolume(float volume) {
        this.sfxVolume = Math.max(0.0f, Math.min(1.0f, volume));
    }

    public float getMusicVolume() { return musicVolume; }
    public float getSfxVolume() { return sfxVolume; }

    private void setClipVolume(Clip clip, float volume) {
        if (clip == null) return;
        try {
            FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            
            // Chuyển đổi giá trị tuyến tính (0.0-1.0) sang logarit (Decibel)
            // Dùng -80f cho mức 0 (tắt tiếng)
            float dB = (volume == 0.0f) ? -80.0f : (float) (Math.log10(volume) * 20.0);
            
            dB = Math.min(dB, gainControl.getMaximum());
            dB = Math.max(dB, gainControl.getMinimum());
            
            gainControl.setValue(dB);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    public void play(SoundManager se) {
        if (se == null) return;
        Clip clip = LoadSave.getSoundClip(se.getIndex());

        if (clip != null) {
            setClipVolume(clip, sfxVolume);
            clip.setFramePosition(0);
            clip.start();
        }
    }
    

    public void loop(SoundManager se) {
        stopMusic(); 
        currentMusicClip = LoadSave.getSoundClip(se.getIndex());
        
        if (currentMusicClip != null) {
            setClipVolume(currentMusicClip, musicVolume);
            currentMusicClip.setFramePosition(0);
            currentMusicClip.loop(Clip.LOOP_CONTINUOUSLY);
        }
    }
    
    public void stopMusic() {
        if (currentMusicClip != null) {
            currentMusicClip.stop();
            currentMusicClip.setFramePosition(0);
        }
    }
}