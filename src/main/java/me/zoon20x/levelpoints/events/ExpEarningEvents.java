package me.zoon20x.levelpoints.events;

import io.lumine.xikage.mythicmobs.MythicMobs;
import io.lumine.xikage.mythicmobs.mobs.ActiveMob;
import me.zoon20x.levelpoints.LevelPoints;
import me.zoon20x.levelpoints.containers.ExtraSupport.MythicMobsData;
import me.zoon20x.levelpoints.containers.Player.PlayerData;
import me.zoon20x.levelpoints.containers.Settings.Blocks.BlockData;
import me.zoon20x.levelpoints.containers.Settings.Blocks.BlockUtils;
import me.zoon20x.levelpoints.containers.Settings.Blocks.Requirement;
import me.zoon20x.levelpoints.containers.Settings.Crafting.CraftingData;
import me.zoon20x.levelpoints.containers.Settings.Crafting.CraftingUtils;
import me.zoon20x.levelpoints.events.CustomEvents.EarnTask;
import me.zoon20x.levelpoints.events.CustomEvents.EventUtils;
import me.zoon20x.levelpoints.utils.DebugSeverity;
import me.zoon20x.levelpoints.utils.Formatter;
import me.zoon20x.levelpoints.utils.MessageUtils;
import me.zoon20x.levelpoints.utils.Permissions.PermissionUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityBreedEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;


public class ExpEarningEvents implements Listener {

    @EventHandler
    public void onKillMob(EntityDeathEvent event) {
        if (event.getEntity().getKiller() == null) {
            return;
        }
        EntityType entityType = event.getEntity().getType();
        Player player = event.getEntity().getKiller();
        PlayerData data = LevelPoints.getPlayerStorage().getLoadedData(player.getUniqueId());
        if(LevelPoints.isMythicMobsEnabled()){
            if(MythicMobs.inst().getAPIHelper().isMythicMob(event.getEntity())){
                ActiveMob mob = MythicMobs.inst().getAPIHelper().getMythicMobInstance(event.getEntity());
                if(!LevelPoints.getMythicMobsSettings().hasMobData(mob.getType().getInternalName())){
                    return;
                }
                EventUtils.triggerEarnExpEvent(data, event, LevelPoints.getMythicMobsSettings().getMobData(mob.getType().getInternalName()).getExp(), player, EarnTask.Mobs);
                return;
            }
        }


        if (LevelPoints.getExpSettings().expType(entityType.name()).equals("none")) {
            return;
        }

        LevelPoints.getDebug(DebugSeverity.NORMAL, LevelPoints.getExpSettings().expType(entityType.name()));
        if(!player.hasPermission(PermissionUtils.getPlayerPermission().expMobs())){
            return;
        }


        EventUtils.triggerEarnExpEvent(data, event, LevelPoints.getExpSettings().getMobEXP(entityType), player, EarnTask.Mobs);
    }

    @EventHandler
    public void onBreed(EntityBreedEvent event){
        if(event.isCancelled()){
            return;
        }
        LivingEntity livingEntity = event.getBreeder();
        if(!(livingEntity instanceof Player)){
            return;
        }
        Player player = (Player) event.getBreeder();
        PlayerData data = LevelPoints.getPlayerStorage().getLoadedData(player.getUniqueId());
        if(!LevelPoints.getLevelSettings().canBreed(event.getEntity().getType(), data)){
            event.setCancelled(true);
            return;
        }
        EventUtils.triggerEarnExpEvent(data, event, LevelPoints.getExpSettings().getBreedEXP(event.getEntity().getType()), player, EarnTask.Breeding);

    }


    @EventHandler
    public void onPlace(BlockPlaceEvent event){
        Player player = event.getPlayer();
        PlayerData data = LevelPoints.getPlayerStorage().getLoadedData(player.getUniqueId());
        Block block = event.getBlock();

        if(!LevelPoints.getLevelSettings().canPlace(block.getType(), block.getData(), data)){
            BlockData blockData = BlockUtils.getBlockData(block.getType(), block.getData());
            if(LevelPoints.getLangSettings().isRequiredPlaceEnabled()){
                Formatter formatter = new Formatter(player.getName(), data.getLevel(), data.getExp(), blockData.getRequiredEXP(Requirement.PLACE, data), data.getPrestige(), blockData.getPlaceRequired(), data.getProgress());
                String message = MessageUtils.getColor(LevelPoints.getLangSettings().getRequiredPlace());
                player.sendMessage(MessageUtils.format(message, formatter));
            }
            event.setCancelled(true);
            return;
        }
    }


    @EventHandler
    public void onCraft(CraftItemEvent event){
        Player player = (Player) event.getWhoClicked();
        ItemStack item = event.getCurrentItem();
        if(LevelPoints.getExpSettings().expType(item.getType().toString()).equalsIgnoreCase("none")){
            return;
        }
        int itemsChecked = 0;
        int possibleCreations = 1;
        if (event.isShiftClick()) {
            for (ItemStack items : event.getInventory().getMatrix()) {
                if (items != null && !items.getType().equals(Material.AIR)) {
                    if (itemsChecked == 0)
                        possibleCreations = items.getAmount();
                    else
                        possibleCreations = Math.min(possibleCreations, items.getAmount());
                    itemsChecked++;
                }
            }
        }
        int amountOfItems = event.getRecipe().getResult().getAmount() * possibleCreations;
        PlayerData data = LevelPoints.getPlayerStorage().getLoadedData(player.getUniqueId());
        CraftingData craftingData = CraftingUtils.getCraftingData(item.getType());
        if(data.getLevel() < craftingData.getCraftingRequired()) {
            event.setCancelled(true);
            return;
        }
        double exp = craftingData.getExp() * amountOfItems;

        EventUtils.triggerEarnExpEvent(data, event, exp, player, EarnTask.Craft);
    }


    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        PlayerData data = LevelPoints.getPlayerStorage().getLoadedData(player.getUniqueId());
        if(data.getBracketData() != null){
            LevelPoints.getDebug(DebugSeverity.SEVER, data.getBracketData().getId());
        }
        Block block = event.getBlock();
        if(LevelPoints.getExpSettings().expType(block.getType().toString()).equals("none")){
            return;
        }
        if(!LevelPoints.getLevelSettings().canBreak(block.getType(), block.getData(),  data)){
            BlockData blockData = BlockUtils.getBlockData(block.getType(), block.getData());
            if(LevelPoints.getLangSettings().isRequiredBreakEnabled()) {
                Formatter formatter = new Formatter(player.getName(), data.getLevel(), data.getExp(), blockData.getRequiredEXP(Requirement.BREAK, data), data.getPrestige(), blockData.getBreakRequired(), data.getProgress());
                String message = MessageUtils.getColor(LevelPoints.getLangSettings().getRequiredBreak());
                player.sendMessage(MessageUtils.format(message, formatter));
            }
            event.setCancelled(true);
            return;
        }

        if(!player.hasPermission(PermissionUtils.getPlayerPermission().expBlock())){
            return;
        }

        EventUtils.triggerEarnExpEvent(data, event, LevelPoints.getExpSettings().getBlockEXP(block.getType(), block.getData()), player, EarnTask.Blocks);
    }
}
