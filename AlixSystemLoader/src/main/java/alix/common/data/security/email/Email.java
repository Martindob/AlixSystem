package alix.common.data.security.email;

import alix.common.data.PersistentUserData;
import alix.common.utils.config.ConfigProvider;
import alix.common.utils.other.keys.secret.MapSecretKey;

import java.util.UUID;

public interface Email {

    static Email readFromSaved(String line, MapSecretKey<UUID> key) throws Exception {
        if (line.equals(PersistentUserData.NO_VALUE))
            return null;

        //it's in bare text
        if (line.contains("@"))
            return fromEmail(line, key);

        var encrypted = EncryptedEmailImpl.readFromEncrypted0(line, key);
        return isConfigEncrypt ? encrypted : new RawTextEmailImpl(encrypted.email());
    }

    static Email fromEmail(String email, MapSecretKey<UUID> key) throws Exception {
        if (isConfigEncrypt)
            return EncryptedEmailImpl.fromUnencrypted0(email, key);

        return new RawTextEmailImpl(email);
    }

    boolean isConfigEncrypt = ConfigProvider.config.getBoolean("encrypt-emails");

    String email();

    String toSavable();
}