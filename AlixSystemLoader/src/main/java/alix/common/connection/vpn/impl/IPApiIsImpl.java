package alix.common.connection.vpn.impl;

import alix.common.connection.vpn.CheckResultHolder;
import alix.common.connection.vpn.IPInfo;
import alix.common.connection.vpn.ProxyCheck;
import alix.common.connection.vpn.ProxyType;
import alix.common.connection.vpn.utils.DailyUTCRateLimiter;
import alix.common.connection.vpn.utils.RateLimiter;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public final class IPApiIsImpl implements ProxyCheck {

    private final RateLimiter rateLimiter = new DailyUTCRateLimiter(1000);

    @Override
    public CheckResultHolder isProxy(String address) {
        if (!rateLimiter.tryAcquire()) return CheckResultHolder.UNAVAILABLE;

        JsonElement out = ProxyCheck.getResponse("https://api.ipapi.is/?q=" + address);
        if (out == null || !out.isJsonObject()) return CheckResultHolder.UNAVAILABLE;

        JsonObject root = out.getAsJsonObject();
        IPInfo.Builder builder = new IPInfo.Builder(address, "ipapi.is");

        boolean isTor = root.has("is_tor") && root.get("is_tor").getAsBoolean();
        boolean isVpn = root.has("is_vpn") && root.get("is_vpn").getAsBoolean();
        boolean isHosting = root.has("is_datacenter") && root.get("is_datacenter").getAsBoolean();
        boolean isAbuser = root.has("is_abuser") && root.get("is_abuser").getAsBoolean();
        boolean isProxyFlag = (root.has("is_proxy") && root.get("is_proxy").getAsBoolean()) || isTor || isVpn || isHosting;

        builder.proxy(isProxyFlag)
                .isTor(isTor)
                .isVpn(isVpn)
                .isHosting(isHosting);

        // Treat their generic abuser flag equivalently to compromised/attack history risk mapping
        if (isAbuser) {
            builder.isCompromised(true);
            builder.riskScore(100);
        }

        if (root.has("location") && root.get("location").isJsonObject()) {
            JsonObject loc = root.getAsJsonObject("location");
            if (loc.has("country")) builder.country(loc.get("country").getAsString());
        }

        if (root.has("company") && root.get("company").isJsonObject()) {
            JsonObject comp = root.getAsJsonObject("company");
            if (comp.has("name")) builder.isp(comp.get("name").getAsString());
        }

        if (root.has("asn") && root.get("asn").isJsonObject()) {
            JsonObject asnObj = root.getAsJsonObject("asn");
            if (asnObj.has("asn")) builder.asn("AS" + asnObj.get("asn").getAsInt());
        }

        ProxyType type = ProxyType.NOT_A_PROXY;
        if (isTor) type = ProxyType.TOR;
        else if (isVpn) type = ProxyType.VPN;
        else if (isHosting) type = ProxyType.DATACENTER;
        else if (isProxyFlag) type = ProxyType.PUBLIC_PROXY;
        builder.proxyType(type);

        IPInfo info = builder.build();
        return isProxyFlag ? CheckResultHolder.proxy(info) : CheckResultHolder.nonProxy(info);
    }
}