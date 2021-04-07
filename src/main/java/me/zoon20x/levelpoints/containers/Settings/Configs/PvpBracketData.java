package me.zoon20x.levelpoints.containers.Settings.Configs;

import java.util.Arrays;
import java.util.List;

public class PvpBracketData {

    private String id;
    private List<String> levelsIncluded;
    private boolean pvp;

    public PvpBracketData(String id, String[] levelsIncluded, boolean pvp){
        this.id = id;
        this.levelsIncluded = Arrays.asList(levelsIncluded.clone());
        this.pvp = pvp;
    }


    public String getId() {
        return id;
    }



    public boolean isPvpEnabled() {
        return pvp;
    }

    public List<String> getLevelsIncluded() {
        return levelsIncluded;
    }
}
