package alix.common.data.fingerprinting;

import alix.common.data.security.password.Password;

public final class Fingerprint {

    private final Password hash;
    //kept alongside the (randomly-salted, so not directly comparable across instances) hash purely for
    //logging/debugging until this is wired into real per-account storage/comparison
    private final int rawValue;

    Fingerprint(FingerprintBuilder builder) {
        this.rawValue = builder.value;
        this.hash = Password.commonHash(Integer.toBinaryString(builder.value));
    }

    @Override
    public String toString() {
        return "Fingerprint[" + Integer.toBinaryString(rawValue) + "]";
    }

    /*public String toSavable() {
        return Integer.toHexString(val);
    }*/
}