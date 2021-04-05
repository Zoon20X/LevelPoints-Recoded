package me.zoon20x.levelpoints.containers.Settings.Configs.Rewards;

import org.bukkit.Sound;

public class RewardSound {

    private Sound sound;
    private float volume;
    private float pitch;

    public RewardSound(Sound sound, float volume, float pitch){
        this.sound = sound;
        this.volume = volume;
        this.pitch = pitch;
    }


    public Sound getSound() {
        return sound;
    }

    public float getVolume() {
        return volume;
    }

    public float getPitch() {
        return pitch;
    }
}
