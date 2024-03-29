package me.zoon20x.levelpoints.containers.Settings.Configs;

import me.zoon20x.levelpoints.LevelPoints;
import me.zoon20x.levelpoints.utils.DataLocation;
import me.zoon20x.levelpoints.utils.DebugSeverity;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.Collection;
import java.util.HashMap;

public class PvpSettings {

    private boolean pvpBracketsEnabled;

    private HashMap<String, PvpBracketData> data = new HashMap<>();
    private boolean differentMessageEnabled;
    private String differentMessageText;
    private boolean noPvpMessageEnabled;
    private String noPvpMessageText;


    public PvpSettings(){
        FileConfiguration configuration = LevelPoints.getInstance().getFilesGenerator().pvpSettings.getConfig();
        pvpBracketsEnabled = configuration.getBoolean(DataLocation.pvpBracketsEnabled);
        differentMessageEnabled = configuration.getBoolean(DataLocation.bracketsMessagesDifferentEnabled);
        differentMessageText = configuration.getString(DataLocation.bracketsMessagesDifferentText);
        noPvpMessageEnabled = configuration.getBoolean(DataLocation.bracketsMessagesNoPvpEnabled);
        noPvpMessageText = configuration.getString(DataLocation.bracketsMessagesNoPvpText);

        if(pvpBracketsEnabled){
            generatePvpBrackets();
        }
    }

    private void generatePvpBrackets(){
        FileConfiguration configuration = LevelPoints.getInstance().getFilesGenerator().pvpSettings.getConfig();
        configuration.getConfigurationSection("Brackets").getKeys(false).forEach(x ->{
           if(!x.equalsIgnoreCase("Enabled")){
               PvpBracketData pvpData = new PvpBracketData(x,
                       configuration.getString(DataLocation.getPvpLevelsIncluded(x)).split(","),
                       configuration.getBoolean(DataLocation.getPvpToggle(x)));
               data.put(x, pvpData);
           }
        });
    }

    public boolean isPvpBracketsEnabled(){
        return pvpBracketsEnabled;
    }

    public Collection<PvpBracketData> getAllPvpBrackets(){
        return data.values();
    }


    public PvpBracketData getPvpBracketData(String x){
        return data.get(x);
    }

    public boolean isDifferentMessageEnabled() {
        return differentMessageEnabled;
    }

    public String getDifferentMessageText() {
        return differentMessageText;
    }

    public boolean isNoPvpMessageEnabled() {
        return noPvpMessageEnabled;
    }

    public String getNoPvpMessageText() {
        return noPvpMessageText;
    }
}
