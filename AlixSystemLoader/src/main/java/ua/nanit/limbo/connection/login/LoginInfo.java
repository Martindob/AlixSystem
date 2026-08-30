package ua.nanit.limbo.connection.login;

import alix.common.login.LoginVerdict;
import io.netty.channel.Channel;
import io.netty.util.AttributeKey;

public record LoginInfo(boolean joinedRegistered, LoginVerdict verdict, boolean pendingEmailVerification) {

    public static final AttributeKey<LoginInfo> JOIN_INFO = AttributeKey.newInstance("alix:join_info");

    public static void set(Channel channel, boolean joinedRegistered, LoginVerdict verdict) {
       channel.attr(LoginInfo.JOIN_INFO).set(new LoginInfo(joinedRegistered, verdict, false));
    }

    //Marks that the email saved during registration (via 'require-email-in-register') still needs to be verified.
    //Read once the player fully joins (see VerifiedPacketProcessor), to automatically kick off verification through the
    //normal /account sendverifyemail flow - the underlying Channel is the same object across the pre-login/post-login
    //transition, so this attribute survives the connection hand-off safely, unlike trying to send further packets
    //through the transient pre-login connection itself.
    public static void markPendingEmailVerification(Channel channel) {
        LoginInfo current = channel.attr(LoginInfo.JOIN_INFO).get();
        if (current == null) return; //defensive - should always already be set by this point
        channel.attr(LoginInfo.JOIN_INFO).set(new LoginInfo(current.joinedRegistered(), current.verdict(), true));
    }
}