package io.github.arnabmaji19.filesplitter.util;

import java.io.*;

public class FileSplitter {

    private static final String PARTITION_EXTENSION = ".part";
    private static final String META_FILE_WARNING = "!!!DO NOT ALTER THIS FILE!!!";

    private File file;
    private String fileName;
    private int maxPartitions;
    private String outputDirectoryPath;
    private String[] outputFileNames;
    private long eachPartitionSize;
    private long lastPartitionSize;
    private ProgressListener progressListener;

    public FileSplitter(File file, int maxPartitions) {
        this.file = file;
        this.fileName = file.getName().substring(0, file.getName().lastIndexOf("."));  // get file name without extension
        this.outputDirectoryPath = file.getAbsolutePath().substring(0, file.getAbsolutePath().lastIndexOf('.'));  // create output directory for all files
        this.maxPartitions = maxPartitions;  // maximum partitions for all the files
        this.outputFileNames = new String[maxPartitions];  // array for all partitions names
        this.eachPartitionSize = file.length() / maxPartitions;  // get size of each partitions in bytes
        this.lastPartitionSize = eachPartitionSize + (file.length() % maxPartitions);  // add left out bytes for last partition
    }

    public void split() {
        var outputDirectoryFile = new File(outputDirectoryPath);
        if (!outputDirectoryFile.mkdir())
            throw new OutputDirectoryException();  // if it fails to create output directory
        // create all names for output files
        for (int i = 0; i < outputFileNames.length; i++) {
            outputFileNames[i] = fileName + PARTITION_EXTENSION + (i + 1);
        }
        // create meta file for storing info about all partitions
        createMetaFile();
        // make different partitions of the file
        makePartitions();

    }

    private void createMetaFile() {
        var metaFileName = outputDirectoryPath + "/" + fileName + "-meta.txt";

        try (var metaFile = new BufferedWriter(new FileWriter(metaFileName))) {
            metaFile.write(META_FILE_WARNING);
            metaFile.newLine();
            metaFile.write("FILE_NAME:" + file.getName() + "\n");

            metaFile.write("PARTITIONS:\n");
            // save all partitions names with size
            for (int i = 0; i < outputFileNames.length; i++) {
                long size = eachPartitionSize;
                if (i == maxPartitions - 1) size = lastPartitionSize;
                metaFile.write(outputFileNames[i] + ":" + size);
                metaFile.newLine();
            }
        } catch (IOException e) {
            var metaFileException = new MetaFileException();
            metaFileException.initCause(e);
            throw metaFileException;
        }
    }

    private void makePartitions() {  // make partitions for all files
        updateProgress(0);
        try (var sourceFileStream = new BufferedInputStream(new FileInputStream(file))) {
            for (int i = 0; i < maxPartitions; i++) {
                long size = eachPartitionSize;
                if (i == maxPartitions - 1) size = lastPartitionSize;

                var fileWithPath = outputDirectoryPath + "/" + outputFileNames[i];
                try (var destinationFileStream = new BufferedOutputStream(new FileOutputStream(fileWithPath))) {
                    // read and write to destination file with buffer limit
                    var ripper = new ContentRipper(sourceFileStream, destinationFileStream, size);
                    ripper.rip();

                } catch (IOException e) {
                    var partitionException = new PartitionException();
                    partitionException.initCause(e);
                    throw partitionException;
                }
                updateProgress(i + 1);  // update progress
            }
        } catch (IOException e) {
            var sourceFileException = new SourceFileException();
            sourceFileException.initCause(e);
            throw sourceFileException;
        }
    }


    private void updateProgress(int completed) {
        if (progressListener != null) progressListener.onProgressChanged(completed, maxPartitions);
    }

    public void addProgressListener(ProgressListener progressListener) {
        this.progressListener = progressListener;
    }

    public interface ProgressListener {
        void onProgressChanged(int completedPartitions, int maxPartitions);
    }

}
