package me.zoon20x.devTools.spigot.events;


import me.zoon20x.devTools.spigot.gui.ExpGUI;
import me.zoon20x.devTools.spigot.gui.LpsGUI;
import me.zoon20x.levelpoints.spigot.LevelPoints;
import me.zoon20x.levelpoints.spigot.utils.messages.DebugSeverity;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class DevEvents implements Listener {


    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();
        if(event.getMessage().contains("dev-gui")){
            event.setCancelled(true);
            player.openInventory(new LpsGUI(LevelPoints.getInstance().getGuiSettings().getGUIData("LpsSettings")).getInventory());
        }


    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event){
        if(event.getClickedInventory() == null){
            return;
        }
        Inventory inventory = event.getClickedInventory();
        if(inventory.getHolder() == null){
            return;
        }
        if(!(inventory.getHolder() instanceof LpsGUI)){
            return;
        }
        LpsGUI lpsGUI = (LpsGUI) inventory.getHolder();

        event.setCancelled(true);
        if(event.getCurrentItem() == null || event.getCurrentItem().getType() == Material.AIR){
            return;
        }
        ItemStack item = event.getCurrentItem();

    }

    @EventHandler
    public void onBreak(BlockBreakEvent event){
        Block block = event.getBlock();
        Player player = event.getPlayer();


    }


}
