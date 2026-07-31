package ua.nanit.limbo.connection.pipeline;

import ua.nanit.limbo.protocol.ByteMessage;
import ua.nanit.limbo.protocol.Packet;
import ua.nanit.limbo.protocol.registry.State;
import ua.nanit.limbo.protocol.registry.Version;
import ua.nanit.limbo.server.Log;

public final class PacketEncoder {

    static ByteMessage encode(Packet packet, State.PacketRegistry registry, Version version, boolean pooled) {
        if (registry == null) return null;

        ByteMessage msg = pooled ? ByteMessage.pooled() : ByteMessage.unpooled();
        int packetId = registry.getPacketId(packet.getClass());

        /*if (packet instanceof PacketSnapshot) {
            packetId = registry.getPacketId(((PacketSnapshot) packet).getWrappedPacket().getClass());
        } else {
            packetId = registry.getPacketId(packet.getClass());
        }*/

        if (packetId < 0) {
            Log.warning("Undefined packet class: %s[0x%s] (%d bytes)", packet.getClass().getName(), Integer.toHexString(packetId), msg.readableBytes());
            return null;
        }

        msg.writeVarInt(packetId);

        try {
            packet.encode(msg, version);

            /*if (Log.isDebug()) {
                Log.debug("Sending %s[0x%s] packet (%d bytes)", packet.toString(), Integer.toHexString(packetId), msg.readableBytes());
            }*/
            return msg;
        } catch (Exception e) {
            Log.error("Cannot encode packet 0x%s (%s): %s", Integer.toHexString(packetId), packet.getClass().getSimpleName(), e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
}
