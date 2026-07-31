package alix.common.utils.file.managers;

import alix.common.connection.vpn.IPInfo;
import alix.common.utils.file.types.IPsCacheFile;

import java.net.InetAddress;

public final class IpsCacheFileManager {

    private static final IPsCacheFile file = new IPsCacheFile();

    public static void add(InetAddress ip, IPInfo ipInfo) {
        file.getMap().put(ip, ipInfo);
        //add(ip.getHostAddress(), isProxy);
    }

    public static IPInfo getInfo(InetAddress ip) {
        return file.getMap().get(ip);
    }

    public static void save() {
        file.save();
    }

    static {
        file.load();
    }

    public static void init() {
    }
}