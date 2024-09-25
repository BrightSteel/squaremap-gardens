package xyz.jpenilla.squaremap.api;


public interface FurnitureProvider {

    FurnitureStore getFurnitureStore(MapWorld world, int x, int y, int z);

}
