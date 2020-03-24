package io.github.arnabmaji19.filesplitter.controller;

import io.github.arnabmaji19.filesplitter.util.FileMerger;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class FileMergerController {
    @FXML
    private Button fileChooserButton;
    @FXML
    private Button mergeButton;
    @FXML
    private ProgressBar progressBar;
    @FXML
    private TextField filePathTextField;
    @FXML
    private Text waitPromptText;

    private File metaFile;
    private FileChooser fileChooser;
    private Stage fileChooserStage;

    public FileMergerController() {
        this.fileChooser = new FileChooser();
        this.fileChooser.getExtensionFilters().add(  // set extension filter to txt file only
                new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt")
        );
        this.fileChooserStage = new Stage();
    }

    @FXML
    private void chooseFile() {
        var selectedFile = fileChooser.showOpenDialog(fileChooserStage);  // open file chooser dialog

        if (selectedFile != null) {
            this.metaFile = selectedFile;  // set selected  file to meta file
            filePathTextField.setText(selectedFile.getAbsolutePath());  // update file path text field
        }
    }

    @FXML
    private void mergePartitions() {

        if (metaFile == null) {  // if user has not selected any file
            // show error dialog
            var errorAlert = new Alert(  // create error dialog
                    Alert.AlertType.ERROR
            );
            errorAlert.setHeaderText("No File Chosen!");
            errorAlert.getButtonTypes().setAll(ButtonType.OK);
            errorAlert.showAndWait();
            return;
        }

        // toggle screen controls
        toggleScreenControls(true);

        // create FileMerger instance
        var fileMerger = new FileMerger(metaFile);
        // add success listener to file merger
        fileMerger.addOnSuccessListener(() -> {
            // run UI changes on FXApplication Thread
            Platform.runLater(() -> {
                toggleScreenControls(false);  // toggle screen controls
                // reset window
                metaFile = null;
                filePathTextField.setText("No File Chosen!");
                // show a confirmation alert
                var successAlert = new Alert(
                        Alert.AlertType.INFORMATION
                );
                successAlert.setHeaderText("Successful!");
                successAlert.getButtonTypes().setAll(ButtonType.OK);
                successAlert.showAndWait();
            });
        });

        // merge files in background thread
        new Thread(() -> {
            try {
                // start FileMerger
                fileMerger.merge();
            } catch (Exception e) {
                Platform.runLater(() -> {
                    toggleScreenControls(false);  // toggle screen controls
                    // create error alert to show StackTrace
                    var errorAlert = new Alert(
                            Alert.AlertType.ERROR
                    );
                    errorAlert.setHeaderText("Something Unexpected Occurred!");
                    errorAlert.setContentText(e.getMessage());  // display exception
                    errorAlert.showAndWait();
                });
            }
        }).start();
    }

    private void toggleScreenControls(boolean toggle) {
        // disable active buttons
        fileChooserButton.setDisable(toggle);
        mergeButton.setDisable(toggle);
        // show waiting related controls
        progressBar.setVisible(toggle);
        waitPromptText.setVisible(toggle);
    }
}
