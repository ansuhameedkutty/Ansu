package com.smartwaste.storage;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class FileDatabase {
    private final Path dataDir;

    public FileDatabase(Path dataDir) {
        this.dataDir = dataDir;
        try {
            Files.createDirectories(dataDir);
        } catch (IOException e) {
            throw new RuntimeException("Failed to create data directory", e);
        }
    }

    @SuppressWarnings("unchecked")
    public synchronized <T> Map<String, T> load(String name) {
        Path file = dataDir.resolve(name + ".ser");
        if (!Files.exists(file)) {
            return new HashMap<>();
        }
        try (ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(Files.newInputStream(file)))) {
            Object obj = ois.readObject();
            return (Map<String, T>) obj;
        } catch (InvalidClassException | StreamCorruptedException | EOFException e) {
            // Incompatible or corrupted data file - reset it
            try { Files.deleteIfExists(file); } catch (IOException ignored) {}
            return new HashMap<>();
        } catch (Exception e) {
            throw new RuntimeException("Failed to load " + name, e);
        }
    }

    public synchronized <T> void save(String name, Map<String, T> data) {
        Path file = dataDir.resolve(name + ".ser");
        try (ObjectOutputStream oos = new ObjectOutputStream(new BufferedOutputStream(Files.newOutputStream(file)))) {
            oos.writeObject(data);
        } catch (IOException e) {
            throw new RuntimeException("Failed to save " + name, e);
        }
    }
}
