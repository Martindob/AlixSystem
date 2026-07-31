package ua.nanit.limbo.protocol.packets.login;

import com.github.retrooper.packetevents.protocol.player.UserProfile;
import com.github.retrooper.packetevents.wrapper.login.server.WrapperLoginServerLoginSuccess;
import ua.nanit.limbo.protocol.packets.retrooper.OutRetrooperPacket;

import java.util.UUID;

public final class PacketLoginSuccess extends OutRetrooperPacket<WrapperLoginServerLoginSuccess> {

    private final UserProfile profile = new UserProfile(null, null);

    public PacketLoginSuccess() {
        super(WrapperLoginServerLoginSuccess.class);
        this.wrapper().setUserProfile(profile);
    }

    public PacketLoginSuccess setSessionId(UUID uuid) {
        this.wrapper().setSessionId(uuid);
        return this;
    }

    public PacketLoginSuccess setUUID(UUID uuid) {
         this.profile.setUUID(uuid);
         return this;
    }

    public PacketLoginSuccess setUsername(String username) {
        this.profile.setName(username);
        return this;
    }
}