package alix.common.data.fingerprinting;

import alix.common.AlixCommonMain;
import alix.common.utils.file.AlixFileManager;
import com.github.retrooper.packetevents.wrapper.configuration.server.WrapperConfigServerResourcePackSend;
import ua.nanit.limbo.protocol.packets.configuration.resourcepack.PacketConfigOutResourcePack;
import ua.nanit.limbo.protocol.snapshot.PacketSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Pattern;

/**
 * EXPERIMENTAL - see NanoLimbo#enableFingerprinting (a hardcoded, source-only switch: there is
 * deliberately no config.yml option for this) and fingerprint-packs.txt's own header comment for what this
 * does and its privacy implications. Not verified against a real client.
 * <p>
 * Builds one {@link PacketSnapshot} per configured hash in fingerprint-packs.txt: each is a resource-pack-
 * send packet carrying that hash and a deliberately unreachable address (BAD_URL) - a client whose local
 * pack cache already has content matching that exact hash answers instantly from its own cache (a "hit"),
 * never even attempting BAD_URL; one that doesn't tries BAD_URL and fails immediately (a "miss"). See
 * {@link FingerprintBuilder} for how the per-connection sequence of hits/misses across every probe becomes
 * one {@link Fingerprint}.
 */
public final class FingerprintManager {

    private static final String BAD_URL = "http://127.0.0.1:0";
    private static final Pattern SHA1_PATTERN = Pattern.compile("[0-9a-fA-F]{40}");

    public static final PacketSnapshot[] PROBES;
    public static final int PROBE_COUNT;
    public static final Map<UUID, Integer> IDX_BY_UUID;

    static {
        List<String> hashes = loadHashes();
        PROBE_COUNT = hashes.size();
        PROBES = new PacketSnapshot[PROBE_COUNT];
        IDX_BY_UUID = new HashMap<>(PROBE_COUNT * 2);

        for (int i = 0; i < PROBE_COUNT; i++) {
            UUID uuid = UUID.randomUUID();
            PROBES[i] = PacketSnapshot.of(new PacketConfigOutResourcePack(
                    new WrapperConfigServerResourcePackSend(uuid, BAD_URL, hashes.get(i), true, null)));
            IDX_BY_UUID.put(uuid, i);
        }

        if (PROBE_COUNT > 0)
            AlixCommonMain.logInfo("Resource-pack fingerprinting: loaded " + PROBE_COUNT + " probe hash(es) from fingerprint-packs.txt (still requires NanoLimbo#enableFingerprinting to actually be on).");
    }

    public static Integer getIndex(UUID uuid) {
        return IDX_BY_UUID.get(uuid);
    }

    private static List<String> loadHashes() {
        List<String> hashes = new ArrayList<>();
        var file = AlixFileManager.getOrCreatePluginFile("fingerprint-packs.txt", AlixFileManager.FileType.CONFIG);

        for (String line : AlixFileManager.getLines(file)) {
            String trimmed = line.trim();
            if (trimmed.isEmpty() || trimmed.startsWith("#")) continue;

            if (!SHA1_PATTERN.matcher(trimmed).matches()) {
                AlixCommonMain.logWarning("fingerprint-packs.txt: ignoring invalid line (expected a 40-character SHA-1 hash): '" + trimmed + "'");
                continue;
            }

            if (hashes.size() == FingerprintBuilder.MAX_LEN) {
                AlixCommonMain.logWarning("fingerprint-packs.txt: more than " + FingerprintBuilder.MAX_LEN + " hashes listed - ignoring the rest.");
                break;
            }

            hashes.add(trimmed);
        }

        return hashes;
    }

    private FingerprintManager() {
    }
}
