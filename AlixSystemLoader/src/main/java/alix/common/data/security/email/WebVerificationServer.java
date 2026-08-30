package alix.common.data.security.email;

import alix.common.AlixCommonMain;
import alix.common.data.PersistentUserData;
import alix.common.data.file.UserFileManager;
import alix.common.utils.AlixCache;
import com.google.common.cache.Cache;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Optional built-in webserver that lets a player verify their email by clicking a link in the verification email,
 * instead of having to type a code into the game. Disabled by default (see 'enable-web-verification' in
 * email-config.yml) since it requires a network port that must be reachable from the internet - the operator is
 * expected to set 'web-verification-public-url' correctly (typically behind their own reverse proxy/domain).
 * <p>
 * Security notes: tokens are single-use, generated via SecureRandom (32 random bytes - not the same, much weaker,
 * generator used for the in-game 6-digit code), and expire automatically after 'web-verification-token-expiry-minutes'.
 * This class only ever attaches an email to an EXISTING account by name - it never creates accounts or changes
 * passwords, and a token only ever maps to the exact (player, email) pair it was generated for.
 */
public final class WebVerificationServer {

    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    private static final Cache<String, PendingWebVerification> TOKENS = AlixCache.<String, PendingWebVerification>newBuilder()
            .maximumSize(2048)
            .expireAfterWrite(Math.max(1, EmailConfig.INSTANCE.webVerificationTokenExpiryMinutes), TimeUnit.MINUTES)
            .build();

    private static HttpServer server;
    private static ExecutorService executor;

    private WebVerificationServer() {
    }

    /**
     * Starts the webserver if 'enable-web-verification' is set in email-config.yml. Safe to call multiple times -
     * a no-op if already running or disabled. Should be called once on plugin enable.
     */
    public static synchronized void startIfEnabled() {
        if (!EmailConfig.INSTANCE.enableWebVerification || server != null) return;

        try {
            var address = new InetSocketAddress(EmailConfig.INSTANCE.webVerificationBindAddress, EmailConfig.INSTANCE.webVerificationPort);
            server = HttpServer.create(address, 0);
            executor = Executors.newFixedThreadPool(4);
            server.setExecutor(executor);
            server.createContext("/verify", WebVerificationServer::handle);
            server.start();
            AlixCommonMain.logInfo("Web email verification server started on " + address);
        } catch (Exception e) {
            AlixCommonMain.logWarning("Could not start the web email verification server (is the port already in use?): " + e.getMessage());
            server = null;
        }
    }

    /**
     * Stops the webserver, if running. Should be called once on plugin disable.
     */
    public static synchronized void stop() {
        if (server != null) {
            server.stop(0);
            server = null;
        }
        if (executor != null) {
            executor.shutdownNow();
            executor = null;
        }
    }

    /**
     * @param playerName the account this link will verify the email for
     * @param email      the email address being verified
     * @return a full, clickable verification URL, or empty if web verification is disabled or misconfigured (in which case a warning is logged)
     */
    public static Optional<String> createVerificationLink(String playerName, String email) {
        if (!EmailConfig.INSTANCE.enableWebVerification) return Optional.empty();

        String publicUrl = EmailConfig.INSTANCE.webVerificationPublicUrl;
        if (publicUrl == null || publicUrl.isBlank()) {
            AlixCommonMain.logWarning("'enable-web-verification' is on, but 'web-verification-public-url' is not set in email-config.yml! No verification link will be included in outgoing emails.");
            return Optional.empty();
        }

        String token = generateToken();
        TOKENS.put(token, new PendingWebVerification(playerName, email));

        String base = publicUrl.endsWith("/") ? publicUrl.substring(0, publicUrl.length() - 1) : publicUrl;
        return Optional.of(base + "/verify?token=" + token);
    }

    private static String generateToken() {
        byte[] bytes = new byte[32];
        SECURE_RANDOM.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

    private static void handle(HttpExchange exchange) {
        try {
            if (!"GET".equalsIgnoreCase(exchange.getRequestMethod())) {
                respond(exchange, 405, page("Method not allowed", "Only GET requests are supported.", false));
                return;
            }

            String token = queryParam(exchange.getRequestURI().getRawQuery(), "token");
            //single-use: the token is removed as soon as it's looked up, regardless of outcome
            PendingWebVerification pending = token != null ? TOKENS.asMap().remove(token) : null;

            if (pending == null) {
                respond(exchange, 400, page("Link invalid or expired", "This verification link is no longer valid. Please request a new one in-game via /account sendverifyemail.", false));
                return;
            }

            PersistentUserData data = UserFileManager.get(pending.playerName());
            if (data == null || !data.setEmail(pending.email())) {
                respond(exchange, 500, page("Something went wrong", "Your account could not be found or the email could not be saved. Please try again in-game.", false));
                return;
            }

            respond(exchange, 200, page("Email verified!", "Your email has been successfully verified. You can now close this page.", true));
        } catch (Exception e) {
            AlixCommonMain.logWarning("Error handling a web verification request: " + e.getMessage());
            try {
                respond(exchange, 500, page("Something went wrong", "Please try again in-game.", false));
            } catch (IOException ignored) {
            }
        }
    }

    private static String queryParam(String rawQuery, String name) {
        if (rawQuery == null) return null;
        for (String part : rawQuery.split("&")) {
            int eq = part.indexOf('=');
            if (eq < 0) continue;
            String key = URLDecoder.decode(part.substring(0, eq), StandardCharsets.UTF_8);
            if (key.equals(name)) return URLDecoder.decode(part.substring(eq + 1), StandardCharsets.UTF_8);
        }
        return null;
    }

    private static void respond(HttpExchange exchange, int status, String html) throws IOException {
        byte[] bytes = html.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().set("Content-Type", "text/html; charset=utf-8");
        exchange.sendResponseHeaders(status, bytes.length);
        try (var out = exchange.getResponseBody()) {
            out.write(bytes);
        }
    }

    private static String page(String title, String message, boolean success) {
        String color = success ? "#2e7d32" : "#c62828";
        return "<!doctype html><html><head><meta charset=\"utf-8\"><title>" + escape(title) + "</title>"
               + "<style>body{font-family:sans-serif;background:#111;color:#eee;display:flex;align-items:center;justify-content:center;height:100vh;margin:0}"
               + ".box{text-align:center;padding:2rem;max-width:32rem}h1{color:" + color + "}</style></head>"
               + "<body><div class=\"box\"><h1>" + escape(title) + "</h1><p>" + escape(message) + "</p></div></body></html>";
    }

    private static String escape(String s) {
        return s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;").replace("\"", "&quot;");
    }

    private record PendingWebVerification(String playerName, String email) {
    }
}
