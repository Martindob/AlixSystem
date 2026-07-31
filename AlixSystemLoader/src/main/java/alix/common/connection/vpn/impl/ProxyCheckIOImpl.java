package alix.common.connection.vpn.impl;

import alix.common.connection.vpn.CheckResultHolder;
import alix.common.connection.vpn.IPInfo;
import alix.common.connection.vpn.ProxyCheck;
import alix.common.connection.vpn.ProxyType;
import alix.common.connection.vpn.utils.DailyUTCRateLimiter;
import alix.common.connection.vpn.utils.RateLimiter;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public final class ProxyCheckIOImpl implements ProxyCheck {

    // ProxyCheck explicitly requests a cap of 100/day on the free tier, resetting at UTC 0:00
    private final RateLimiter rateLimiter = new DailyUTCRateLimiter(100);

    @Override
    public CheckResultHolder isProxy(String address) {
        if (!rateLimiter.tryAcquire()) return CheckResultHolder.UNAVAILABLE;

        JsonElement out = ProxyCheck.getResponse("https://proxycheck.io/v3/" + address);
        if (out == null || !out.isJsonObject()) return CheckResultHolder.UNAVAILABLE;

        JsonObject root = out.getAsJsonObject();

        JsonElement statusEle = root.get("status");
        if (statusEle == null || !"ok".equalsIgnoreCase(statusEle.getAsString())) {
            return CheckResultHolder.UNAVAILABLE;
        }

        JsonElement ipElement = root.get(address);
        if (ipElement == null || !ipElement.isJsonObject()) return CheckResultHolder.UNAVAILABLE;

        JsonObject ipObj = ipElement.getAsJsonObject();
        IPInfo.Builder builder = new IPInfo.Builder(address, "proxycheck.io (v3)");

        // 1. Network / ISP
        if (ipObj.has("network") && ipObj.get("network").isJsonObject()) {
            JsonObject network = ipObj.getAsJsonObject("network");
            if (network.has("asn")) builder.asn(network.get("asn").getAsString());
            if (network.has("provider")) builder.isp(network.get("provider").getAsString());
        }

        // 2. Location
        if (ipObj.has("location") && ipObj.get("location").isJsonObject()) {
            JsonObject loc = ipObj.getAsJsonObject("location");
            if (loc.has("country_name")) builder.country(loc.get("country_name").getAsString());
        }

        // 3. Detections
        boolean proxyFlag = false;
        if (ipObj.has("detections") && ipObj.get("detections").isJsonObject()) {
            JsonObject det = ipObj.getAsJsonObject("detections");

            boolean isProxy = det.has("proxy") && det.get("proxy").getAsBoolean();
            boolean isVpn = det.has("vpn") && det.get("vpn").getAsBoolean();
            boolean isTor = det.has("tor") && det.get("tor").getAsBoolean();
            boolean isScraper = det.has("scraper") && det.get("scraper").getAsBoolean();
            boolean isCompromised = det.has("compromised") && det.get("compromised").getAsBoolean();
            boolean isHosting = det.has("hosting") && det.get("hosting").getAsBoolean();

            proxyFlag = isProxy || isVpn || isTor || isScraper || isCompromised || isHosting;

            builder.proxy(proxyFlag)
                    .isVpn(isVpn)
                    .isTor(isTor)
                    .isScraper(isScraper)
                    .isCompromised(isCompromised)
                    .isHosting(isHosting);

            if (det.has("risk")) builder.riskScore(det.get("risk").getAsInt());
            if (det.has("confidence")) builder.confidenceScore(det.get("confidence").getAsInt());

            // Primary ProxyType assignment
            ProxyType type = ProxyType.NOT_A_PROXY;
            if (isTor) type = ProxyType.TOR;
            else if (isScraper) type = ProxyType.SCRAPER;
            else if (isCompromised) type = ProxyType.COMPROMISED;
            else if (isVpn) type = ProxyType.VPN;
            else if (isHosting) type = ProxyType.DATACENTER;
            else if (isProxy) type = ProxyType.PUBLIC_PROXY;

            builder.proxyType(type);
        }

        // 4. Attack History Count
        if (ipObj.has("attack_history") && ipObj.get("attack_history").isJsonObject()) {
            //JsonObject attacks = ipObj.getAsJsonObject("attack_history");
            builder.hasAttackHistory(true);
        }

        IPInfo info = builder.build();
        return proxyFlag ? CheckResultHolder.proxy(info) : CheckResultHolder.nonProxy(info);
    }
}