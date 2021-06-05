package me.zoon20x.levelpoints.commands.TabComplete;

import me.zoon20x.levelpoints.LevelPoints;
import me.zoon20x.levelpoints.containers.Player.PlayerData;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class lpsTabComplete implements TabCompleter {
    private static final List<String> args1 = Arrays.asList(new String[]{"info", "prestige", "top", "booster"});
    private static final List<String> boosterArgs = Arrays.asList(new String[]{"list", "use", "check"});
    private static final ArrayList<String> boosters = new ArrayList<>();
    private static final ArrayList<String> players = new ArrayList<>();

    @Override
    public List<String> onTabComplete(CommandSender sender,  Command command,  String alias,  String[] args) {
        final List<String> completions = new ArrayList<>();
        if(!(sender instanceof Player)){
            return completions;
        }
        Player player = (Player) sender;
        if (args.length == 1) {
            StringUtil.copyPartialMatches(args[0], args1, completions);
        }
        if(args.length == 2){
            if(args[0].equalsIgnoreCase("booster")){
                StringUtil.copyPartialMatches(args[1], boosterArgs, completions);

            }
            if(args[0].equalsIgnoreCase("info")){
                players.clear();
                Bukkit.getOnlinePlayers().forEach(p ->{
                    players.add(p.getName());
                });
                StringUtil.copyPartialMatches(args[1], players, completions);
            }

        }
        if(args.length == 3){
            if(args[0].equalsIgnoreCase("booster")){
                if(args[1].equalsIgnoreCase("use")){
                    boosters.clear();
                    PlayerData playerData = LevelPoints.getPlayerStorage().getLoadedData(player.getUniqueId());
                    playerData.getAllBoosters().forEach(boosterString ->{
                        boosters.add(boosterString);
                    });
                    StringUtil.copyPartialMatches(args[2], boosters, completions);
                }
            }
        }
        Collections.sort(completions);
        return completions;
    }
}
