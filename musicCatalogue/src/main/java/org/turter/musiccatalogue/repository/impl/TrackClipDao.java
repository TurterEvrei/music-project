package org.turter.musiccatalogue.repository.impl;

import org.springframework.stereotype.Repository;
import org.turter.musiccatalogue.entity.TrackClip;

@Repository
public class TrackClipDao extends GenericDaoImpl<TrackClip, Long> {
    protected TrackClipDao() { super(TrackClip.class); }
}
