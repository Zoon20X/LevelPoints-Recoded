package me.zoon20x.levelpoints.events;

import me.zoon20x.levelpoints.LevelPoints;
import me.zoon20x.levelpoints.containers.Player.PlayerData;
import me.zoon20x.levelpoints.containers.Settings.Blocks.BlockData;
import me.zoon20x.levelpoints.containers.Settings.Blocks.BlockRequired;
import me.zoon20x.levelpoints.containers.Settings.Blocks.BlockUtils;
import me.zoon20x.levelpoints.containers.Settings.Crafting.CraftingData;
import me.zoon20x.levelpoints.containers.Settings.Crafting.CraftingUtils;
import me.zoon20x.levelpoints.events.CustomEvents.EarnTask;
import me.zoon20x.levelpoints.events.CustomEvents.EventUtils;
import me.zoon20x.levelpoints.utils.DebugSeverity;
import me.zoon20x.levelpoints.utils.Formatter;
import me.zoon20x.levelpoints.utils.MessageUtils;
import me.zoon20x.levelpoints.utils.Permissions.PermissionUtils;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class ExpEarningEvents implements Listener {

    @EventHandler
    public void onKillMob(EntityDeathEvent event) {
        if (event.getEntity().getKiller() == null) {
            return;
        }
        EntityType entityType = event.getEntity().getType();

        if (LevelPoints.getExpSettings().expType(entityType.name()).equals("none")) {
            return;
        }
        Player player = event.getEntity().getKiller();
        PlayerData data = LevelPoints.getPlayerStorage().getLoadedData(player.getUniqueId());
        LevelPoints.getDebug(DebugSeverity.NORMAL, LevelPoints.getExpSettings().expType(entityType.name()));
        if(!player.hasPermission(PermissionUtils.getPlayerPermission().expMobs())){
            return;
        }
        EventUtils.triggerEarnExpEvent(data, event, LevelPoints.getExpSettings().getMobEXP(entityType), player, EarnTask.Mobs);
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent event){
        Player player = event.getPlayer();
        PlayerData data = LevelPoints.getPlayerStorage().getLoadedData(player.getUniqueId());
        Block block = event.getBlock();
        if(!LevelPoints.getLevelSettings().canPlace(block.getType(), block.getData(), data)){
            BlockData blockData = BlockUtils.getBlockData(block.getType(), block.getData());
            if(LevelPoints.getLangSettings().isRequiredPlaceEnabled()){
                Formatter formatter = new Formatter(player.getName(), data.getLevel(), data.getExp(), blockData.getRequiredEXP(BlockRequired.PLACE, data), data.getPrestige(), blockData.getPlaceRequired(), data.getProgress());
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
        Block block = event.getBlock();
        if(LevelPoints.getExpSettings().expType(block.getType().toString()).equals("none")){
            return;
        }
        if(!LevelPoints.getLevelSettings().canBreak(block.getType(), block.getData(),  data)){
            BlockData blockData = BlockUtils.getBlockData(block.getType(), block.getData());
            if(LevelPoints.getLangSettings().isRequiredBreakEnabled()) {
                Formatter formatter = new Formatter(player.getName(), data.getLevel(), data.getExp(), blockData.getRequiredEXP(BlockRequired.BREAK, data), data.getPrestige(), blockData.getBreakRequired(), data.getProgress());
                String message = MessageUtils.getColor(LevelPoints.getLangSettings().getRequiredBreak());
                player.sendMessage(MessageUtils.format(message, formatter));
            }
            event.setCancelled(true);
            return;
        }

        if(!player.hasPermission(PermissionUtils.getPlayerPermission().expBlock())){
            return;
        }
        LevelPoints.getDebug(DebugSeverity.WARNING, block);

        EventUtils.triggerEarnExpEvent(data, event, LevelPoints.getExpSettings().getBlockEXP(block.getType(), block.getData()), player, EarnTask.Blocks);
    }
}
