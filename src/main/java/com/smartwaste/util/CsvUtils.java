package com.smartwaste.util;

import java.util.ArrayList;
import java.util.List;

public final class CsvUtils {
    private CsvUtils() {}

    private static final char DELIM = '|';
    private static final char ESC = '\\';

    public static String escape(String value) {
        if (value == null) return "";
        StringBuilder sb = new StringBuilder(value.length() + 8);
        for (char c : value.toCharArray()) {
            if (c == ESC || c == DELIM || c == '\n' || c == '\r') {
                sb.append(ESC);
            }
            // normalize newlines to spaces for single-line storage
            if (c == '\n' || c == '\r') {
                sb.append(' ');
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    public static String[] split(String line) {
        List<String> parts = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        boolean escaping = false;
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (escaping) {
                current.append(c);
                escaping = false;
            } else if (c == ESC) {
                escaping = true;
            } else if (c == DELIM) {
                parts.add(current.toString());
                current.setLength(0);
            } else {
                current.append(c);
            }
        }
        parts.add(current.toString());
        return parts.toArray(new String[0]);
    }

    public static String join(String... values) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < values.length; i++) {
            if (i > 0) sb.append(DELIM);
            sb.append(escape(values[i]));
        }
        return sb.toString();
    }
}
