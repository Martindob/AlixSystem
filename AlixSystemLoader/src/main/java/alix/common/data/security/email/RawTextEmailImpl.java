package alix.common.data.security.email;

final class RawTextEmailImpl implements Email {

    private final String email;

    RawTextEmailImpl(String email) {
        this.email = email;
    }

    @Override
    public String email() {
        return this.email;
    }

    @Override
    public String toSavable() {
        return this.email;
    }
}