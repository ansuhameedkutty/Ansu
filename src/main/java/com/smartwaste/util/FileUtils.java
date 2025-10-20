package com.smartwaste.util;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public final class FileUtils {
    private FileUtils() {}

    public static Path path(String first, String... more) {
        return Paths.get(first, more);
    }

    public static synchronized void ensureFile(Path path) {
        try {
            if (Files.notExists(path.getParent())) {
                Files.createDirectories(path.getParent());
            }
            if (Files.notExists(path)) {
                Files.createFile(path);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to ensure file: " + path, e);
        }
    }

    public static synchronized List<String> readAllLines(Path path) {
        ensureFile(path);
        try {
            return Files.readAllLines(path, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read file: " + path, e);
        }
    }

    public static synchronized void writeAllLines(Path path, List<String> lines) {
        ensureFile(path);
        try {
            Files.write(path, lines, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException("Failed to write file: " + path, e);
        }
    }

    public static synchronized void appendLine(Path path, String line) {
        ensureFile(path);
        try {
            List<String> lines = new ArrayList<>(readAllLines(path));
            lines.add(line);
            writeAllLines(path, lines);
        } catch (Exception e) {
            throw new RuntimeException("Failed to append line: " + path, e);
        }
    }
}
