module org.turter.musicapp {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;
    requires java.desktop;
    requires javafx.media;
    requires com.github.kokorin.jaffree;
    requires org.slf4j;
    requires com.fasterxml.jackson.databind;
    requires java.net.http;
    requires org.mapstruct;
    requires static lombok;

    opens org.turter.musicapp to javafx.fxml;
    opens org.turter.musicapp.data.dto to com.fasterxml.jackson.databind;
    exports org.turter.musicapp;
    exports org.turter.musicapp.ui.main;
    opens org.turter.musicapp.ui.main to javafx.fxml;
    exports org.turter.musicapp.ui.main.components;
    opens org.turter.musicapp.ui.main.components to javafx.fxml;
    exports org.turter.musicapp.ui.modal.track.add;
    opens org.turter.musicapp.ui.modal.track.add to javafx.fxml;
    opens org.turter.musicapp.data.dto.payload to com.fasterxml.jackson.databind;
    exports org.turter.musicapp.ui.modal.composition.selector;
    opens org.turter.musicapp.ui.modal.composition.selector to javafx.fxml;
    exports org.turter.musicapp.ui.modal.composition.titleeditor;
    opens org.turter.musicapp.ui.modal.composition.titleeditor to javafx.fxml;
}