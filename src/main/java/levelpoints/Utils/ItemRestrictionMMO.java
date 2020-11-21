package levelpoints.Utils;

import net.Indyuce.mmoitems.api.player.RPGPlayer;
import net.Indyuce.mmoitems.stat.type.ItemRestriction;
import net.mmogroup.mmolib.api.item.NBTItem;

public class ItemRestrictionMMO implements ItemRestriction {
    @Override
    public boolean canUse(RPGPlayer rpgPlayer, NBTItem nbtItem, boolean b) {
        return false;
    }
}
