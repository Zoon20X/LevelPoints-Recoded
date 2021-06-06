package me.zoon20x.levelpoints.files;

import me.zoon20x.levelpoints.LevelPoints;
import me.zoon20x.levelpoints.utils.DebugSeverity;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class Files {

    private String name;
    private File file;
    private FileConfiguration fileConfiguration;
    private String location;

    public Files(String name){
        this.name = name;
    }

    public Files setLocation(String dir){
        location = dir;
        return this;
    }
    public File getFile(){
        return file;
    }
    public FileConfiguration getConfig(){
        return fileConfiguration;
    }

    public Files build(){
        if (file == null) {
            file = new File(LevelPoints.getInstance().getDataFolder() + "/" +location, name);
        }

        if (!file.exists()) {
            LevelPoints.getDebug(DebugSeverity.NORMAL, "Creating file " + name);
            LevelPoints.getInstance().saveResource(location + "" + name, false);
        }
        fileConfiguration = YamlConfiguration.loadConfiguration(file);

        return this;
    }




}
