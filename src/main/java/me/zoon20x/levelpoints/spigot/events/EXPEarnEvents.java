package me.zoon20x.levelpoints.spigot.events;

import io.lumine.mythic.bukkit.MythicBukkit;
import me.zoon20x.levelpoints.spigot.LevelPoints;
import me.zoon20x.levelpoints.spigot.containers.Blocks.BlockData;
import me.zoon20x.levelpoints.spigot.containers.Blocks.BlockSettings;
import me.zoon20x.levelpoints.spigot.containers.Mobs.MobData;
import me.zoon20x.levelpoints.spigot.containers.Mobs.MobSettings;
import me.zoon20x.levelpoints.spigot.containers.Player.PlayerData;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;

public class EXPEarnEvents implements Listener {

    private final LevelPoints levelPoints;


    public EXPEarnEvents(LevelPoints levelPoints){
        this.levelPoints = levelPoints;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onBlockBreak(BlockBreakEvent event){
        if(event.isCancelled()){
            return;
        }

        Player player = event.getPlayer();
        Block block = event.getBlock();
        BlockSettings blockSettings = levelPoints.getLpsSettings().getBlockSettings();
        if(!blockSettings.isEnabled()){
            return;
        }

        if(!blockSettings.hasBlock(block.getType())){
            return;
        }

        BlockData blockData = blockSettings.getBlockData(block.getType());
        if(!levelPoints.getPlayerStorage().hasPlayer(player.getUniqueId())){
            return;
        }
        PlayerData playerData = levelPoints.getPlayerStorage().getPlayerData(player.getUniqueId());

        if(playerData.getLevel() < blockData.getBreakLevelRequired()){
            event.setCancelled(true);
            return;
        }
        levelPoints.getEventUtils().triggerEXPEarn(player, playerData, blockData.getExp(), event);


    }


    @EventHandler(priority = EventPriority.MONITOR)
    public void onBlockPlace(BlockPlaceEvent event){
        if(event.isCancelled()){
            return;
        }
        BlockSettings blockSettings = levelPoints.getLpsSettings().getBlockSettings();
        if(!blockSettings.isEnabled()){
            return;
        }

        Player player = event.getPlayer();
        Block block = event.getBlockPlaced();


        if(!blockSettings.hasBlock(block.getType())){
            return;
        }
        BlockData blockData = blockSettings.getBlockData(block.getType());
        if(!levelPoints.getPlayerStorage().hasPlayer(player.getUniqueId())){
            return;
        }
        PlayerData playerData = levelPoints.getPlayerStorage().getPlayerData(player.getUniqueId());
        if(playerData.getLevel() < blockData.getPlaceLevelRequired()){
            event.setCancelled(true);
            return;
        }

    }


    @EventHandler(priority = EventPriority.MONITOR)
    public void onAttackEntity(EntityDamageByEntityEvent event){
        if(event.isCancelled()){
            return;
        }
        if(!(event.getDamager() instanceof Player)){
            return;
        }
        if(event.getEntity() instanceof Player){
            return;
        }
        Player player = (Player) event.getDamager();
        Entity entity = event.getEntity();
        MobSettings mobSettings = levelPoints.getLpsSettings().getMobSettings();
        if(!mobSettings.isEnabled()){
            return;
        }


        boolean isMythicMob = MythicBukkit.inst().getMobManager().isMythicMob(entity);
        String entityType;
        if(isMythicMob){
            entityType = MythicBukkit.inst().getMobManager().getMythicMobInstance(entity).getType().getInternalName();
        }else{
            entityType = entity.getType().toString();
        }
        if(!mobSettings.hasMob(entityType)){
            return;
        }
        MobData mobData = mobSettings.getMobData(entityType);
        if(!levelPoints.getPlayerStorage().hasPlayer(player.getUniqueId())){
            return;
        }
        PlayerData playerData = levelPoints.getPlayerStorage().getPlayerData(player.getUniqueId());
        if(playerData.getLevel() < mobData.getAttackRequired()){
            event.setCancelled(true);
            return;
        }

    }
    @EventHandler(priority = EventPriority.MONITOR)
    public void onEntityDeath(EntityDeathEvent event){
        if(event.getEntity() instanceof Player){
            return;
        }

        LivingEntity entity = event.getEntity();
        if(entity.getKiller() == null){
            return;
        }
        Player player = entity.getKiller();
        String entityType;
        if(LevelPoints.getInstance().getLpsSettings().isMythicMobsEnabled()){
            boolean isMythicMob = MythicBukkit.inst().getMobManager().isMythicMob(entity);
            if(isMythicMob){
                entityType = MythicBukkit.inst().getMobManager().getMythicMobInstance(entity).getType().getInternalName();
            }else{
                entityType = entity.getType().toString();
            }
        }else{
            entityType = entity.getType().toString();
        }
        MobSettings mobSettings = levelPoints.getLpsSettings().getMobSettings();
        if(!mobSettings.isEnabled()){
            return;
        }
        if(!mobSettings.hasMob(entityType)){
            return;
        }
        MobData mobData = mobSettings.getMobData(entityType);
        if(!levelPoints.getPlayerStorage().hasPlayer(player.getUniqueId())){
            return;
        }
        PlayerData playerData = levelPoints.getPlayerStorage().getPlayerData(player.getUniqueId());
        levelPoints.getEventUtils().triggerEXPEarn(player, playerData, mobData.getExp(), event);
    }

}
