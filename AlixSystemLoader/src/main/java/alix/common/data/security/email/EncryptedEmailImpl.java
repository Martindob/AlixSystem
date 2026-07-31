package alix.common.data.security.email;

import alix.common.antibot.captcha.secrets.files.UserTokensFileManager;
import alix.common.data.crypto.EncryptedSequence;
import alix.common.utils.other.keys.secret.MapSecretKey;

import java.util.UUID;

final class EncryptedEmailImpl implements Email {

    private final EncryptedSequence email;

    EncryptedEmailImpl(EncryptedSequence email) {
        this.email = email;
    }

    static EncryptedEmailImpl readFromEncrypted0(String line, MapSecretKey<UUID> key) throws Exception {
        return new EncryptedEmailImpl(EncryptedSequence.fromEncrypted(line, UserTokensFileManager.getTokenOrSupply(key)));
    }

    static EncryptedEmailImpl fromUnencrypted0(String email, MapSecretKey<UUID> key) throws Exception {
        return new EncryptedEmailImpl(EncryptedSequence.fromUnencrypted(email, UserTokensFileManager.getTokenOrSupply(key)));
    }

    @Override
    public String email() {
        return this.email.decrypted();
    }

    @Override
    public String toSavable() {
        return this.email.encrypted();
    }
}