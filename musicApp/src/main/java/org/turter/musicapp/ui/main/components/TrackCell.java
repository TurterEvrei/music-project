package org.turter.musicapp.ui.main.components;

import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;
import org.turter.musicapp.domain.AudioTrack;

import java.util.function.Consumer;


/**
 * Визуализация элемента списка треков - AudioTrack
 */
public class TrackCell extends ListCell<AudioTrack> {
    private final Button playButton = new Button("▶");
    private final Button addButton = new Button("+");
    private final Button deleteButton = new Button("Удалить");
    private final HBox hBox = new HBox(playButton, addButton, deleteButton);

    public TrackCell(Consumer<AudioTrack> startTrack, Consumer<AudioTrack> addTrack, Consumer<AudioTrack> deleteTrack) {
        hBox.setSpacing(10);

        playButton.setOnAction(e -> {
            AudioTrack track = getItem();
            if (track != null) {
                startTrack.accept(track);
            }
        });

        addButton.setOnAction(e -> addTrack.accept(getItem()));

        deleteButton.setOnAction(e -> deleteTrack.accept(getItem()));
    }

    @Override
    protected void updateItem(AudioTrack track, boolean empty) {
        super.updateItem(track, empty);
        if (empty || track == null) {
            setGraphic(null);
            setText(null);
        } else {
            setText(track.getName());
            setGraphic(hBox);
        }
    }
}
