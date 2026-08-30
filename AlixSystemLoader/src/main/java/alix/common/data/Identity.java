package alix.common.data;

import alix.common.antibot.captcha.secrets.files.UserTokensFileManager;
import alix.common.utils.other.keys.secret.MapSecretKey;

import java.security.SecureRandom;
import java.util.UUID;

public final class Identity {

    private static final SecureRandom RANDOM = new SecureRandom();
    private final String identity;
    private final MapSecretKey<UUID> tokenKey;

    Identity(String identity) {
        this.identity = identity;
        this.tokenKey = MapSecretKey.fromName(this.identity);
    }

    public String identity() {
        return identity;
    }

    public MapSecretKey<UUID> tokenKey() {
        return tokenKey;
    }

    public String getToken() {
        return UserTokensFileManager.getTokenOrSupply(this);
    }

    public static Identity newIdentity(String name) {
        return new Identity(name + "*" + Long.toHexString(RANDOM.nextLong()));
    }

    public static Identity fromSaved(String name, String saved) {
        return new Identity(saved.equals("0") ? name : saved);
    }

    @Override
    public boolean equals(Object obj) {
        return obj == this || obj instanceof Identity id && id.identity.equals(this.identity);
    }

    @Override
    public int hashCode() {
        return this.identity.hashCode();
    }
}