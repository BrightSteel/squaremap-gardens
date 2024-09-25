package xyz.jpenilla.squaremap.paper.data;

import de.tr7zw.nbtapi.NBTChunk;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.block.Block;
import xyz.jpenilla.squaremap.api.BukkitAdapter;
import xyz.jpenilla.squaremap.api.FurnitureProvider;
import xyz.jpenilla.squaremap.api.FurnitureStore;
import xyz.jpenilla.squaremap.api.MapWorld;

import java.util.Objects;

public class PaperFurnitureProvider implements FurnitureProvider {

    public static final PaperFurnitureProvider INSTANCE = new PaperFurnitureProvider();

    private static final String IDENTIFIER_KEY = "customItem";
    private static final String BLOCK_DATA_KEY = "furnitureStore";

    public FurnitureStore getFurnitureStore(Block block) {
        return getFurnitureStore(block.getChunk(), block.getX(), block.getY(), block.getZ());
    }

    public FurnitureStore getFurnitureStore(Chunk chunk, int x, int y, int z) {
        var nbtChunk = new NBTChunk(chunk).getPersistentDataContainer();
        var chunkBlockCompound = nbtChunk.getOrCreateCompound(getBlockKey(x, y, z));

        var customItem = chunkBlockCompound.getString(IDENTIFIER_KEY);
        var blockData = chunkBlockCompound.getCompound(BLOCK_DATA_KEY);
        // no blockData
        if (blockData == null) {
            return new FurnitureStore(customItem);
        }

        int multiplier = blockData.hasTag("multiplier") ? blockData.getInteger("multiplier") : 0;
        return new FurnitureStore(
            customItem,
            multiplier,
            blockData.getString("facing")
        );
    }

    private String getBlockKey(Block block) {
        return getBlockKey(block.getX(), block.getY(), block.getZ());
    }

    private String getBlockKey(int x, int y, int z) {
        return String.format("%s.%s.%s", x, y, z);
    }

    @Override
    public FurnitureStore getFurnitureStore(MapWorld mapWorld, int x, int y, int z) {
        var bukkitWorld = BukkitAdapter.bukkitWorld(mapWorld);
        var block = Objects.requireNonNull(bukkitWorld).getBlockAt(x, y, z);
        return getFurnitureStore(block);
    }
}
