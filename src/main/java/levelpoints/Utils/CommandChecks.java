package levelpoints.Utils;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandChecks {


    public static Player getPlayerFromSender(CommandSender sender){
        return (Player) sender;
    }

    public static Boolean isPlayer(CommandSender sender){
        if(sender instanceof Player){
            return true;
        }
        return false;
    }
    public static Boolean hasPermission(CommandSender sender, String permission){
        if(isPlayer(sender)){
            if(sender.hasPermission(permission)){
                return true;
            }
            return false;
        }
        return true;
    }

}
