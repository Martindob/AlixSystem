package alix.common.data.security.email.recovery;

import alix.common.data.PersistentUserData;
import alix.common.messages.Messages;
import alix.common.utils.formatter.AlixFormatter;

import java.util.Arrays;

public final class EmailRecovery {

    private static final String reminderMessage = Messages.getWithPrefix("email-recovery-reminder");

    public static String recoveryReminder(PersistentUserData data) {
        var email = data.getEmail().email();
        char[] c = new char[email.length()];
        int at = email.indexOf('@');
        //show a max of 4 chars before @
        //hide a min of 4 chars
        int visibleFromIndex = Math.max(at - 4, 3);

        Arrays.fill(c, 0, visibleFromIndex, '*');

        for (int i = visibleFromIndex; i < email.length(); i++)
            c[i] = email.charAt(i);

        var obfEmail = new String(c);
        return AlixFormatter.format(reminderMessage, obfEmail);
    }
}