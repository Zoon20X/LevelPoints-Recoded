package levelpoints.commands;

import levelpoints.Cache.FileCache;
import levelpoints.Cache.LangCache;
import levelpoints.Containers.*;
import levelpoints.Utils.AsyncEvents;
import levelpoints.Utils.AsyncFileCache;
import levelpoints.Utils.CommandChecks;
import levelpoints.Utils.InventoryHolders.BoosterHolder;
import levelpoints.events.CustomEvents.TopEnums;
import levelpoints.levelpoints.Formatting;

import levelpoints.levelpoints.LevelPoints;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.MemorySection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;


public class lpsCommand implements CommandExecutor {
    public lpsCommand(Plugin plugin) {
    }


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(args.length == 0){
            new BukkitRunnable() {
                @Override
                public void run() {

                    for(String x : LangCache.getlpHelpMessage()){
                        sender.sendMessage(Formatting.basicColor(x));
                    }
                }
            }.runTaskAsynchronously(LevelPoints.getInstance());
        }
        if(args.length == 1){
            new BukkitRunnable() {
                @Override
                public void run() {
                    args1(sender, args[0]);
                }
            }.runTaskAsynchronously(LevelPoints.getInstance());
        }
        if(args.length == 2){
            new BukkitRunnable() {
                @Override
                public void run() {
                    args2(sender, args);
                }
            }.runTaskAsynchronously(LevelPoints.getInstance());
        }
        if(args.length == 3){
            new BukkitRunnable() {
                @Override
                public void run() {
                    args3(sender, args);
                }
            }.runTaskAsynchronously(LevelPoints.getInstance());
        }
        if(args.length == 6){
            new BukkitRunnable() {
                @Override
                public void run() {
                    args6(sender, args);
                }
            }.runTaskAsynchronously(LevelPoints.getInstance());
        }
        return true;

    }
    private void args1(CommandSender sender, String args){

        switch(args) {
            case "reload":
                if (sender.hasPermission("lp.admin.reload")) {


                    for (Player x : Bukkit.getOnlinePlayers()) {
                        PlayerContainer container = AsyncEvents.getPlayerContainer(x);
                        container.saveCacheToFile();
                        container.clearPlayerCache();
                    }
                    EXPContainer.clearCache(EXPCache.ALL);
                    LevelsContainer.clearCache();
                    LangCache.clearCache();
                    LevelPoints.getInstance().reloadConfig();
                    AsyncFileCache.startAsyncCache();
                    sender.sendMessage(Formatting.basicColor(FileCache.getConfig("langConfig").getString("LpsReload.PlayerData")));
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        AsyncEvents.LoadPlayerData(player);
                        AsyncEvents.addPlayerToContainerCache(player);
                    }
                    sender.sendMessage(Formatting.basicColor(FileCache.getConfig("langConfig").getString("LpsReload.Complete")));
                    FileCache.clearCache();

                }
                break;
            case "info":
                if (CommandChecks.isPlayer(sender)) {
                    AsyncEvents.getPlayerContainer((Player) sender);
                    for (String x : LangCache.getInfoMessage()) {
                        sender.sendMessage(Formatting.formatInfoTags(CommandChecks.getPlayerFromSender(sender), x));
                    }
                }
                break;
            case "prestige":
                if (CommandChecks.isPlayer(sender)) {
                    PlayerContainer container = AsyncEvents.getPlayerContainer((Player) sender);
                    if (container.canPrestige()) {
                        container.removeEXP(container.getRequiredEXP());
                        container.setLevel(LevelsContainer.getStartingLevel());
                        container.addPrestige(1);
                    } else {
                        if (FileCache.getConfig("langConfig").getBoolean("Prestige.Cannot.Enabled")) {
                            sender.sendMessage(Formatting.basicColor(FileCache.getConfig("langConfig").getString("Prestige.Cannot.Message")));
                        }
                    }
                }
                break;
            case "top":
                if (CommandChecks.isPlayer(sender)) {
                    sender.sendMessage(Formatting.basicColor(LangCache.getLevelTopMessage(TopEnums.TopDisplay)));
                    File TopFile = new File(LevelPoints.getInstance().getDataFolder(), "TopList.yml");
                    FileConfiguration TopConfig = YamlConfiguration.loadConfiguration(TopFile);
                    TopConfig.getConfigurationSection("").getValues(false)
                            .entrySet()
                            .stream()
                            .sorted((a1, a2) -> {
                                int points1 = ((MemorySection) a1.getValue()).getInt("Level");
                                int points2 = ((MemorySection) a2.getValue()).getInt("Level");
                                return points2 - points1;
                            })
                            .limit(10) // Limit the number of 'results'
                            .forEach(f -> {
                                int posTop = 0;
                                posTop += 1;

                                int points = ((MemorySection) f.getValue()).getInt("Level");
                                String playername = ((MemorySection) f.getValue()).getString("Name");
                                sender.sendMessage(Formatting.formatTopTags(playername,
                                        LangCache.getLevelTopMessage(TopEnums.MiddleDisplay),
                                        posTop,
                                        points));
                            });
                    sender.sendMessage(Formatting.basicColor(LangCache.getLevelTopMessage(TopEnums.TopDisplay)));
                }
                break;
            case "booster":
                if (CommandChecks.isPlayer(sender)) {
                    Player player = (Player) sender;
                    PlayerContainer container = AsyncEvents.getPlayerContainer(player);
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            BoosterHolder bh = new BoosterHolder(player, t(container, 0) * 9);
                            player.openInventory(bh.getInventory());
                        }
                    }.runTask(LevelPoints.getInstance());

                }

        }
    }

    private int t(PlayerContainer container, int value){
        if(container.getBoosters().size() != 27) {
            while (container.getBoosters().size() >= value * 9) {
                value++;
            }
        }else{
            return 3;
        }

        return value;
    }

    private void args2(CommandSender sender, String[] args) {
        switch (args[0]) {
            case "info":
                if (Bukkit.getPlayer(args[1]) != null) {
                    Player player = Bukkit.getPlayer(args[1]);
                    AsyncEvents.getPlayerContainer(player);
                    for (String x : LangCache.getInfoMessage()) {

                        sender.sendMessage(Formatting.formatInfoTags(player, x));
                    }
                } else {
                    OfflinePlayer player = Bukkit.getOfflinePlayer(args[1]);
                    for (String x : LangCache.getInfoMessage()) {
                        sender.sendMessage(Formatting.formatInfoTags(player, x));
                    }
                }
                break;
        }
    }
    private void args3(CommandSender sender, String[] args){
        switch(args[0]) {
            case "expremove":
                if (sender.hasPermission("lp.admin.remove")) {
                    if (Bukkit.getPlayer(args[1]) != null) {
                        AsyncEvents.getPlayerContainer(Bukkit.getPlayer(args[1]));


                        PlayerContainer playerContainer = AsyncEvents.getPlayerContainer(Bukkit.getPlayer(args[1]));
                        playerContainer.removeEXP(Double.parseDouble(args[2]));

                    } else {
                    }
                }
                break;
            case "expgive":
                if (sender.hasPermission("lp.admin.give")) {
                    if (Bukkit.getPlayer(args[1]) != null) {
                        AsyncEvents.getPlayerContainer(Bukkit.getPlayer(args[1]));
                        PlayerContainer playerContainer = AsyncEvents.getPlayerContainer(Bukkit.getPlayer(args[1]));
                        playerContainer.addEXP(Double.parseDouble(args[2]));
                    } else {
                    }
                }
                break;
            case "setlevel":
                if (sender.hasPermission("lp.admin.level")) {
                    if(Bukkit.getPlayer(args[1]) != null){
                        AsyncEvents.getPlayerContainer(Bukkit.getPlayer(args[1]));
                        PlayerContainer container = AsyncEvents.getPlayerContainer(Bukkit.getPlayer(args[1]));
                        container.setLevel(Integer.parseInt(args[2]));
                    }else{

                    }
                }
                break;
            case "addlevel":
                if (sender.hasPermission("lp.admin.level")) {
                    if(Bukkit.getPlayer(args[1]) != null){
                        AsyncEvents.getPlayerContainer(Bukkit.getPlayer(args[1]));
                        PlayerContainer container = AsyncEvents.getPlayerContainer(Bukkit.getPlayer(args[1]));
                        container.addLevel(Integer.parseInt(args[2]), false);
                    }else{

                    }
                }
                break;
        }

    }
    private void args6(CommandSender sender, String[] args){
        switch (args[0]){
            case "booster":
                switch (args[1]){
                    case "give":
                        if(Bukkit.getPlayer(args[2]) == null){
                            sender.sendMessage(Formatting.basicColor("&4LevelPoints>> &cPlayer is not online"));
                            return;
                        }
                        Player player = Bukkit.getPlayer(args[2]);
                        PlayerContainer container = AsyncEvents.getPlayerContainer(player);
                        if(container.getBoosters().size() == 27){
                            sender.sendMessage(Formatting.basicColor("&4LevelPoints>> &cPlayer has too many unique boosters"));
                            return;
                        }
                        container.giveBooster(Double.valueOf(args[3]), args[4], Integer.valueOf(args[5]));
                        break;
                }
                break;
        }
    }
}
