package org.turter.dao;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.turter.entity.AudioTrack;
import org.turter.mapper.AudioTrackMapper;
import org.turter.servlet.payload.NewAudioTrackPayload;
import org.turter.util.ExceptionSupplier;
import org.turter.util.HibernateUtil;

import java.util.List;

public class HibernateAudioTrackDao implements AudioTrackDao {
    @Override
    public AudioTrack getAudioTrackById(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(AudioTrack.class, id);
        }
    }

    @Override
    public List<AudioTrack> getAllAudioTracks() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from AudioTrack", AudioTrack.class).list();
        }
    }

    @Override
    public AudioTrack saveAudioTrack(NewAudioTrackPayload payload) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            AudioTrack newEntity = AudioTrackMapper.toEntity(payload);
            session.save(newEntity);
            transaction.commit();
            return newEntity;
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw e;
        }
    }

    @Override
    public AudioTrack updateAudioTrack(AudioTrack audioTrack) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            Long id = audioTrack.getId();
            AudioTrack entity = session.get(AudioTrack.class, id);
            if (entity == null) throw ExceptionSupplier.audioTrackNotFound(id);
            session.merge(audioTrack);
            transaction.commit();
            return audioTrack;
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw e;
        }
    }

    @Override
    public void deleteAudioTrack(Long id) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            AudioTrack audioTrack = session.get(AudioTrack.class, id);
            if (audioTrack != null) session.delete(audioTrack);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw e;
        }
    }
}
