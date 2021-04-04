package me.zoon20x.levelpoints.utils;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.domains.Association;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.association.Associables;
import com.sk89q.worldguard.protection.association.RegionAssociable;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.managers.RegionManager;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.logging.Level;

public class WorldGuardAPI {
    private final Plugin owningPlugin;
    private Object worldGuard = null;
    private WorldGuardPlugin worldGuardPlugin = null;

    private Object regionContainer = null;
    private Method regionContainerGetMethod = null;
    private Method createQueryMethod = null;
    private Method regionQueryTestStateMethod = null;
    private Method locationAdaptMethod = null;
    private Method worldAdaptMethod = null;
    private Method regionManagerGetMethod = null;
    private Constructor<?> vectorConstructor = null;
    private Method vectorConstructorAsAMethodBecauseWhyNot = null;
    private boolean initialized = false;

    public boolean isEnabled() {
        return worldGuardPlugin != null;
    }

    public WorldGuardAPI(Plugin plugin, Plugin owningPlugin) {
        this.owningPlugin = owningPlugin;
        if (plugin instanceof WorldGuardPlugin) {
            worldGuardPlugin = (WorldGuardPlugin)plugin;

            try {
                Class<?> worldGuardClass = Class.forName("com.sk89q.worldguard.WorldGuard");
                Method getInstanceMethod = worldGuardClass.getMethod("getInstance");
                worldGuard = getInstanceMethod.invoke(null);

            } catch (Exception ex) {

            }

            try {
            } catch (NoSuchMethodError incompatible) {
                owningPlugin.getLogger().log(Level.WARNING, "NOFLAGS", incompatible);
                // Ignored, will follow up in checkFlagSupport
            } catch (Throwable ex) {
                owningPlugin.getLogger().log(Level.WARNING, "Unexpected error setting up custom flags, please make sure you are on WorldGuard 6.2 or above", ex);
            }
        }
    }

    private void initialize() {
        if (!initialized) {
            initialized = true;
            // Super hacky reflection to deal with differences in WorldGuard 6 and 7+
            if (worldGuard != null) {
                try {
                    Method getPlatFormMethod = worldGuard.getClass().getMethod("getPlatform");
                    Object platform = getPlatFormMethod.invoke(worldGuard);
                    Method getRegionContainerMethod = platform.getClass().getMethod("getRegionContainer");
                    regionContainer = getRegionContainerMethod.invoke(platform);
                    createQueryMethod = regionContainer.getClass().getMethod("createQuery");
                    Class<?> worldEditLocationClass = Class.forName("com.sk89q.worldedit.util.Location");
                    Class<?> worldEditWorldClass = Class.forName("com.sk89q.worldedit.world.World");
                    Class<?> worldEditAdapterClass = Class.forName("com.sk89q.worldedit.bukkit.BukkitAdapter");
                    worldAdaptMethod = worldEditAdapterClass.getMethod("adapt", World.class);
                    locationAdaptMethod = worldEditAdapterClass.getMethod("adapt", Location.class);
                    regionContainerGetMethod = regionContainer.getClass().getMethod("get", worldEditWorldClass);
                    Class<?> regionQueryClass = Class.forName("com.sk89q.worldguard.protection.regions.RegionQuery");
                    regionQueryTestStateMethod = regionQueryClass.getMethod("testState", worldEditLocationClass, RegionAssociable.class, StateFlag[].class);

                    Class<?> flagsClass = Class.forName("com.sk89q.worldguard.protection.flags.Flags");
                } catch (Exception ex) {
                    owningPlugin.getLogger().log(Level.WARNING, "Failed to bind to WorldGuard, integration will not work!", ex);
                    regionContainer = null;
                    return;
                }
            } else {
                regionContainer = worldGuardPlugin.getRegionContainer();
                try {
                    createQueryMethod = regionContainer.getClass().getMethod("createQuery");
                    regionContainerGetMethod = regionContainer.getClass().getMethod("get", World.class);
                    Class<?> regionQueryClass = Class.forName("com.sk89q.worldguard.bukkit.RegionQuery");
                    regionQueryTestStateMethod = regionQueryClass.getMethod("testState", Location.class, RegionAssociable.class, StateFlag[].class);

                    Class<?> flagsClass = Class.forName("com.sk89q.worldguard.protection.flags.DefaultFlag");
                } catch (Exception ex) {
                    owningPlugin.getLogger().log(Level.WARNING, "Failed to bind to WorldGuard, integration will not work!", ex);
                    regionContainer = null;
                    return;
                }
            }

            try {
                Class<?> vectorClass = Class.forName("com.sk89q.worldedit.Vector");
                vectorConstructor = vectorClass.getConstructor(Double.TYPE, Double.TYPE, Double.TYPE);
                regionManagerGetMethod = RegionManager.class.getMethod("getApplicableRegions", vectorClass);
            } catch (Exception ex) {
                try {
                    Class<?> vectorClass = Class.forName("com.sk89q.worldedit.math.BlockVector3");
                    vectorConstructorAsAMethodBecauseWhyNot = vectorClass.getMethod("at", Double.TYPE, Double.TYPE, Double.TYPE);
                    regionManagerGetMethod = RegionManager.class.getMethod("getApplicableRegions", vectorClass);
                } catch (Exception sodonewiththis) {
                    owningPlugin.getLogger().log(Level.WARNING, "Failed to bind to WorldGuard (no Vector class?), integration will not work!", ex);
                    regionContainer = null;
                    return;
                }
            }

            if (regionContainer == null) {
                owningPlugin.getLogger().warning("Failed to find RegionContainer, WorldGuard integration will not function!");
            }
        }
    }


    public RegionManager getRegionManager(World world) {
        initialize();
        if (regionContainer == null || regionContainerGetMethod == null) return null;
        RegionManager regionManager = null;
        try {
            if (worldAdaptMethod != null) {
                Object worldEditWorld = worldAdaptMethod.invoke(null, world);
                regionManager = (RegionManager)regionContainerGetMethod.invoke(regionContainer, worldEditWorld);
            } else {
                regionManager = (RegionManager)regionContainerGetMethod.invoke(regionContainer, world);
            }
        } catch (Exception ex) {
            owningPlugin.getLogger().log(Level.WARNING, "An error occurred looking up a WorldGuard RegionManager", ex);
        }
        return regionManager;
    }


    public ApplicableRegionSet getRegionSet(Location location) {
        RegionManager regionManager = getRegionManager(location.getWorld());
        if (regionManager == null) return null;
        try {
            Object vector = vectorConstructorAsAMethodBecauseWhyNot == null
                    ? vectorConstructor.newInstance(location.getX(), location.getY(), location.getZ())
                    : vectorConstructorAsAMethodBecauseWhyNot.invoke(null, location.getX(), location.getY(), location.getZ());
            return (ApplicableRegionSet)regionManagerGetMethod.invoke(regionManager, vector);
        } catch (Exception ex) {
            owningPlugin.getLogger().log(Level.WARNING, "An error occurred looking up a WorldGuard ApplicableRegionSet", ex);
        }
        return null;
    }
}