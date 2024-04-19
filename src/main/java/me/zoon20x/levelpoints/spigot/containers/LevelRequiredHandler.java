package me.zoon20x.levelpoints.spigot.containers;

import com.sk89q.worldedit.util.Location;
import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.session.MoveType;
import com.sk89q.worldguard.session.Session;

import com.sk89q.worldguard.session.handler.Handler;
import com.sk89q.worldguard.util.MessagingUtil;
import me.zoon20x.levelpoints.spigot.LevelPoints;
import me.zoon20x.levelpoints.spigot.containers.Player.PlayerData;


import java.util.Set;

public class LevelRequiredHandler extends Handler  {

    public static final Factory FACTORY = new Factory();

    public static class Factory extends Handler.Factory<LevelRequiredHandler> {
        @Override
        public LevelRequiredHandler create(Session session) {
            return new LevelRequiredHandler(session);
        }
    }

    public LevelRequiredHandler(Session session) {
        super(session);
    }

    @Override
    public boolean onCrossBoundary(LocalPlayer player, Location from, Location to, ApplicableRegionSet toSet, Set<ProtectedRegion> entered, Set<ProtectedRegion> exited, MoveType moveType) {
        if(toSet.queryValue(player, LevelPoints.getInstance().getWorldGuardSettings().getLevelRequiredFlag()) == null){
            return true;
        }
        int levelRequired = toSet.queryValue(player, LevelPoints.getInstance().getWorldGuardSettings().getLevelRequiredFlag());
        PlayerData data = LevelPoints.getInstance().getPlayerStorage().getPlayerData(player.getUniqueId());
        if(data.getLevel() < levelRequired){
            if(toSet.queryValue(player, LevelPoints.getInstance().getWorldGuardSettings().getLevelRequiredDenyMessage()) == null){
                return false;
            }
            String message = toSet.queryValue(player, LevelPoints.getInstance().getWorldGuardSettings().getLevelRequiredDenyMessage());
            message = message.replace("{level_required}", String.valueOf(levelRequired));
            MessagingUtil.sendStringToChat(player, message);
            return false;
        }

        return true;
    }


}