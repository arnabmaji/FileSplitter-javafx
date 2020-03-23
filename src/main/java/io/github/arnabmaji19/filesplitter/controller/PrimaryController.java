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
        stage.setScene(scene);
        stage.setTitle("File Splitter");
        stage.show();
    }

    @FXML
    private void viewFileMerger() {
        System.out.println("File Merger");
    }
}
