package me.zoon20x.levelpoints.containers.Settings.Configs;

import io.lumine.xikage.mythicmobs.MythicMobs;
import me.zoon20x.levelpoints.LevelPoints;
import me.zoon20x.levelpoints.containers.Breeding.BreedingData;
import me.zoon20x.levelpoints.containers.Breeding.BreedingUtils;
import me.zoon20x.levelpoints.containers.ExtraSupport.MythicMobsData;
import me.zoon20x.levelpoints.containers.ExtraSupport.MythicMobsSettings;
import me.zoon20x.levelpoints.containers.Player.PlayerData;
import me.zoon20x.levelpoints.containers.Settings.Blocks.BlockData;
import me.zoon20x.levelpoints.containers.Settings.Blocks.BlockUtils;
import me.zoon20x.levelpoints.containers.Settings.Mobs.MobData;
import me.zoon20x.levelpoints.containers.Settings.Mobs.MobUtils;
import me.zoon20x.levelpoints.utils.*;
import me.zoon20x.levelpoints.utils.Formatter;
import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;

import java.util.*;

public class LevelsSettings {

    private static HashMap<Integer, Double> requiredLeveledEXP = new HashMap<>();
    private Integer maxLevel;
    private Integer maxPrestige;
    private Integer startingLevel;
    private double startingXp;
    private Integer startingPrestige;
    private FormulaType formulaType;
    private LevelUpType levelUpType;
    private HashSet<PlayerData> data = new HashSet<>();
    private int topValue;

    public LevelsSettings() {
        requiredLeveledEXP.clear();
        this.startingXp = LevelPoints.getInstance().getFilesGenerator().levelSettings.getConfig().getDouble(DataLocation.StartingExp);
        this.startingXp = LevelPoints.getInstance().getFilesGenerator().levelSettings.getConfig().getDouble(DataLocation.StartingExp);
        this.startingLevel = LevelPoints.getInstance().getFilesGenerator().levelSettings.getConfig().getInt(DataLocation.StartingLevel);
        this.startingPrestige = LevelPoints.getInstance().getFilesGenerator().levelSettings.getConfig().getInt(DataLocation.StartingPrestige);
        this.maxLevel = LevelPoints.getInstance().getFilesGenerator().levelSettings.getConfig().getInt(DataLocation.MaxLevel);
        this.maxPrestige = LevelPoints.getInstance().getFilesGenerator().levelSettings.getConfig().getInt(DataLocation.MaxPrestige);
        this.formulaType = FormulaType.valueOf(LevelPoints.getInstance().getFilesGenerator().levelSettings.getConfig().getString(DataLocation.FormulaType));
        this.levelUpType = LevelUpType.valueOf(LevelPoints.getInstance().getFilesGenerator().levelSettings.getConfig().getString(DataLocation.LevelUpType));
    }


    public Integer getMaxLevel() {
        return maxLevel;
    }

    public Integer getMaxPrestige() {
        return maxPrestige;
    }

    public Integer getStartingLevel() {
        return startingLevel;
    }

    public String getBasicFormula() {
        return LevelPoints.getInstance().getFilesGenerator().levelSettings.getConfig().getString(DataLocation.BasicFormula);
    }

    public String getAdvancedFormula(int level) {
        return LevelPoints.getInstance().getFilesGenerator().levelSettings.getConfig().getString(DataLocation.getAdvancedFormula(level));
    }

    public Double getCustomLevel(int level) {
        return LevelPoints.getInstance().getFilesGenerator().levelSettings.getConfig().getDouble(DataLocation.getCustomLeveling(level));
    }

    public Set<Integer> getLoadedLevels() {
        return requiredLeveledEXP.keySet();
    }

    public Double getRequireExp(Integer level) {
        return requiredLeveledEXP.get(level);
    }

    public Boolean canBreak(Material material, Byte x, PlayerData data) {
        if (!BlockUtils.hasBlockData(material)) {
            return true;
        }
        if (!BlockUtils.hasBlockExtraData(material, x)) {
            return true;
        }

        BlockData a = BlockUtils.getBlockData(material, x);
        if (data.getLevel() >= a.getBreakRequired()) {
            return true;
        }
        return false;
    }
    public Boolean canBreed(EntityType type, PlayerData data) {
        if (!BreedingUtils.hasBreed(type)) {
            return true;
        }

        BreedingData a = BreedingUtils.getBreedData(type);
        if (data.getLevel() >= a.getBreedRequirement()) {
            return true;
        }
        return false;
    }
    public Boolean canDamage(EntityType type, PlayerData data){
        if(!MobUtils.hasMob(type)){
            return true;
        }
        MobData a = MobUtils.getMobData(type);
        if(data.getLevel() >= a.getDamageRequired()){
            return true;
        }
        return false;
    }
    public Boolean canDamageMythicMobs(String type, PlayerData data){
        if(!LevelPoints.getInstance().getMythicMobsSettings().hasMobData(type)){
            return true;
        }
        MythicMobsData a = LevelPoints.getInstance().getMythicMobsSettings().getMobData(type);
        if(data.getLevel() >= a.getLevelMin() && data.getLevel() <= a.getLevelMax()){
            return true;
        }
        return false;
    }

    public Boolean canPlace(Material material, Byte x, PlayerData data) {
        if (!BlockUtils.hasBlockData(material)) {
            return true;
        }
        BlockData a = BlockUtils.getBlockData(material, x);
        if(a == null)
            return true;
        return data.getLevel() >= a.getPlaceRequired();
    }


    public void generateRequired() {

        long startTime = System.nanoTime();

        for (int i = getStartingLevel(); i != getMaxLevel() + 1; i++) {
            Formatter formatter = new Formatter(null, i, 0, 0, 0, 0, 0.0);
            if (getLevelUpType() == LevelUpType.CUSTOMLEVEL) {
                if (LevelPoints.getInstance().getFilesGenerator().levelSettings.getConfig().isSet(DataLocation.getCustomLeveling(i))) {

                    requiredLeveledEXP.put(i, getCustomLevel(i));
                } else {
                    LevelPoints.getDebug(DebugSeverity.SEVER, "Failed to load required experience for level " + i + "; exit code:" + ErrorCodes.CustomExperience);
                }
            } else {
                String form;
                if (getFormulaType() == FormulaType.BASIC) {
                    form = MessageUtils.format(getBasicFormula(), formatter);
                } else {
                    if (getAdvancedFormula(i) == null) {
                        LevelPoints.getDebug(DebugSeverity.SEVER, "Failed to load required experience for level " + i + "; exit code:" + ErrorCodes.AdvancedFormulaError);
                        this.maxLevel = i - 1;
                        return;
                    }
                    form = MessageUtils.format(getAdvancedFormula(i), formatter);
                }

                //.println(form);
                Double requiredEXP = 1.0;
                try {
                    Expression expression = new ExpressionBuilder(form).build();
                    requiredEXP = expression.evaluate();
                } catch (Exception e) {
                    if (getFormulaType() == FormulaType.BASIC) {
                        LevelPoints.getDebug(DebugSeverity.SEVER, "Failed to load required experience for level " + i + "; exit code:" + ErrorCodes.BasicFormulaError);
                    } else {
                        LevelPoints.getDebug(DebugSeverity.SEVER, "Failed to load required experience for level " + i + "; exit code:" + ErrorCodes.AdvancedFormulaError);
                    }

                }
                //LevelPoints.getDebug(DebugSeverity.NORMAL, requiredEXP);
                requiredLeveledEXP.put(i, requiredEXP);
            }
        }

        long endTime = System.nanoTime();
        long duration = ((endTime - startTime) / 1000000);
        LevelPoints.getDebug(DebugSeverity.NORMAL, "Required Exp generator took " + duration + "ms, to load " + requiredLeveledEXP.size() + " levels");
    }
    public FormulaType getFormulaType() {
        return formulaType;
    }

    public LevelUpType getLevelUpType() {
        return levelUpType;
    }



    public double getStartingExp() {
        return startingXp;
    }

    public Integer getStartingPrestige() {
        return startingPrestige;
    }
}
