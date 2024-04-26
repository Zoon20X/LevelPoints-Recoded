package me.zoon20x.levelpoints.spigot.guis;

import dev.dejvokep.boostedyaml.YamlDocument;
import me.zoon20x.levelpoints.spigot.LevelPoints;
import me.zoon20x.levelpoints.spigot.utils.Items.ItemBuilder;
import org.bukkit.Material;

import java.util.HashMap;

public class GuiSettings {

    private HashMap<String, GUIData> guiDataMap = new HashMap<>();

    public GuiSettings(){
        loadGUI(LevelPoints.getInstance().getConfigUtils().getLpsSettingsGUI());

    }
    private void loadGUI(YamlDocument config){
        String name = config.getFile().getName().replace(".yml", "");
        String title = config.getString("Title");
        int size = config.getInt("Rows") * 9;
        guiDataMap.put(name, new GUIData(title, size,
                new ItemBuilder(
                        Material.getMaterial(config.getString("BlankSpace.Material"))).
                        setName(config.getString("BlankSpace.Name")).build())
        );

    }

    public GUIData getGUIData(String value){
        return guiDataMap.get(value);
    }


}
