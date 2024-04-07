package me.zoon20x.levelpoints.events;

import me.zoon20x.levelpoints.LevelPoints;
import me.zoon20x.levelpoints.containers.Blocks.BlockData;
import me.zoon20x.levelpoints.containers.Blocks.BlockSettings;
import me.zoon20x.levelpoints.containers.Mobs.MobData;
import me.zoon20x.levelpoints.containers.Mobs.MobSettings;
import me.zoon20x.levelpoints.containers.Player.PlayerData;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.inventory.meta.SpawnEggMeta;
import org.bukkit.material.SpawnEgg;

public class EXPEarnEvents implements Listener {

    private LevelPoints levelPoints;


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
        BlockSettings blockSettings = LevelPoints.getInstance().getLpsSettings().getBlockSettings();
        if(!blockSettings.isEnabled()){
            return;
        }

        if(!blockSettings.hasBlock(block.getType())){
            return;
        }

        BlockData blockData = blockSettings.getBlockData(block.getType());
        if(!LevelPoints.getInstance().getPlayerStorage().hasPlayer(player.getUniqueId())){
            return;
        }
        PlayerData playerData = LevelPoints.getInstance().getPlayerStorage().getPlayerInfo(player.getUniqueId());

        if(playerData.getLevel() < blockData.getBreakLevelRequired()){
            event.setCancelled(true);
            return;
        }
        LevelPoints.getInstance().getEventUtils().triggerEXPEarn(player, playerData, blockData.getExp(), event);


    }


    @EventHandler(priority = EventPriority.MONITOR)
    public void onBlockPlace(BlockPlaceEvent event){
        if(event.isCancelled()){
            return;
        }
        BlockSettings blockSettings = LevelPoints.getInstance().getLpsSettings().getBlockSettings();
        if(!blockSettings.isEnabled()){
            return;
        }

        Player player = event.getPlayer();
        Block block = event.getBlockPlaced();


        if(!blockSettings.hasBlock(block.getType())){
            return;
        }
        BlockData blockData = blockSettings.getBlockData(block.getType());
        if(!LevelPoints.getInstance().getPlayerStorage().hasPlayer(player.getUniqueId())){
            return;
        }
        PlayerData playerData = LevelPoints.getInstance().getPlayerStorage().getPlayerInfo(player.getUniqueId());
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
        MobSettings mobSettings = LevelPoints.getInstance().getLpsSettings().getMobSettings();
        if(!mobSettings.isEnabled()){
            return;
        }
        if(!mobSettings.hasMob(entity.getType())){
            return;
        }
        MobData mobData = mobSettings.getMobData(entity.getType());
        if(!LevelPoints.getInstance().getPlayerStorage().hasPlayer(player.getUniqueId())){
            return;
        }
        PlayerData playerData = LevelPoints.getInstance().getPlayerStorage().getPlayerInfo(player.getUniqueId());
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
        Player player = entity.getKiller();
        MobSettings mobSettings = LevelPoints.getInstance().getLpsSettings().getMobSettings();
        if(!mobSettings.isEnabled()){
            return;
        }
        if(!mobSettings.hasMob(entity.getType())){
            return;
        }
        MobData mobData = mobSettings.getMobData(entity.getType());
        if(!LevelPoints.getInstance().getPlayerStorage().hasPlayer(player.getUniqueId())){
            return;
        }
        PlayerData playerData = LevelPoints.getInstance().getPlayerStorage().getPlayerInfo(player.getUniqueId());
        LevelPoints.getInstance().getEventUtils().triggerEXPEarn(player, playerData, mobData.getExp(), event);
    }

}
