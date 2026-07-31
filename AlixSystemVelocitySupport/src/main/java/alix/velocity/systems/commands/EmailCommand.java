/*
package alix.velocity.systems.commands;

import alix.common.data.file.UserFileManager;
import alix.common.data.security.email.EmailHandler;
import alix.velocity.Main;
import alix.velocity.utils.AlixUtils;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.velocitypowered.api.command.BrigadierCommand;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;

import static alix.velocity.systems.commands.CommandManager.command;
import static alix.velocity.systems.commands.CommandManager.isConsole;
import static com.mojang.brigadier.Command.SINGLE_SUCCESS;

final class EmailCommand {

    static void register_VerifyEmail(ProxyServer server) {
        var cmd = command("verifyemail", ctx -> {
            if (isConsole(ctx)) return SINGLE_SUCCESS;

            Player player = (Player) ctx.getSource();
            AlixUtils.sendMessage(player, "&eSpecify the verify code!");
            return SINGLE_SUCCESS;
        }).then(BrigadierCommand.requiredArgumentBuilder("verify-code", StringArgumentType.word())
                .executes(ctx -> {
                    if (isConsole(ctx)) return SINGLE_SUCCESS;
                    Player player = (Player) ctx.getSource();
                    String code = StringArgumentType.getString(ctx, "verify-code");
                    EmailHandler.verifyMail(player, UserFileManager.get(player.getUsername()), code, false, AlixUtils::sendMessage);
                    return SINGLE_SUCCESS;
                })
        ).build();
        var manager = server.getCommandManager();

        manager.register(manager.metaBuilder("verifyemail").plugin(Main.PLUGIN).build(), new BrigadierCommand(cmd));
    }

    static void register_SendVerifyEmail(ProxyServer server) {
        var cmd = command("sendverifyemail", ctx -> {
            if (isConsole(ctx)) return SINGLE_SUCCESS;

            Player player = (Player) ctx.getSource();
            AlixUtils.sendMessage(player, "&eUsage: /sendverifyemail <email>");
            return SINGLE_SUCCESS;
        }).then(BrigadierCommand.requiredArgumentBuilder("email", StringArgumentType.greedyString())
                .executes(ctx -> {
                    if (isConsole(ctx)) return SINGLE_SUCCESS;

                    Player player = (Player) ctx.getSource();
                    String email = StringArgumentType.getString(ctx, "email");

                    EmailHandler.sendVerifyMail(
                            player,
                            email,
                            false,
                            AlixUtils::sendMessage
                    );
                    return SINGLE_SUCCESS;
                })
        ).build();

        var manager = server.getCommandManager();
        manager.register(
                manager.metaBuilder("sendverifyemail").plugin(Main.PLUGIN).build(),
                new BrigadierCommand(cmd)
        );
    }
}*/
