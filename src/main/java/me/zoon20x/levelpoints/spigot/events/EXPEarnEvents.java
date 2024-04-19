package me.zoon20x.levelpoints.spigot.events;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.util.Location;
import com.sk89q.worldedit.world.World;
import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.internal.platform.WorldGuardPlatform;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import com.sk89q.worldguard.protection.regions.RegionQuery;
import io.lumine.mythic.bukkit.MythicBukkit;
import io.lumine.mythic.bukkit.utils.lib.jooq.impl.QOM;
import me.zoon20x.levelpoints.spigot.LevelPoints;
import me.zoon20x.levelpoints.spigot.containers.Blocks.BlockData;
import me.zoon20x.levelpoints.spigot.containers.Blocks.BlockSettings;
import me.zoon20x.levelpoints.spigot.containers.Farming.FarmData;
import me.zoon20x.levelpoints.spigot.containers.Farming.FarmSettings;
import me.zoon20x.levelpoints.spigot.containers.Mobs.MobData;
import me.zoon20x.levelpoints.spigot.containers.Mobs.MobSettings;
import me.zoon20x.levelpoints.spigot.containers.Player.PlayerData;
import me.zoon20x.levelpoints.spigot.containers.World.WorldSettings;
import me.zoon20x.levelpoints.spigot.containers.WorldGuardSettings;
import me.zoon20x.levelpoints.spigot.events.CustomEvents.EventUtils;
import me.zoon20x.levelpoints.spigot.events.CustomEvents.FarmEvent;
import me.zoon20x.levelpoints.spigot.utils.AntiAbuse;
import org.bukkit.CropState;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.data.Ageable;
import org.bukkit.enchantments.Enchantment;
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
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.Crops;
import org.bukkit.material.MaterialData;

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
        if(LevelPoints.getInstance().getLpsSettings().getWorldSettings().isEnabled()){
            WorldSettings worldSettings = LevelPoints.getInstance().getLpsSettings().getWorldSettings();
            if(worldSettings.hasWorld(player.getWorld().getName())){
                return;
            }
        }



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
        if(!AntiAbuse.checkSilkTouch(player)){
            return;
        }

        if(LevelPoints.getInstance().isWorldGuardEnabled()){
            if(!AntiAbuse.checkWorldGuard(BukkitAdapter.adapt(block.getLocation()))){
                return;
            }
        }

        levelPoints.getEventUtils().triggerEXPEarn(player, playerData, blockData.getExp(), event);


    }



    @EventHandler(priority = EventPriority.MONITOR)
    public void onBlockPlace(BlockPlaceEvent event){
        if(event.isCancelled()){
            return;
        }
        Player player = event.getPlayer();
        if(LevelPoints.getInstance().getLpsSettings().getWorldSettings().isEnabled()){
            WorldSettings worldSettings = LevelPoints.getInstance().getLpsSettings().getWorldSettings();
            if(worldSettings.hasWorld(player.getWorld().getName())){
                return;
            }
        }
        BlockSettings blockSettings = levelPoints.getLpsSettings().getBlockSettings();
        if(!blockSettings.isEnabled()){
            return;
        }


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
        if(LevelPoints.getInstance().getLpsSettings().getWorldSettings().isEnabled()){
            WorldSettings worldSettings = LevelPoints.getInstance().getLpsSettings().getWorldSettings();
            if(worldSettings.hasWorld(player.getWorld().getName())){
                return;
            }
        }
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
        if(LevelPoints.getInstance().getLpsSettings().getWorldSettings().isEnabled()){
            WorldSettings worldSettings = LevelPoints.getInstance().getLpsSettings().getWorldSettings();
            if(worldSettings.hasWorld(player.getWorld().getName())){
                return;
            }
        }
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

    @EventHandler
    public void onFarmEvent(FarmEvent event){
        Player player = event.getPlayer();
        if(event.isCancelled()){
            return;
        }
        Ageable crop = event.getCrop();
        FarmSettings farmSettings = LevelPoints.getInstance().getLpsSettings().getFarmSettings();
        if(!farmSettings.isEnabled()){
            return;
        }
        PlayerData playerData = levelPoints.getPlayerStorage().getPlayerData(player.getUniqueId());
        if(!farmSettings.hasFarm(crop.getMaterial())){
            return;
        }
        FarmData data = farmSettings.getFarmData(crop.getMaterial());
        if(!AntiAbuse.checkWorldGuard(BukkitAdapter.adapt(event.getBlock().getLocation()))){
            return;
        }
        if(event.isRipe()){
            if(playerData.getLevel() < data.getFarmRequired()){
                event.setCancelled(true);
                return;
            }
            levelPoints.getEventUtils().triggerEXPEarn(player, playerData, data.getExp(), event);
        }
    }



    @EventHandler(priority = EventPriority.MONITOR)
    public void triggerFarmEvent(BlockBreakEvent event) {
        if (event.isCancelled()) {
            return;
        }
        Player player = event.getPlayer();
        if (LevelPoints.getInstance().getLpsSettings().getWorldSettings().isEnabled()) {
            WorldSettings worldSettings = LevelPoints.getInstance().getLpsSettings().getWorldSettings();
            if (worldSettings.hasWorld(player.getWorld().getName())) {
                return;
            }
        }
        Block block = event.getBlock();
        org.bukkit.block.data.BlockData blockData = block.getBlockData();
        if(!(blockData instanceof Ageable)){
            System.out.println(blockData);
            return;
        }
        Ageable ageable = (Ageable) blockData;
        LevelPoints.getInstance().getEventUtils().triggerFarmEvent(block, player);
    }

}
