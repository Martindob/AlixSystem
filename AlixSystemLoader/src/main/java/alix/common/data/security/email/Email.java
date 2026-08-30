package alix.common.data.security.email;

import alix.common.data.PersistentUserData;
import alix.common.utils.config.ConfigProvider;

public interface Email {

    static Email readFromSaved(String line, String token) throws Exception {
        if (line.equals(PersistentUserData.NO_VALUE))
            return null;

        //it's in bare text
        if (line.contains("@"))
            return fromEmail(line, token);

        var encrypted = EncryptedEmailImpl.readFromEncrypted0(line, token);
        return isConfigEncrypt ? encrypted : new RawTextEmailImpl(encrypted.email());
    }

    static Email fromEmail(String email, String token) throws Exception {
        if (isConfigEncrypt)
            return EncryptedEmailImpl.fromUnencrypted0(email, token);

        return new RawTextEmailImpl(email);
    }

    boolean isConfigEncrypt = ConfigProvider.config.getBoolean("encrypt-emails");

    String email();

    String toSavable();
}