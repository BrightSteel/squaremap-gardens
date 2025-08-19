package xyz.jpenilla.squaremap.paper.data;

import com.gardensmc.gardensfurniture.store.FurnitureStore;
import com.gardensmc.gardensfurniture.store.FurnitureStoreHandler;
import xyz.jpenilla.squaremap.api.BukkitAdapter;
import xyz.jpenilla.squaremap.api.FurnitureProvider;
import xyz.jpenilla.squaremap.api.MapWorld;

import java.util.Objects;

public class PaperFurnitureProvider implements FurnitureProvider {

    public static final PaperFurnitureProvider INSTANCE = new PaperFurnitureProvider();
    @Override
    public FurnitureStore getFurnitureStore(MapWorld mapWorld, int x, int y, int z) {
        var bukkitWorld = BukkitAdapter.bukkitWorld(mapWorld);
        var block = Objects.requireNonNull(bukkitWorld).getBlockAt(x, y, z);
        return FurnitureStoreHandler.INSTANCE.getFurnitureStore(block);
    }
}
