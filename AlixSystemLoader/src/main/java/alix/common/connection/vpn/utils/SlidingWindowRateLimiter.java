package alix.common.connection.vpn.utils;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public final class SlidingWindowRateLimiter implements RateLimiter {

    private final int maxRequests;
    private final long windowMillis;
    private final AtomicInteger requestCount = new AtomicInteger();
    private final AtomicLong windowStart = new AtomicLong();

    public SlidingWindowRateLimiter(int maxRequests, long windowDuration, TimeUnit unit) {
        this.maxRequests = maxRequests;
        this.windowMillis = unit.toMillis(windowDuration);
    }

    @Override
    public boolean tryAcquire() {
        long now = System.currentTimeMillis();
        long start = windowStart.get();

        if (now - start >= windowMillis) {
            if (windowStart.compareAndSet(start, now)) {
                requestCount.set(0);
            }
        }
        return requestCount.getAndIncrement() < maxRequests;
    }
}