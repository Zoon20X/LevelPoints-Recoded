package me.zoon20x.devTools.spigot.events;


import me.zoon20x.devTools.spigot.gui.LpsGUI;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class DevEvents implements Listener {


    @EventHandler
    public void onBreak(BlockBreakEvent event){
        Block block = event.getBlock();
        Player player = event.getPlayer();
        if(block.getType() != Material.ENDER_CHEST){
            return;
        }
        event.setCancelled(true);
        player.openInventory(new LpsGUI().getInventory());

    }


}
