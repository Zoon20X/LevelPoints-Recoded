package me.zoon20x.levelpoints.spigot.utils;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.util.Location;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import com.sk89q.worldguard.protection.regions.RegionQuery;
import me.zoon20x.levelpoints.spigot.LevelPoints;
import me.zoon20x.levelpoints.spigot.containers.WorldGuardSettings;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class AntiAbuse {


    public static boolean checkWorldGuard(Location location){
        if(LevelPoints.getInstance().isWorldGuardEnabled()){
            WorldGuardSettings worldGuardSettings = LevelPoints.getInstance().getWorldGuardSettings();
            RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
            RegionQuery query = container.createQuery();
            ApplicableRegionSet set = query.getApplicableRegions(location);
            for (ProtectedRegion region : set) {
                if(region.getFlags().containsKey(worldGuardSettings.getLpsDisabled())){
                    if(region.getFlag(worldGuardSettings.getLpsDisabled()) == StateFlag.State.DENY){
                        return false;
                    }
                }
            }
        }
        return true;
    }
    public static boolean checkSilkTouch(Player player){
        ItemStack itemStack = player.getInventory().getItemInMainHand();
        if(itemStack.hasItemMeta()){
            ItemMeta meta = itemStack.getItemMeta();
            assert meta != null;
            if(meta.hasEnchant(Enchantment.SILK_TOUCH)){
                return false;
            }
        }
        return true;
    }
}
