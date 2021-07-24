package me.zoon20x.levelpoints.utils;

public class DataLocation {


    public static String sqlHost = "MySQL.Data.host";
    public static String sqlPort = "MySQL.Data.port";
    public static String sqlUsername = "MySQL.Data.username";
    public static String sqlDatabase = "MySQL.Data.database";
    public static String sqlPassword = "MySQL.Data.password";
    public static String sqlTable = "MySQL.Data.table";
    public static String sqlSsl = "MySQL.Data.ssl";


    public static String Name = "Name";
    public static String Level = "Level";
    public static String EXP = "EXP.Amount";
    public static String Prestige = "Prestige";
    public static String ActiveBoosterTime = "ActiveBooster.Time";
    public static String ActiveBoosterMultiplier = "ActiveBooster.Multiplier";
    public static String ActiveBoosterID = "ActiveBooster.ID";
    public static String BoosterList = "BoosterData";


    public static String BlockEnabled = "Blocks.Enabled";
    public static String BlockSettings = "Blocks.Settings";
    public static String MobEnabled = "Mobs.Enabled";
    public static String MobSettings = "Mobs.Settings";
    public static String CraftingEnabled = "Crafting.Enabled";
    public static String CraftingSettings = "Crafting.Settings";

    public static String StartingLevel = "Starting.Level";
    public static String StartingExp = "Starting.Experience";
    public static String StartingPrestige = "Starting.Prestige";

    public static String MaxLevel = "Max.Level";
    public static String MaxPrestige = "Max.Prestige";

    public static String LevelUpType = "Experience.LevelUpType";
    public static String FormulaType = "Experience.Formula.FormulaType";
    public static String getCustomLeveling(int level){
        return "Experience.CustomLevel.Level-" + level;
    }
    public static String BasicFormula = "Experience.Formula.FormulaString";
    public static String getAdvancedFormula(int level){
        return "Experience.Formula.CustomFormula.Level-" + level;
    }

    public static String LevelColorsList = "PlaceholderSettings.LevelColors.Colors";
    public static String getLevelColorsData(String x){
        return LevelColorsList + "." +x;
    }

    public static String langExperienceBreakMessage = "Experience.Earning.Break.Message";
    public static String langExperienceBreakEnabled = "Experience.Earning.Break.Enabled";


    public static String langTopListTopText = "TopList.Top.Text";
    public static String langTopListTopEnabled = "TopList.Top.Text";

    public static String langTopListMiddleText = "TopList.Middle.Text";
    public static String langTopListMiddleEnabled = "TopList.Middle.Text";

    public static String langTopListBottomText = "TopList.Bottom.Text";
    public static String langTopListBottomEnabled = "TopList.Bottom.Text";



    public static String langExperienceMobsMessage = "Experience.Earning.Mobs.Message";
    public static String langExperienceMobsEnabled = "Experience.Earning.Mobs.Enabled";

    public static String langExperiencePvpMessage = "Experience.Earning.Pvp.Message";
    public static String langExperiencePvpEnabled = "Experience.Earning.Pvp.Enabled";

    public static String langExperienceCommandSenderMessage = "Experience.Earning.Command.Sender.Message";
    public static String langExperienceCommandSenderEnabled = "Experience.Earning.Command.Sender.Enabled";

    public static String langExperienceCommandReceiverMessage = "Experience.Earning.Command.Receiver.Message";
    public static String langExperienceCommandReceiverEnabled = "Experience.Earning.Command.Receiver.Enabled";

    public static String langRemoveCommandSenderMessage = "Experience.Remove.Command.Sender.Message";
    public static String langRemoveCommandSenderEnabled = "Experience.Remove.Command.Sender.Enabled";

    public static String langRemoveCommandReceiverMessage = "Experience.Remove.Command.Receiver.Message";
    public static String langRemoveCommandReceiverEnabled = "Experience.Remove.Command.Receiver.Enabled";

    public static String langLevelsAddCommandSenderMessage = "Levels.Add.Command.Sender.Message";
    public static String langLevelsAddCommandSenderEnabled = "Levels.Add.Command.Sender.Enabled";

    public static String langLevelsAddCommandReceiverMessage = "Levels.Add.Command.Receiver.Message";
    public static String langLevelsAddCommandReceiverEnabled = "Levels.Add.Command.Receiver.Enabled";


    public static String langLevelsRemoveCommandSenderMessage = "Levels.Remove.Command.Sender.Message";
    public static String langLevelsRemoveCommandSenderEnabled = "Levels.Remove.Command.Sender.Enabled";

    public static String langLevelsRemoveCommandReceiverMessage = "Levels.Remove.Command.Receiver.Message";
    public static String langLevelsRemoveCommandReceiverEnabled = "Levels.Remove.Command.Receiver.Enabled";

    public static String langLevelsSetCommandSenderMessage = "Levels.Set.Command.Sender.Message";
    public static String langLevelsSetCommandSenderEnabled = "Levels.Set.Command.Sender.Enabled";

    public static String langLevelsSetCommandReceiverMessage = "Levels.Set.Command.Receiver.Message";
    public static String langLevelsSetCommandReceiverEnabled = "Levels.Set.Command.Receiver.Enabled";



    public static String langPrestigeCommandDeniedMessage = "Prestige.Command.Denied.Message";
    public static String langPrestigeCommandDeniedEnabled = "Prestige.Command.Denied.Enabled";

    public static String langPrestigeCommandApprovedMessage = "Prestige.Command.Approved.Message";
    public static String langPrestigeCommandApprovedEnabled = "Prestige.Command.Approved.Enabled";


    public static String langPrestigeAddCommandSenderMessage = "Prestige.Add.Command.Sender.Message";
    public static String langPrestigeAddCommandSenderEnabled = "Prestige.Add.Command.Sender.Enabled";

    public static String langPrestigeAddCommandReceiverMessage = "Prestige.Add.Command.Receiver.Message";
    public static String langPrestigeAddCommandReceiverEnabled = "Prestige.Add.Command.Receiver.Enabled";


    public static String langPrestigeRemoveCommandSenderMessage = "Prestige.Remove.Command.Sender.Message";
    public static String langPrestigeRemoveCommandSenderEnabled = "Prestige.Remove.Command.Sender.Enabled";

    public static String langPrestigeRemoveCommandReceiverMessage = "Prestige.Remove.Command.Receiver.Message";
    public static String langPrestigeRemoveCommandReceiverEnabled = "Prestige.Remove.Command.Receiver.Enabled";

    public static String langPrestigeSetCommandSenderMessage = "Prestige.Set.Command.Sender.Message";
    public static String langPrestigeSetCommandSenderEnabled = "Prestige.Set.Command.Sender.Enabled";

    public static String langPrestigeSetCommandReceiverMessage = "Prestige.Set.Command.Receiver.Message";
    public static String langPrestigeSetCommandReceiverEnabled = "Prestige.Set.Command.Receiver.Enabled";
    

    public static String langRequiredBreakMessage = "RequiredLevels.Break.Message";
    public static String langRequiredBreakEnabled = "RequiredLevels.Break.Enabled";

    public static String langRequiredPlaceMessage = "RequiredLevels.Place.Message";
    public static String langRequiredPlaceEnabled = "RequiredLevels.Place.Enabled";

    public static String langRequiredDamageMessage = "RequiredLevels.Damage.Message";
    public static String langRequiredDamageEnabled = "RequiredLevels.Damage.Enabled";

    public static String langRequiredPvpMessage = "RequiredLevels.Pvp.Message";
    public static String langRequiredPvpEnabled = "RequiredLevels.Pvp.Enabled";


    public static String abuseRegionLockedEnabled = "WorldGuard.LockedRegions.Enabled";
    public static String abuseRegionLockedRegions = "WorldGuard.LockedRegions.Regions";

    public static String abuseWorldGuardDisableEnabled = "WorldGuard.DisabledEarn.Enabled";
    public static String abuseWorldGuardDisableRegions = "WorldGuard.DisabledEarn.Regions";
    public static String abuseSilkTouchEnabled = "SilkTouch.Enabled";

    public static String pvpBracketsEnabled = "Brackets.Enabled";
    public static String bracketsMessagesDifferentEnabled = "BracketsMessages.DifferentBrackets.Enabled";
    public static String bracketsMessagesDifferentText = "BracketsMessages.DifferentBrackets.Text";
    public static String bracketsMessagesNoPvpEnabled = "BracketsMessages.NoPvp.Enabled";
    public static String bracketsMessagesNoPvpText = "BracketsMessages.NoPvp.Text";

    public static String getPvpLevelsIncluded(String x){
        return "Brackets." + x + ".levelsIncluded";
    }
    public static String getPvpToggle(String x){
        return "Brackets." + x + ".pvp";
    }

    public static String getAbuseRegionLevel(String type, String region){
        if(type.equalsIgnoreCase("Min")){
            return "WorldGuard.LockedRegions.Regions." + region + ".Levels.Min";
        }
        return "WorldGuard.LockedRegions.Regions." + region + ".Levels.Max";
    }
    public static String getAbuseRegionTeleportEnabled(String region){
        return "WorldGuard.LockedRegions.Regions." + region + ".Teleport.Enabled";
    }
    public static String getAbuseRegionTeleportCords(String region){
        return "WorldGuard.LockedRegions.Regions." + region + ".Teleport.Cords";
    }
    public static String getAbuseRegionMessageEnabled(String region){
        return "WorldGuard.LockedRegions.Regions." + region + ".Message.Enabled";
    }
    public static String getAbuseRegionMessageText(String region){
        return "WorldGuard.LockedRegions.Regions." + region + ".Message.Message";
    }

    public static String getBlockMinEXP(String x){
        return BlockSettings + "." + x + ".Exp.Min";
    }
    public static String getBlockMaxEXP(String x){
        return BlockSettings + "." + x + ".Exp.Max";
    }

    public static String getMobsMinEXP(String x){
        return MobSettings + "." + x + ".Exp.Min";
    }
    public static String getMobsMaxEXP(String x){
        return MobSettings + "." + x + ".Exp.Max";
    }
    public static String getCraftMinEXP(String x){
        return CraftingSettings + "." + x + ".Exp.Min";
    }
    public static String getCraftMaxEXP(String x){
        return CraftingSettings + "." + x + ".Exp.Max";
    }

    public static String getRequiredBreak(String x){
        return BlockSettings + "." + x + ".RequiredLevel.Break";
    }

    public static String getRequiredPlace(String x){
        return BlockSettings + "." + x + ".RequiredLevel.Place";
    }

    public static String getRequiredCraft(String x){
        return CraftingSettings + "." + x + ".RequiredLevel.Craft";
    }
    public static String getRequiredDamage(String x){
        return MobSettings + "." + x + ".RequiredLevel.Damage";
    }
    public static String getUserBoosterList(String x){
        return "BoosterData." + x;
    }
}
