package levelpoints.Utils.InventoryHolders;

import com.cryptomorin.xseries.XItemStack;
import com.cryptomorin.xseries.XMaterial;
import levelpoints.Containers.BoostersContainer;
import levelpoints.Containers.PlayerContainer;
import levelpoints.Utils.AsyncEvents;
import levelpoints.levelpoints.Formatting;
import levelpoints.levelpoints.LevelPoints;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;


import java.util.ArrayList;

public class BoosterHolder implements InventoryHolder {
    private int size;
    private Player player;

    public BoosterHolder(Player player, int size){
        this.size = size;
        this.player = player;
    }

    @Override
    public Inventory getInventory() {
        Inventory inventory = Bukkit.createInventory(this, size, player.getName() + " Boosters");
        new BukkitRunnable() {
            @Override
            public void run() {
                getBooster(inventory);
            }
        }.runTaskAsynchronously(LevelPoints.getInstance());

        return inventory;
    }
    public static ItemStack getBoosterItem(){
        ItemStack item = new ItemStack(XMaterial.EXPERIENCE_BOTTLE.parseMaterial());
        ItemMeta meta = item.getItemMeta();
        return item;
    }

    public void getBooster(Inventory inventory) {
        int i = 0;
        ItemStack item = new ItemStack(XMaterial.EXPERIENCE_BOTTLE.parseMaterial());
        ItemMeta meta = item.getItemMeta();
        PlayerContainer container = AsyncEvents.getPlayerContainer(player);
        for (BoostersContainer x : container.getBoosters().keySet()) {
            meta.setDisplayName(Formatting.basicColor("&b" + x.getMultiplier() + "x"));
            ArrayList<String> lore = new ArrayList<>();
            lore.add(Formatting.basicColor("&3Time: " + x.getTime()));
            lore.add(Formatting.basicColor("&3Amount: " + container.getBoosterAmount(x.getId())));
            meta.setLore(lore);
            item.setItemMeta(meta);
            inventory.setItem(i, item);
            i++;
        }
    }
}
