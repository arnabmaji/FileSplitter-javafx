package io.github.arnabmaji19.filesplitter.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ContentRipper {

    private static final int BUFFER_SIZE = 4096;  // 4 kb

    private InputStream inputStream;
    private OutputStream outputStream;
    private long size;

    public ContentRipper(InputStream inputStream, OutputStream outputStream, long size) {
        this.inputStream = inputStream;
        this.outputStream = outputStream;
        this.size = size;
    }

    public void rip() throws IOException {
        int bytesRead = 0;
        while (true) {
            bytesRead += BUFFER_SIZE;
            if (bytesRead > size) break;
            writeToDestination(BUFFER_SIZE);

        }
        bytesRead -= BUFFER_SIZE;
        // write left out bytes
        if (bytesRead < size) writeToDestination((int) (size - bytesRead));
    }

    private void writeToDestination(int bytesToRead) throws IOException {
        var content = new byte[bytesToRead];
        inputStream.read(content);
        outputStream.write(content);
    }
}
