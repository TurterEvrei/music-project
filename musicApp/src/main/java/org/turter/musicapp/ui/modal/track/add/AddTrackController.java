package org.turter.musicapp.ui.modal.track.add;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.turter.musicapp.data.service.audiotrack.AudioTrackUploadService;
import org.turter.musicapp.ui.utils.UiUtils;

import java.io.File;

import static org.turter.musicapp.ui.utils.UiUtils.showErrorAlert;

public class AddTrackController {
    @FXML
    private AnchorPane overlayPane;
    @FXML
    private Label selectedFileLabel;
    @FXML
    private Button chooseFileButton;
    @FXML
    private Button closeButton;
    @FXML
    private ProgressIndicator progressIndicator;

    private File selectedFile;

    /**
     * Хэндлер для выбора файла в качестве источника трека
     */
    @FXML
    private void handleChooseFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Выберите аудиофайл");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("MP3 файлы", "*.mp3"));

        File file = fileChooser.showOpenDialog(chooseFileButton.getScene().getWindow());
        if (file != null) {
            selectedFile = file;
            selectedFileLabel.setText(file.getName());
        }
    }

    /**
     * Хэндлер для подтверждения выбора файла в качестве источника трека
     */
    @FXML
    private void handleConfirm() {
        if (selectedFile == null || !selectedFile.getName().endsWith(".mp3")) {
            showErrorAlert("Ошибка", "Выберите файл формата .mp3");
            return;
        }

        showLoadingOverlay();

        AudioTrackUploadService uploadService = new AudioTrackUploadService(selectedFile);

        uploadService.stateProperty().addListener(UiUtils.getFinalListener(this::hideLoadingOverlay));
        uploadService.setOnSucceeded(event -> closeWindow());
        uploadService.setOnFailed(event -> showErrorAlert("Ошибка", "Не удалось загрузить трек"));
        uploadService.setOnCancelled(event -> showErrorAlert("Ошибка", "Загрузка отменена"));

        uploadService.start();
    }

    /**
     * Хэндлер для закрытия модального окна
     */
    @FXML
    private void handleClose() {
        closeWindow();
    }

    /**
     * Закрытие модального окна
     */
    private void closeWindow() {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }

    public void showLoadingOverlay() {
        Platform.runLater(() -> {
            overlayPane.setVisible(true);
            progressIndicator.setProgress(-1);
        });
    }

    public void hideLoadingOverlay() {
        Platform.runLater(() -> {
            overlayPane.setVisible(false);
            progressIndicator.setProgress(0);
        });
    }
}

