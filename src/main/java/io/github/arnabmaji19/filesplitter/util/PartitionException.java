package io.github.arnabmaji19.filesplitter.util;

public class PartitionException extends RuntimeException {
    public PartitionException() {
        super("Problems with output file.");
    }

    public PartitionException(String message) {
        super(message);
    }
}
