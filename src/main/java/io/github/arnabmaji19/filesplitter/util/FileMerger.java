package io.github.arnabmaji19.filesplitter.util;

import java.io.*;

public class FileMerger {

    private static final int FILE_NAME_LINE_NUMBER = 2;
    private static final int PARTITIONS_NAME_START_NUMBER = 3;

    private File metaFile;
    private String workingDirectory;
    private String outputFileName;
    private OnSuccessListener onSuccessListener;

    public FileMerger(File metaFile) {
        this.metaFile = metaFile;
        this.workingDirectory = metaFile.getAbsolutePath()
                .substring(0, metaFile.getAbsolutePath().lastIndexOf('/')) + "/";
    }

    public void merge() {
        // get partitions from meta file
        joinPartitions();
        if (onSuccessListener != null) onSuccessListener.onSuccess();  // send success message
    }

    private void joinPartitions() {
        int completed = 0;
        var destinationFile = new File(workingDirectory + outputFileName);
        try (
                var metaFileStream = new BufferedReader(new FileReader(metaFile));
                var destinationFileStream = new BufferedOutputStream(
                        new FileOutputStream(destinationFile)
                )
        ) {
            var line = "";
            var lineCount = 0;
            while ((line = metaFileStream.readLine()) != null) {
                ++lineCount;
                if (lineCount == FILE_NAME_LINE_NUMBER) {  // get file name from line 2
                    this.outputFileName = line.split(":")[1];
                }
                if (lineCount > PARTITIONS_NAME_START_NUMBER) {  // read partitions name and size
                    var parts = line.split(":");

                    var partitionName = parts[0];
                    var partitionSize = Long.parseLong(parts[1]);

                    var partitionFile = new File(workingDirectory + partitionName);
                    // check if current partition is in current directory
                    if (!partitionFile.exists() || (partitionFile.length() != partitionSize))
                        throw new PartitionException("Partition not found.");  // error if all partitions are not found

                    // read file content from each partitions
                    try (var partitionStream = new BufferedInputStream(new FileInputStream(partitionFile))) {
                        var ripper = new ContentRipper(
                                partitionStream,
                                destinationFileStream,
                                partitionSize
                        );
                        ripper.rip();
                    } catch (IOException e) {
                        var partitionException = new PartitionException("Can not read " + partitionName);
                        partitionException.initCause(e);
                        throw partitionException;
                    }
                }
            }
            destinationFile.renameTo(new File(workingDirectory + outputFileName));
        } catch (IOException e) {
            var metaFileException = new MetaFileException("Problems with Meta File.");
            metaFileException.initCause(e);
            throw metaFileException;
        }
    }

    public void addOnSuccessListener(OnSuccessListener onSuccessListener) {
        this.onSuccessListener = onSuccessListener;
    }

    public interface OnSuccessListener {
        void onSuccess();
    }

}
