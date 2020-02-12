package levelpoints.Events;

import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.regions.Regions;
import com.sk89q.worldedit.world.World;
import com.sk89q.worldguard.LocalPlayer;
;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

import levelpoints.LevelPointsEvents.FarmEvent;
import levelpoints.LevelPointsEvents.LevelUpEvent;
import levelpoints.levelpoints.LevelPoints;
import levelpoints.levelpoints.mySQL;
import levelpoints.utils.utils.API;
import levelpoints.utils.utils.UtilCollector;
import lpsapi.lpsapi.LPSAPI;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.*;
import org.bukkit.block.Block;
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
import java.util.Iterator;
import java.util.List;
import java.util.Random;


public class MainEvents implements Listener {
    private Plugin plugin = LevelPoints.getPlugin(LevelPoints.class);
    private LevelPoints lp = LevelPoints.getPlugin(LevelPoints.class);

    private LPSAPI lpapi = (LPSAPI) Bukkit.getPluginManager().getPlugin("LPSAPI");
    UtilCollector uc = new UtilCollector();

    public MainEvents(LevelPoints levelPoints) {
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        File userdata = new File(lp.userFolder, player.getUniqueId() + ".yml");
        FileConfiguration UsersConfig = YamlConfiguration.loadConfiguration(userdata);
        int level = UsersConfig.getInt(player.getName() + ".level");
        int prestige = UsersConfig.getInt(player.getName() + ".Prestige");
        String chat = event.getFormat();
        String message = event.getMessage();
        String levels = String.valueOf(level);
        String prestigess = String.valueOf(prestige);
        String color = lp.getConfig().getString("PrefixColor");
        String symbol = lp.getConfig().getString("Symbol");
        Boolean lpsChat = lp.getConfig().getBoolean("LPSFormat");


        if(lpsChat) {

            for (String key : uc.FormatsConfig.getKeys(false)) {

                ConfigurationSection formats = uc.FormatsConfig.getConfigurationSection("");

                List<String> formatlevels = formats.getStringList(key + ".Levels");



                if (formatlevels.contains(levels)) {
                    chat = chat.replace("%1$s", player.getName()).replace("%2$s", message);

                    String Format = uc.FormatsConfig.getString(key + ".Format");
                    String FormatTags = Format.replace("{level}", levels).replace("{symbol}", symbol).replace("{prestige}", prestigess).replace("{name}", player.getName()).replace("{message}", message).replace("{format}", chat);
                    String Text = PlaceholderAPI.setPlaceholders(player, FormatTags);




                    event.setCancelled(true);
                    for(Player p : Bukkit.getOnlinePlayers()){
                        p.sendMessage(Text);
                    }
                }
            }
        }

    }

    @EventHandler
    public void PreJoin(AsyncPlayerPreLoginEvent event) {

        uc.PlayerAdd(event.getUniqueId(), event.getName());


        if (lp.getConfig().getBoolean("UseSQL")) {
            try {
                lp.setConnection(DriverManager.getConnection("jdbc:mysql://" + lp.host + ":" + lp.port + "/" + lp.database, lp.username, lp.password));
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                lp.statment = lp.connection.createStatement();
            } catch (SQLException e) {
                e.printStackTrace();
            }



            mySQL sql = new mySQL();
            sql.createPlayer(event.getUniqueId(), event.getName());
        }
    }


    @EventHandler
    public void onJoin(PlayerJoinEvent event) throws IOException {

        uc.PlayerDataLoad(event.getPlayer());
        if (lp.getConfig().getBoolean("UseSQL")) {
            uc.RunSQLDownload(event.getPlayer());
        }
    }

    @EventHandler
    public void Leave(PlayerQuitEvent event) {
        if (lp.getConfig().getBoolean("UseSQL")) {
            try {
                lp.setConnection(DriverManager.getConnection("jdbc:mysql://" + lp.host + ":" + lp.port + "/" + lp.database, lp.username, lp.password));
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                lp.statment = lp.connection.createStatement();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            uc.RunSQLUpdate(event.getPlayer());
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

        uc.Rewards(player, Level);

        if (hasOverlap) {
            int Overamount = event.getOverlapAmount();
            uc.GainEXP(player, Overamount);
            uc.TopListConfig.set(player.getUniqueId() + ".Name", player.getName());
            uc.TopListConfig.set(player.getUniqueId() + ".Level", Level);
            try {
                uc.TopListConfig.save(uc.TopListFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {

            if (lp.getConfig().getBoolean("UseSQL")) {
                try {
                    lp.setConnection(DriverManager.getConnection("jdbc:mysql://" + lp.host + ":" + lp.port + "/" + lp.database, lp.username, lp.password));
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                try {
                    lp.statment = lp.connection.createStatement();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                uc.RunSQLUpdate(player);
            }
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
         if (uc.EXPConfig.getBoolean("Resources")) {

            if (uc.EXPConfig.getBoolean("Debug")) {
                player.sendMessage(block.getType().toString());
            }

            if(uc.EXPConfig.getBoolean("WorldGuard.RestrictRegions")){
                    LocalPlayer LocalP = lp.worldGuardPlugin.wrapPlayer(player);
                    Location playerVector = block.getLocation();
                    RegionManager rm = lp.worldGuardPlugin.getRegionManager(block.getWorld());
                    ApplicableRegionSet appregion = rm.getApplicableRegions(playerVector);

                    if(!appregion.getRegions().isEmpty()) {
                        for (ProtectedRegion regions : appregion) {

                            if (uc.EXPConfig.getStringList("WorldGuard.regionsToRestrict").contains(regions.getId())) {
                                return;
                            } else {
                                if (UsersConfig.getInt("Level") < uc.EXPConfig.getInt("o" + block.getType().toString())) {
                                    int level = uc.EXPConfig.getInt("o" + block.getType().toString());
                                    player.sendMessage(API.format(uc.LangConfig.getString("ucPerLevelOre").replace("{uc_required_level}", String.valueOf(level))));
                                    event.setCancelled(true);
                                } else if (uc.EXPConfig.getBoolean("RandomEXP")) {

                                    int max = uc.EXPConfig.getInt(block.getType().toString());
                                    int min = 0;

                                    Random r = new Random();
                                    int re = r.nextInt((max - min) + 1) + min;
                                    if (uc.EXPConfig.getBoolean("Anti-Silk-EXP")) {
                                        if (!item.getEnchantments().containsKey(Enchantment.SILK_TOUCH)) {
                                            uc.GainEXP(player, uc.EXPConfig.getInt(block.getType().toString()));
                                        }
                                    } else {
                                        uc.GainEXP(player, uc.EXPConfig.getInt(block.getType().toString()));
                                    }
                                } else {
                                    if (uc.EXPConfig.getBoolean("Anti-Silk-EXP")) {
                                        if (!item.getEnchantments().containsKey(Enchantment.SILK_TOUCH)) {
                                            uc.GainEXP(player, uc.EXPConfig.getInt(block.getType().toString()));
                                        }
                                    } else {
                                        uc.GainEXP(player, uc.EXPConfig.getInt(block.getType().toString()));
                                    }
                                }
                            }
                        }
                    }else {
                        if (UsersConfig.getInt("Level") < uc.EXPConfig.getInt("o" + block.getType().toString())) {
                            int level = uc.EXPConfig.getInt("o" + block.getType().toString());
                            player.sendMessage(API.format(uc.LangConfig.getString("ucPerLevelOre").replace("{uc_required_level}", String.valueOf(level))));
                            event.setCancelled(true);
                        } else if (uc.EXPConfig.getBoolean("RandomEXP")) {

                            int max = uc.EXPConfig.getInt(block.getType().toString());
                            int min = 0;

                            Random r = new Random();
                            int re = r.nextInt((max - min) + 1) + min;
                            if (uc.EXPConfig.getBoolean("Anti-Silk-EXP")) {
                                if (!item.getEnchantments().containsKey(Enchantment.SILK_TOUCH)) {
                                    uc.GainEXP(player, uc.EXPConfig.getInt(block.getType().toString()));
                                }
                            } else {
                                uc.GainEXP(player, uc.EXPConfig.getInt(block.getType().toString()));
                            }
                        } else {
                            if (uc.EXPConfig.getBoolean("Anti-Silk-EXP")) {
                                if (!item.getEnchantments().containsKey(Enchantment.SILK_TOUCH)) {
                                    uc.GainEXP(player, uc.EXPConfig.getInt(block.getType().toString()));
                                }
                            } else {
                                uc.GainEXP(player, uc.EXPConfig.getInt(block.getType().toString()));
                            }
                        }
                    }

            }else if (uc.EXPConfig.getBoolean("PerWorld")) {

                List<String> worlds = uc.EXPConfig.getStringList("Worlds");
                for (String world : worlds) {
                    if (player.getLocation().getWorld().getName().equalsIgnoreCase(world)) {
                        if (UsersConfig.getInt("Level") < uc.EXPConfig.getInt("o" + block.getType().toString())) {
                            int level = uc.EXPConfig.getInt("o" + block.getType().toString());
                            player.sendMessage(API.format(uc.LangConfig.getString("ucPerLevelOre").replace("{uc_required_level}", String.valueOf(level))));
                            event.setCancelled(true);
                        } else if (uc.EXPConfig.getBoolean("RandomEXP")) {

                            int max = uc.EXPConfig.getInt(block.getType().toString());
                            int min = 0;

                            Random r = new Random();
                            int re = r.nextInt((max - min) + 1) + min;
                            if (uc.EXPConfig.getBoolean("Anti-Silk-EXP")) {
                                if (!item.getEnchantments().containsKey(Enchantment.SILK_TOUCH)) {
                                    uc.GainEXP(player, uc.EXPConfig.getInt(block.getType().toString()));
                                }
                            } else {
                                uc.GainEXP(player, uc.EXPConfig.getInt(block.getType().toString()));
                            }
                        } else {
                            if (uc.EXPConfig.getBoolean("Anti-Silk-EXP")) {
                                if (!item.getEnchantments().containsKey(Enchantment.SILK_TOUCH)) {
                                    uc.GainEXP(player, uc.EXPConfig.getInt(block.getType().toString()));
                                }
                            } else {
                                uc.GainEXP(player, uc.EXPConfig.getInt(block.getType().toString()));
                            }
                        }
                    }
                }
            } else {

                if (UsersConfig.getInt("Level") < uc.EXPConfig.getInt("o" + block.getType().toString())) {
                    int level = uc.EXPConfig.getInt("o" + block.getType().toString());
                    player.sendMessage(API.format(uc.LangConfig.getString("ucPerLevelOre").replace("{uc_required_level}", String.valueOf(level))));
                    event.setCancelled(true);
                } else if (uc.EXPConfig.getBoolean("RandomEXP")) {

                    int max = uc.EXPConfig.getInt(block.getType().toString());
                    int min = 0;

                    Random r = new Random();
                    int re = r.nextInt((max - min) + 1) + min;
                    if (uc.EXPConfig.getBoolean("Anti-Silk-EXP")) {
                        if (!item.getEnchantments().containsKey(Enchantment.SILK_TOUCH)) {
                            uc.GainEXP(player, uc.EXPConfig.getInt(block.getType().toString()));
                        }
                    } else {
                        uc.GainEXP(player, uc.EXPConfig.getInt(block.getType().toString()));
                    }
                } else {
                    if (uc.EXPConfig.getBoolean("Anti-Silk-EXP")) {
                        if (!item.getEnchantments().containsKey(Enchantment.SILK_TOUCH)) {
                            uc.GainEXP(player, uc.EXPConfig.getInt(block.getType().toString()));
                        }
                    } else {
                        uc.GainEXP(player, uc.EXPConfig.getInt(block.getType().toString()));
                    }
                }
            }
        }
        if(uc.EXPConfig.getBoolean("FarmEXP")){

            if(block.getType() == Material.CROPS || block.getType() == Material.CARROT || block.getType() == Material.POTATO || block.getType() == Material.BEETROOT_BLOCK || block.getType() == Material.NETHER_WARTS) {
                MaterialData FarmData = block.getState().getData();

                Crops crop = new Crops();
                crop.setData(FarmData.getData());

                if (crop.getData() == (byte) 7 || block.getType() == Material.BEETROOT_BLOCK && crop.getData() == (byte) 3) {

                    ConfigurationSection FarmBlocks = uc.EXPConfig.getConfigurationSection("Farming.");

                    for (ItemStack x : block.getDrops()) {


                        if (FarmBlocks.contains(x.getType().toString().replace("_ITEM", ""))) {

                            int amount = uc.EXPConfig.getInt("Farming." + x.getType().toString().replace("_ITEM", ""));

                            uc.FarmEventTrigger(player, x.getType().toString().replace("_ITEM", ""), amount, "Farming");
                        }
                    }
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

            ItemStack AIR = new ItemStack(Material.AIR);
            ItemStack CaughtItem = item.getItemStack();
            ItemMeta CaughtMeta = CaughtItem.getItemMeta();

            if(uc.EXPConfig.getBoolean("FishingEXP")){
                double EXPChance = uc.EXPConfig.getInt("Fishing.CatchChance");
                int EXPAmount = uc.EXPConfig.getInt("Fishing.EXP");

                if(uc.EXPConfig.getBoolean("Fishing.RandomEXP")){
                    int chanceAfter = (int) (EXPChance * 10);

                    int max = 1000;
                    int min = 0;

                    Random r = new Random();
                    int re = r.nextInt(((max - min) + 1)) + min;
                    if (re <= chanceAfter) {
                        item.setItemStack(AIR);
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
                        item.setItemStack(AIR);

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
        player.sendMessage(API.format(uc.LangConfig.getString("EXPEarn").replace("{EXP_Amount}", String.valueOf(exp)).replace("{Earn_Task}", task)));

    }

    @EventHandler
    public void place(BlockPlaceEvent event) throws IOException {
        Block block = event.getBlock();
        Player player = event.getPlayer();

        File userdata = new File(lp.userFolder, player.getUniqueId() + ".yml");
        FileConfiguration UsersConfig = YamlConfiguration.loadConfiguration(userdata);
        if (uc.EXPConfig.getBoolean("Anti-EXP-Dupe")) {

            int expp = uc.getCurrentEXP(player);
            int t = uc.EXPConfig.getInt(block.getType().toString());
            if (t <= expp) {
                int tep = expp - t;
                UsersConfig.set("EXP.Amount", tep);
                UsersConfig.save(userdata);
            }
        }
    }
    
    
    //KILLING MOBS EVENTS BELOW
    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {

        if (uc.EXPConfig.getBoolean("PvpLeveluse")) {
            int levelpvp = uc.EXPConfig.getInt("PvpLevel");
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

                    if ((PlayerConfig.getInt("Level") < levelpvp)) {
                        event.setCancelled(true);
                        uc.Title(player,  ChatColor.DARK_RED + "You Must BE", ChatColor.RED + "Level 5 to PVP");
                        Attacker.sendMessage(ChatColor.RED + player.getName() + " Must to Level 5 to allow pvp");
                        Attacker.playSound(Attacker.getLocation(), Sound.ENTITY_ENDERDRAGON_GROWL, 10F, 15);
                    }
                    if ((AttackerConfig.getInt("Level") < levelpvp)) {
                        event.setCancelled(true);
                        uc.Title(Attacker,ChatColor.DARK_RED + "You Must BE", ChatColor.RED + "Level 5 to PVP");
                        //player.sendMessage(ChatColor.RED + Attacker.getName() + " Must to Level 5 to allow pvp");
                        player.playSound(player.getLocation(), Sound.ENTITY_ENDERDRAGON_GROWL, 10F, 15);
                    }
                } else {
                    return;
                }
            }

            if (event.getDamager() instanceof Arrow) {
                final Arrow arrow = (Arrow) event.getDamager();

                Player Attacker = (Player) arrow.getShooter();
                Player player = (Player) event.getEntity();
                File playerData = new File(lp.userFolder, player.getUniqueId() + ".yml");
                File attackerdata = new File(lp.userFolder, Attacker.getUniqueId() + ".yml");
                FileConfiguration PlayerConfig = YamlConfiguration.loadConfiguration(playerData);
                FileConfiguration AttackerConfig = YamlConfiguration.loadConfiguration(attackerdata);

                if ((PlayerConfig.getInt("Level") < levelpvp)) {
                    event.setCancelled(true);
                    uc.Title(player,  ChatColor.DARK_RED + "You Must BE", ChatColor.RED + "Level 5 to PVP");
                    Attacker.sendMessage(ChatColor.RED + player.getName() + " Must to Level 5 to allow pvp");
                    Attacker.playSound(Attacker.getLocation(), Sound.ENTITY_ENDERDRAGON_GROWL, 10F, 15);
                }
            }
        }
    }


    @EventHandler
    public void onKill(PlayerDeathEvent d) throws IOException, SQLException {
        Player player = d.getEntity();

        Player Killer = d.getEntity().getKiller();
        if (Killer instanceof Player) {

            if (uc.EXPConfig.getBoolean("PerWorld")) {
                List<String> worlds = uc.EXPConfig.getStringList("Worlds");
                for (String world : worlds) {
                    if (player.getLocation().getWorld().getName().equalsIgnoreCase(world)) {
                        File userdata = new File(lp.userFolder, player.getUniqueId() + ".yml");
                        File killerData = new File(lp.userFolder, Killer.getUniqueId() + ".yml");
                        FileConfiguration UsersConfig = YamlConfiguration.loadConfiguration(userdata);
                        FileConfiguration KillerConfig = YamlConfiguration.loadConfiguration(userdata);
                        if (uc.EXPConfig.getBoolean("Exp-Kill-players")) {
                            uc.GainEXP(Killer, uc.EXPConfig.getInt("Kill-Player-Amount"));
                            if (uc.EXPConfig.getBoolean("EXP-Lost-On-Death-Player")) {
                                int expp = uc.getCurrentEXP(player);
                                int t = uc.EXPConfig.getInt("EXP-Lost-Amount");
                                if (t <= expp) {
                                    int tep = expp - t;
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
                if (uc.EXPConfig.getBoolean("Exp-Kill-players")) {
                    uc.GainEXP(Killer, uc.EXPConfig.getInt("Kill-Player-Amount"));
                    if (uc.EXPConfig.getBoolean("EXP-Lost-On-Death-Player")) {
                        int expp = uc.getCurrentEXP(player);
                        int t = uc.EXPConfig.getInt("EXP-Lost-Amount");
                        if (t <= expp) {
                            int tep = expp - t;
                            UsersConfig.set("EXP.Amount", tep);
                            UsersConfig.save(userdata);
                        }
                    }
                }
            }
        }

    }

    @EventHandler
    public void onEntityDeath(final EntityDeathEvent event) throws IOException, SQLException {

        if (event.getEntity() instanceof Monster) {
            Monster monsterEnt = (Monster) event.getEntity();
            Object mcPlayer = monsterEnt.getKiller();
            Player player = ((Player) mcPlayer);
            if (mcPlayer == null) {
                return;
            }else {
                if (uc.EXPConfig.getBoolean("Debug")) {
                    player.sendMessage(monsterEnt.getType().toString());
                }
            }


            if (uc.EXPConfig.getBoolean("PerWorld")) {
                List<String> worlds = uc.EXPConfig.getStringList("Worlds");
                for (String world : worlds) {
                    if (player.getLocation().getWorld().getName().equalsIgnoreCase(world)) {

                        if (mcPlayer == null)
                            return;


                        if (uc.EXPConfig.getBoolean("Exp-Kill-Mob")) {
                            if (uc.EXPConfig.getBoolean("RandomEXP")) {

                                int max = uc.EXPConfig.getInt(monsterEnt.getType().toString());
                                int min = 0;

                                Random r = new Random();
                                int re = r.nextInt((max - min) + 1) + min;
                                uc.GainEXP(player, re);
                            } else {
                                uc.GainEXP(player, uc.EXPConfig.getInt(monsterEnt.getType().toString()));
                            }
                        }
                    }
                }
            } else {
                if (mcPlayer == null)
                    return;


                if (uc.EXPConfig.getBoolean("Exp-Kill-Mob")) {
                    if (uc.EXPConfig.getBoolean("RandomEXP")) {

                        int max = uc.EXPConfig.getInt(monsterEnt.getType().toString());
                        int min = 0;

                        Random r = new Random();
                        int re = r.nextInt((max - min) + 1) + min;
                        uc.GainEXP(player, re);
                    } else {
                        uc.GainEXP(player, uc.EXPConfig.getInt(monsterEnt.getType().toString()));
                    }
                }

            }
        } else {
            if (event.getEntity() instanceof Animals) {
                Animals ani = (Animals) event.getEntity();
                Object mcplayer = ani.getKiller();
                Player player = (Player) mcplayer;
                if (mcplayer == null) {
                    return;
                }else {
                    if (uc.EXPConfig.getBoolean("Debug")) {
                        player.sendMessage(ani.getType().toString());
                    }
                }


                if (uc.EXPConfig.getBoolean("PerWorld")) {

                    List<String> worlds = uc.EXPConfig.getStringList("Worlds");
                    for (String world : worlds)
                        if (player.getLocation().getWorld().getName().equalsIgnoreCase(world)) {
                            if (mcplayer == null)
                                return;

                            if (uc.EXPConfig.getBoolean("Passive-Mobs")) {
                                if (uc.EXPConfig.getBoolean("RandomEXP")) {

                                    int max = uc.EXPConfig.getInt(ani.getType().toString());
                                    int min = 0;

                                    Random r = new Random();
                                    int re = r.nextInt((max - min) + 1) + min;
                                    uc.GainEXP(player, re);
                                } else {
                                    uc.GainEXP(player, uc.EXPConfig.getInt(ani.getType().toString()));
                                }

                            }
                        }
                } else {
                    if (mcplayer == null)
                        return;
                    if (uc.EXPConfig.getBoolean("RandomEXP")) {

                        int max = uc.EXPConfig.getInt(ani.getType().toString());
                        int min = 0;

                        Random r = new Random();
                        int re = r.nextInt((max - min) + 1) + min;
                        uc.GainEXP(player, re);
                    } else {
                        uc.GainEXP(player, uc.EXPConfig.getInt(ani.getType().toString()));
                    }

                }

            }
        }
    }
    
    

}
