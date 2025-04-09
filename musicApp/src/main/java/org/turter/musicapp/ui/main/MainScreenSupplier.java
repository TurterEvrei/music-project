package org.turter.musicapp.ui.main;

import org.turter.musicapp.domain.Composition;

public class MainScreenSupplier {

    public static Composition getDefaultComposition() {
        return new Composition("Новая композиция");
    }

}
