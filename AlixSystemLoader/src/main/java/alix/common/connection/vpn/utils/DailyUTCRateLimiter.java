package alix.common.connection.vpn.utils;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public final class DailyUTCRateLimiter implements RateLimiter {

    private final int maxRequests;
    private final AtomicInteger requestCount = new AtomicInteger();
    private final AtomicLong nextResetTime = new AtomicLong();

    public DailyUTCRateLimiter(int maxRequests) {
        this.maxRequests = maxRequests;
        this.updateResetTime();
    }

    private void updateResetTime() {
        long now = System.currentTimeMillis();
        long nextMidnightUTC = getNewResetTime(now);
        nextResetTime.set(nextMidnightUTC);
    }

    @Override
    public boolean tryAcquire() {
        long now = System.currentTimeMillis();
        long resetTime = nextResetTime.get();

        if (now >= resetTime) {
            long newResetTime = getNewResetTime(now);
            if (nextResetTime.compareAndSet(resetTime, newResetTime)) {
                requestCount.set(0);
            }
        }
        return requestCount.getAndIncrement() < maxRequests;
    }

    private static long getNewResetTime(long now) {
        // Calculates the next 0:00 UTC.
        // 86,400,000 is milliseconds in a day. Since Unix epoch starts at 0:00 UTC, this is exact.
        return ((now / 86400000L) + 1L) * 86400000L;
    }
}