package me.zoon20x.levelpoints.containers.Settings.Configs;

import me.zoon20x.levelpoints.files.FilesStorage;
import me.zoon20x.levelpoints.utils.DataLocation;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.List;

public class LangSettings {

    private final List<String> playerHelp;
    private final List<String> adminHelp;


    private final List<String> playerInfo;


    private final List<String> topMessageTopText;
    private final boolean topMessageTopEnabled;

    private final List<String> topMessageMiddleText;
    private final boolean topMessageMiddleEnabled;

    private final List<String> topMessageBottomText;
    private final boolean topMessageBottomEnabled;

    private final String ExperienceBreak;
    private final boolean expBreakEnabled;

    private final String ExperienceMobs;
    private final boolean expMobsEnabled;

    private final String ExperiencePvp;
    private final boolean expPvpEnabled;

    private final String ExperienceCommandSender;
    private final boolean expCommandSenderEnabled;

    private final String ExperienceCommandReceiver;
    private final boolean expCommandReceiverEnabled;

    private final String RemoveCommandSender;
    private final boolean removeCommandSenderEnabled;

    private final String RemoveCommandReceiver;
    private final boolean removeCommandReceiverEnabled;

    private final String LevelsAddCommandSender;
    private final boolean levelsAddCommandSenderEnabled;

    private final String LevelsAddCommandReceiver;
    private final boolean levelsAddCommandReceiverEnabled;


    private final String LevelsRemoveCommandSender;
    private final boolean levelsRemoveCommandSenderEnabled;

    private final String LevelsRemoveCommandReceiver;
    private final boolean levelsRemoveCommandReceiverEnabled;


    private final String LevelsSetCommandSender;
    private final boolean levelsSetCommandSenderEnabled;

    private final String LevelsSetCommandReceiver;
    private final boolean levelsSetCommandReceiverEnabled;


    private final String PrestigeCommandDeniedMessage;
    private final boolean prestigeCommandDeniedEnabled;

    private final String PrestigeCommandApprovedMessage;
    private final boolean prestigeCommandApprovedEnabled;


    private final String PrestigeAddCommandSender;
    private final boolean prestigeAddCommandSenderEnabled;

    private final String PrestigeAddCommandReceiver;
    private final boolean prestigeAddCommandReceiverEnabled;


    private final String PrestigeRemoveCommandSender;
    private final boolean prestigeRemoveCommandSenderEnabled;

    private final String PrestigeRemoveCommandReceiver;
    private final boolean prestigeRemoveCommandReceiverEnabled;


    private final String PrestigeSetCommandSender;
    private final boolean prestigeSetCommandSenderEnabled;

    private final String PrestigeSetCommandReceiver;
    private final boolean prestigeSetCommandReceiverEnabled;
    
    
    private final String RequiredBreak;
    private final boolean requiredBreakEnabled;

    private final String RequiredPlace;
    private final boolean requiredPlaceEnabled;


    private final String RequiredDamage;
    private final boolean requiredDamageEnabled;

    private final String RequiredPvp;
    private final boolean requiredPvpEnabled;



    public LangSettings(){
        FileConfiguration config = FilesStorage.getConfig("langConfig");

        this.playerHelp = config.getStringList("Help.Player");
        this.adminHelp = config.getStringList("Help.Admin");

        this.playerInfo = config.getStringList("Info.Player");

        this.topMessageTopText = config.getStringList(DataLocation.langTopListTopText);
        this.topMessageTopEnabled = config.getBoolean(DataLocation.langTopListTopEnabled);

        this.topMessageMiddleText = config.getStringList(DataLocation.langTopListMiddleText);
        this.topMessageMiddleEnabled = config.getBoolean(DataLocation.langTopListMiddleEnabled);

        this.topMessageBottomText = config.getStringList(DataLocation.langTopListBottomText);
        this.topMessageBottomEnabled = config.getBoolean(DataLocation.langTopListBottomEnabled);

        this.ExperienceBreak = config.getString(DataLocation.langExperienceBreakMessage);
        this.expBreakEnabled = config.getBoolean(DataLocation.langExperienceBreakEnabled);

        this.ExperienceMobs = config.getString(DataLocation.langExperienceMobsMessage);
        this.expMobsEnabled = config.getBoolean(DataLocation.langExperienceMobsEnabled);

        this.ExperiencePvp = config.getString(DataLocation.langExperiencePvpMessage);
        this.expPvpEnabled = config.getBoolean(DataLocation.langExperiencePvpEnabled);

        this.ExperienceCommandSender = config.getString(DataLocation.langExperienceCommandSenderMessage);
        this.expCommandSenderEnabled = config.getBoolean(DataLocation.langExperienceCommandSenderEnabled);

        this.ExperienceCommandReceiver = config.getString(DataLocation.langExperienceCommandReceiverMessage);
        this.expCommandReceiverEnabled = config.getBoolean(DataLocation.langExperienceCommandReceiverEnabled);

        this.RemoveCommandSender = config.getString(DataLocation.langRemoveCommandSenderMessage);
        this.removeCommandSenderEnabled = config.getBoolean(DataLocation.langRemoveCommandSenderEnabled);

        this.RemoveCommandReceiver = config.getString(DataLocation.langRemoveCommandReceiverMessage);
        this.removeCommandReceiverEnabled = config.getBoolean(DataLocation.langRemoveCommandReceiverEnabled);

        this.LevelsAddCommandSender = config.getString(DataLocation.langLevelsAddCommandSenderMessage);
        this.levelsAddCommandSenderEnabled = config.getBoolean(DataLocation.langLevelsAddCommandSenderEnabled);

        this.LevelsAddCommandReceiver = config.getString(DataLocation.langLevelsAddCommandReceiverMessage);
        this.levelsAddCommandReceiverEnabled = config.getBoolean(DataLocation.langLevelsAddCommandReceiverEnabled);

        this.LevelsRemoveCommandSender = config.getString(DataLocation.langLevelsRemoveCommandSenderMessage);
        this.levelsRemoveCommandSenderEnabled = config.getBoolean(DataLocation.langLevelsRemoveCommandSenderEnabled);

        this.LevelsRemoveCommandReceiver = config.getString(DataLocation.langLevelsRemoveCommandReceiverMessage);
        this.levelsRemoveCommandReceiverEnabled = config.getBoolean(DataLocation.langLevelsRemoveCommandReceiverEnabled);

        this.LevelsSetCommandSender = config.getString(DataLocation.langLevelsSetCommandSenderMessage);
        this.levelsSetCommandSenderEnabled = config.getBoolean(DataLocation.langLevelsSetCommandSenderEnabled);

        this.LevelsSetCommandReceiver = config.getString(DataLocation.langLevelsSetCommandReceiverMessage);
        this.levelsSetCommandReceiverEnabled = config.getBoolean(DataLocation.langLevelsSetCommandReceiverEnabled);


        this.PrestigeCommandDeniedMessage = config.getString(DataLocation.langPrestigeCommandDeniedMessage);
        this.prestigeCommandDeniedEnabled = config.getBoolean(DataLocation.langPrestigeCommandDeniedEnabled);

        this.PrestigeCommandApprovedMessage = config.getString(DataLocation.langPrestigeCommandApprovedMessage);
        this.prestigeCommandApprovedEnabled = config.getBoolean(DataLocation.langPrestigeCommandApprovedEnabled);

        this.PrestigeAddCommandSender = config.getString(DataLocation.langPrestigeAddCommandSenderMessage);
        this.prestigeAddCommandSenderEnabled = config.getBoolean(DataLocation.langPrestigeAddCommandSenderEnabled);

        this.PrestigeAddCommandReceiver = config.getString(DataLocation.langPrestigeAddCommandReceiverMessage);
        this.prestigeAddCommandReceiverEnabled = config.getBoolean(DataLocation.langPrestigeAddCommandReceiverEnabled);

        this.PrestigeRemoveCommandSender = config.getString(DataLocation.langPrestigeRemoveCommandSenderMessage);
        this.prestigeRemoveCommandSenderEnabled = config.getBoolean(DataLocation.langPrestigeRemoveCommandSenderEnabled);

        this.PrestigeRemoveCommandReceiver = config.getString(DataLocation.langPrestigeRemoveCommandReceiverMessage);
        this.prestigeRemoveCommandReceiverEnabled = config.getBoolean(DataLocation.langPrestigeRemoveCommandReceiverEnabled);

        this.PrestigeSetCommandSender = config.getString(DataLocation.langPrestigeSetCommandSenderMessage);
        this.prestigeSetCommandSenderEnabled = config.getBoolean(DataLocation.langPrestigeSetCommandSenderEnabled);

        this.PrestigeSetCommandReceiver = config.getString(DataLocation.langPrestigeSetCommandReceiverMessage);
        this.prestigeSetCommandReceiverEnabled = config.getBoolean(DataLocation.langPrestigeSetCommandReceiverEnabled);



        
        this.RequiredBreak = config.getString(DataLocation.langRequiredBreakMessage);
        this.requiredBreakEnabled = config.getBoolean(DataLocation.langRequiredBreakEnabled);

        this.RequiredPlace = config.getString(DataLocation.langRequiredPlaceMessage);
        this.requiredPlaceEnabled = config.getBoolean(DataLocation.langRequiredPlaceEnabled);

        this.RequiredDamage = config.getString(DataLocation.langRequiredDamageMessage);
        this.requiredDamageEnabled = config.getBoolean(DataLocation.langRequiredDamageEnabled);

        this.RequiredPvp = config.getString(DataLocation.langRequiredPvpMessage);
        this.requiredPvpEnabled = config.getBoolean(DataLocation.langRequiredPvpEnabled);





    }

    public String getExperienceBreak(){
        return ExperienceBreak;
    }
    public String getExperienceMobs(){
        return ExperienceMobs;
    }
    public String getExperiencePvp(){
        return ExperiencePvp;
    }



    public List<String> getLpHelp(){
        return playerHelp;
    }
    public List<String> getAdminHelp(){
        return adminHelp;
    }

    public String getRequiredBreak() {
        return RequiredBreak;
    }

    public String getRequiredPlace() {
        return RequiredPlace;
    }

    public String getRequiredDamage() {
        return RequiredDamage;
    }

    public String getRequiredPvp() {
        return RequiredPvp;
    }

    public boolean isExpBreakEnabled() {
        return expBreakEnabled;
    }

    public boolean isExpMobsEnabled() {
        return expMobsEnabled;
    }

    public boolean isExpPvpEnabled() {
        return expPvpEnabled;
    }

    public boolean isRequiredBreakEnabled() {
        return requiredBreakEnabled;
    }

    public boolean isRequiredPlaceEnabled() {
        return requiredPlaceEnabled;
    }

    public boolean isRequiredDamageEnabled() {
        return requiredDamageEnabled;
    }

    public boolean isRequiredPvpEnabled() {
        return requiredPvpEnabled;
    }

    public List<String> getPlayerInfo() {
        return playerInfo;
    }

    public String getExperienceCommandSender() {
        return ExperienceCommandSender;
    }

    public boolean isExpCommandSenderEnabled() {
        return expCommandSenderEnabled;
    }

    public boolean isExpCommandReceiverEnabled() {
        return expCommandReceiverEnabled;
    }

    public String getExperienceCommandReceiver() {
        return ExperienceCommandReceiver;
    }

    public String getRemoveCommandSender() {
        return RemoveCommandSender;
    }

    public boolean isRemoveCommandSenderEnabled() {
        return removeCommandSenderEnabled;
    }

    public String getRemoveCommandReceiver() {
        return RemoveCommandReceiver;
    }

    public boolean isRemoveCommandReceiverEnabled() {
        return removeCommandReceiverEnabled;
    }

    public String getLevelsAddCommandSender() {
        return LevelsAddCommandSender;
    }

    public boolean isLevelsAddCommandSenderEnabled() {
        return levelsAddCommandSenderEnabled;
    }

    public String getLevelsAddCommandReceiver() {
        return LevelsAddCommandReceiver;
    }

    public boolean isLevelsAddCommandReceiverEnabled() {
        return levelsAddCommandReceiverEnabled;
    }

    public String getLevelsRemoveCommandSender() {
        return LevelsRemoveCommandSender;
    }

    public boolean isLevelsRemoveCommandSenderEnabled() {
        return levelsRemoveCommandSenderEnabled;
    }

    public String getLevelsRemoveCommandReceiver() {
        return LevelsRemoveCommandReceiver;
    }

    public boolean isLevelsRemoveCommandReceiverEnabled() {
        return levelsRemoveCommandReceiverEnabled;
    }

    public String getLevelsSetCommandSender() {
        return LevelsSetCommandSender;
    }

    public boolean isLevelsSetCommandSenderEnabled() {
        return levelsSetCommandSenderEnabled;
    }

    public String getLevelsSetCommandReceiver() {
        return LevelsSetCommandReceiver;
    }

    public boolean isLevelsSetCommandReceiverEnabled() {
        return levelsSetCommandReceiverEnabled;
    }

    public String getPrestigeAddCommandSender() {
        return PrestigeAddCommandSender;
    }

    public boolean isPrestigeAddCommandSenderEnabled() {
        return prestigeAddCommandSenderEnabled;
    }

    public String getPrestigeAddCommandReceiver() {
        return PrestigeAddCommandReceiver;
    }

    public boolean isPrestigeAddCommandReceiverEnabled() {
        return prestigeAddCommandReceiverEnabled;
    }

    public String getPrestigeRemoveCommandSender() {
        return PrestigeRemoveCommandSender;
    }

    public boolean isPrestigeRemoveCommandSenderEnabled() {
        return prestigeRemoveCommandSenderEnabled;
    }

    public String getPrestigeRemoveCommandReceiver() {
        return PrestigeRemoveCommandReceiver;
    }

    public boolean isPrestigeRemoveCommandReceiverEnabled() {
        return prestigeRemoveCommandReceiverEnabled;
    }

    public String getPrestigeSetCommandSender() {
        return PrestigeSetCommandSender;
    }

    public boolean isPrestigeSetCommandSenderEnabled() {
        return prestigeSetCommandSenderEnabled;
    }

    public String getPrestigeSetCommandReceiver() {
        return PrestigeSetCommandReceiver;
    }

    public boolean isPrestigeSetCommandReceiverEnabled() {
        return prestigeSetCommandReceiverEnabled;
    }

    public String getPrestigeCommandDeniedMessage() {
        return PrestigeCommandDeniedMessage;
    }

    public boolean isPrestigeCommandDeniedEnabled() {
        return prestigeCommandDeniedEnabled;
    }

    public String getPrestigeCommandApprovedMessage() {
        return PrestigeCommandApprovedMessage;
    }

    public boolean isPrestigeCommandApprovedEnabled() {
        return prestigeCommandApprovedEnabled;
    }

    public List<String> getTopMessageTopText() {
        return topMessageTopText;
    }

    public boolean isTopMessageTopEnabled() {
        return topMessageTopEnabled;
    }

    public List<String> getTopMessageMiddleText() {
        return topMessageMiddleText;
    }

    public boolean isTopMessageMiddleEnabled() {
        return topMessageMiddleEnabled;
    }

    public List<String> getTopMessageBottomText() {
        return topMessageBottomText;
    }

    public boolean isTopMessageBottomEnabled() {
        return topMessageBottomEnabled;
    }
}
