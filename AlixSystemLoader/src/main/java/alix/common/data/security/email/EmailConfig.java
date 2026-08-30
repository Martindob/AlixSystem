package alix.common.data.security.email;

import alix.common.utils.config.alix.AlixYamlConfig;
import alix.common.utils.file.AlixFileManager;

public final class EmailConfig {

    public static final EmailConfig INSTANCE = new EmailConfig();
    private final AlixYamlConfig config;
    public final String host, username, password, email, sender;
    public final int port;
    //file name of a custom HTML verification-email template, relative to the plugin's "email-templates" folder; empty means the built-in default template is used
    public final String customVerifyEmailTemplate;

    EmailConfig() {
        var file = AlixFileManager.getOrCreatePluginFile("email-config.yml", AlixFileManager.FileType.CONFIG);
        this.config = new AlixYamlConfig(file);
        this.host = config.getString("host");
        this.username = config.getString("username");
        this.password = config.getString("password");
        this.email = config.getString("email");
        this.sender = config.getString("sender");
        this.port = config.getInt("port");
        this.customVerifyEmailTemplate = config.getString("custom-verify-email-template", "");
    }

    public static void init() {
    }

    public static AlixYamlConfig getConfig() {
        return INSTANCE.config;
    }
}