module io.github.arnabmaji19.filesplitter {
    requires javafx.controls;
    requires javafx.fxml;

    opens io.github.arnabmaji19.filesplitter to javafx.fxml;
    exports io.github.arnabmaji19.filesplitter;
    opens io.github.arnabmaji19.filesplitter.controller;
}
