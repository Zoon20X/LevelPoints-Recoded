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
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockGrowEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityBreedEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import xyz.rgnt.levelpoints.ArtificialBlockCache;


public class ExpEarningEvents implements Listener {

    @EventHandler(priority = EventPriority.MONITOR)
    public void onEntityDamage(EntityDamageByEntityEvent event){
        if(event.isCancelled())
            return;
        if(!(event.getDamager() instanceof Player)){
            return;
        }
        Player player = (Player) event.getDamager();
        PlayerData data = LevelPoints.getInstance().getPlayerStorage().getLoadedData(player.getUniqueId());
        if(LevelPoints.getInstance().isMythicMobsEnabled()){
            if(MythicMobs.inst().getAPIHelper().isMythicMob(event.getEntity())){
                ActiveMob mob = MythicMobs.inst().getAPIHelper().getMythicMobInstance(event.getEntity());
                if(!LevelPoints.getInstance().getMythicMobsSettings().hasMobData(mob.getType().getInternalName())){
                    return;
                }
                if(!LevelPoints.getInstance().getLevelSettings().canDamageMythicMobs(mob.getType().getInternalName(), data)){
                    event.setCancelled(true);
                    return;
                }
            }
        }
        if (LevelPoints.getInstance().getExpSettings().expType(event.getEntity().getType().name()).equals("none")) {
            return;
        }
        if(!LevelPoints.getInstance().getLevelSettings().canDamage(event.getEntity().getType(), data)){
            event.setCancelled(true);
            return;
        }
    }


    @EventHandler(priority = EventPriority.MONITOR)
    public void onKillMob(EntityDeathEvent event) {
        if (event.getEntity().getKiller() == null)
            return;

        EntityType entityType = event.getEntity().getType();
        Player player = event.getEntity().getKiller();
        PlayerData data = LevelPoints.getInstance().getPlayerStorage()
                .getLoadedData(player.getUniqueId());

        if(LevelPoints.getInstance().isMythicMobsEnabled()){
            if(MythicMobs.inst().getAPIHelper().isMythicMob(event.getEntity())){
                ActiveMob mob = MythicMobs.inst().getAPIHelper().getMythicMobInstance(event.getEntity());
                if(!LevelPoints.getInstance().getMythicMobsSettings().hasMobData(mob.getType().getInternalName())){
                    return;
                }
                EventUtils.triggerEarnExpEvent(data, event, LevelPoints.getInstance().getMythicMobsSettings().getMobData(mob.getType().getInternalName()).getExp(), player, EarnTask.Mobs);
                return;
            }
        }

        if (LevelPoints.getInstance().getExpSettings()
                .expType(entityType.name()).equals("none"))
            return;


        if(!player.hasPermission(PermissionUtils.getPlayerPermission().expMobs()))
            return;


        EventUtils.triggerEarnExpEvent(data, event, LevelPoints.getInstance().getExpSettings().getMobEXP(entityType), player, EarnTask.Mobs);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onBreed(EntityBreedEvent event){
        if(event.isCancelled()){
            return;
        }
        LivingEntity livingEntity = event.getBreeder();
        if(!(livingEntity instanceof Player)){
            return;
        }
        Player player = (Player) event.getBreeder();
        PlayerData data = LevelPoints.getInstance().getPlayerStorage().getLoadedData(player.getUniqueId());
        if(!LevelPoints.getInstance().getLevelSettings().canBreed(event.getEntity().getType(), data)){
            event.setCancelled(true);
            return;
        }
        EventUtils.triggerEarnExpEvent(data, event, LevelPoints.getInstance().getExpSettings().getBreedEXP(event.getEntity().getType()), player, EarnTask.Breeding);

    }


    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlace(BlockPlaceEvent event){
        if(event.isCancelled())
            return;

        Player player = event.getPlayer();
        PlayerData data = LevelPoints.getInstance().getPlayerStorage().getLoadedData(player.getUniqueId());
        Block block = event.getBlock();

        ArtificialBlockCache.addArtificialBlock(block);

        if(!LevelPoints.getInstance().getLevelSettings().canPlace(block.getType(), block.getData(), data)){
            BlockData blockData = BlockUtils.getBlockData(block.getType(), block.getData());
            if(LevelPoints.getInstance().getLangSettings().isRequiredPlaceEnabled()){
                Formatter formatter = new Formatter(player.getName(), data.getLevel(), data.getExp(), blockData.getRequiredEXP(Requirement.PLACE, data), data.getPrestige(), blockData.getPlaceRequired(), data.getProgress());
                String message = MessageUtils.getColor(LevelPoints.getInstance().getLangSettings().getRequiredPlace());
                player.sendMessage(MessageUtils.format(message, formatter));
            }

            event.setCancelled(true);
            return;
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onGrow(BlockGrowEvent event) {
        ArtificialBlockCache.remArtificialBlock(event.getBlock());
    }


    @EventHandler(priority = EventPriority.MONITOR)
    public void onCraft(CraftItemEvent event){
        if(event.isCancelled())
            return;

        Player player = (Player) event.getWhoClicked();
        ItemStack item = event.getCurrentItem();
        if(LevelPoints.getInstance().getExpSettings().expType(item.getType().toString()).equalsIgnoreCase("none")){
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
        PlayerData data = LevelPoints.getInstance().getPlayerStorage().getLoadedData(player.getUniqueId());
        CraftingData craftingData = CraftingUtils.getCraftingData(item.getType());
        if(data.getLevel() < craftingData.getCraftingRequired()) {
            event.setCancelled(true);
            return;
        }
        double exp = craftingData.getExp() * amountOfItems;

        EventUtils.triggerEarnExpEvent(data, event, exp, player, EarnTask.Craft);
    }


    @EventHandler(priority = EventPriority.MONITOR)
    public void onBreak(BlockBreakEvent event) {
        if(event.isCancelled())
            return;
        Player player = event.getPlayer();
        PlayerData data = LevelPoints.getInstance().getPlayerStorage().getLoadedData(player.getUniqueId());
        if(data.getBracketData() != null){
        }
        Block block = event.getBlock();

        if(ArtificialBlockCache.isArtificialBlock(block))
            return;

        if(LevelPoints.getInstance().getExpSettings().expType(block.getType().toString()).equals("none")){
            return;
        }
        if(!LevelPoints.getInstance().getLevelSettings().canBreak(block.getType(), block.getData(),  data)){
            BlockData blockData = BlockUtils.getBlockData(block.getType(), block.getData());
            if(LevelPoints.getInstance().getLangSettings().isRequiredBreakEnabled()) {
                Formatter formatter = new Formatter(player.getName(), data.getLevel(), data.getExp(), blockData.getRequiredEXP(Requirement.BREAK, data), data.getPrestige(), blockData.getBreakRequired(), data.getProgress());
                String message = MessageUtils.getColor(LevelPoints.getInstance().getLangSettings().getRequiredBreak());
                player.sendMessage(MessageUtils.format(message, formatter));
            }
            event.setCancelled(true);
            return;
        }

        if(!player.hasPermission(PermissionUtils.getPlayerPermission().expBlock())){
            return;
        }

        EventUtils.triggerEarnExpEvent(data, event, LevelPoints.getInstance().getExpSettings().getBlockEXP(block.getType(), block.getData()), player, EarnTask.Blocks);
    }
}
