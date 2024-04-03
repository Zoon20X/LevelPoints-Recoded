package me.zoon20x.levelpoints;

import me.zoon20x.levelpoints.utils.files.ConfigUtils;
import org.apache.commons.jexl3.*;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;

public final class LevelPoints extends JavaPlugin {
    private static LevelPoints instance;

    private static ConfigUtils configUtils;


    private HashMap<Integer, Double> levels = new HashMap<>();

    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;
        configUtils = new ConfigUtils();


//        long startTime = System.nanoTime();
//        String expressionString = "50*level+15D";
//        JexlExpression expression = new JexlBuilder().safe(true).create().createExpression(expressionString);
//        JexlContext context = new MapContext();
//        for(int i=0;i<100;i++) {
//            context.set("level", i);
//            double result = (double) expression.evaluate(context);
//            levels.put(i, result);
//        }
//        long endTime = System.nanoTime();
//        long duration = ((endTime - startTime) / 1000000);
//        System.out.println(duration + " ms");
//        System.out.println(levels.size());

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }


    public static LevelPoints getInstance(){
        return instance;
    }


}
