package org.turter.util;

import javax.persistence.EntityNotFoundException;

public class ExceptionSupplier {
    public static EntityNotFoundException audioTrackNotFound(Long id) {
        return new EntityNotFoundException("Audio track not found for id " + id);
    }
}
