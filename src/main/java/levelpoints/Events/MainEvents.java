package levelpoints.Events;

import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.regions.Regions;
import com.sk89q.worldedit.world.World;
import com.sk89q.worldguard.LocalPlayer;
;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

import io.lumine.xikage.mythicmobs.MythicMobs;
import io.lumine.xikage.mythicmobs.mobs.ActiveMob;
import levelpoints.LevelPointsEvents.FarmEvent;
import levelpoints.LevelPointsEvents.LevelUpEvent;
import levelpoints.levelpoints.LevelPoints;

import levelpoints.levelpoints.mySQL;
import levelpoints.utils.utils.API;
import levelpoints.utils.utils.UtilCollector;
import lpsapi.lpsapi.LPSAPI;
import me.clip.placeholderapi.PlaceholderAPI;
import me.ryanhamshire.GriefPrevention.Claim;
import me.ryanhamshire.GriefPrevention.DataStore;
import me.ryanhamshire.GriefPrevention.GriefPrevention;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.Crops;
import org.bukkit.material.MaterialData;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.*;

import static net.md_5.bungee.api.ChatColor.AQUA;
import static net.md_5.bungee.api.ChatColor.GOLD;


public class MainEvents implements Listener {
    private Plugin plugin = LevelPoints.getPlugin(LevelPoints.class);
    private LevelPoints lp = LevelPoints.getPlugin(LevelPoints.class);


    private LPSAPI lpapi = (LPSAPI) Bukkit.getPluginManager().getPlugin("LPSAPI");
    UtilCollector uc = new UtilCollector();
    private HashMap<String, String> drops = new HashMap<>();


    public MainEvents(LevelPoints levelPoints) {
    }


    @EventHandler(priority = EventPriority.HIGHEST)
    public void onChat(AsyncPlayerChatEvent event) {

        Player player = event.getPlayer();
        File userdata = new File(lp.userFolder, player.getUniqueId() + ".yml");
        FileConfiguration UsersConfig = YamlConfiguration.loadConfiguration(userdata);
        int level = uc.getCurrentLevel(player);
        int prestige = uc.getCurrentPrestige(player);
        String chat = event.getFormat();
        String message = event.getMessage();
        String levels = String.valueOf(level);
        String prestigess = String.valueOf(prestige);
        String color = lp.getConfig().getString("PrefixColor");
        String symbol = lp.getConfig().getString("Symbol");
        Boolean lpsChat = lp.getConfig().getBoolean("LPSFormat");

        if (lpsChat) {

            for (String key : uc.getFormatsConfig().getKeys(false)) {

                ConfigurationSection formats = uc.getFormatsConfig().getConfigurationSection("");

                int formatMin = formats.getInt(key + ".MinLevel");
                int formatMax = formats.getInt(key + ".MaxLevel");



                if (prestige == formats.getInt(key + ".Prestige"))
                    if (level >= formatMin && level <= formatMax) {
                        chat = chat.replace("%1$s", player.getName()).replace("%2$s", message);

                        String Format = uc.getFormatsConfig().getString(key + ".Format");

                        String FormatTags = null;
                        String FormatColor = null;

                        FormatTags = Format.replace("{level}", levels).replace("{symbol}", symbol).replace("{prestige}", prestigess).replace("{name}", player.getName()).replace("{message}", message).replace("{format}", chat);

                        String Text = PlaceholderAPI.setPlaceholders(player, FormatTags);


                        event.setCancelled(true);
                        for (Player p : Bukkit.getOnlinePlayers()) {
                            if (player.hasPermission("lp.admin.color")) {
                                p.sendMessage(Text);
                            } else {
                                if (Text.contains(API.format(message))) {

                                    String messageStrip = ChatColor.stripColor(message);

                                    String Texts = Text.replace(API.format(message), ChatColor.stripColor(messageStrip));

                                    p.sendMessage(Texts);
                                }
                            }

                        }
                    }
            }
        }

    }

    @EventHandler
    public void PreJoin(AsyncPlayerPreLoginEvent event) {

        uc.PlayerAdd(event.getUniqueId(), event.getName());

        wait(3, event.getUniqueId(), event.getName());

    }

    public void wait(int seconds, UUID uuid, String name) {
        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(lp, new Runnable() {
            public void run() {
                if (lp.getConfig().getBoolean("UseSQL")) {
                    try {
                        if (lp.connection.isClosed()) {
                            uc.SQLReconnect();
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    mySQL sql = new mySQL();
                    sql.createPlayer(uuid, name);
                }
            }
        }, (seconds * 25));


    }

    public void up(int seconds, Player player) {
        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(lp, new Runnable() {
            public void run() {
                UtilCollector uc = new UtilCollector();
                uc.updateBossbar(uc.getBossbar(player), player);
            }
        }, (seconds * 10));
    }
    @EventHandler
    public void onJoin(PlayerJoinEvent event) throws IOException {


        uc.PlayerDataLoad(event.getPlayer());
        if (lp.getConfig().getBoolean("UseSQL")) {


            uc.wait(3, event.getPlayer());
            up(4, event.getPlayer());
            //uc.RunSQLDownload(event.getPlayer());
        }
        if (lp.getConfig().getBoolean("BossBar")) {
            if (!lp.getConfig().getBoolean("ShowOnEXPOnly")) {
                if (uc.getBossbar(event.getPlayer()) == null) {
                    uc.createBossbar(event.getPlayer());
                }
                if (!uc.getBossbar(event.getPlayer()).getPlayers().contains(event.getPlayer())) {
                    uc.bossbarAddPlayer(uc.getBossbar(event.getPlayer()), event.getPlayer());
                    uc.updateBossbar(uc.getBossbar(event.getPlayer()), event.getPlayer());
                }
            }
        }
    }

    @EventHandler
    public void Leave(PlayerQuitEvent event) {
        if (lp.getConfig().getBoolean("UseSQL")) {

            uc.RunSQLUpdate(event.getPlayer());
        }
        if(lp.getConfig().getBoolean("BossBar")) {
            if(uc.getBossbar(event.getPlayer()) != null) {
                if (uc.getBossbar(event.getPlayer()).getPlayers().contains(event.getPlayer())) {
                    uc.bossbarRemovePlayer(uc.getBossbar(event.getPlayer()), event.getPlayer());
                }
            }
        }
    }
    @EventHandler
    public void OnLevelUp(LevelUpEvent event) {
        if(lpapi != null){
            lpapi.LevelUpEventTrigger(event.getPlayer(), event.getLevel());
        }
        Player player = event.getPlayer();
        Boolean hasOverlap = event.getOverlap();
        int Level = event.getLevel();
        int Prestige = uc.getCurrentPrestige(player);
        uc.Rewards(player, Level, Prestige);
        File file = new File(lp.getDataFolder(), "TopList.yml");
        FileConfiguration config = uc.getTopListConfig();
        if (hasOverlap) {
            int Overamount = event.getOverlapAmount();

            config.set(player.getUniqueId() + ".Name", player.getName());
            config.set(player.getUniqueId() + ".Level", Level);

            try {
                config.save(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
            uc.GainEXP(player, 0);
        } else {

            if (lp.getConfig().getBoolean("UseSQL")) {
                uc.RunSQLUpdate(player);
            }
            config.set(player.getUniqueId() + ".Name", player.getName());
            config.set(player.getUniqueId() + ".Level", Level);

            try {
                config.save(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if(lp.getConfig().getBoolean("BossBar")) {
            uc.updateBossbar(uc.getBossbar(player), player);
        }


    }

    private boolean Check(Player player){

        DataStore dataStore = GriefPrevention.instance.dataStore;
        Claim claim = dataStore.getClaimAt(player.getLocation(), true, null);
        if(claim != null){
            if(player.getName().equalsIgnoreCase(claim.getOwnerName())){

                return true;
            }else{

                return false;
            }
        }else{
            return true;
        }
    }

    @EventHandler
    public void BlockBreak(BlockBreakEvent event) {

        Block block = event.getBlock();
        Player player = event.getPlayer();
        ItemStack item = player.getItemInHand();
        ItemMeta itemm = item.getItemMeta();
        File userdata = new File(lp.userFolder, player.getUniqueId() + ".yml");
        FileConfiguration UsersConfig = YamlConfiguration.loadConfiguration(userdata);
        if(lp.getConfig().getBoolean("GriefPrevention")){
            if(!Check(player)){
                return;
            }
        }

        if(lp.getConfig().getBoolean("RequiredItemLore")) {

            ItemStack items = player.getInventory().getItemInMainHand();
            if (items.hasItemMeta()) {
                ItemMeta im = item.getItemMeta();
                String Level = null;
                if (im.hasLore()) {
                    for (String x : im.getLore()) {
                        if (x.contains(API.format(uc.getLangConfig().getString("lpRequirement").replace("{Required_Level}", "")))) {
                            Level = x;
                        }
                    }
                    if (Level != null) {
                        Level = Level.replace(API.format(uc.getLangConfig().getString("lpRequirement").replace("{Required_Level}", "")), "");
                        if (uc.getCurrentLevel(player) < Integer.parseInt(Level)) {
                            event.setCancelled(true);
                            return;
                        }
                    }
                }
            }
        }

        if (uc.getEXPConfig().getBoolean("Resources")) {

            if (uc.getEXPConfig().getBoolean("Debug")) {
                player.sendMessage(block.getType().toString());
            }

            if (uc.getEXPConfig().getBoolean("WorldGuard.RestrictRegions")) {
                LocalPlayer LocalP = lp.worldGuardPlugin.wrapPlayer(player);
                Location playerVector = block.getLocation();
                RegionManager rm = lp.worldGuardPlugin.getRegionManager(block.getWorld());
                ApplicableRegionSet appregion = rm.getApplicableRegions(playerVector);

                if (!appregion.getRegions().isEmpty()) {
                    for (ProtectedRegion regions : appregion) {

                        if (uc.getEXPConfig().getStringList("WorldGuard.regionsToRestrict").contains(regions.getId())) {
                            return;
                        } else {
                            if (UsersConfig.getInt("Level") < uc.getEXPConfig().getInt("o" + block.getType().toString())) {
                                int level = uc.getEXPConfig().getInt("o" + block.getType().toString());
                                player.sendMessage(API.format(uc.getLangConfig().getString("ucPerLevelOre").replace("{uc_required_level}", String.valueOf(level))));
                                event.setCancelled(true);
                            } else if (uc.getEXPConfig().getBoolean("RandomEXP")) {

                                int max = uc.getEXPConfig().getInt(block.getType().toString());
                                int min = 0;

                                Random r = new Random();
                                int re = r.nextInt((max - min) + 1) + min;
                                if (uc.getEXPConfig().getBoolean("Anti-Silk-EXP")) {
                                    if (!item.getEnchantments().containsKey(Enchantment.SILK_TOUCH)) {
                                        uc.GainEXP(player, re);
                                    }
                                } else {
                                    uc.GainEXP(player, re);
                                }
                            } else {
                                if (uc.getEXPConfig().getBoolean("Anti-Silk-EXP")) {
                                    if (!item.getEnchantments().containsKey(Enchantment.SILK_TOUCH)) {
                                        uc.GainEXP(player, uc.getEXPConfig().getInt(block.getType().toString()));
                                    }
                                } else {
                                    uc.GainEXP(player, uc.getEXPConfig().getInt(block.getType().toString()));
                                }
                            }
                        }
                    }
                } else {
                    if (UsersConfig.getInt("Level") < uc.getEXPConfig().getInt("o" + block.getType().toString())) {
                        int level = uc.getEXPConfig().getInt("o" + block.getType().toString());
                        player.sendMessage(API.format(uc.getLangConfig().getString("ucPerLevelOre").replace("{uc_required_level}", String.valueOf(level))));
                        event.setCancelled(true);
                    } else if (uc.getEXPConfig().getBoolean("RandomEXP")) {

                        int max = uc.getEXPConfig().getInt(block.getType().toString());
                        int min = 0;

                        Random r = new Random();
                        int re = r.nextInt((max - min) + 1) + min;
                        if (uc.getEXPConfig().getBoolean("Anti-Silk-EXP")) {
                            if (!item.getEnchantments().containsKey(Enchantment.SILK_TOUCH)) {
                                uc.GainEXP(player, re);
                            }
                        } else {
                            uc.GainEXP(player, re);
                        }
                    } else {
                        if (uc.getEXPConfig().getBoolean("Anti-Silk-EXP")) {
                            if (!item.getEnchantments().containsKey(Enchantment.SILK_TOUCH)) {
                                uc.GainEXP(player, uc.getEXPConfig().getInt(block.getType().toString()));
                            }
                        } else {
                            uc.GainEXP(player, uc.getEXPConfig().getInt(block.getType().toString()));
                        }
                    }
                }

            } else if (uc.getEXPConfig().getBoolean("PerWorld")) {

                List<String> worlds = uc.getEXPConfig().getStringList("Worlds");
                for (String world : worlds) {
                    if (player.getLocation().getWorld().getName().equalsIgnoreCase(world)) {
                        if (UsersConfig.getInt("Level") < uc.getEXPConfig().getInt("o" + block.getType().toString())) {
                            int level = uc.getEXPConfig().getInt("o" + block.getType().toString());
                            player.sendMessage(API.format(uc.getLangConfig().getString("ucPerLevelOre").replace("{uc_required_level}", String.valueOf(level))));
                            event.setCancelled(true);
                        } else if (uc.getEXPConfig().getBoolean("RandomEXP")) {

                            int max = uc.getEXPConfig().getInt(block.getType().toString());
                            int min = 0;

                            Random r = new Random();
                            int re = r.nextInt((max - min) + 1) + min;
                            if (uc.getEXPConfig().getBoolean("Anti-Silk-EXP")) {
                                if (!item.getEnchantments().containsKey(Enchantment.SILK_TOUCH)) {
                                    uc.GainEXP(player, re);
                                }
                            } else {
                                uc.GainEXP(player, re);
                            }
                        } else {
                            if (uc.getEXPConfig().getBoolean("Anti-Silk-EXP")) {
                                if (!item.getEnchantments().containsKey(Enchantment.SILK_TOUCH)) {
                                    uc.GainEXP(player, uc.getEXPConfig().getInt(block.getType().toString()));
                                }
                            } else {
                                uc.GainEXP(player, uc.getEXPConfig().getInt(block.getType().toString()));
                            }
                        }
                    }
                }
            } else {

                if (UsersConfig.getInt("Level") < uc.getEXPConfig().getInt("o" + block.getType().toString())) {
                    int level = uc.getEXPConfig().getInt("o" + block.getType().toString());
                    player.sendMessage(API.format(uc.getLangConfig().getString("lpPerLevelOre").replace("{lp_required_level}", String.valueOf(level))));
                    event.setCancelled(true);
                } else if (uc.getEXPConfig().getBoolean("RandomEXP")) {

                    int max = uc.getEXPConfig().getInt(block.getType().toString());
                    int min = 0;

                    Random r = new Random();
                    int re = r.nextInt((max - min) + 1) + min;
                    if (uc.getEXPConfig().getBoolean("Anti-Silk-EXP")) {
                        if (!item.getEnchantments().containsKey(Enchantment.SILK_TOUCH)) {
                            uc.GainEXP(player, re);
                        }
                    } else {
                        uc.GainEXP(player, re);
                    }
                } else {
                    if (uc.getEXPConfig().getBoolean("Anti-Silk-EXP")) {
                        if (!item.getEnchantments().containsKey(Enchantment.SILK_TOUCH)) {
                            uc.GainEXP(player, uc.getEXPConfig().getInt(block.getType().toString()));
                        }

                    } else {
                        uc.GainEXP(player, uc.getEXPConfig().getInt(block.getType().toString()));
                    }
                }
            }
        }

        if (uc.getEXPConfig().getBoolean("FarmEXP")) {
            ConfigurationSection Farms = uc.getEXPConfig().getConfigurationSection("Farming");

            if (uc.getEXPConfig().getBoolean("1.8-1.13.2")) {

                if (block.getType() == Material.CROPS) {
                    MaterialData FarmData = block.getState().getData();

                    Crops crop = new Crops();
                    crop.setData(FarmData.getData());

                    if (crop.getData() == (byte) 7) {

                        ConfigurationSection FarmBlocks = uc.getEXPConfig().getConfigurationSection("Farming.");
                        int amount = 0;
                        for (ItemStack x : block.getDrops()) {
                            if (drops.isEmpty()) {
                                drops.put(x.getType().toString(), x.getType().toString());
                            }
                        }
                        for (String x : drops.keySet()) {
                            if (x.equals("CROPS")) {
                                amount = uc.getEXPConfig().getInt("Farming.WHEAT");
                                uc.FarmEventTrigger(player, "WHEAT", amount, "Farming");
                            } else if (FarmBlocks.contains(x.replace("_ITEM", ""))) {
                                amount = uc.getEXPConfig().getInt("Farming." + x.replace("_ITEM", ""));

                                uc.FarmEventTrigger(player, x.replace("_ITEM", ""), amount, "Farming");
                            }
                        }
                    }
                }
            } else {


                MaterialData FarmData = block.getState().getData();

                Crops crop = new Crops();
                crop.setData(FarmData.getData());

                if (crop.getData() == (byte) 7) {

                    ConfigurationSection FarmBlocks = uc.getEXPConfig().getConfigurationSection("Farming.");
                    int amount = 0;
                    for (ItemStack x : block.getDrops()) {
                        if (drops.isEmpty()) {
                            drops.put(x.getType().toString(), x.getType().toString());
                        }
                    }
                    for (String x : drops.keySet()) {
                        if (x.equals("LEGACY_CROPS")) {
                            amount = uc.getEXPConfig().getInt("Farming.WHEAT");
                            uc.FarmEventTrigger(player, "WHEAT", amount, "Farming");
                        } else if (FarmBlocks.contains(x.replace("_ITEM", ""))) {
                            amount = uc.getEXPConfig().getInt("Farming." + x.replace("_ITEM", ""));

                            uc.FarmEventTrigger(player, x.replace("_ITEM", ""), amount, "Farming");
                        }
                    }
                    drops.clear();
                }
            }
        }

        if(lp.getConfig().getBoolean("SaveBlockBreaks&Placed")){
            if(UsersConfig.getConfigurationSection("").contains("BlocksBroken")){
                int Current = UsersConfig.getInt("BlocksBroken");
                UsersConfig.set("BlocksBroken", Current + 1);
                try {
                    UsersConfig.save(userdata);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }else{
                UsersConfig.set("BlocksBroken", 1);
                try {
                    UsersConfig.save(userdata);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    @EventHandler
    public void fish(PlayerFishEvent event){

        Player player = event.getPlayer();
        if(event.getState() != PlayerFishEvent.State.CAUGHT_FISH){
            return;
        }else{
            Item item = (Item) event.getCaught();

            ItemStack CaughtItem = item.getItemStack();
            ItemMeta CaughtMeta = CaughtItem.getItemMeta();

            if(uc.getEXPConfig().getBoolean("FishingEXP")){
                double EXPChance = uc.getEXPConfig().getInt("Fishing.CatchChance");
                int EXPAmount = uc.getEXPConfig().getInt("Fishing.EXP");

                if(uc.getEXPConfig().getBoolean("Fishing.RandomEXP")){
                    int chanceAfter = (int) (EXPChance * 10);


                    int max = 1000;
                    int min = 0;

                    Random r = new Random();
                    int re = r.nextInt(((max - min) + 1)) + min;
                    if (re <= chanceAfter) {
                        item.setItemStack(null);
                        int EXPMax = EXPAmount;
                        int EXPMin = 0;

                        Random er = new Random();
                        int ere = r.nextInt(((EXPMax - EXPMin) + 1)) + EXPMin;
                        uc.GainEXP(player, ere);
                    }
                }else{
                    int chanceAfter = (int) (EXPChance * 10);

                    int max = 1000;
                    int min = 0;

                    Random r = new Random();
                    int re = r.nextInt(((max - min) + 1)) + min;
                    if (re <= chanceAfter) {
                        item.setItemStack(null);

                        uc.GainEXP(player, EXPAmount);
                    }
                }
            }


        }

    }
    @EventHandler
    public void onFarm(FarmEvent event){
        Player player = event.getPlayer();
        String Item = event.getFarmedItem();

        int exp = event.getEXPAmount();
        String task = event.getTask();


        uc.GainEXP(player, exp);
        if(uc.getEXPConfig().getBoolean("TaskMessage")) {
            player.sendMessage(API.format(uc.getLangConfig().getString("EXPEarn").replace("{lp_Earn_Exp}", String.valueOf(exp)).replace("{lp_Earn_Task}", task)));
        }
    }

    @EventHandler
    public void place(BlockPlaceEvent event) throws IOException {
        Block block = event.getBlock();
        Player player = event.getPlayer();

        File userdata = new File(lp.userFolder, player.getUniqueId() + ".yml");
        FileConfiguration UsersConfig = YamlConfiguration.loadConfiguration(userdata);
        if (uc.getEXPConfig().getBoolean("Anti-EXP-Dupe")) {

            double expp = uc.getCurrentEXP(player);
            int t = uc.getEXPConfig().getInt(block.getType().toString());
            if (t <= expp) {
                double tep = expp - t;
                UsersConfig.set("EXP.Amount", tep);
                UsersConfig.save(userdata);
            }
        }
        if(lp.getConfig().getBoolean("SaveBlockBreaks&Placed")){
            if(UsersConfig.getConfigurationSection("").contains("BlocksPlaced")){
                int Current = UsersConfig.getInt("BlocksPlaced");
                UsersConfig.set("BlocksPlaced", Current + 1);
                try {
                    UsersConfig.save(userdata);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }else{
                UsersConfig.set("BlocksPlaced", 1);
                try {
                    UsersConfig.save(userdata);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        if(lp.getConfig().getBoolean("RequiredItemLore")){
            if(event.getDamager() instanceof Player){
                Player player = (Player) event.getDamager();
                ItemStack item = player.getInventory().getItemInMainHand();
                if(item.hasItemMeta()){
                    ItemMeta im = item.getItemMeta();
                    String Level = null;
                    if(im.hasLore()){
                        for(String x : im.getLore()){
                            if(x.contains(API.format(uc.getLangConfig().getString("lpRequirement").replace("{Required_Level}", "")))) {
                                Level = x;
                            }
                        }
                        if(Level !=null){
                            Level = Level.replace(API.format(uc.getLangConfig().getString("lpRequirement").replace("{Required_Level}", "")), "");
                            if(uc.getCurrentLevel(player)<Integer.parseInt(Level)){
                                event.setCancelled(true);
                            }
                        }
                    }
                }
            }
        }

        if (uc.getLevelsConfig().getBoolean("PvpLeveluse")) {
            int levelpvp = uc.getLevelsConfig().getInt("PvpLevel");
            if (!(event.getDamager() instanceof Player)) {
                return;
            } else {
                if (event.getEntity() instanceof Player) {
                    Player Attacker = (Player) event.getDamager();
                    Player player = (Player) event.getEntity();

                    File playerData = new File(lp.userFolder, player.getUniqueId() + ".yml");
                    File attackerdata = new File(lp.userFolder, Attacker.getUniqueId() + ".yml");

                    FileConfiguration PlayerConfig = YamlConfiguration.loadConfiguration(playerData);
                    FileConfiguration AttackerConfig = YamlConfiguration.loadConfiguration(attackerdata);
                    if (uc.getCurrentLevel(player) < levelpvp) {
                        event.setCancelled(true);
                        uc.Title(player,  ChatColor.DARK_RED + "You must be", ChatColor.RED + "Level "+levelpvp+" to PVP");
                        Attacker.sendMessage(ChatColor.RED + player.getName() + " Must to Level "+levelpvp+" to allow pvp");

                    }
                    if (uc.getCurrentLevel(Attacker) < levelpvp) {
                        event.setCancelled(true);
                        uc.Title(Attacker,ChatColor.DARK_RED + "You must be", ChatColor.RED + "Level "+levelpvp+" to PVP");

                    }
                } else {
                    return;
                }
            }

            if (event.getDamager() instanceof Arrow) {
                Bukkit.getConsoleSender().sendMessage("tesst");
                final Arrow arrow = (Arrow) event.getDamager();

                Player Attacker = (Player) arrow.getShooter();
                Player player = (Player) event.getEntity();
                File playerData = new File(lp.userFolder, player.getUniqueId() + ".yml");
                File attackerdata = new File(lp.userFolder, Attacker.getUniqueId() + ".yml");
                FileConfiguration PlayerConfig = YamlConfiguration.loadConfiguration(playerData);
                FileConfiguration AttackerConfig = YamlConfiguration.loadConfiguration(attackerdata);


                if (uc.getCurrentLevel(player) < levelpvp) {
                    event.setCancelled(true);
                    uc.Title(player,  ChatColor.DARK_RED + "You must be", ChatColor.RED + "Level "+ levelpvp +" to PVP");
                    Attacker.sendMessage(ChatColor.RED + player.getName() + " must be level "+ levelpvp +" to allow pvp");

                }
                if (uc.getCurrentLevel(Attacker) < levelpvp) {
                    event.setCancelled(true);
                    uc.Title(Attacker,ChatColor.DARK_RED + "You must be", ChatColor.RED + "Level "+levelpvp+" to PVP");

                }
            }
        }
        if(uc.getLevelsConfig().getBoolean("PvpDistance")){
            if (!(event.getDamager() instanceof Player)) {
                return;
            } else {
                if (event.getEntity() instanceof Player) {
                    Player Attacker = (Player) event.getDamager();
                    Player player = (Player) event.getEntity();
                    int AttackerLevel = uc.getCurrentLevel(Attacker);
                    int playerLevel = uc.getCurrentLevel(player);
                    if(AttackerLevel > playerLevel) {

                        if(AttackerLevel - playerLevel > uc.getLevelsConfig().getInt("PvpMin")){
                            event.setCancelled(true);
                        }
                    }else{
                        if(playerLevel - AttackerLevel > uc.getLevelsConfig().getInt("PvpMin")){
                            event.setCancelled(true);
                        }
                    }
                }
            }
        }
        if(lp.getConfig().getBoolean("MythicMobs")) {
            if (event.getDamager() instanceof Player) {
                Entity ent = event.getEntity();
                Player player = (Player) event.getDamager();
                if (MythicMobs.inst().getAPIHelper().isMythicMob(ent)) {
                    ActiveMob mob = MythicMobs.inst().getAPIHelper().getMythicMobInstance(ent);
                    ConfigurationSection cs = uc.getMMobsConfig().getConfigurationSection("");
                    Set<String> css = cs.getKeys(false);
                    if (css.contains(mob.getType().getInternalName())) {
                        int cl = uc.getCurrentLevel(player);
                        int re = uc.getMMobsConfig().getInt(mob.getType().getInternalName() + ".Level");
                        if (cl < re) {
                            event.setCancelled(true);
                        }
                    }
                }
            }else if (event.getDamager() instanceof Arrow){
                final Arrow arrow = (Arrow) event.getDamager();
                if (MythicMobs.inst().getAPIHelper().isMythicMob(event.getEntity())) {
                    if (arrow.getShooter() instanceof Player) {
                        ActiveMob mob = MythicMobs.inst().getAPIHelper().getMythicMobInstance(event.getEntity());
                        ConfigurationSection cs = uc.getMMobsConfig().getConfigurationSection("");
                        Set<String> css = cs.getKeys(false);
                        if (css.contains(mob.getType().getInternalName())) {
                            int cl = uc.getCurrentLevel((Player) arrow.getShooter());
                            int re = uc.getMMobsConfig().getInt(mob.getType().getInternalName() + ".Level");
                            if (cl < re) {
                                event.setCancelled(true);
                            }
                        }
                    }
                }

            }
        }

        if(event.getDamager() instanceof Player) {
            Player player = (Player) event.getDamager();
            if (uc.getLevelsConfig().getBoolean("LevelBonus.use")) {
                double Damage = uc.getLevelsConfig().getDouble("LevelBonus.Level-" + uc.getCurrentLevel(player) + ".Damage");

                event.setDamage(event.getDamage() + Damage);
            }
        }
    }


    @EventHandler
    public void onKill(PlayerDeathEvent d) throws IOException, SQLException {
        Player player = d.getEntity();

        Player Killer = d.getEntity().getKiller();
        if (Killer instanceof Player) {

            if (uc.getEXPConfig().getBoolean("PerWorld")) {
                List<String> worlds = uc.getEXPConfig().getStringList("Worlds");
                for (String world : worlds) {
                    if (player.getLocation().getWorld().getName().equalsIgnoreCase(world)) {
                        File userdata = new File(lp.userFolder, player.getUniqueId() + ".yml");
                        File killerData = new File(lp.userFolder, Killer.getUniqueId() + ".yml");
                        FileConfiguration UsersConfig = YamlConfiguration.loadConfiguration(userdata);
                        FileConfiguration KillerConfig = YamlConfiguration.loadConfiguration(userdata);
                        if (uc.getEXPConfig().getBoolean("Exp-Kill-players")) {
                            uc.GainEXP(Killer, uc.getEXPConfig().getInt("Kill-Player-Amount"));
                            if (uc.getEXPConfig().getBoolean("EXP-Lost-On-Death-Player")) {
                                double expp = uc.getCurrentEXP(player);
                                int t = uc.getEXPConfig().getInt("EXP-Lost-Amount");
                                if (t <= expp) {
                                    double tep = expp - t;
                                    UsersConfig.set("EXP.Amount", tep);
                                    UsersConfig.save(userdata);
                                }
                            }
                        }
                    }
                }
            }else{
                File userdata = new File(lp.userFolder, player.getUniqueId() + ".yml");
                File killerData = new File(lp.userFolder, Killer.getUniqueId() + ".yml");
                FileConfiguration UsersConfig = YamlConfiguration.loadConfiguration(userdata);
                FileConfiguration KillerConfig = YamlConfiguration.loadConfiguration(userdata);
                if (uc.getEXPConfig().getBoolean("Exp-Kill-players")) {
                    uc.GainEXP(Killer, uc.getEXPConfig().getInt("Kill-Player-Amount"));
                    if (uc.getEXPConfig().getBoolean("EXP-Lost-On-Death-Player")) {
                        double expp = uc.getCurrentEXP(player);
                        int t = uc.getEXPConfig().getInt("EXP-Lost-Amount");
                        if (t <= expp) {
                            double tep = expp - t;
                            UsersConfig.set("EXP.Amount", tep);
                            UsersConfig.save(userdata);
                        }
                    }
                }
            }
            if(lp.getConfig().getBoolean("SaveKills&Deaths")){
                File userdata = new File(lp.userFolder, Killer.getUniqueId() + ".yml");
                FileConfiguration UsersConfig = YamlConfiguration.loadConfiguration(userdata);
                if(UsersConfig.getConfigurationSection("").contains("Kills")){
                    int Current = UsersConfig.getInt("Kills");
                    UsersConfig.set("Kills", Current + 1);
                    try {
                        UsersConfig.save(userdata);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }else{
                    UsersConfig.set("Kills", 1);
                    try {
                        UsersConfig.save(userdata);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        if(lp.getConfig().getBoolean("SaveKills&Deaths")){
            File userdata = new File(lp.userFolder, player.getUniqueId() + ".yml");
            FileConfiguration UsersConfig = YamlConfiguration.loadConfiguration(userdata);
            if(UsersConfig.getConfigurationSection("").contains("Deaths")){
                int Current = UsersConfig.getInt("Deaths");
                UsersConfig.set("Deaths", Current + 1);
                try {
                    UsersConfig.save(userdata);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }else{
                UsersConfig.set("Deaths", 1);
                try {
                    UsersConfig.save(userdata);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) throws IOException, SQLException {

        if (event.getEntity() instanceof LivingEntity) {
            LivingEntity monsterEnt = event.getEntity();
            Object mcPlayer = monsterEnt.getKiller();
            Player player = ((Player) mcPlayer);
            if (mcPlayer == null) {
                return;
            }else {
                if (uc.getEXPConfig().getBoolean("Debug")) {
                    player.sendMessage(monsterEnt.getType().toString());

                }
            }
            if (uc.getEXPConfig().getBoolean("PerWorld")) {
                List<String> worlds = uc.getEXPConfig().getStringList("Worlds");
                for (String world : worlds) {
                    if (player.getLocation().getWorld().getName().equalsIgnoreCase(world)) {

                        if (mcPlayer == null) {
                            return;
                        }
                        uc.RandomConfigurator(player, monsterEnt);
                    }
                }
            } else {
                if (mcPlayer == null) {
                    return;
                }
                uc.RandomConfigurator(player, monsterEnt);
            }
        }
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event){
        Player player = event.getPlayer();
        if(uc.getLevelsConfig().getBoolean("WorldGuard.RequiredLevels")){
            ConfigurationSection cs = uc.getLevelsConfig().getConfigurationSection("WorldGuard.Regions");
            RegionManager rm = lp.worldGuardPlugin.getRegionManager(player.getWorld());
            ApplicableRegionSet appregion = rm.getApplicableRegions(player.getLocation());
            Set<String> css = cs.getKeys(false);
            if (!appregion.getRegions().isEmpty()) {
                for (ProtectedRegion regions : appregion) {
                    if (css.contains(regions.getId())) {
                        if (uc.getLevelsConfig().getInt("WorldGuard.Regions." + regions.getId() + ".Prestige") >= uc.getCurrentPrestige(player)) {
                            if (uc.getLevelsConfig().getInt("WorldGuard.Regions." + regions.getId() + ".Level") > uc.getCurrentLevel(player)) {
                                player.teleport(player.getLocation().add(event.getFrom().toVector().subtract(event.getTo().toVector()).normalize().multiply(2)));
                                if (uc.getLevelsConfig().getBoolean("WorldGuard.Regions." + regions.getId() + ".MessageUse")) {
                                    player.sendMessage(API.format(uc.getLevelsConfig().getString("WorldGuard.Regions." + regions.getId() + ".Message").replace("{LevelRequired}", String.valueOf(uc.getLevelsConfig().getInt("WorldGuard.Regions." + regions.getId() + ".Level")))));
                                }
                                return;
                            }
                        }
                    }
                }
            }
        }
    }
}
