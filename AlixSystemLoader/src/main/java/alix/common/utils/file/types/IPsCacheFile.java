package alix.common.utils.file.types;

import alix.common.connection.vpn.IPInfo;
import alix.common.connection.vpn.serializer.IPInfoBinarySerializer;
import alix.common.utils.file.AlixFileManager;
import alix.common.utils.other.throwable.AlixException;

import java.io.*;
import java.net.InetAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class IPsCacheFile {

    private final File file;
    private final Map<InetAddress, IPInfo> map = new ConcurrentHashMap<>(1 << 7);

    public IPsCacheFile() {
        this.deleteOld();
        this.file = new File(AlixFileManager.INTERNAL_FOLDER, "ips_cache.bin");
        this.load();
    }

    void deleteOld() {
        new File(AlixFileManager.INTERNAL_FOLDER, "ips_cache.txt").delete();
    }

    public void load() {
        if (!file.exists() || file.length() == 0) return;

        try (DataInputStream in = new DataInputStream(new BufferedInputStream(new FileInputStream(file), 65536))) {
            int entryCount = in.readInt();
            for (int i = 0; i < entryCount; i++) {
                // Read IP byte array
                int ipLen = in.readByte() & 0xFF; // 4 bytes for IPv4, 16 for IPv6
                byte[] ipAddressBytes = new byte[ipLen];
                in.readFully(ipAddressBytes);

                InetAddress address = InetAddress.getByAddress(ipAddressBytes);
                IPInfo info = IPInfoBinarySerializer.deserialize(address.getHostAddress(), in);

                this.map.put(address, info);
            }
        } catch (IOException e) {
            throw new AlixException("Failed to load IPs binary cache", e);
        }
    }

    public void save() {
        try (DataOutputStream out = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(file), 65536))) {
            out.writeInt(this.map.size());

            for (Map.Entry<InetAddress, IPInfo> entry : this.map.entrySet()) {
                byte[] ipBytes = entry.getKey().getAddress();

                // Write IP length & IP raw bytes
                out.writeByte(ipBytes.length);
                out.write(ipBytes);

                // Write packed IPInfo binary payload
                IPInfoBinarySerializer.serialize(entry.getValue(), out);
            }
            out.flush();
        } catch (IOException e) {
            throw new AlixException("Failed to save IPs binary cache", e);
        }
    }

    public Map<InetAddress, IPInfo> getMap() {
        return map;
    }
}