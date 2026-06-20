package model;

import java.util.ArrayList;
import java.util.List;

public class User {
    private String userId;
    private String username;
    private List<Playlist> playlists; // Danh sách playlist sở hữu hoặc theo dõi

    public User(String userId, String username) {
        this.userId = userId;
        this.username = username;
        this.playlists = new ArrayList<>();
    }

    public void addPlaylist(Playlist playlist) {
        if (playlist != null) {
            playlists.add(playlist);
        }
    }

    // Getters and Setters
    public String getUserId() { return userId; }
    public String getUsername() { return username; }
    public List<Playlist> getPlaylists() { return playlists; }
}
