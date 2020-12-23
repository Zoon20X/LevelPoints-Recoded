package levelpoints.Utils;

import levelpoints.Containers.PlayerContainer;
import levelpoints.levelpoints.Formatting;
import levelpoints.levelpoints.LevelPoints;
import me.clip.placeholderapi.PlaceholderAPI;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;

public class MessagesUtil {

    static HashMap<Player, BossBar> bossBars = new HashMap<>();


    public static void sendBossBar(Player player, String msg, BarColor color, BarStyle style, double val) {
        if (LevelPoints.getInstance().getConfig().getBoolean("BossBar")) {
            if (!bossBars.containsKey(player)) {
                BossBar bar = Bukkit.createBossBar(msg, color, style);
                bossBars.put(player, bar);
                if(LevelPoints.getInstance().getConfig().getBoolean("ShowOnEXPOnly")) {
                    bossBarRemove(player);
                }
            }
            getBossBar(player).addPlayer(player);

            BossBar bar = bossBars.get(player);

            bar.setTitle(Formatting.formatInfoTags(player, msg.replace("{level}", String.valueOf(AsyncEvents.getPlayerContainer(player).getLevel()))));

            System.out.println(val);
            bar.setProgress(val);
        }
    }

    public static BossBar getBossBar(Player player){
        return bossBars.get(player);
    }

    private static void bossBarRemove(Player player) {
        new BukkitRunnable(){

            @Override
            public void run() {
                getBossBar(player).removePlayer(player);
                bossBars.remove(player);
            }
        }.runTaskLaterAsynchronously(LevelPoints.getInstance(), LevelPoints.getInstance().getConfig().getInt("ShowTime") * 20);
    }

    public static void sendActionBar(Player player, String msgs){
        final String[] msg = {msgs};

        new BukkitRunnable() {
            @Override
            public void run() {
                if (LevelPoints.getInstance().getConfig().getBoolean("Actionbar.PlaceholderAPI")) {
                    msg[0] = PlaceholderAPI.setPlaceholders(player, msg[0]);
                }
                if (!Bukkit.getVersion().contains("1.8")) {
                    player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(Formatting.formatInfoTags(player, msg[0])));
                } else {
                    String version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];

                    try {
                        Class<?> c1 = Class.forName("org.bukkit.craftbukkit." + version + ".entity.CraftPlayer");
                        Object p = c1.cast(player);
                        Object ppoc;
                        Class<?> c4 = Class.forName("net.minecraft.server." + version + ".PacketPlayOutChat");
                        Class<?> c5 = Class.forName("net.minecraft.server." + version + ".Packet");

                        Class<?> c2 = Class.forName("net.minecraft.server." + version + ".ChatComponentText");
                        Class<?> c3 = Class.forName("net.minecraft.server." + version + ".IChatBaseComponent");


                        Object o = c2.getConstructor(new Class<?>[]{String.class}).newInstance(Formatting.formatInfoTags(player, msg[0]));
                        ppoc = c4.getConstructor(new Class<?>[]{c3, byte.class}).newInstance(o, (byte) 2);
                        Method getHandle = c1.getDeclaredMethod("getHandle");
                        Object handle = getHandle.invoke(p);

                        Field fieldConnection = handle.getClass().getDeclaredField("playerConnection");
                        Object playerConnection = fieldConnection.get(handle);

                        Method sendPacket = playerConnection.getClass().getDeclaredMethod("sendPacket", c5);
                        sendPacket.invoke(playerConnection, ppoc);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }.runTaskAsynchronously(LevelPoints.getInstance());


    }

    public static void sendTitle(Player player, String Title, String Subtitle) {
        new BukkitRunnable() {
            @Override
            public void run() {
                    if (!Bukkit.getVersion().contains("1.8")) {
                        player.sendTitle(Title, Subtitle, 10, 20, 10);
                    }
                }
        }.runTaskAsynchronously(LevelPoints.getInstance());

    }
}
