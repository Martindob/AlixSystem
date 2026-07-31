package ua.nanit.limbo.protocol.packets.status;


import ua.nanit.limbo.protocol.ByteMessage;
import ua.nanit.limbo.protocol.PacketOut;
import ua.nanit.limbo.protocol.registry.Version;

public final class PacketStatusResponse implements PacketOut {

    private String jsonResponse;

    public PacketStatusResponse() {
    }

    public PacketStatusResponse(String jsonResponse) {
        this.jsonResponse = jsonResponse;
    }

    @Override
    public void encode(ByteMessage msg, Version version) {
        msg.writeString(jsonResponse);
    }
}
