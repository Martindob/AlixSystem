package alix.common.connection.vpn;

import alix.common.utils.other.annotation.serialization.CacheField;

public final class IPInfo {

    @CacheField(id = 1)
    private final String ip;
    @CacheField(id = 2)
    private final Boolean proxy;
    @CacheField(id = 3)
    private final String providerName;
    @CacheField(id = 4)
    private final String country;
    @CacheField(id = 5)
    private final String isp;
    @CacheField(id = 6)
    private final String asn;

    @CacheField(id = 7)
    private final ProxyType proxyType;
    @CacheField(id = 8)
    private final Boolean isVpn;
    @CacheField(id = 9)
    private final Boolean isTor;
    @CacheField(id = 10)
    private final Boolean isScraper;
    @CacheField(id = 11)
    private final Boolean isCompromised;
    @CacheField(id = 12)
    private final Boolean isHosting;

    @CacheField(id = 13)
    private final int riskScore;
    @CacheField(id = 14)
    private final int confidenceScore;
    @CacheField(id = 15)
    private final boolean hasAttackHistory;

    IPInfo(Builder builder) {
        this.ip = builder.ip;
        this.proxy = builder.proxy;
        this.providerName = builder.providerName;
        this.country = builder.country != null ? builder.country : "Unknown";
        this.isp = builder.isp != null ? builder.isp : "Unknown";
        this.asn = builder.asn != null ? builder.asn : "Unknown";

        this.proxyType = builder.proxyType != null ? builder.proxyType : ProxyType.PROXY;
        this.isVpn = builder.isVpn;
        this.isTor = builder.isTor;
        this.isScraper = builder.isScraper;
        this.isCompromised = builder.isCompromised;
        this.isHosting = builder.isHosting;

        this.riskScore = builder.riskScore;
        this.confidenceScore = builder.confidenceScore;
        this.hasAttackHistory = builder.hasAttackHistory;
    }

    public String getIp() {
        return ip;
    }

    public Boolean isProxy() {
        return proxy;
    }

    public String getProviderName() {
        return providerName;
    }

    public String getCountry() {
        return country;
    }

    public String getIsp() {
        return isp;
    }

    public String getAsn() {
        return asn;
    }

    public ProxyType getProxyType() {
        return proxyType;
    }

    public Boolean isVpn() {
        return isVpn;
    }

    public Boolean isTor() {
        return isTor;
    }

    public Boolean isScraper() {
        return isScraper;
    }

    public Boolean isCompromised() {
        return isCompromised;
    }

    public Boolean isHosting() {
        return isHosting;
    }

    public int getRiskScore() {
        return riskScore;
    }

    public int getConfidenceScore() {
        return confidenceScore;
    }

    public boolean isHasAttackHistory() {
        return hasAttackHistory;
    }

    public static class Builder {
        private final String ip;
        private final String providerName;

        private Boolean proxy;
        private String country;
        private String isp;
        private String asn;

        private ProxyType proxyType = ProxyType.PROXY;
        private Boolean isVpn, isTor, isScraper, isCompromised, isHosting;
        private boolean hasAttackHistory;
        private int riskScore, confidenceScore;

        public Builder(String ip, String providerName) {
            this.ip = ip;
            this.providerName = providerName;
        }

        public Builder proxy(Boolean proxy) {
            this.proxy = proxy;
            return this;
        }

        public Builder country(String country) {
            this.country = country;
            return this;
        }

        public Builder isp(String isp) {
            this.isp = isp;
            return this;
        }

        public Builder asn(String asn) {
            this.asn = asn;
            return this;
        }

        public Builder proxyType(ProxyType type) {
            this.proxyType = type;
            return this;
        }

        public Builder isVpn(Boolean isVpn) {
            this.isVpn = isVpn;
            return this;
        }

        public Builder isTor(Boolean isTor) {
            this.isTor = isTor;
            return this;
        }

        public Builder isScraper(Boolean isScraper) {
            this.isScraper = isScraper;
            return this;
        }

        public Builder isCompromised(Boolean isCompromised) {
            this.isCompromised = isCompromised;
            return this;
        }

        public Builder isHosting(Boolean isHosting) {
            this.isHosting = isHosting;
            return this;
        }

        public Builder riskScore(int riskScore) {
            this.riskScore = riskScore;
            return this;
        }

        public Builder confidenceScore(int confidenceScore) {
            this.confidenceScore = confidenceScore;
            return this;
        }

        public Builder hasAttackHistory(boolean hasAttackHistory) {
            this.hasAttackHistory = hasAttackHistory;
            return this;
        }

        public IPInfo build() {
            return new IPInfo(this);
        }
    }
}