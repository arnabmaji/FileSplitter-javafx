package io.github.arnabmaji19.filesplitter.controller;

import io.github.arnabmaji19.filesplitter.util.FileSplitter;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class FileSplitterController {
    @FXML
    private Text waitPromptText;
    @FXML
    private ProgressBar progressBar;
    @FXML
    private Button fileChooserButton;
    @FXML
    private Button generateButton;
    @FXML
    private TextField filePathTextField;
    @FXML
    private Spinner<Integer> maxPartitionsSpinner;

    private File file;
    private FileChooser fileChooser;

    public FileSplitterController() {
        this.fileChooser = new FileChooser();
    }

    @FXML
    private void chooseFile() {
        var selectedFile = fileChooser.showOpenDialog(new Stage());  // open file chooser window

        if (selectedFile != null) {
            file = selectedFile;
            // update file path in the file path text field
            filePathTextField.setText(file.getAbsolutePath());
        }
    }

    @FXML
    private void generatePartitions() {
        if (file == null) { // if user has not selected any file
            // show error dialog
            var errorAlert = new Alert(
                    Alert.AlertType.ERROR,
                    "No File Chosen",
                    ButtonType.OK
            );
            errorAlert.showAndWait();
            return;
        }
        int partitions = maxPartitionsSpinner.getValue();  // get number of partitions
        // disable all buttons and spinner
        toggleScreenControls(true);

        // start FileSplitter
        var fileSplitter = new FileSplitter(file, partitions);
        fileSplitter.addProgressListener(
                (completedPartitions, maxPartitions) -> {

                    Runnable runnable;  // create runnable to run on fx application thread

                    if (completedPartitions == maxPartitions) {  // if it is completed
                        runnable = () -> {
                            // show complete dialog
                            var alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.getButtonTypes().setAll(ButtonType.OK);
                            alert.setHeaderText("Successful!");
                            // show this alert on fx application thread
                            alert.showAndWait();
                            toggleScreenControls(false);
                            // set selected file to null
                            file = null;
                            // update text on selected file path text field
                            filePathTextField.setText("No File Chosen");
                        };
                    } else {
                        // update progress bar
                        runnable = () -> progressBar.setProgress(
                                completedPartitions / (double) maxPartitions
                        );
                    }
                    Platform.runLater(runnable);  // run the runnable on fx application thread
                }
        );

        // run FileSplitter in Background Thread
        new Thread(() -> {
            try {
                fileSplitter.split();  // start FileSplitter
            } catch (Exception e) {
                Platform.runLater(() -> {
                    toggleScreenControls(false);  // toggle screen controls
                    // create an error alert
                    var errorAlert = new Alert(
                            Alert.AlertType.ERROR
                    );
                    errorAlert.setContentText(e.getMessage());
                    errorAlert.setHeaderText("Something Unexpected Occurred!");
                    errorAlert.showAndWait();
                });
            }
        });
    }

    private void toggleScreenControls(boolean toggle) {
        generateButton.setDisable(toggle);
        fileChooserButton.setDisable(toggle);
        maxPartitionsSpinner.setDisable(toggle);
        progressBar.setVisible(toggle);
        waitPromptText.setVisible(toggle);
    }
}
