package io.github.arnabmaji19.filesplitter.util;

public class MetaFileException extends RuntimeException {
    public MetaFileException() {
        super("Failed to create meta file.");
    }

    public MetaFileException(String message) {
        super(message);
    }
}
