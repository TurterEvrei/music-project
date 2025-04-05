package org.turter.musicapp.ui.utils;

import javafx.beans.value.ChangeListener;
import javafx.concurrent.Worker;
import javafx.scene.control.Alert;
import javafx.scene.control.ProgressIndicator;

import java.util.function.Supplier;

public class UiUtils {

    public static <T> ChangeListener<T> getPIFinalListener(ProgressIndicator progressIndicator) {
        return getFinalListener(() -> progressIndicator.setVisible(false));
    }

    public static <T> ChangeListener<T> getFinalListener(Runnable action) {
        return (observable, oldState, newState) -> {
            if (newState == Worker.State.SUCCEEDED ||
                    newState == Worker.State.FAILED ||
                    newState == Worker.State.CANCELLED) {
                action.run();
            }
        };
    }

    public static void showErrorAlert(String title, String message) {
        showAlert(Alert.AlertType.ERROR, title, message);
    }

    public static void shoWarningAlert(String title, String message) {
        showAlert(Alert.AlertType.WARNING, title, message);
    }

    public static void showConfirmationAlert(String title, String message) {
        showAlert(Alert.AlertType.CONFIRMATION, title, message);
    }

    public static void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}
