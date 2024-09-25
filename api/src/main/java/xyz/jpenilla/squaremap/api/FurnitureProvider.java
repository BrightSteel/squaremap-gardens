package xyz.jpenilla.squaremap.api;


import com.gardensmc.gardensfurniture.store.FurnitureStore;

public interface FurnitureProvider {

    FurnitureStore getFurnitureStore(MapWorld world, int x, int y, int z);

}
