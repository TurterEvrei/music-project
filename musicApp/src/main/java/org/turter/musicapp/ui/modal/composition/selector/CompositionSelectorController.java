package org.turter.musicapp.ui.modal.composition.selector;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.turter.musicapp.data.dto.CompositionInfoDto;
import org.turter.musicapp.data.dto.CompositionPreviewDto;
import org.turter.musicapp.data.service.composition.CompositionInfoFetcher;
import org.turter.musicapp.data.service.composition.CompositionPreviewsFetcher;
import org.turter.musicapp.ui.utils.UiUtils;

import java.util.List;
import java.util.function.Consumer;

public class CompositionSelectorController {

    @FXML
    private ListView<CompositionPreviewDto> compositionListView;

    @FXML
    private Button selectButton;

    private final CompositionPreviewsFetcher previewsFetcher = new CompositionPreviewsFetcher();
    private Consumer<CompositionInfoDto> onCompositionSelected;

    @FXML
    private void initialize() {
        selectButton.setDisable(true);

        compositionListView.setCellFactory(list -> new ListCell<>() {
            @Override
            protected void updateItem(CompositionPreviewDto item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.title());
                }
            }
        });

        compositionListView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            selectButton.setDisable(newVal == null);
        });

        previewsFetcher.setOnSucceeded(evt -> {
            List<CompositionPreviewDto> previews = previewsFetcher.getValue();
            compositionListView.setItems(FXCollections.observableArrayList(previews));
        });

        previewsFetcher.setOnFailed(evt -> {
            showError("Ошибка при получении списка композиций");
        });

        previewsFetcher.start();
    }

    public void setOnCompositionSelected(Consumer<CompositionInfoDto> callback) {
        this.onCompositionSelected = callback;
    }

    @FXML
    private void handleSelect() {
        CompositionPreviewDto selected = compositionListView.getSelectionModel().getSelectedItem();
        if (selected == null) return;

        CompositionInfoFetcher infoFetcher = new CompositionInfoFetcher(selected.id());

        infoFetcher.setOnSucceeded(evt -> {
            CompositionInfoDto info = infoFetcher.getValue();
            if (info != null && onCompositionSelected != null) {
                onCompositionSelected.accept(info);
            }
            closeWindow();
        });

        infoFetcher.setOnFailed(evt -> {
            showError("Ошибка при получении информации о композиции");
        });

        infoFetcher.start();
    }

    @FXML
    private void handleClose() {
        closeWindow();
    }

    private void closeWindow() {
        Stage stage = (Stage) compositionListView.getScene().getWindow();
        stage.close();
    }

    private void showError(String message) {
        UiUtils.showErrorAlert("Ошибка", message);
    }
}

