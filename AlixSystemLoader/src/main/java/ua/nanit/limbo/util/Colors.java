package ua.nanit.limbo.util;

public final class Colors {

    private static final char CHAR_FROM = '&';
    private static final char CHAR_TO = '§';

    private Colors() {
    }

    public static String of(String text) {
        if (text == null) return null;
        return text.replace(CHAR_FROM, CHAR_TO);
    }
}
