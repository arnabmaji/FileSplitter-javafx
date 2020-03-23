package io.github.arnabmaji19.filesplitter.util;

public class OutputDirectoryException extends RuntimeException {
    public OutputDirectoryException() {
        super("Failed to create output directory.");
    }
}
