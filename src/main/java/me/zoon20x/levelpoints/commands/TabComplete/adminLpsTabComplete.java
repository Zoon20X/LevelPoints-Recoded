package me.zoon20x.levelpoints.commands.TabComplete;

import me.zoon20x.levelpoints.LevelPoints;
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

public class  adminLpsTabComplete implements TabCompleter {
    private static final List<String> args1 = Arrays.asList(new String[]{"exp", "reload", "level", "prestige", "booster"});
    private static final List<String> expArgs1 = Arrays.asList(new String[]{"give", "remove"});
    private static final List<String> levelArgs1 = Arrays.asList(new String[]{"add", "set", "remove"});
    private static final List<String> boosterArgs1 = Arrays.asList(new String[]{"delete","create", "give"});
    private static final List<String> values = Arrays.asList(new String[]{"0.0", "1.0", "2.0", "3.0", "4.20", "5.0", "6.9", "7.0", "8.0", "9.0", "10.0"});
    private static final List<String> levels = Arrays.asList(new String[]{"1","2","3","4","5","6","7","8","9","10"});
    private static final ArrayList<String> players = new ArrayList<>();
    private static final ArrayList<String> boosterID = new ArrayList<>();

    @Override
    public List<String> onTabComplete( CommandSender sender,  Command command,  String alias,  String[] args) {
        final List<String> completions = new ArrayList<>();
        players.clear();
        boosterID.clear();
        if (args.length == 1) {
            StringUtil.copyPartialMatches(args[0], args1, completions);
        }
        if(args.length == 2){
            if(args[0].equalsIgnoreCase("exp")){
                StringUtil.copyPartialMatches(args[1], expArgs1, completions);
            }
            if(args[0].equalsIgnoreCase("level")){
                StringUtil.copyPartialMatches(args[1], levelArgs1, completions);
            }
            if(args[0].equalsIgnoreCase("prestige")){
                StringUtil.copyPartialMatches(args[1], levelArgs1, completions);
            }
            if(args[0].equalsIgnoreCase("booster")){
                StringUtil.copyPartialMatches(args[1], boosterArgs1, completions);
            }
        }
        if(args.length == 3){
            if(args[0].equalsIgnoreCase("exp")){
                if(expArgs1.contains(args[1])){
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        players.add(player.getName());
                    }
                    StringUtil.copyPartialMatches(args[2], players, completions);
                }
            }
            if(args[0].equalsIgnoreCase("level")){
                if(levelArgs1.contains(args[1])){
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        players.add(player.getName());
                    }
                    StringUtil.copyPartialMatches(args[2], players, completions);
                }
            }
            if(args[0].equalsIgnoreCase("prestige")){
                if(levelArgs1.contains(args[1])){
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        players.add(player.getName());
                    }
                    StringUtil.copyPartialMatches(args[2], players, completions);
                }
            }
            if(args[0].equalsIgnoreCase("booster")){
                if(args[1].equalsIgnoreCase("delete")){
                    LevelPoints.getInstance().getBoosterSettings().getAllBoosters().forEach(boosterData -> {
                        boosterID.add(boosterData.getId());
                    });
                    StringUtil.copyPartialMatches(args[2], boosterID, completions);
                }
                if(args[1].equalsIgnoreCase("give")){
                    players.clear();
                    Bukkit.getOnlinePlayers().forEach(player -> {
                        players.add(player.getName());
                    });
                    StringUtil.copyPartialMatches(args[2], players, completions);
                }
            }//adminlps booster give <player>
        }
        if(args.length == 4){
            if(args[0].equalsIgnoreCase("booster")){
                if(args[1].equalsIgnoreCase("give")){
                    LevelPoints.getInstance().getBoosterSettings().getAllBoosters().forEach(boosterData -> {
                        boosterID.add(boosterData.getId());
                    });
                    StringUtil.copyPartialMatches(args[3], boosterID, completions);
                }
            }

            if(args[0].equalsIgnoreCase("exp")){
                if(expArgs1.contains(args[1])){
                    StringUtil.copyPartialMatches(args[3], values, completions);
                }
            }
            if(args[0].equalsIgnoreCase("level")){
                if(levelArgs1.contains(args[1])){
                    StringUtil.copyPartialMatches(args[3], levels, completions);
                }
            }
            if(args[0].equalsIgnoreCase("prestige")){
                if(levelArgs1.contains(args[1])){
                    StringUtil.copyPartialMatches(args[3], levels, completions);
                }
            }
        }
        if(args.length == 5){
            if(args[0].equalsIgnoreCase("booster")){
                if(args[1].equalsIgnoreCase("give")){
                    StringUtil.copyPartialMatches(args[4], levels, completions);
                }
            }
        }
        Collections.sort(completions);
        return completions;
    }
}