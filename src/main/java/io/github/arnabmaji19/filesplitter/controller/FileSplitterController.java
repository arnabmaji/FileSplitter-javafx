package io.github.arnabmaji19.filesplitter.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class FileSplitterController {
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
        int maxPartitions = maxPartitionsSpinner.getValue();  // get number of partitions
    }
}
