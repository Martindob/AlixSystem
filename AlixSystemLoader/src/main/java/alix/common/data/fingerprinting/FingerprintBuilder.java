package alix.common.data.fingerprinting;

/**
 * Accumulates one connection's resource-pack-probe responses into a single fingerprint value - see
 * {@link FingerprintManager} for what a "probe" is and {@link ua.nanit.limbo.protocol.packets.configuration.resourcepack.PacketConfigInResourcePackResponse}
 * for where responses feed into this. One instance is created per connection that's being fingerprinted.
 */
public final class FingerprintBuilder {

    //Hard cap on how many probes a single fingerprint can be built from - the accumulated bits are packed
    //into a plain int, so this can never exceed 32; kept well under that for headroom.
    public static final int MAX_LEN = 24;

    private final int total;
    private final boolean[] resolved;
    private int resolvedCount;
    int value;

    public FingerprintBuilder(int total) {
        if (total < 0 || total > MAX_LEN)
            throw new IllegalArgumentException("total must be within [0, " + MAX_LEN + "]: " + total);
        this.total = total;
        this.resolved = new boolean[total];
    }

    /**
     * @param idx a probe's bit index, as returned by {@link FingerprintManager#getIndex}
     * @param hit whether the client's response for that probe indicated a cache hit (it already had that
     *            probe's content) rather than a miss (it tried and failed the deliberately unreachable URL)
     * @return true once every probe has been resolved exactly once - {@link #getFingerprint()} can be
     * called now; false if more responses are still outstanding. A duplicate/late response for an
     * already-resolved index (a client can send more than one status packet per pack, e.g. an
     * intermediate "accepted" ack before the final outcome - only the first one reaching here is trusted)
     * is ignored rather than double-counted.
     */
    public boolean resolve(int idx, boolean hit) {
        if (this.resolved[idx]) return this.resolvedCount == this.total;
        this.resolved[idx] = true;
        this.value |= (hit ? 1 : 0) << idx;
        return ++this.resolvedCount == this.total;
    }

    public Fingerprint getFingerprint() {
        return new Fingerprint(this);
    }
}
