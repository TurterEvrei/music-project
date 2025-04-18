package org.turter.musicapp.ui.main;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.turter.musicapp.MusicApplication;
import org.turter.musicapp.data.local.cache.TrackCacheStore;
import org.turter.musicapp.data.mapper.CompositionMapper;
import org.turter.musicapp.data.service.audiotrack.AudioTrackRemoveService;
import org.turter.musicapp.data.service.audiotrack.AudioTracksUpdateService;
import org.turter.musicapp.data.service.composition.*;
import org.turter.musicapp.domain.AudioTrack;
import org.turter.musicapp.domain.Composition;
import org.turter.musicapp.domain.TrackClip;
import org.turter.musicapp.ui.main.components.TrackCell;
import org.turter.musicapp.ui.main.components.TrackRowContainer;
import org.turter.musicapp.ui.modal.composition.selector.CompositionSelectorController;
import org.turter.musicapp.ui.modal.composition.titleeditor.CompositionTitleEditorController;
import org.turter.musicapp.ui.utils.UiUtils;

import java.io.File;
import java.io.IOException;

import static org.turter.musicapp.ui.utils.UiUtils.*;

public class MainController {
    private Composition currentComposition = MainScreenSupplier.getDefaultComposition();

    @FXML
    private Label compositionLabel;
    @FXML
    private AnchorPane overlayPane;
    @FXML
    private ProgressIndicator mainProgressIndicator;
    @FXML
    private Button compositionExportBtn;
    @FXML
    private ListView<AudioTrack> trackListView;
    @FXML
    private Label trackNameLabel, timeLabel;
    @FXML
    private Slider progressSlider, volumeSlider;
    @FXML
    private VBox sequencerTracksVBox;

    private MediaPlayer mediaPlayer;
    private AudioTrack currentTrack;
    private boolean isSeeking = false;
    private double volume = 0.5;

    @FXML
    public void initialize() {
        setUpTracks();
        setupVolumeSlider();
        setupPlayerProgressSlider();
        renderComposition();
    }

    /**
     * Инициализация списка треков
     */
    private void setUpTracks() {
        TrackCacheStore.evictCache();
        trackListView.setCellFactory(listView ->
                new TrackCell(this::startTrack, this::addTrackClip, this::deleteTrack));
        this.updateTracksFromRemote();
    }

    /**
     * Инициализация ползунка громкости плеера
     */
    private void setupVolumeSlider() {
        volumeSlider.setMin(0);
        volumeSlider.setMax(1);
        volumeSlider.setValue(volume);
        volumeSlider.setShowTickMarks(true);
        volumeSlider.setShowTickLabels(false);

        volumeSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            volume = newVal.doubleValue();
            if (mediaPlayer != null) {
                mediaPlayer.setVolume(volume);
            }
        });
    }

    /**
     * Инициализация шкалы прогресса плеера
     */
    private void setupPlayerProgressSlider() {
        progressSlider.setOnMousePressed(event -> {
            if (mediaPlayer != null) {
                isSeeking = true;
                double newTime = progressSlider.getValue();
                mediaPlayer.seek(Duration.seconds(newTime));
            }
        });

        progressSlider.setOnMouseReleased(event -> {
            if (mediaPlayer != null) {
                double newTime = progressSlider.getValue();
                mediaPlayer.seek(Duration.seconds(newTime));
                isSeeking = false;
            }
        });
    }

    /**
     * Отрисовка всех треков в секвенсоре
     */
    private void renderSequencer() {
        sequencerTracksVBox.getChildren().clear();

        for (TrackClip clip : currentComposition.getTracks()) {
            TrackRowContainer trackRowContainer = new TrackRowContainer(clip, () -> {
                currentComposition.removeTrack(clip);
                renderSequencer();
                return null;
            });
            sequencerTracksVBox.getChildren().add(trackRowContainer);
        }
    }

    /**
     * Отрисовка композиции
     */
    private void renderComposition() {
        compositionLabel.setText(currentComposition.getTitle());
    }

    /**
     * Хендлер для кнопки "Слушать" композиции
     */
    @FXML
    private void handleListenResult(ActionEvent event) {
        if (!validateComposition()) return;
        CompositionMixService mixService = new CompositionMixService(currentComposition);

        mixService.setOnSucceeded(e -> startTrack(CompositionMapper.toAudioTrack(currentComposition)));
        mixService.setOnFailed(e -> showErrorAlert("Информация", "Ошибка микширования"));

        mixService.start();
    }

    /**
     * Хендлер для кнопки "Экспортировать" композиции
     */
    @FXML
    private void handleExportComposition(ActionEvent event) {
        if (!validateComposition()) return;
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Сохранить как...");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("MP3 (*.mp3)", "*.mp3"),
                new FileChooser.ExtensionFilter("FLAC (*.flac)", "*.flac"),
                new FileChooser.ExtensionFilter("WAV (*.wav)", "*.wav")
        );
        fileChooser.setInitialFileName(currentComposition.getTitle() + ".mp3");
        File file = fileChooser.showSaveDialog(compositionExportBtn.getScene().getWindow());

        if (file != null) {
            String outputPath = file.getAbsolutePath();
            CompositionMixer.AudioFormat format = CompositionMixer.AudioFormat.getByExtension(outputPath);

            if (format == null) {
                showErrorAlert("Ошибка", "Неподдерживаемый формат файла.");
                return;
            }

            CompositionExportService exportService = new CompositionExportService(outputPath, currentComposition, format);
            exportService.setOnSucceeded(ev -> showConfirmationAlert("Успех", "Композиция экспортирована:\n" + outputPath));
            exportService.setOnFailed(ev -> showErrorAlert("Ошибка", "Ошибка при экспорте: " + exportService.getException().getMessage()));
            exportService.start();
        }
    }

    /**
     * Валидация композиции
     */
    private boolean validateComposition() {
        if (currentComposition.getTracks().isEmpty()) {
            shoWarningAlert("Внимание.", "Список треков секвенсора не должен быть пуст");
            return false;
        }
        return true;
    }

    /**
     * Хендлер для кнопки проигрыша плеера
     */
    @FXML
    private void handlePlay() {
        if (currentTrack == null) return;

        if (mediaPlayer == null || mediaPlayer.getStatus() == MediaPlayer.Status.STOPPED) {
            startTrack(currentTrack);
        } else {
            if (mediaPlayer.getStatus() == MediaPlayer.Status.PAUSED || mediaPlayer.getStatus() == MediaPlayer.Status.READY) {
                mediaPlayer.play();
            }
        }
    }

    /**
     * Хендлер для кнопки паузы плеера
     */
    @FXML
    private void handlePause() {
        if (mediaPlayer != null && mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING) {
            mediaPlayer.pause();
        }
    }

    /**
     * Хендлер для кнопки стоп плеера
     */
    @FXML
    private void handleStop() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.seek(Duration.ZERO);
            progressSlider.setValue(0);
        }
    }

    /**
     * Добавляет новый TrackClip в секвенсор
     */
    public void addTrackClip(AudioTrack audioTrack) {
        long durationMs = audioTrack.getDurationMs(); // Длительность из файла
        TrackClip newClip = new TrackClip(audioTrack, 0, durationMs);

        currentComposition.addTrack(newClip);
        renderSequencer();
    }

    /**
     * Удаляет AudioTrack из удаленного каталога и кэша
     */
    public void deleteTrack(AudioTrack audioTrack) {
        showLoadingOverlay();
        AudioTrackRemoveService removeService = new AudioTrackRemoveService(audioTrack);

        removeService.stateProperty().addListener(UiUtils.getFinalListener(this::hideLoadingOverlay));
        removeService.setOnSucceeded(event -> {
            showConfirmationAlert("Готово!", "Трек удален");
            renderTracks();
        });
        removeService.setOnFailed(event -> showErrorAlert("Ошибка", "Не удалось удалить трек"));
        removeService.setOnCancelled(event -> showErrorAlert("Ошибка", "Удаление отменено"));

        removeService.start();
    }

    /**
     * Добавление в плеер и воспроизведение указанного AudioTrack
     */
    private void startTrack(AudioTrack track) {
        File file = new File(track.getFilePath());
        if (!file.exists()) {
            System.out.println("Файл не найден: " + file.getAbsolutePath());
            return;
        }

        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.dispose();
        }

        Media media = new Media(file.toURI().toString());
        mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setVolume(volume);
        currentTrack = track;
        trackNameLabel.setText("Трек: " + track.getName());

        mediaPlayer.setOnReady(() -> {
            progressSlider.setMax(mediaPlayer.getTotalDuration().toSeconds());
            updateTimer();
            mediaPlayer.play();
        });

        mediaPlayer.currentTimeProperty().addListener((obs, oldTime, newTime) -> {
            if (!isSeeking) {
                progressSlider.setValue(newTime.toSeconds());
                updateTimer();
            }
        });

        mediaPlayer.setOnEndOfMedia(() -> {
            mediaPlayer.seek(Duration.ZERO);
            progressSlider.setValue(0);
        });
    }

    /**
     * Обновление таймера плеера
     */
    private void updateTimer() {
        Platform.runLater(() -> {
            if (mediaPlayer == null) return;

            Duration currentTime = mediaPlayer.getCurrentTime();
            Duration totalTime = mediaPlayer.getTotalDuration();

            String timeString = String.format("%02d:%02d / %02d:%02d",
                    (int) currentTime.toMinutes(), (int) currentTime.toSeconds() % 60,
                    (int) totalTime.toMinutes(), (int) totalTime.toSeconds() % 60
            );

            timeLabel.setText(timeString);
        });
    }

    /**
     * Хэндлер для кнопки "Обновить" в меню "Треки"
     */
    @FXML
    private void handleUpdateTracks(ActionEvent event) {
        updateTracksFromRemote();
    }

    /**
     * Обновление треков в соответствии с удаленным каталогом
     */
    private void updateTracksFromRemote() {
        try {
            showLoadingOverlay();

            AudioTracksUpdateService updateService = new AudioTracksUpdateService();

            updateService.stateProperty().addListener(UiUtils.getFinalListener(this::hideLoadingOverlay));
            updateService.setOnSucceeded(e -> renderTracks());
            updateService.setOnFailed(e -> showErrorAlert("Ошибка", "Не удалось обновить треки"));
            updateService.setOnCancelled(e -> showErrorAlert("Ошибка", "Загрузка отменена"));

            updateService.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Отрисовка списка треков
     */
    private void renderTracks() {
        trackListView.getItems().setAll(TrackCacheStore.getCachedTracks());
    }

    /**
     * Хэндлер для кнопки "Добавить" в меню "Треки"
     */
    @FXML
    private void handleAddTrack(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(MusicApplication.class.getResource("add-track.fxml"));
            Parent root = loader.load();

            Stage modalStage = new Stage();
            modalStage.initModality(Modality.APPLICATION_MODAL);
            modalStage.setTitle("Добавить трек");
            modalStage.setScene(new Scene(root));
            modalStage.showAndWait();
            renderTracks();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showLoadingOverlay() {
        Platform.runLater(() -> {
            overlayPane.setVisible(true);
            mainProgressIndicator.setProgress(-1); // Индикатор бесконечный
        });
    }

    public void hideLoadingOverlay() {
        Platform.runLater(() -> {
            overlayPane.setVisible(false);
            mainProgressIndicator.setProgress(0);
        });
    }

    /**
     * Хэндлер для кнопки "Открыть" в меню "Композиции"
     */
    @FXML
    private void handleOpenCompositionSelector(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(MusicApplication.class.getResource("composition-selector.fxml"));
            Parent root = loader.load();

            CompositionSelectorController controller = loader.getController();

            controller.setOnCompositionSelected(composition -> {
                this.currentComposition = CompositionMapper.toComposition(composition);
                System.out.println("Выбрана композиция: " + composition.title());
                renderComposition();
                renderSequencer();
            });

            Stage modalStage = new Stage();
            modalStage.initModality(Modality.APPLICATION_MODAL);
            modalStage.setTitle("Выбор композиции");
            modalStage.setScene(new Scene(root));
            modalStage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Ошибка при открытии окна выбора композиции");
            alert.showAndWait();
        }
    }

    /**
     * Хэндлер для изменения названия композиции
     */
    @FXML
    private void handleOpenCompositionTitleEditor(ActionEvent event) {
        if (currentComposition == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Сначала выберите композицию.");
            alert.showAndWait();
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(MusicApplication.class.getResource("composition-title-editor.fxml"));
            Parent root = loader.load();

            CompositionTitleEditorController controller = loader.getController();
            controller.setInitialTitle(currentComposition.getTitle());
            controller.setOnTitleConfirmed(newTitle -> {
                currentComposition.setTitle(newTitle);
                System.out.println("Новое название композиции: " + newTitle);
                renderComposition();
            });

            Stage modalStage = new Stage();
            modalStage.initModality(Modality.APPLICATION_MODAL);
            modalStage.setTitle("Изменение названия композиции");
            modalStage.setScene(new Scene(root));
            modalStage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Не удалось открыть окно редактирования");
            alert.showAndWait();
        }
    }

    /**
     * Сохраняет изменения композиции или создает новую в удаленном репозитории
     */
    public void handleSaveOrUpdateComposition(ActionEvent event) {
        showLoadingOverlay();

        CompositionSaveService saveService = new CompositionSaveService(currentComposition);

        saveService.stateProperty().addListener(UiUtils.getFinalListener(this::hideLoadingOverlay));
        saveService.setOnSucceeded(e -> {
            currentComposition = CompositionMapper.toComposition(saveService.getValue());
            showConfirmationAlert("Готово!", "Композиция сохранена");
            renderComposition();
            renderSequencer();
        });
        saveService.setOnFailed(e -> showErrorAlert("Ошибка", "Не удалось сохранить композицию"));
        saveService.setOnCancelled(e -> showErrorAlert("Ошибка", "Сохранение отменено"));

        saveService.start();
    }

    /**
     * Удаляет композицию из удаленного репозитория
     */
    public void handleDeleteComposition(ActionEvent event) {
        if (currentComposition.isNew()) {
            resetCompositionToDefault();
        } else {
            showLoadingOverlay();

            CompositionDeleteService deleteService = new CompositionDeleteService(currentComposition.getId());

            deleteService.stateProperty().addListener(UiUtils.getFinalListener(this::hideLoadingOverlay));
            deleteService.setOnSucceeded(e -> {
                currentComposition = MainScreenSupplier.getDefaultComposition();
                showConfirmationAlert("Готово!", "Композиция удалена");
                renderComposition();
                renderSequencer();
            });
            deleteService.setOnFailed(e -> showErrorAlert("Ошибка", "Не удалось удалить композицию"));
            deleteService.setOnCancelled(e -> showErrorAlert("Ошибка", "Удаление отменено"));

            deleteService.start();
        }
    }

    /**
     * Сбрасывает композицию до значения по умолчанию и вызывает ререндер информации о композиции и секвенсора
     */
    private void resetCompositionToDefault() {
        currentComposition = MainScreenSupplier.getDefaultComposition();
        renderComposition();
        renderSequencer();
    }
}

