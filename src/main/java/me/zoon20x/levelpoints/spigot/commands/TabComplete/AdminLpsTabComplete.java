package me.zoon20x.levelpoints.spigot.commands.TabComplete;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;
import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class AdminLpsTabComplete implements TabCompleter {
    private static final List<String> args1 = Arrays.asList(new String[]{"reload", "exp", "level", "prestige"});
    private static final List<String> args2 = Arrays.asList(new String[]{"add", "remove", "set"});
    private static final List<String> onlinePlayers = new ArrayList<>();
    private static final List<String> values = Arrays.asList(new String[]{"1", "2", "3","4","5","6","7","8","9"});


    @Override
    public List<String> onTabComplete(CommandSender sender,  Command command,  String alias,  String[] args) {
        final List<String> completions = new ArrayList<>();
        onlinePlayers.clear();
        if(!(sender instanceof Player)){
            return completions;
        }
        if(args.length == 1){
            StringUtil.copyPartialMatches(args[0], args1, completions);
        }
        if(args.length == 2){
            if(!args[0].equalsIgnoreCase("reload")){
                StringUtil.copyPartialMatches(args[1], args2, completions);
            }
        }
        if(args.length == 3){
            if(args2.contains(args[1])){
                for (Player player : Bukkit.getOnlinePlayers()) {
                    onlinePlayers.add(player.getName());
                }
                StringUtil.copyPartialMatches(args[2], onlinePlayers, completions);
            }
        }
        if(args.length == 4){
            if(args2.contains(args[1])){
                StringUtil.copyPartialMatches(args[3], values, completions);
            }
        }
        Collections.sort(completions);
        return completions;
    }
}
