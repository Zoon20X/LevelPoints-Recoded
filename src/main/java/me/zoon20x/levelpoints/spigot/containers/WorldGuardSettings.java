package me.zoon20x.levelpoints.spigot.containers;

import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.flags.Flag;
import com.sk89q.worldguard.protection.flags.IntegerFlag;
import com.sk89q.worldguard.protection.flags.StringFlag;
import com.sk89q.worldguard.protection.flags.registry.FlagConflictException;
import com.sk89q.worldguard.protection.flags.registry.FlagRegistry;
import com.sk89q.worldguard.session.SessionManager;

public class WorldGuardSettings {
    private final IntegerFlag levelRequiredFlag;
    private final StringFlag levelRequiredDenyMessage;

    public WorldGuardSettings() {
      levelRequiredFlag = new IntegerFlag("level-required-entry");
      levelRequiredDenyMessage = new StringFlag("level-required-deny-message");
      loadFlag(levelRequiredFlag);
      loadFlag(levelRequiredDenyMessage);
    }

    private void loadFlag(Flag flag) {
        FlagRegistry registry = WorldGuard.getInstance().getFlagRegistry();
        try {
            registry.register(flag);
        } catch (FlagConflictException e) {
            e.printStackTrace();
        }
    }

    public void loadHandler(){
        SessionManager sessionManager = WorldGuard.getInstance().getPlatform().getSessionManager();
        sessionManager.registerHandler(LevelRequiredHandler.FACTORY, null);
    }

    public IntegerFlag getLevelRequiredFlag(){
        return levelRequiredFlag;
    }

    public StringFlag getLevelRequiredDenyMessage() {
        return levelRequiredDenyMessage;
    }
}
