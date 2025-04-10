package org.turter.musiccatalogue.repository.impl;

import org.springframework.stereotype.Repository;
import org.turter.musiccatalogue.entity.Composition;

@Repository
//@Transactional
public class CompositionDao extends GenericDaoImpl<Composition, Long> {
    public CompositionDao() { super(Composition.class); }
}