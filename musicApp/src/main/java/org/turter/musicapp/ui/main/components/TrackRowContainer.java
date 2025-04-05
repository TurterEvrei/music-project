package org.turter.musicapp.ui.main.components;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.turter.musicapp.domain.TrackClip;

import java.util.function.Supplier;

import static org.turter.musicapp.ui.main.SequencerDefaults.*;

/**
 * Контейнер для визуализации положения и состояния TrackClip
 */
public class TrackRowContainer extends VBox {
    public TrackRowContainer(TrackClip clip, Supplier<Void> onRemoveBtn) {
        super(2);
        this.setMinHeight(80);

        HBox trackHeader = new HBox(10);
        Label trackLabel = new Label(clip.getAudioTrack().getName());
        Slider volumeSlider = new Slider(0, 1, clip.getVolume());
        volumeSlider.setBlockIncrement(0.1);
        volumeSlider.setOnMouseReleased(e -> clip.setVolume(volumeSlider.getValue()));
        Button removeBtn = new Button("Убрать");
        removeBtn.setOnAction(e -> onRemoveBtn.get());
        trackHeader.getChildren().addAll(trackLabel, volumeSlider, removeBtn);

        AnchorPane timelinePane = new AnchorPane();
        timelinePane.setMinHeight(40);
        timelinePane.setMinWidth(totalMs * msToPx);

        renderTimeRuler(timelinePane);
        ClipBox clipBox = new ClipBox(clip, timelinePane);
        timelinePane.getChildren().add(clipBox);

        this.getChildren().addAll(trackHeader, timelinePane);
    }

    private void renderTimeRuler(AnchorPane timelinePane) {
        for (int ms = 0; ms <= totalMs; ms += stepMs) {
            double x = ms * msToPx;

            javafx.scene.shape.Line tick = new javafx.scene.shape.Line(x, 0, x, (ms % (stepSec * 1000) == 0) ? 15 : 7);
            tick.setStrokeWidth(1);
            tick.setStyle("-fx-stroke: #555;");
            timelinePane.getChildren().add(tick);

            if (ms % 1000 == 0) {
                Label timeLabel = new Label(String.valueOf(ms / (stepSec * 1000)));
                timeLabel.setLayoutX(x + 2);
                timeLabel.setLayoutY(15);
                timeLabel.setRotate(-90);
                timelinePane.getChildren().add(timeLabel);
            }
        }
    }
}
