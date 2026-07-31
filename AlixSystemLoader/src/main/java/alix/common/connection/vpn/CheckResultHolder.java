package alix.common.connection.vpn;

public final class CheckResultHolder {

    public static final CheckResultHolder UNAVAILABLE = new CheckResultHolder(CheckResult.UNAVAILABLE, null);

    private final CheckResult status;
    private final IPInfo info;

    private CheckResultHolder(CheckResult status, IPInfo info) {
        this.status = status;
        this.info = info;
    }

    public static CheckResultHolder proxy(IPInfo info) {
        return new CheckResultHolder(CheckResult.PROXY, info);
    }

    public static CheckResultHolder nonProxy(IPInfo info) {
        return new CheckResultHolder(CheckResult.NON_PROXY, info);
    }

    public CheckResult getStatus() {
        return status;
    }

    public IPInfo getInfo() {
        return info;
    }
}