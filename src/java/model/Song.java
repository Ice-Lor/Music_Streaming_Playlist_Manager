package model;

public class Song {
    private String songId;
    private String title;
    private String artist;
    private String album;
    private int duration; // tính bằng giây
    private String genre;

    public Song(String songId, String title, String artist, String album, int duration, String genre) {
        this.songId = songId;
        this.title = title;
        this.artist = artist;
        this.album = album;
        this.duration = duration;
        this.genre = genre;
    }

    // Getters and Setters
    public String getSongId() { return songId; }
    public String getTitle() { return title; }
    public String getArtist() { return artist; }
    public String getAlbum() { return album; }
    public int getDuration() { return duration; }
    public String getGenre() { return genre; }

    @Override
    public String toString() {
        return String.format("[%s] %s - %s (%ds)", songId, title, artist, duration);
    }
}
