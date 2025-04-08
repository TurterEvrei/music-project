package org.turter.musiccatalogue.util;

import jakarta.persistence.EntityNotFoundException;

public class ExceptionSupplier {
    public static EntityNotFoundException audioTrackNotFound(Long id) {
        return new EntityNotFoundException("Audio track not found for id " + id);
    }

    public static EntityNotFoundException compositionNotFound(Long id) {
        return new EntityNotFoundException("Composition not found for id " + id);
    }
}
