package org.turter.musicapp.data.remote.client;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.turter.musicapp.data.dto.AudioTrackDto;
import org.turter.musicapp.data.dto.NewAudioTrackDto;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Collections;
import java.util.List;

public class TrackApiClientImpl implements TrackApiClient {
    private static final String BASE_URL = "http://localhost:8080/musicCatalogue/tracks";
    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private static TrackApiClient instance;

    private TrackApiClientImpl() {
    }

    public static TrackApiClient getInstance() {
        if (instance == null) instance = new TrackApiClientImpl();
        return instance;
    }

    @Override
    public List<AudioTrackDto> getAllTracks() {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL))
                .header("Accept", "application/json")
                .GET()
                .build();

        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                return objectMapper.readValue(response.body(), new TypeReference<>() {});
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

    @Override
    public AudioTrackDto createTrack(NewAudioTrackDto payload) {
        try {
            String json = objectMapper.writeValueAsString(payload);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 201 || response.statusCode() == 200) {
                return objectMapper.readValue(response.body(), AudioTrackDto.class);
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean deleteTrack(long trackId) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "?id=" + trackId))
                .DELETE()
                .build();

        try {
            HttpResponse<Void> response = httpClient.send(request, HttpResponse.BodyHandlers.discarding());
            return response.statusCode() == 200 || response.statusCode() == 204;
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }
}