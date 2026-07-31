package alix.common.connection.vpn.utils;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public final class DynamicMonthlyRateLimiter implements RateLimiter {

    private final AtomicInteger queriesLeft;
    private final AtomicLong nextResetTime = new AtomicLong(0);
    private final int maxMonthly;

    public DynamicMonthlyRateLimiter(int maxMonthly) {
        this.maxMonthly = maxMonthly;
        this.queriesLeft = new AtomicInteger(maxMonthly); // Optimistic start
        this.updateResetTime();
    }

    private void updateResetTime() {
        ZonedDateTime now = ZonedDateTime.now(ZoneOffset.UTC);
        ZonedDateTime firstOfNextMonth = now.with(TemporalAdjusters.firstDayOfNextMonth())
                .withHour(0).withMinute(0).withSecond(0).withNano(0);

        nextResetTime.set(firstOfNextMonth.toInstant().toEpochMilli());
    }

    @Override
    public boolean tryAcquire() {
        long now = System.currentTimeMillis();

        // Reset if we crossed into a new month
        if (now >= nextResetTime.get()) {
            updateResetTime();
            queriesLeft.set(maxMonthly);
        }

        int left = queriesLeft.decrementAndGet();
        if (left < 0) {
            queriesLeft.set(0); // Prevent deep negative counts
            return false;
        }

        return true;
    }

    /**
     * Synchronizes our local counter with the definitive limit returned by the API.
     */
    public void sync(int actualQueriesLeft) {
        this.queriesLeft.set(actualQueriesLeft);
    }
}