package levelpoints.Utils;

import net.Indyuce.mmoitems.api.player.RPGPlayer;
import net.Indyuce.mmoitems.stat.type.ItemRestriction;
import net.mmogroup.mmolib.api.item.NBTItem;
import org.bukkit.entity.Player;

public class ItemRestrictionMMO implements ItemRestriction {
    @Override
    public boolean canUse(RPGPlayer rpgPlayer, NBTItem item, boolean b) {
        int level = item.getInteger("MMOITEMS_REQUIRED_LEVEL");
        Player player = rpgPlayer.getPlayer();
        if(level < AsyncEvents.getPlayerContainer(player).getLevel()){
            return false;
        }
        return true;
    }
}
