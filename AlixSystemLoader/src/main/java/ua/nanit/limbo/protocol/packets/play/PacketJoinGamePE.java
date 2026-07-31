/*
package ua.nanit.limbo.protocol.packets.play;

import com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import com.github.retrooper.packetevents.protocol.player.GameMode;
import com.github.retrooper.packetevents.util.adventure.AdventureNbtUtil;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerJoinGame;
import lombok.SneakyThrows;
import ua.nanit.limbo.NanoLimbo;
import ua.nanit.limbo.protocol.ByteMessage;
import ua.nanit.limbo.protocol.packets.retrooper.OutRetrooperPacket;
import ua.nanit.limbo.protocol.registry.Version;

import java.util.Arrays;

public class PacketJoinGamePE extends OutRetrooperPacket<WrapperPlayServerJoinGame> {

    public PacketJoinGamePE() {
        super(WrapperPlayServerJoinGame.class);
    }

    public void setEntityId(int entityId) {
        this.wrapper().setEntityId(entityId);
    }

    public void setHardcore(boolean hardcore) {
        this.wrapper().setHardcore(hardcore);
    }

    public void setGameMode(GameMode gameMode) {
        this.wrapper().setGameMode(gameMode);
    }

    public void setPreviousGameMode(GameMode previousGameMode) {
        this.wrapper().setPreviousGameMode(previousGameMode);
    }

    public void setWorldNames(String... worldNames) {
        this.wrapper().setWorldNames(Arrays.asList(worldNames));
    }

    public void setWorldName(String worldName) {
        this.wrapper().setWorldName(worldName);
    }

    public void setHashedSeed(long hashedSeed) {
        this.wrapper().setHashedSeed(hashedSeed);
    }

    public void setMaxPlayers(int maxPlayers) {
        this.wrapper().setMaxPlayers(maxPlayers);
    }

    public void setViewDistance(int viewDistance) {
        this.wrapper().setViewDistance(viewDistance);
    }

    public void setReducedDebugInfo(boolean reducedDebugInfo) {
        this.wrapper().setReducedDebugInfo(reducedDebugInfo);
    }

    public void setEnableRespawnScreen(boolean enableRespawnScreen) {
        this.wrapper().setRespawnScreenEnabled(enableRespawnScreen);
    }

    public void setDebug(boolean debug) {
        this.wrapper().setDebug(debug);
    }

    public void setFlat(boolean flat) {
        this.wrapper().setFlat(flat);
    }

    public void setLimitedCrafting(boolean limitedCrafting) {
        this.wrapper().setLimitedCrafting(limitedCrafting);
    }

    public void setSecureProfile(boolean secureProfile) {
        this.wrapper().setEnforcesSecureChat(secureProfile);
    }

    @SneakyThrows
    @Override
    public void encode(ByteMessage msg, Version version) {
        var limbo = NanoLimbo.LIMBO;
        var registry = limbo.getDimensionRegistry();
        this.wrapper().setDimensionTypeRef(limbo.getConfig().getDimensionType(version).asRef(this.wrapper()));

        */
/*var out = ByteStreams.newDataOutput();//new ByteBufOutputStream(BufUtils.pooledBuffer());
        BinaryTagIO.writer().write(, out);
        NBTCompound nbt = (NBTCompound) DefaultNBTSerializer.INSTANCE.deserializeTag(NBTLimiter.noop(), ByteStreams.newDataInput(out.toByteArray()));

        Log.error("Kyori=" + registry.getCodec(version));
        Log.warning("PE=" + nbt);*//*


        var kyoriCodec = registry.getCodec(version);
        var nbt = AdventureNbtUtil.fromAdventure(kyoriCodec); //ByteMessage.convertKyoriToPE(kyoriCodec);

        this.wrapper().setDimensionCodec((NBTCompound) nbt);
    }
}
*/
