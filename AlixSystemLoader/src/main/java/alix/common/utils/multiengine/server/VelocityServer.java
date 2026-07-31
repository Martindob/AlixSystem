package alix.common.utils.multiengine.server;

import com.velocitypowered.api.command.CommandSource;
import net.kyori.adventure.text.Component;

public final class VelocityServer implements AbstractServer<CommandSource> {

    @Override
    public void sendMessage(CommandSource receiver, String message) {
        receiver.sendMessage(Component.text(message));
    }
}