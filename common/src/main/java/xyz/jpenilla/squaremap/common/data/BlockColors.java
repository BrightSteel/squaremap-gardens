package xyz.jpenilla.squaremap.common.data;

import it.unimi.dsi.fastutil.objects.Reference2IntMap;
import it.unimi.dsi.fastutil.objects.Reference2IntMaps;
import it.unimi.dsi.fastutil.objects.Reference2IntOpenHashMap;
import it.unimi.dsi.fastutil.objects.Reference2ObjectMap;
import it.unimi.dsi.fastutil.objects.Reference2ObjectMaps;
import it.unimi.dsi.fastutil.objects.Reference2ObjectOpenHashMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.checkerframework.framework.qual.DefaultQualifier;
import xyz.jpenilla.squaremap.common.util.Colors;

@DefaultQualifier(NonNull.class)
public final class BlockColors {
    private final Reference2IntMap<Block> staticColorMap;
    private final HashMap<String, Integer> furnitureColorMap;
    private final HashMap<String, Integer> noteBlockColorMap;
    private final Reference2ObjectMap<Block, DynamicColorGetter> dynamicColorMap;

    private BlockColors(final MapWorldInternal world) {
        final Reference2IntMap<Block> staticColors = new Reference2IntOpenHashMap<>(world.advanced().COLOR_OVERRIDES_BLOCKS);
        staticColors.defaultReturnValue(-1);
        this.staticColorMap = Reference2IntMaps.unmodifiable(staticColors);
        this.furnitureColorMap = world.advanced().COLOR_OVERRIDES_FURNITURE;
        this.noteBlockColorMap = world.advanced().COLOR_OVERRIDES_NOTE_BLOCK;

        this.dynamicColorMap = this.loadDynamicColors();
    }

    private Reference2ObjectMap<Block, DynamicColorGetter> loadDynamicColors() {
        final Map<Block, DynamicColorGetter> map = new HashMap<>();

        map.put(Blocks.MELON_STEM, BlockColors::melonAndPumpkinStem);
        map.put(Blocks.PUMPKIN_STEM, BlockColors::melonAndPumpkinStem);
        map.put(Blocks.WHEAT, BlockColors::wheat);

        return Reference2ObjectMaps.unmodifiable(new Reference2ObjectOpenHashMap<>(map));
    }

    /**
     * Get a special color for a {@link BlockState}, if it exists. Will return -1 if there
     * is no special color for the provided {@link BlockState}.
     *
     * @param state {@link BlockState} to test
     * @return special color, or -1
     */
    public int color(final BlockState state) {
        final Block block = state.getBlock();

        final int staticColor = this.staticColorMap.getInt(block);
        if (staticColor != -1) {
            return staticColor;
        }

        Optional<NoteBlockInstrument> instrument = state.getOptionalValue(NoteBlock.INSTRUMENT);
        Optional<Integer> note = state.getOptionalValue(NoteBlock.NOTE);
        Optional<Boolean> powered = state.getOptionalValue(NoteBlock.POWERED);
        if (instrument.isPresent() && note.isPresent() && powered.isPresent()) {
            String key = String.format("[%s,%s,%s]", note.get(), instrument.get().getSerializedName(), powered.get());
            var noteBlockColor = this.noteBlockColorMap.get(key);
            if (noteBlockColor != null) {
                return noteBlockColor;
            }
        }

        final @Nullable DynamicColorGetter func = this.dynamicColorMap.get(block);
        if (func != null) {
            return func.color(state);
        }

        return -1;
    }

    public int furnitureColor(String furnitureIdentifier) {
        var color = this.furnitureColorMap.get(furnitureIdentifier);
        if (color == null) {
            return -1;
        }
        return color;
    }

    private static int melonAndPumpkinStem(final BlockState state) {
        int age = state.getValue(StemBlock.AGE);
        int k = age * 32;
        int l = 255 - age * 8;
        int m = age * 4;
        return k << 16 | l << 8 | m;
    }

    private static int wheat(final BlockState state) {
        float factor = (state.getValue(CropBlock.AGE) + 1) / 8F;
        return Colors.mix(Colors.plantMapColor(), 0xDCBB65, factor);
    }

    public static BlockColors create(final MapWorldInternal mapWorld) {
        return new BlockColors(mapWorld);
    }

    @FunctionalInterface
    private interface DynamicColorGetter {
        int color(BlockState state);
    }
}
