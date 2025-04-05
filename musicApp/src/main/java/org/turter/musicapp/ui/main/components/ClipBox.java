package org.turter.musicapp.ui.main.components;

import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import org.turter.musicapp.domain.TrackClip;

import static org.turter.musicapp.ui.main.SequencerDefaults.msToPx;

/**
 * Визуализация TrackClip в секвенсоре
 */
public class ClipBox extends HBox {
    
    public ClipBox(TrackClip clip, AnchorPane timelinePane) {
        super();
        this.setStyle("-fx-background-color: lightblue; -fx-border-color: black;");
        this.setMinHeight(30);

//        double msToPx = 0.02; // 1 мс = 0.1 px
        double width = clip.getDurationMs() * msToPx;
        this.setPrefWidth(width);

        Label titleLabel = new Label(clip.getAudioTrack().getName());
        this.getChildren().add(titleLabel);

        // Установка позиции по времени старта
        this.setLayoutX(clip.getStartTimeMs() * msToPx);
        this.setLayoutY(5); // отступ сверху от линейки

        // Перетаскивание
        this.setOnMousePressed(event -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                this.setUserData(event.getSceneX());
            }
        });

        this.setOnMouseDragged(event -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                double startX = (double) this.getUserData();
                double deltaX = event.getSceneX() - startX;

                double newX = this.getLayoutX() + deltaX;
                newX = Math.max(0, Math.min(newX, timelinePane.getWidth() - this.getWidth()));

                long newStartTimeMs = (long) (newX / msToPx);
                clip.setStartTimeMs(newStartTimeMs);

                this.setLayoutX(newX);
                this.setUserData(event.getSceneX()); // обновим для smooth drag
            }
        });
    }
    
}
