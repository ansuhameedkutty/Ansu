package com.smartwaste.util;

import java.nio.file.Path;
import java.util.List;

public final class IdGenerator {
    private IdGenerator() {}

    public static synchronized long nextId(String namespace) {
        Path p = FileUtils.path("data", "seq", namespace + ".txt");
        FileUtils.ensureFile(p);
        List<String> lines = FileUtils.readAllLines(p);
        long current = 0L;
        if (!lines.isEmpty()) {
            try { current = Long.parseLong(lines.get(0).trim()); } catch (NumberFormatException ignored) {}
        }
        long next = current + 1L;
        FileUtils.writeAllLines(p, List.of(Long.toString(next)));
        return next;
    }
}
