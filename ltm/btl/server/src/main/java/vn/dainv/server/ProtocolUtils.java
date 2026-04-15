package vn.dainv.server;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.List;

public final class ProtocolUtils {
    private ProtocolUtils() {
    }

    public static String encode(String value) {
        String safe = value == null ? "" : value;
        return Base64.getEncoder().encodeToString(safe.getBytes(StandardCharsets.UTF_8));
    }

    public static String decode(String value) {
        if (value == null || value.isEmpty()) {
            return "";
        }
        try {
            return new String(Base64.getDecoder().decode(value), StandardCharsets.UTF_8);
        } catch (IllegalArgumentException ex) {
            return "";
        }
    }

    public static List<String> split(String text, String delimiter) {
        if (text == null || text.isEmpty()) {
            return Collections.emptyList();
        }
        String[] parts = text.split(delimiter, -1);
        List<String> output = new ArrayList<>(parts.length);
        Collections.addAll(output, parts);
        return output;
    }
}
