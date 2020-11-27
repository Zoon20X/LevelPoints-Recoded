package levelpoints.Cache;

import java.util.HashMap;

public class SettingsCache {
    static HashMap<String, Boolean> cachedBooleans = new HashMap<>();

    public static void cacheBoolean(String x, Boolean value){
        cachedBooleans.put(x, value);
    }
    public static Boolean isInCache(String x){
        return cachedBooleans.containsKey(x);
    }
    public static HashMap<String, Boolean> getCachedBooleans(){
        return cachedBooleans;
    }
    public static Boolean isBooleansEmpty(){
        return cachedBooleans.isEmpty();
    }
    public static Boolean getBoolean(String x){
        return cachedBooleans.get(x);
    }
    public static void clearBooleanCache(){
        cachedBooleans.clear();
    }


}
