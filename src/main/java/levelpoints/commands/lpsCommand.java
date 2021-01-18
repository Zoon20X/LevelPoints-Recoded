package levelpoints.commands;

import levelpoints.Cache.FileCache;
import levelpoints.Cache.LangCache;
import levelpoints.Cache.SettingsCache;
import levelpoints.Containers.*;
import levelpoints.Utils.AsyncEvents;
import levelpoints.Utils.AsyncFileCache;
import levelpoints.Utils.CommandChecks;
import levelpoints.Utils.InventoryHolders.BoosterHolder;
import levelpoints.events.CustomEvents.MessageType;
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
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.util.ArrayList;


public class lpsCommand implements CommandExecutor {
    public lpsCommand(Plugin plugin) {
    }

    int posTop = 0;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            new BukkitRunnable() {
                @Override
                public void run() {

                    for (String x : LangCache.getlpHelpMessage()) {
                        sender.sendMessage(Formatting.basicColor(x));
                    }
                }
            }.runTaskAsynchronously(LevelPoints.getInstance());
            return true;
        }
        if (sender.hasPermission("lp.admin")) {
            if (args[0].equalsIgnoreCase("announce")) {
                String Name = "";
                for (int i = 2; i < args.length; i++) {
                    Name += args[i] + " ";
                }
                if (Bukkit.getPlayer(args[1]) != null) {
                    Player player = Bukkit.getPlayer(args[1]);
                    PlayerContainer container = AsyncEvents.getPlayerContainer(player);
                    Name = Name
                            .replace("{lp_player}", player.getName())
                            .replace("{lp_level}", String.valueOf(container.getLevel()))
                            .replace("{lp_prestige}", String.valueOf(container.getPrestige()));

                    LevelPoints.getInstance().getServer().broadcastMessage(Formatting.basicColor(Name));
                }
            }
        }


        if (args.length == 1) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    args1(sender, args[0]);
                }
            }.runTaskAsynchronously(LevelPoints.getInstance());
        }
        if (args.length == 2) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    args2(sender, args);
                }
            }.runTaskAsynchronously(LevelPoints.getInstance());
        }
        if (args.length == 3) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    args3(sender, args);
                }
            }.runTaskAsynchronously(LevelPoints.getInstance());
        }
        if (args.length == 4) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    args4(sender, args);
                }
            }.runTaskAsynchronously(LevelPoints.getInstance());
        }
        if (args.length == 6) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    if (sender.hasPermission("lp.admin.*")) {
                        args6(sender, args);
                    }
                }
            }.runTaskAsynchronously(LevelPoints.getInstance());
        }
        return true;

    }

    private void args1(CommandSender sender, String args) {
        switch (args) {
            case "removerequirement":
                if (sender instanceof Player) {
                    Player player = (Player) sender;
                    if (sender.hasPermission("lp.admin")) {
                        ItemStack item;
                        if (!Bukkit.getVersion().contains("1.8")) {
                            item = player.getInventory().getItemInMainHand();
                        } else {
                            item = player.getInventory().getItemInHand();
                        }
                        if (item != null) {
                            ItemMeta im = item.getItemMeta();
                            ArrayList<String> lore = new ArrayList<>();
                            if (im.hasLore()) {
                                for (String x : im.getLore()) {
                                    if (!x.contains(Formatting.basicColor(FileCache.getConfig("langConfig").getString("lpRequirement").replace("{Required_Level}", "")))) {
                                        lore.add(x);
                                    }
                                }
                            }
                            im.setLore(lore);
                            item.setItemMeta(im);

                        }
                    }
                }
                break;
            case "reload":
                if (sender.hasPermission("lp.admin.reload")) {

                    if (!LevelPoints.getInstance().getConfig().getBoolean("UseSQL")) {
                        AsyncEvents.MassSaveCache();
                        for (Player player : Bukkit.getOnlinePlayers()) {
                            AsyncEvents.LoadPlayerData(player.getUniqueId(), player.getName());
                            AsyncEvents.addPlayerToContainerCache(player.getUniqueId());
                        }
                    }
                    sender.sendMessage(Formatting.basicColor(FileCache.getConfig("langConfig").getString("LpsReload.Files")));
                    EXPContainer.clearCache(EXPCache.ALL);
                    LevelsContainer.clearCache();
                    LangCache.clearCache();
                    LevelPoints.getInstance().reloadConfig();
                    AsyncFileCache.startAsyncCache();
                    sender.sendMessage(Formatting.basicColor(FileCache.getConfig("langConfig").getString("LpsReload.PlayerData")));
                    sender.sendMessage(Formatting.basicColor(FileCache.getConfig("langConfig").getString("LpsReload.Complete")));
                    FileCache.clearCache();
                    SettingsCache.clearBooleanCache();

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
                    posTop = 0;
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
            case "setrequirement":
                if(sender.hasPermission("lp.admin")){
                    ItemStack item;
                    if(!(sender instanceof Player)){
                        return;
                    }
                    Player player = (Player) sender;
                    if (!Bukkit.getVersion().contains("1.8")) {
                        item = player.getInventory().getItemInMainHand();
                    }else{
                        item = player.getInventory().getItemInHand();
                    }
                    if(item !=null){
                        ItemMeta im = item.getItemMeta();

                        ArrayList<String> lore = new ArrayList<>();
                        if(im == null){
                            return;
                        }
                        if(im.hasLore()){
                            for(String x : im.getLore()) {
                                if(!x.contains(FileCache.getConfig("langConfig").getString("lpRequirement").replace("{Required_Level}", ""))) {
                                    lore.add(x);
                                }
                            }
                        }
                        lore.add(Formatting.basicColor(FileCache.getConfig("langConfig").getString("lpRequirement").replace("{Required_Level}", args[1])));
                        im.setLore(lore);
                        item.setItemMeta(im);

                    }
                }
                break;
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
            case "reload":
                if (sender.hasPermission("lp.admin.reload")) {

                    switch (args[1]) {
                        case "files":
                            sender.sendMessage(Formatting.basicColor(FileCache.getConfig("langConfig").getString("LpsReload.Files")));
                            new BukkitRunnable() {
                                @Override
                                public void run() {
                                    EXPContainer.clearCache(EXPCache.ALL);
                                    LevelsContainer.clearCache();
                                    LangCache.clearCache();
                                    LevelPoints.getInstance().reloadConfig();
                                    AsyncFileCache.startAsyncCache();
                                    FileCache.clearCache();
                                    SettingsCache.clearBooleanCache();
                                }
                            }.runTaskAsynchronously(LevelPoints.getInstance());

                            sender.sendMessage(Formatting.basicColor(FileCache.getConfig("langConfig").getString("LpsReload.Complete")));
                            break;
                        case "players":
                            sender.sendMessage(Formatting.basicColor(FileCache.getConfig("langConfig").getString("LpsReload.PlayerData")));

                            new BukkitRunnable() {
                                @Override
                                public void run() {
                                    AsyncEvents.MassSaveCache();


                                    for (Player player : Bukkit.getOnlinePlayers()) {
                                        AsyncEvents.LoadPlayerData(player.getUniqueId(), player.getName());
                                        AsyncEvents.addPlayerToContainerCache(player.getUniqueId());
                                    }
                                }
                            }.runTaskAsynchronously(LevelPoints.getInstance());
                            sender.sendMessage(Formatting.basicColor(FileCache.getConfig("langConfig").getString("LpsReload.Complete")));

                            break;
                        case "all":
                            sender.sendMessage(Formatting.basicColor(FileCache.getConfig("langConfig").getString("LpsReload.Files")));
                            AsyncEvents.MassSaveCache();
                            EXPContainer.clearCache(EXPCache.ALL);
                            LevelsContainer.clearCache();
                            LangCache.clearCache();
                            LevelPoints.getInstance().reloadConfig();
                            AsyncFileCache.startAsyncCache();
                            sender.sendMessage(Formatting.basicColor(FileCache.getConfig("langConfig").getString("LpsReload.PlayerData")));
                            for (Player player : Bukkit.getOnlinePlayers()) {
                                AsyncEvents.LoadPlayerData(player.getUniqueId(), player.getName());
                                AsyncEvents.addPlayerToContainerCache(player.getUniqueId());
                            }
                            sender.sendMessage(Formatting.basicColor(FileCache.getConfig("langConfig").getString("LpsReload.Complete")));
                            FileCache.clearCache();
                            SettingsCache.clearBooleanCache();
                            break;
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
                        if(FileCache.getConfig("langConfig").getBoolean("EXP.give.sender.enabled")) {
                            sender.sendMessage(Formatting.basicColor(FileCache.getConfig("langConfig").getString("EXP.give.sender.message")
                                    .replace("{lp_exp_sender}", sender.getName())
                                    .replace("{lp_exp_player}", Bukkit.getPlayer(args[1]).getName())
                                    .replace("{lp_exp_amount}", String.valueOf(Double.parseDouble(args[2])))));
                        }
                        if(FileCache.getConfig("langConfig").getBoolean("EXP.give.player.enabled")) {
                            Bukkit.getPlayer(args[1]).sendMessage(Formatting.basicColor(FileCache.getConfig("langConfig").getString("EXP.give.player.message")
                                    .replace("{lp_exp_sender}", sender.getName())
                                    .replace("{lp_exp_player}", Bukkit.getPlayer(args[1]).getName())
                                    .replace("{lp_exp_amount}", String.valueOf(Double.parseDouble(args[2])))));
                        }
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
            case "setprestige":
                if (sender.hasPermission("lp.admin.level")) {
                    if(Bukkit.getPlayer(args[1]) != null){
                        AsyncEvents.getPlayerContainer(Bukkit.getPlayer(args[1]));
                        PlayerContainer container = AsyncEvents.getPlayerContainer(Bukkit.getPlayer(args[1]));
                        container.setPrestige(Integer.parseInt(args[2]));
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
    private void args4(CommandSender sender, String[] args){
        switch (args[0]){
            case "booster":
                switch (args[1]){
                    case "use":
                        if(sender instanceof Player){
                            Player player = (Player) sender;
                            PlayerContainer container = AsyncEvents.getPlayerContainer(player);

                            if(container.hasBooster(Double.valueOf(args[2]), args[3])){
                                AsyncEvents.triggerBoosterActivation(Double.valueOf(args[2]), args[3], player);
                            }else{
                                for (String x : FileCache.getConfig("langConfig").getStringList("Booster.DoNotOwn")) {
                                    sender.sendMessage(Formatting.basicColor(x));
                                }
                            }
                        }else{
                            sender.sendMessage(Formatting.basicColor("&4LevelPoints>> &cYou must be a player to use this command"));
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
                        for (String x : FileCache.getConfig("langConfig").getStringList("Booster.Give.Player")) {
                            player.sendMessage(Formatting.basicColor(x).replace("{lp_booster_multiplier}", String.valueOf(Double.valueOf(args[3]))).replace("{lp_booster_time}", args[4]).replace("{lp_booster_amount}", args[5]));
                        };
                        break;
                }
                break;
        }
    }
}
