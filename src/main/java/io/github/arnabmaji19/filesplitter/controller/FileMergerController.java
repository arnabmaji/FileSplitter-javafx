package io.github.arnabmaji19.filesplitter.controller;

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
            Alert errorAlert = new Alert(  // create error dialog
                    Alert.AlertType.ERROR
            );
            errorAlert.setHeaderText("No File Chosen!");
            errorAlert.getButtonTypes().setAll(ButtonType.OK);
            errorAlert.showAndWait();
            return;
        }
    }
}
