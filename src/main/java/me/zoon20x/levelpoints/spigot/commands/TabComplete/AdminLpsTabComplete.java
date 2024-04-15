package me.zoon20x.levelpoints.spigot.commands.TabComplete;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class AdminLpsTabComplete implements TabCompleter {
    private static final List<String> args1 = Arrays.asList(new String[]{""});


    @Override
    public List<String> onTabComplete(CommandSender sender,  Command command,  String alias,  String[] args) {
        final List<String> completions = new ArrayList<>();
        if(!(sender instanceof Player)){
            return completions;
        }
        if(args.length == 1){
            StringUtil.copyPartialMatches(args[0], args1, completions);
        }
        Collections.sort(completions);
        return completions;
    }
}
