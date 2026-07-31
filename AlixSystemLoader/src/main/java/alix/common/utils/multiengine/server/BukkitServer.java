package alix.common.utils.multiengine.server;

import org.bukkit.command.ConsoleCommandSender;

public final class BukkitServer implements AbstractServer<ConsoleCommandSender> {

    @Override
    public void sendMessage(ConsoleCommandSender receiver, String message) {
        receiver.sendMessage(message);
    }
}