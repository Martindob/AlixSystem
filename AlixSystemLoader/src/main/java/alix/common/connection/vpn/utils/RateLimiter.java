package alix.common.connection.vpn.utils;

public interface RateLimiter {

    boolean tryAcquire();

}