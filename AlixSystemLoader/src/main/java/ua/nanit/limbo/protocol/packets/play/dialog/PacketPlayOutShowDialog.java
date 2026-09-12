package ua.nanit.limbo.protocol.packets.play.dialog;

import alix.common.data.security.password.Password;
import alix.common.messages.Messages;
import com.github.retrooper.packetevents.protocol.dialog.CommonDialogData;
import com.github.retrooper.packetevents.protocol.dialog.Dialog;
import com.github.retrooper.packetevents.protocol.dialog.DialogAction;
import com.github.retrooper.packetevents.protocol.dialog.MultiActionDialog;
import com.github.retrooper.packetevents.protocol.dialog.action.DialogTemplate;
import com.github.retrooper.packetevents.protocol.dialog.action.DynamicRunCommandAction;
import com.github.retrooper.packetevents.protocol.dialog.body.PlainMessage;
import com.github.retrooper.packetevents.protocol.dialog.body.PlainMessageDialogBody;
import com.github.retrooper.packetevents.protocol.dialog.button.ActionButton;
import com.github.retrooper.packetevents.protocol.dialog.button.CommonButtonData;
import com.github.retrooper.packetevents.protocol.dialog.input.Input;
import com.github.retrooper.packetevents.protocol.dialog.input.TextInputControl;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerShowDialog;
import net.kyori.adventure.text.Component;
import ua.nanit.limbo.connection.ClientConnection;
import ua.nanit.limbo.protocol.packets.retrooper.OutRetrooperPacket;
import ua.nanit.limbo.protocol.registry.Version;

import java.util.List;

/**
 * EXPERIMENTAL - see NanoLimbo#enableDialogLogin (a hardcoded, source-only switch: there is deliberately
 * no config.yml option for this). Uses the "Dialog"/"Menus &amp; Dialogs" protocol feature (1.21.6+) to
 * show a native password-entry prompt instead of the anvil-rename-GUI trick older versions rely on -
 * intended as a drop-in alternative to the anvil GUI on clients modern enough to support it, falling back
 * to the anvil/command flow otherwise (see the caller, LoginState#writeCommands).
 * <p>
 * The submitted password never needs a new incoming packet type: the dialog's one button uses a
 * {@link DynamicRunCommandAction}, which makes the client itself send a normal command-execution packet
 * (as if the player had typed it), with the password Input's value substituted into the template - so it
 * reaches {@link ua.nanit.limbo.connection.login.LoginState#handleCommand} exactly the same way a typed
 * "/register &lt;password&gt;"/"/login &lt;password&gt;" would, reusing every existing validation/rate-
 * limiting/terms-gating path those already go through. Not yet verified against a real client - the
 * dialog/input/button API shape here is very new and has changed across packetevents versions.
 */
public final class PacketPlayOutShowDialog extends OutRetrooperPacket<WrapperPlayServerShowDialog> {

    public PacketPlayOutShowDialog() {
        super(WrapperPlayServerShowDialog.class);
    }

    public PacketPlayOutShowDialog(WrapperPlayServerShowDialog wrapper) {
        super(wrapper);
    }

    /**
     * @param connection   used only to check the client's version - too old (pre-1.21.6) clients don't
     *                     understand dialogs at all, so the caller should fall back to the anvil/command flow
     * @param isRegistered whether to show the login dialog (password check against an existing account)
     *                     or the register dialog (setting a new password)
     * @return true if the dialog was sent (the caller should stop here), false if the client is too old
     */
    public static boolean write(ClientConnection connection, boolean isRegistered) {
        if (connection.getClientVersion().less(Version.V1_21_6))
            return false;

        connection.writePacket(isRegistered ? forLogin() : forRegister());
        return true;
    }

    public static PacketPlayOutShowDialog forLogin() {
        return new PacketPlayOutShowDialog(new WrapperPlayServerShowDialog(dialog(Messages.get("dialog-title-login"), "login")));
    }

    public static PacketPlayOutShowDialog forRegister() {
        return new PacketPlayOutShowDialog(new WrapperPlayServerShowDialog(dialog(Messages.get("dialog-title-register"), "register")));
    }

    //'commandName' becomes the command the client executes on submit ("register"/"login") - handleCommand()
    //accepts a command with or without a leading '/', matching how the standard 1.19+ command-execution
    //packets already reach it (see LoginState#handleCommand), so no leading slash is needed here either.
    private static Dialog dialog(String title, String commandName) {
        return new MultiActionDialog(new CommonDialogData(
                Component.text(title), null, false, false, DialogAction.NONE,
                List.of(new PlainMessageDialogBody(new PlainMessage(Component.text(Messages.get("dialog-password-body")), 200))),
                List.of(new Input("alix:password", new TextInputControl(200, Component.text(Messages.get("dialog-password-input-label")), true, "",
                        Password.MAX_PASSWORD_LEN, null)))),
                List.of(new ActionButton(new CommonButtonData(Component.text(Messages.get("dialog-submit-button")), null, 150),
                        new DynamicRunCommandAction(new DialogTemplate(commandName + " $(alix:password)")))),
                null, 2);
    }
}
