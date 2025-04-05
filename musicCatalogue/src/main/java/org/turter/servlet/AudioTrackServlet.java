package org.turter.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.turter.dao.AudioTrackDao;
import org.turter.dao.HibernateAudioTrackDao;
import org.turter.dto.ProblemDetails;
import org.turter.entity.AudioTrack;
import org.turter.servlet.payload.NewAudioTrackPayload;

import javax.persistence.EntityNotFoundException;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;
import java.util.function.Supplier;

public class AudioTrackServlet extends HttpServlet {
    private final AudioTrackDao audioTrackDao = new HibernateAudioTrackDao();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<AudioTrack> audioTracks = audioTrackDao.getAllAudioTracks();
        resp.setContentType("application/json");
        objectMapper.writeValue(resp.getOutputStream(), audioTracks);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        BufferedReader reader = req.getReader();
        try {
            NewAudioTrackPayload payload = objectMapper.readValue(reader, NewAudioTrackPayload.class);
            AudioTrack result = audioTrackDao.saveAudioTrack(payload);
            resp.setStatus(HttpServletResponse.SC_CREATED);
            resp.setContentType("application/json");
            objectMapper.writeValue(resp.getOutputStream(), result);
        } catch (Exception e) {
            this.exceptionHandler(e, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, resp);
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        BufferedReader reader = req.getReader();
        try {
            AudioTrack audioTrack = objectMapper.readValue(reader, AudioTrack.class);
            AudioTrack result = audioTrackDao.updateAudioTrack(audioTrack);
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.setContentType("application/json");
            objectMapper.writeValue(resp.getOutputStream(), result);
        } catch (EntityNotFoundException e) {
            this.exceptionHandler(e, HttpServletResponse.SC_NOT_FOUND, resp);
        } catch (Exception e) {
            this.exceptionHandler(e, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, resp);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            Long id = Long.parseLong(req.getParameter("id"));
            audioTrackDao.deleteAudioTrack(id);
            resp.setStatus(HttpServletResponse.SC_OK);
        } catch (Exception e) {
            this.exceptionHandler(e, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, resp);
        }
    }

    private void exceptionHandler(Exception ex, int status, HttpServletResponse resp) throws IOException {
        ex.printStackTrace();
        resp.setStatus(status);
        resp.setContentType("application/json");
        objectMapper.writeValue(resp.getOutputStream(), new ProblemDetails(ex));
    }
}
