package io.github.arnabmaji19.filesplitter.controller;

import io.github.arnabmaji19.filesplitter.App;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class PrimaryController {

    private Stage stage;
    private Scene scene;

    public PrimaryController() {
        this.stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setResizable(false);
    }

    @FXML
    private void viewFileSplitter() throws IOException {
        this.scene = new Scene(App.loadFXML("file_splitter"), 600, 120);
        showStage("File Splitter");  // show File Splitter stage
    }

    @FXML
    private void viewFileMerger() throws IOException {
        this.scene = new Scene(App.loadFXML("file_merger"), 600, 120);
        showStage("File Merger");  // show file Merger stage
    }

    private void showStage(String title) {
        stage.setScene(scene);
        stage.setTitle(title);
        stage.show();
    }
}
