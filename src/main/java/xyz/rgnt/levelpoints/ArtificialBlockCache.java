package xyz.rgnt.levelpoints;

import me.zoon20x.levelpoints.containers.Settings.Blocks.BlockData;
import org.apache.http.annotation.ThreadSafe;
import org.bukkit.block.Block;
import org.jetbrains.annotations.NotNull;

import java.lang.ref.WeakReference;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Artificial block-cache contains block keys of all blocks considered artificial(placed by player).
 */
@ThreadSafe
public class ArtificialBlockCache {

    private static final @NotNull Set<Long> blockSet
            = new HashSet<>();

    /**
     * Marks blocks as artificial block.
     * @param block Block.
     */
    public static synchronized void addArtificialBlock(final Block block) {
        blockSet.add(block.getBlockKey());
    }

    /**
     * Unmarks blocks as artificial block.
     * @param block Block.
     */
    public static synchronized void remArtificialBlock(final Block block) {
        blockSet.remove(block.getBlockKey());
    }

    /**
     * @param block Block to check.
     */
    public static synchronized boolean isArtificialBlock(final Block block) {
        return blockSet.contains(block.getBlockKey());
    }
}
