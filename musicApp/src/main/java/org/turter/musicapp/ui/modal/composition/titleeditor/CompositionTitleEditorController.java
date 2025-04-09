package org.turter.musicapp.ui.modal.composition.titleeditor;

import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.function.Consumer;

public class CompositionTitleEditorController {

    @FXML
    private TextField titleField;

    @FXML
    private Button confirmButton;

    private Consumer<String> onTitleConfirmed;

    @FXML
    private void initialize() {
        confirmButton.disableProperty().bind(
                Bindings.createBooleanBinding(() ->
                                titleField.getText().trim().isEmpty(),
                        titleField.textProperty())
        );
    }

    public void setInitialTitle(String currentTitle) {
        titleField.setText(currentTitle);
    }

    public void setOnTitleConfirmed(Consumer<String> callback) {
        this.onTitleConfirmed = callback;
    }

    @FXML
    private void handleConfirm() {
        String newTitle = titleField.getText().trim();
        if (!newTitle.isEmpty() && onTitleConfirmed != null) {
            onTitleConfirmed.accept(newTitle);
        }
        closeWindow();
    }

    @FXML
    private void handleCancel() {
        closeWindow();
    }

    private void closeWindow() {
        Stage stage = (Stage) titleField.getScene().getWindow();
        stage.close();
    }
}
