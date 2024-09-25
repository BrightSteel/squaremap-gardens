package xyz.jpenilla.squaremap.api;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * Represents a world mapped by squaremap.
 */
public interface MapWorld {

    /**
     * Gets the layer registry for this world.
     *
     * @return the layer registry
     */
    @NonNull Registry<LayerProvider> layerRegistry();

    /**
     * Get the identifier of this world.
     *
     * @return identifier
     */
    @NonNull WorldIdentifier identifier();

    int getFurnitureColor(int x, int y, int z);
}
