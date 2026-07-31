package ua.nanit.limbo.world;

import net.kyori.adventure.nbt.CompoundBinaryTag;
import ua.nanit.limbo.server.data.NamespacedKey;

public record Dimension(NamespacedKey key,
                        int id,
                        int height,
                        CompoundBinaryTag codec,
                        CompoundBinaryTag defaultCodec) {

}