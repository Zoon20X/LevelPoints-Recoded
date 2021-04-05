package me.zoon20x.levelpoints.containers.Settings.Blocks;

import me.zoon20x.levelpoints.LevelPoints;
import me.zoon20x.levelpoints.containers.Player.PlayerData;
import org.bukkit.Material;

public class BlockData {

    private Material material;
    private Byte data;
    private Integer breakRequired;
    private Integer placeRequired;
    private Double exp;
    private Double maxExp;

    public BlockData(Material material, Byte data, Double exp, Double maxExp, Integer breakRequired, Integer placeRequired){
        this.material = material;
        this.data = data;

        this.exp = exp;
        this.maxExp = maxExp;

        this.breakRequired = breakRequired;
        this.placeRequired = placeRequired;

    }



    public Double getRequiredEXP(BlockRequired required, PlayerData data){
        Double exp = 0.0;
        switch (required){
            case BREAK:
                for(int i = data.getLevel(); i<getBreakRequired() + 1; i++){
                    double ex = LevelPoints.getLevelSettings().getRequireExp(i);
                    exp += ex;
                }
                break;
            case PLACE:
                for(int i = data.getLevel(); i<getPlaceRequired() + 1; i++){
                    double ex = LevelPoints.getLevelSettings().getRequireExp(i);
                    exp += ex;
                }
                break;
        }
        return exp - data.getExp();
    }

    public Material getMaterial() {
        return material;
    }

    public Byte getData() {
        return data;
    }


    public Double getExp() {
        return exp;
    }

    public void setExp(Double exp) {
        this.exp = exp;
    }

    public Integer getBreakRequired() {
        return breakRequired;
    }

    public void setBreakRequired(Integer breakRequired) {
        this.breakRequired = breakRequired;
    }

    public Integer getPlaceRequired() {
        return placeRequired;
    }

    public void setPlaceRequired(Integer placeRequired) {
        this.placeRequired = placeRequired;
    }

    public Double getMaxExp() {
        return maxExp;
    }
}
