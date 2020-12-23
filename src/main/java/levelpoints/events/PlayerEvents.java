package levelpoints.events;

import com.bekvon.bukkit.residence.Residence;
import com.bekvon.bukkit.residence.containers.ResidencePlayer;
import com.cryptomorin.xseries.XBlock;
import com.cryptomorin.xseries.XMaterial;
import io.lumine.xikage.mythicmobs.MythicMobs;
import io.lumine.xikage.mythicmobs.mobs.ActiveMob;
import io.lumine.xikage.mythicmobs.utils.bossbar.BossBarColor;
import io.lumine.xikage.mythicmobs.utils.bossbar.BossBarStyle;
import levelpoints.Cache.FileCache;
import levelpoints.Cache.LangCache;
import levelpoints.Cache.SettingsCache;
import levelpoints.Containers.BoostersContainer;
import levelpoints.Containers.EXPContainer;
import levelpoints.Containers.LevelsContainer;
import levelpoints.Containers.PlayerContainer;
import levelpoints.Utils.AntiAbuseSystem;
import levelpoints.Utils.AsyncEvents;
import levelpoints.Utils.InventoryHolders.BoosterHolder;
import levelpoints.Utils.MessagesUtil;
import levelpoints.events.CustomEvents.*;
import levelpoints.levelpoints.Formatting;
import levelpoints.levelpoints.LevelPoints;

import levelpoints.levelpoints.SQL;
import me.lorinth.rpgmobs.LorinthsRpgMobs;
import net.sacredlabyrinth.Phaed.PreciousStones.PreciousStones;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.Crops;
import org.bukkit.material.MaterialData;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.ThreadLocalRandom;

public class PlayerEvents implements Listener {

    static HashMap<Player, Material> cachedBlocks = new HashMap<>();
    static HashMap<Player, Location> cachedLocations = new HashMap<>();

    public PlayerEvents(Plugin plugin) {
    }

    @EventHandler
    public void onPlayerHit(EntityDamageByEntityEvent event){
        if(event.getEntity() instanceof Player && event.getDamager() instanceof Player){
            Player attacker = (Player) event.getDamager();
            Player target = (Player) event.getEntity();

        }
        if (event.getDamager() instanceof Arrow) {
            final Arrow arrow = (Arrow) event.getDamager();

            if (arrow.getShooter() instanceof Player) {
                Player Attacker = (Player) arrow.getShooter();
                PlayerContainer con = AsyncEvents.getPlayerContainer(Attacker);
                Entity ent = null;
                if (!(event.getEntity() instanceof Player)) {
                    ent = event.getEntity();
                }
                if (ent != null) {
                    if (LevelPoints.getInstance().getConfig().getBoolean("MythicMobs")) {
                        if (MythicMobs.inst().getAPIHelper().isMythicMob(ent)) {
                            ActiveMob mob = MythicMobs.inst().getAPIHelper().getMythicMobInstance(ent);
                            if (FileCache.getConfig("mmConfig").getConfigurationSection("").getKeys(false).contains(mob.getType().getInternalName())) {
                                int min = FileCache.getConfig("mmConfig").getInt(mob.getType().getInternalName() + ".Level.Min");
                                int max = FileCache.getConfig("mmConfig").getInt(mob.getType().getInternalName() + ".Level.Max");
                                if (min > con.getLevel() || max < con.getLevel()) {
                                    event.setCancelled(true);
                                }
                                return;
                            }
                        }
                    }
                    if (LevelPoints.getInstance().getConfig().getBoolean("LorinthsRpgMobs")) {
                        if (LorinthsRpgMobs.GetLevelOfEntity(ent) != null) {
                            int level = LorinthsRpgMobs.GetLevelOfEntity(ent);
                            FileConfiguration configuration = FileCache.getConfig("rgbMobsConfig");
                            int rl = AsyncEvents.getLevelRequired(ent.getType().toString(), level, configuration);
                            if (con.getLevel() < rl) {
                                event.setCancelled(true);
                                return;

                            }
                        }
                    }
                }
            }
        }
        if(event.getDamager() instanceof Player){
            Player attacker = (Player) event.getDamager();
            if(event.getEntity() instanceof LivingEntity) {
                LivingEntity entity = (LivingEntity) event.getEntity();

                if (EXPContainer.gainEXP(TasksEnum.MobDeath)) {
                    PlayerContainer container = AsyncEvents.getPlayerContainer(attacker);
                    if(LevelsContainer.hasLevelBonus("Damage", container.getLevel())) {
                        event.setDamage(event.getDamage() + LevelsContainer.getLevelBonus("Damage", container.getLevel()));
                    }
                    if (LevelPoints.getInstance().getConfig().getBoolean("MythicMobs")) {
                        if (MythicMobs.inst().getAPIHelper().isMythicMob(entity)) {
                            ActiveMob mob = MythicMobs.inst().getAPIHelper().getMythicMobInstance(entity);
                            if (FileCache.getConfig("mmConfig").getConfigurationSection("").getKeys(false).contains(mob.getType().getInternalName())) {
                                int min = FileCache.getConfig("mmConfig").getInt(mob.getType().getInternalName() + ".Level.Min");
                                int max = FileCache.getConfig("mmConfig").getInt(mob.getType().getInternalName() + ".Level.Max");
                                if (min > container.getLevel() || max < container.getLevel()) {
                                    event.setCancelled(true);
                                }
                                return;
                            }
                        }
                    }
                    if (LevelPoints.getInstance().getConfig().getBoolean("LorinthsRpgMobs")) {
                        if (LorinthsRpgMobs.GetLevelOfEntity(entity) != null) {
                            int level = LorinthsRpgMobs.GetLevelOfEntity(entity);
                            FileConfiguration configuration = FileCache.getConfig("rgbMobsConfig");
                            int rl = AsyncEvents.getLevelRequired(entity.getType().toString(), level, configuration);
                            if(container.getLevel() < rl){
                                event.setCancelled(true);
                                return;

                            }
                        }
                    }
                    if (EXPContainer.getRequiredLevel(entity.getType(), SettingsEnum.Damage) > container.getLevel()) {
                        event.setCancelled(true);
                    }
                }
            }
        }
    }
    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        if (event.getEntity().getKiller() instanceof Player) {

            LivingEntity entity = event.getEntity();
            Player player = event.getEntity().getKiller();
            if (EXPContainer.gainEXP(TasksEnum.MobDeath)) {
                if (LevelPoints.getInstance().getConfig().getBoolean("MythicMobs")) {
                    if (MythicMobs.inst().getAPIHelper().isMythicMob(entity)) {
                        ActiveMob mob = MythicMobs.inst().getAPIHelper().getMythicMobInstance(entity);
                        double exp = FileCache.getConfig("mmConfig").getDouble(mob.getType().getInternalName() + ".EXP");
                        AsyncEvents.triggerEarnEXPEvent(TasksEnum.MobDeath, event, exp, player);
                        return;
                    }
                }
                if (LevelPoints.getInstance().getConfig().getBoolean("LorinthsRpgMobs")) {
                    if (LorinthsRpgMobs.GetLevelOfEntity(entity) != null) {
                        int level = LorinthsRpgMobs.GetLevelOfEntity(entity);
                        FileConfiguration configuration = FileCache.getConfig("rgbMobsConfig");

                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                double exp = AsyncEvents.getLevelEXP(entity.getType().toString(), level, configuration);
                                AsyncEvents.triggerEarnEXPEvent(TasksEnum.MobDeath, event, exp, player);
                            }
                        }.runTaskAsynchronously(LevelPoints.getInstance());
                        return;
                    }
                }
                if (EXPContainer.getEXP(entity.getType(), false) > 0) {
                    AsyncEvents.triggerEarnEXPEvent(TasksEnum.MobDeath, event, EXPContainer.getEXP(entity.getType(), true), player);
                }
            }
        }
    }

    @EventHandler
    public void onClick(InventoryClickEvent event){
        if(event.getClickedInventory() == null || event.getInventory() == null){
            return;
        }
        if(event.getClickedInventory().getHolder() instanceof BoosterHolder) {
            event.setCancelled(true);
            new BukkitRunnable() {
                @Override
                public void run() {
                    if (event.getCurrentItem().getType().equals(BoosterHolder.getBoosterItem().getType())) {
                        ItemStack item = event.getCurrentItem();
                        ItemMeta meta = item.getItemMeta();
                        String multiplier = Formatting.stripColor(meta.getDisplayName().replace("x", ""));
                        String time = Formatting.stripColor(meta.getLore().get(0).replace("Time: ", ""));
                        String amount = Formatting.stripColor(meta.getLore().get(1));
                        //event.getWhoClicked().sendMessage(String.valueOf(Formatting.formatDate(time)));

                        PlayerContainer container = AsyncEvents.getPlayerContainer((Player) event.getWhoClicked());
                        AsyncEvents.triggerBoosterActivation(Double.parseDouble(multiplier), time, (Player) event.getWhoClicked());
                        //container.removeBooster(Double.parseDouble(multiplier), time);
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                event.getWhoClicked().closeInventory();
                            }
                        }.runTask(LevelPoints.getInstance());

                    }
                }
            }.runTaskAsynchronously(LevelPoints.getInstance());
        }
    }

    @EventHandler
    public void onXP(PlayerExpChangeEvent event){
        if(SettingsCache.isBooleansEmpty() || !SettingsCache.isInCache("LpsToXpBar")) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    SettingsCache.cacheBoolean("LpsToXpBar", LevelPoints.getInstance().getConfig().getBoolean("LpsToXpBar"));
                    if (LevelPoints.getInstance().getConfig().getBoolean("LpsToXpBar")) {
                        event.setAmount(0);
                    }
                }
            }.runTaskAsynchronously(LevelPoints.getInstance());
        }else{
           if(SettingsCache.getBoolean("LpsToXpBar")){
               event.setAmount(0);
           }
        }

        //PlayerContainer container = AsyncEvents.getPlayerContainer(player);


    }
    @EventHandler
    public void boosterActivationEvent(BoosterActivationEvent event) {
        Player player = event.getPlayer();
        PlayerContainer container = AsyncEvents.getPlayerContainer(player);
        Date date = null;
        date = container.getBoosterDate();

        Date current = new Date();
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyy HH:mm:ss");
        try {
            current = format.parse(format.format(current));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (!current.after(date)) {
            event.setCancelled(true);
            return;
        }

    }


    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        if (!AsyncEvents.isPlayerInCache(event.getPlayer())) {
            cachedBlocks.put(event.getPlayer(), event.getBlock().getType());
            AsyncEvents.LoadPlayerData(event.getPlayer());
            AsyncEvents.addPlayerToContainerCache(event.getPlayer());

        }
        PlayerContainer container = AsyncEvents.getPlayerContainer(player);
        if (EXPContainer.gainEXP(TasksEnum.BlockBreak)) {
            if (EXPContainer.getRequiredLevel(event.getBlock().getType(), SettingsEnum.Break) <= container.getLevel()) {

                if (EXPContainer.getEXP(event.getBlock(), false, false) > 0) {

                    AsyncEvents.triggerEarnEXPEvent(TasksEnum.BlockBreak, event, EXPContainer.getEXP(event.getBlock(), true, false), event.getPlayer());
                }
            } else {
                if (FileCache.getConfig("langConfig").getBoolean("RequiredLevelOre.Enabled")) {
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            for (String x : LangCache.getRequiredLevel()) {
                                player.sendMessage(Formatting.basicColor(x, player).replace("{lp_Required_Level}",
                                        String.valueOf(EXPContainer.getRequiredLevel(event.getBlock().getType(), SettingsEnum.Break))));
                            }
                        }
                    }.runTaskAsynchronously(LevelPoints.getInstance());
                    event.setCancelled(true);
                }
            }
        }
        if(EXPContainer.gainEXP(TasksEnum.Farming)) {
            if (FileCache.getConfig("expConfig").getBoolean("FarmingEXP.Enabled")) {
                if (FileCache.getConfig("expConfig").getConfigurationSection("FarmingEXP.Items").getKeys(false).contains(event.getBlock().getType().toString())) {
                    Crops crops = new Crops();
                    crops.setData(event.getBlock().getData());
                    if (crops.getData() == FileCache.getConfig("expConfig").getInt("FarmingEXP.Items." + event.getBlock().getType().toString() + ".Age")) {
                        AsyncEvents.triggerEarnEXPEvent(TasksEnum.BlockBreak, event, EXPContainer.getEXP(event.getBlock(), true, true), event.getPlayer());
                    }
                }
            }
        }


        if (container.isBoosterDone()) {
            container.setMultiplier(1.0);
        }
    }


    @EventHandler
    public void onEarnEXP(EarnExpEvent event) {
        TasksEnum tasksEnum = null;
        if (event.getTaskEvent() instanceof BlockBreakEvent) {
            tasksEnum = TasksEnum.BlockBreak;
            BlockBreakEvent event1 = (BlockBreakEvent) event.getTaskEvent();
            if (LevelPoints.getInstance().getConfig().getBoolean("GriefPrevention")) {
                if (!AntiAbuseSystem.canEarnEXPGP(event.getPlayer(), event1.getBlock().getLocation())) {
                    event.setCancelled(true);
                    return;
                }
            }
            if (AntiAbuseSystem.denyWorldGuard(event.getPlayer(), event1.getBlock())) {
                event.setCancelled(true);
                return;
            }
            if (!AntiAbuseSystem.canEarnEXP(event1.getBlock().getLocation())) {
                if (AntiAbuseSystem.canBreakBlock(event1.getBlock().getLocation())) {
                    AntiAbuseSystem.removeBlockFromLocation(event1.getBlock().getLocation());
                } else {
                    event1.setCancelled(true);
                }
                event.setCancelled(true);
                return;
            }
            if(AntiAbuseSystem.cancelPreciousStones()){
                if (!PreciousStones.API().canBreak(event.getPlayer(), event1.getBlock().getLocation())) {
                    event.setCancelled(true);
                    return;
                }
            }
            if(AntiAbuseSystem.denyWorldSupport(event.getPlayer())){
                event.setCancelled(true);
                return;
            }
            new BukkitRunnable() {
                @Override
                public void run() {
                    if(AntiAbuseSystem.cancelResidence()){
                        ResidencePlayer rPlayer = Residence.getInstance().getPlayerManager().getResidencePlayer(event.getPlayer());
                        boolean canBreak = rPlayer.canBreakBlock(event1.getBlock(), false);
                        event.setCancelled(!canBreak);
                        if(event.isCancelled()) {
                            return;
                        }
                    }
                }
            }.runTask(LevelPoints.getInstance());



            if (LevelPoints.getInstance().getConfig().getBoolean("Actionbar.Enabled")) {
                MessagesUtil.sendActionBar(event.getPlayer(), LevelPoints.getInstance().getConfig().getString("Actionbar.Details.Text").replace("{EXP_Earn_Amount}", String.valueOf(event.getAmount())));
            }

        }
        AsyncEvents.getPlayerContainer(event.getPlayer()).setXpBar();
        if (event.getTaskEvent() instanceof EntityDeathEvent) {
            tasksEnum = TasksEnum.MobDeath;
            if (LevelPoints.getInstance().getConfig().getBoolean("Actionbar.Enabled")) {
                MessagesUtil.sendActionBar(event.getPlayer(), LevelPoints.getInstance().getConfig().getString("Actionbar.Details.Text").replace("{EXP_Earn_Amount}", String.valueOf(event.getAmount())));
            }
        }
        TasksEnum finalTasksEnum = tasksEnum;
        if (FileCache.getConfig("langConfig").getBoolean("EXPEarn.Enabled")) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    for (String x : LangCache.getEXPEarn()) {
                        event.getPlayer().sendMessage(Formatting.basicColor(x, event.getPlayer())
                                .replace("{lp_Earn_Exp}", String.valueOf(event.getAmount()))
                                .replace("{lp_Earn_Task}", finalTasksEnum.toString()));
                    }
                }
            }.runTaskAsynchronously(LevelPoints.getInstance());
        }
    }
    @EventHandler
    public void onPreJoinEvent(AsyncPlayerPreLoginEvent event){
        AsyncEvents.RunPlayerCache(event.getUniqueId(), event.getName());
        if(LevelPoints.getInstance().getConfig().getBoolean("UseSQL")) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    try {
                        if (SQL.getConnection().isClosed()) {
                            SQL.SQLReconnect();
                        }
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }
                    SQL.createPlayer(event.getUniqueId(), event.getName());
                }
            }.runTaskLaterAsynchronously(LevelPoints.getInstance(), 10);
        }
    }
    @EventHandler
    public void onPreCommand(PlayerCommandPreprocessEvent event){
        if(event.getMessage().equalsIgnoreCase("/stop") || event.getMessage().equalsIgnoreCase("/restart")){
            AsyncEvents.MassSaveCache();
        }
    }

    @EventHandler
    public void onJoinEvent(PlayerJoinEvent event){
        AsyncEvents.LoadPlayerData(event.getPlayer());
        AsyncEvents.addPlayerToContainerCache(event.getPlayer());

        new BukkitRunnable() {
            @Override
            public void run() {
                if (!AsyncEvents.isInTopCache(event.getPlayer())) {
                    AsyncEvents.updateLevelInCache(event.getPlayer(), AsyncEvents.getPlayerContainer(event.getPlayer()).getLevel());
                }
            }
        }.runTaskLaterAsynchronously(LevelPoints.getInstance(), (20) * 5);

    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        if (AsyncEvents.isPlayerInCache(event.getPlayer())) {

            if(LevelPoints.getInstance().getConfig().getBoolean("UseSQL")) {
                try {
                    if (SQL.getConnection().isClosed()) {
                        SQL.SQLReconnect();
                    }
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        SQL.RunSQLUpload(event.getPlayer());
                    }
                }.runTaskAsynchronously(LevelPoints.getInstance());
            }

            AsyncEvents.RunSaveCache(event.getPlayer());


        }
    }

    @EventHandler
    public void onCraft(CraftItemEvent event){
        ItemStack item = event.getCurrentItem();
        PlayerContainer container = AsyncEvents.getPlayerContainer((Player) event.getWhoClicked());
        if(!FileCache.getConfig("levelConfig").getBoolean("Crafting.RequiredLevel.Enabled")){
            return;
        }

        if(FileCache.getConfig("levelConfig").getInt("Crafting.RequiredLevel." + item.getType() + ".Level") > container.getLevel()){
            if(FileCache.getConfig("levelConfig").getBoolean("Crafting.RequiredLevel." + item.getType() + ".Message.Enabled")){
                String x = FileCache.getConfig("levelConfig").getString("Crafting.RequiredLevel." + item.getType() + ".Message.Text");
                event.getWhoClicked().sendMessage(Formatting.basicColor(x, (Player) event.getWhoClicked())
                        .replace("{lp_Crafting_Level}", String.valueOf(FileCache.getConfig("levelConfig").getInt("Crafting.RequiredLevel." + item.getType() + ".Level")))
                        .replace("{lp_Crafting_Item}", item.getType().toString()));
            }
            event.setCancelled(true);
            return;
        }

    }
    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Block block = event.getBlock();
        if (!AsyncEvents.isPlayerInCache(event.getPlayer())) {
            cachedBlocks.put(event.getPlayer(), event.getBlock().getType());
            cachedLocations.put(event.getPlayer(), event.getBlock().getLocation());
            AsyncEvents.LoadPlayerData(event.getPlayer());
            AsyncEvents.addPlayerToContainerCache(event.getPlayer());
        }
        if (EXPContainer.gainEXP(TasksEnum.BlockBreak)) {
            PlayerContainer container = AsyncEvents.getPlayerContainer(event.getPlayer());
            if (EXPContainer.getRequiredLevel(event.getBlock().getType(), SettingsEnum.Place) > container.getLevel()) {
                event.setCancelled(true);
            }
            if (EXPContainer.getEXP(block, false, false) > 0) {
                AntiAbuseSystem.addBlockToLocation(block.getLocation());
            }
        }
    }

    @EventHandler
    public void onFish(PlayerFishEvent event){
        if(event.getState().equals(PlayerFishEvent.State.CAUGHT_FISH)){
            if(FileCache.getConfig("levelConfig").getBoolean("Fishing.RequiredLevel.Enabled")){
                if(AsyncEvents.getPlayerContainer(event.getPlayer()).getLevel() >= FileCache.getConfig("levelConfig").getInt("Fishing.RequiredLevel.Level")){
                    return;
                }else{
                    if(FileCache.getConfig("levelConfig").getBoolean("Fishing.RequiredLevel.Message.Enabled")){
                        String x = FileCache.getConfig("levelConfig").getString("Fishing.RequiredLevel.Message.Text");
                        event.getPlayer().sendMessage(Formatting.basicColor(x, event.getPlayer()));
                    }
                    event.setCancelled(true);
                }
            }else{
                return;
            }
        }
    }

    @EventHandler
    public void onLevelUp(LevelUpEvent event) {
        Player player = event.getPlayer();
        AsyncEvents.getPlayerContainer(player).setXpBar();
        int level = event.getLevel();
        AsyncEvents.updateLevelInCache(player, level);
        File TopFile = new File(LevelPoints.getInstance().getDataFolder(), "TopList.yml");
        FileConfiguration TopConfig = YamlConfiguration.loadConfiguration(TopFile);
        TopConfig.set(player.getUniqueId() + ".Name", player.getName());
        TopConfig.set(player.getUniqueId() + ".Level", level);
        if (FileCache.getConfig("levelConfig").getBoolean("LevelBonus.Enabled")) {
            if (LevelsContainer.hasLevelBonus("Health", level)) {
                player.setMaxHealth(player.getMaxHealth() + LevelsContainer.getLevelBonus("Health", level));
            } else {
                player.setMaxHealth(20);
            }
        }
        try {
            TopConfig.save(TopFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (FileCache.getConfig("langConfig").getBoolean("Formats.LevelUp.Chat.Enabled")) {
            for (String x : FileCache.getConfig("langConfig").getStringList("Formats.LevelUp.Chat.Text")) {
                player.sendMessage(Formatting.basicColor(x, player)
                        .replace("{level}", String.valueOf(level))
                        .replace("{player}", player.getName()));
            }
        }
        if (FileCache.getConfig("langConfig").getBoolean("Formats.LevelUp.ActionBar.Enabled")) {
            MessagesUtil.sendActionBar(player, Formatting.basicColor(FileCache.getConfig("langConfig").getString("Formats.LevelUp.ActionBar.Text"), player)
                    .replace("{level}", String.valueOf(level))
                    .replace("{player}", player.getName()));
        }
        if (FileCache.getConfig("langConfig").getBoolean("Formats.LevelUp.Title.Enabled")) {
            MessagesUtil.sendTitle(player,
                    Formatting.basicColor(
                            FileCache.getConfig("langConfig").getString("Formats.LevelUp.Title.Text.Top"), player)
                            .replace("{level]", String.valueOf(level))
                            .replace("{player}", player.getName()),
                    Formatting.basicColor(
                            FileCache.getConfig("langConfig").getString("Formats.LevelUp.Title.Text.Bottom"), player)
                            .replace("{level}", String.valueOf(level))
                            .replace("{player}", player.getName())
            );
        }
        AsyncEvents.giveReward(player, level, RewardsType.valueOf(FileCache.getConfig("rewardsConfig")
                .getString("Settings.Method")));
        if (LevelPoints.getInstance().getConfig().getBoolean("UseSQL")) {
            SQL.RunSQLUpload(player);
        }
        float percentage = (float) AsyncEvents.getPlayerContainer(player).getEXP();
        double val = (percentage / AsyncEvents.getPlayerContainer(player).getRequiredEXP());
        if (!Bukkit.getVersion().contains("1.8")) {
            MessagesUtil.sendBossBar(player,
                    FileCache.getConfig("langConfig").getString("Formats.LevelUp.BossBar.Text"),
                    BarColor.valueOf(LevelPoints.getInstance().getConfig().getString("BossBarColor")),
                    BarStyle.SOLID,
                    val);
        }
    }

}
