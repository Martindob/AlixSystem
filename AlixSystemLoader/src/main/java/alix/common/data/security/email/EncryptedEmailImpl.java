package alix.common.data.security.email;

import alix.common.data.crypto.EncryptedSequence;

final class EncryptedEmailImpl implements Email {

    private final EncryptedSequence email;

    EncryptedEmailImpl(EncryptedSequence email) {
        this.email = email;
    }

    static EncryptedEmailImpl readFromEncrypted0(String line, String token) throws Exception {
        return new EncryptedEmailImpl(EncryptedSequence.fromEncrypted(line, token));
    }

    static EncryptedEmailImpl fromUnencrypted0(String email, String token) throws Exception {
        return new EncryptedEmailImpl(EncryptedSequence.fromUnencrypted(email, token));
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