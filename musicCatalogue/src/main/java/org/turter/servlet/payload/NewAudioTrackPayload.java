package org.turter.servlet.payload;

public class NewAudioTrackPayload {
    private String title;
    private byte[] data;

    public String getTitle() {
        return title;
    }

    public NewAudioTrackPayload setTitle(String title) {
        this.title = title;
        return this;
    }

    public byte[] getData() {
        return data;
    }

    public NewAudioTrackPayload setData(byte[] data) {
        this.data = data;
        return this;
    }
}
