package me.zoon20x.levelpoints.containers.Breeding;

import me.zoon20x.levelpoints.LevelPoints;
import me.zoon20x.levelpoints.containers.Player.PlayerData;
import me.zoon20x.levelpoints.containers.Settings.Blocks.Requirement;
import org.bukkit.entity.EntityType;

public class BreedingData {

    private EntityType mob;

    private Integer breedRequirement;
    private Double exp;
    private Double maxExp;

    public BreedingData(EntityType mob, Double exp, Double maxExp, Integer breedRequirement){
        this.mob = mob;
        this.exp = exp;
        this.maxExp = maxExp;
        this.breedRequirement = breedRequirement;

    }



    public Double getRequiredEXP(PlayerData data) {
        Double exp = 0.0;
        for (int i = data.getLevel(); i < getBreedRequirement() + 1; i++) {
            double ex = LevelPoints.getInstance().getLevelSettings().getRequireExp(i);
            exp += ex;
        }

        return exp - data.getExp();
    }

    public EntityType getMob() {
        return mob;
    }
    public Double getExp() {
        return exp;
    }

    public void setExp(Double exp) {
        this.exp = exp;
    }

    public Integer getBreedRequirement() {
        return breedRequirement;
    }

    public void setBreedRequirement(Integer breedRequirement) {
        this.breedRequirement = breedRequirement;
    }

    public Double getMaxExp() {
        return maxExp;
    }
}

