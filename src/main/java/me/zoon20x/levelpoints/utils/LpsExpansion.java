package me.zoon20x.levelpoints.utils;


import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.zoon20x.levelpoints.LevelPoints;
import me.zoon20x.levelpoints.containers.Player.PlayerData;
import me.zoon20x.levelpoints.containers.Settings.Blocks.BlockUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class LpsExpansion extends PlaceholderExpansion {
    @Override
    public boolean canRegister() {
        return Bukkit.getPluginManager().getPlugin("LevelPoints") != null;
    }

    @Override
    public boolean register() {
        if (!canRegister()) {
            return false;
        }
        if (LevelPoints.getInstance() == null) {
            return false;
        }
        return super.register();
    }

    @Override
    public String getAuthor() {
        return "Zoon20X";
    }

    @Override
    public String getIdentifier() {
        return "LevelPoints";
    }

    @Override
    public String getRequiredPlugin() {
        return "LevelPoints";
    }

    @Override
    public String getVersion() {
        return "2.0";
    }

    @Override
    public String onRequest(OfflinePlayer player, String identifier) {
        if (LevelPoints.getInstance().isReloading()) {
            return "Reloading";
        }

        UUID uuid = player.getUniqueId();
        if (!LevelPoints.getInstance().getPlayerStorage().hasPlayerFile(uuid)) {
            return "";
        }

        PlayerData data = LevelPoints.getInstance().getPlayerStorage().getLoadedData(uuid);
        if (identifier.equals("player_level")) {
            return MessageUtils.getLevelColor(uuid);
        }
        if (identifier.equals("player_exp")) {
            return String.valueOf(data.getExp());
        }
        if (identifier.equals("player_required_exp")) {
            return String.valueOf(data.getRequiredExp());
        }
        if (identifier.equals("player_exp_remaining")) {
            return String.valueOf(data.getRemainingExp());
        }
        if (identifier.equals("player_prestige")) {
            return String.valueOf(data.getPrestige());
        }
        if (identifier.equals("player_progress")) {
            return data.getProgress() + "%";
        }
        if (identifier.equals("player_progress_bar"))
            return ProgressStatics.makeProgressBar(null, data.getExp(), data.getRequiredExp());


        if (identifier.equals("player_booster_id")) {
            return data.getActiveBooster().getID();
        }
        if (identifier.equals("player_booster_time")) {
            return MessageUtils.formatDate(data.getActiveBooster().getDateExpire());
        }
        if (identifier.equals("player_booster_date")) {
            return data.getActiveBooster().getDateExpire().toString();
        }
        if (identifier.equals("player_booster_multiplier")) {
            return String.valueOf(data.getActiveBooster().getMultiplier());
        }

        if (identifier.equals("player_pvp_rank")) {
            if (data.getBracketData() == null) {
                return "none";
            }
            return data.getBracketData().getId();
        }
        if (identifier.equals("player_pvp_enabled")) {
            if (data.getBracketData() == null) {
                return "true";
            }
            return String.valueOf(data.getBracketData().isPvpEnabled());
        }

        if (identifier.contains("required_break_")) {
            String id = identifier.replace("required_break_", "");
            List<String> dat = Arrays.asList(id.split(":"));

            if (!BlockUtils.hasBlockData(Material.getMaterial(dat.get(0)))) {
                return "0";
            }
            if (dat.size() == 2) {
                return String.valueOf(BlockUtils.getBlockData(Material.getMaterial(dat.get(0)), Byte.valueOf(dat.get(1))).getBreakRequired());

            }
            return String.valueOf(BlockUtils.getBlockData(Material.getMaterial(id), (byte) 0).getBreakRequired());
        }

        return "";
    }
}