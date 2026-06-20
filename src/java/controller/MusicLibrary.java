package controller;

import Utils.DBContext;
import model.Song;
import model.Playlist;
import model.Node;
import java.sql.*;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;

public class MusicLibrary {
    private HashMap<String, Song> songMap;
    private List<Song> songList;
    private HashMap<String, Playlist> playlistMap;
    private boolean useDatabase = false;

    public MusicLibrary() {
        songMap = new HashMap<>();
        songList = new ArrayList<>();
        playlistMap = new HashMap<>();
        
        // Thử tải dữ liệu từ Database SQL Server khi khởi tạo
        try {
            loadFromDatabase();
            useDatabase = true;
            System.out.println("[DB] Kết nối SQL Server thành công và đã tải dữ liệu!");
        } catch (Exception e) {
            System.err.println("[DB] Không thể kết nối SQL Server (Sẽ dùng bộ nhớ tạm Demo): " + e.getMessage());
            useDatabase = false;
        }
    }

    // Tải dữ liệu từ database vào các cấu trúc dữ liệu
    public void loadFromDatabase() throws SQLException, ClassNotFoundException {
        songMap.clear();
        songList.clear();
        playlistMap.clear();

        try (Connection conn = DBContext.getConnection()) {
            // 1. Tải tất cả bài hát
            String sqlSongs = "SELECT * FROM Songs";
            try (PreparedStatement stmt = conn.prepareStatement(sqlSongs);
                 ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Song song = new Song(
                        rs.getString("songId"),
                        rs.getString("title"),
                        rs.getString("artist"),
                        rs.getString("album"),
                        rs.getInt("duration"),
                        rs.getString("genre"),
                        rs.getString("fileUrl")
                    );
                    songMap.put(song.getSongId(), song);
                    songList.add(song);
                }
            }

            // 2. Tải tất cả playlist
            String sqlPlaylists = "SELECT * FROM Playlists";
            try (PreparedStatement stmt = conn.prepareStatement(sqlPlaylists);
                 ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Playlist playlist = new Playlist(
                        rs.getString("playlistId"),
                        rs.getString("name"),
                        rs.getString("creationDate")
                    );
                    playlistMap.put(playlist.getPlaylistId(), playlist);
                }
            }

            // 3. Tải các bài hát trong từng playlist và liên kết chúng (Doubly Linked List)
            String sqlPlaylistSongs = "SELECT * FROM Playlist_Songs ORDER BY playlistId";
            try (PreparedStatement stmt = conn.prepareStatement(sqlPlaylistSongs);
                 ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String playlistId = rs.getString("playlistId");
                    String songId = rs.getString("songId");
                    Playlist playlist = playlistMap.get(playlistId);
                    Song song = songMap.get(songId);
                    if (playlist != null && song != null) {
                        playlist.addSong(song);
                    }
                }
            }
        }
    }

    public void addSongToLibrary(Song song) {
        if (song == null) return;
        
        songMap.put(song.getSongId(), song);
        if (!songList.contains(song)) {
            songList.add(song);
        }

        if (useDatabase) {
            String sql = "IF NOT EXISTS (SELECT 1 FROM Songs WHERE songId = ?) " +
                         "INSERT INTO Songs (songId, title, artist, album, duration, genre, fileUrl) VALUES (?, ?, ?, ?, ?, ?, ?)";
            try (Connection conn = DBContext.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, song.getSongId());
                stmt.setString(2, song.getSongId());
                stmt.setString(3, song.getTitle());
                stmt.setString(4, song.getArtist());
                stmt.setString(5, song.getAlbum());
                stmt.setInt(6, song.getDuration());
                stmt.setString(7, song.getGenre());
                stmt.setString(8, song.getFileUrl());
                stmt.executeUpdate();
            } catch (Exception e) {
                System.err.println("[DB Error] Thêm bài hát thất bại: " + e.getMessage());
            }
        }
    }

    public void addPlaylistToLibrary(String playlistId, Playlist playlist) {
        if (playlist == null) return;
        
        playlistMap.put(playlistId, playlist);

        if (useDatabase) {
            String sql = "IF NOT EXISTS (SELECT 1 FROM Playlists WHERE playlistId = ?) " +
                         "INSERT INTO Playlists (playlistId, name, creationDate, userId) VALUES (?, ?, ?, ?)";
            try (Connection conn = DBContext.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, playlistId);
                stmt.setString(2, playlistId);
                stmt.setString(3, playlist.getName());
                stmt.setString(4, playlist.getCreationDate());
                stmt.setString(5, "U01"); // Mặc định gán cho user U01
                stmt.executeUpdate();
            } catch (Exception e) {
                System.err.println("[DB Error] Thêm playlist thất bại: " + e.getMessage());
            }
        }
    }

    public List<Song> getAllSongs() {
        return songList;
    }

    public Playlist getPlaylistById(String playlistId) {
        return playlistMap.get(playlistId);
    }

    public Song getSongById(String songId) {
        return songMap.get(songId);
    }

    public List<Song> searchSongs(String keyword) {
        List<Song> result = new ArrayList<>();
        if (keyword == null || keyword.trim().isEmpty()) {
            return result;
        }
        
        String lowerKeyword = keyword.toLowerCase();
        for (Song song : songList) {
            if (song.getTitle().toLowerCase().contains(lowerKeyword) || 
                song.getArtist().toLowerCase().contains(lowerKeyword)) {
                result.add(song);
            }
        }
        return result;
    }

    // QUY TRÌNH THUẬT TOÁN CHUẨN 6 BƯỚC: THÊM BÀI HÁT VÀO PLAYLIST (Đồng bộ DB)
    public String addSongToPlaylist(String playlistId, String songId) {
        System.out.println("[Yêu cầu] Thêm bài hát " + songId + " vào Playlist " + playlistId);

        Playlist playlist = playlistMap.get(playlistId);
        Song song = songMap.get(songId);
        if (playlist == null || song == null) {
            return "Lỗi: Không tìm thấy dữ liệu (Playlist hoặc Bài hát không tồn tại)";
        }

        // Kiểm tra trùng lặp trong Playlist (Duyệt qua DLL)
        Node current = playlist.getHead();
        while (current != null) {
            if (current.data.getSongId().equals(songId)) {
                return "Cảnh báo: Bài hát đã tồn tại trong danh sách phát";
            }
            current = current.next;
        }

        // Lưu vào Database
        if (useDatabase) {
            String sql = "INSERT INTO Playlist_Songs (playlistId, songId) VALUES (?, ?)";
            try (Connection conn = DBContext.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, playlistId);
                stmt.setString(2, songId);
                stmt.executeUpdate();
            } catch (Exception e) {
                System.err.println("[DB Error] Thêm bài hát vào playlist thất bại: " + e.getMessage());
                return "Lỗi: Lỗi kết nối cơ sở dữ liệu!";
            }
        }

        // Chèn vào DLL và cập nhật Metadata trong memory
        playlist.addSong(song);

        return "Thông báo: Thêm bài hát thành công!";
    }

    public boolean isUseDatabase() {
        return useDatabase;
    }
}

