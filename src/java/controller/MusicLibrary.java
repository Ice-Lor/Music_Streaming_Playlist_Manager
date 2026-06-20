package controller;

import model.Song;
import model.Playlist;
import model.Node;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;

public class MusicLibrary {
    // HashMap lưu trữ kho bài hát tổng hợp để tra cứu nhanh bằng ID - O(1)
    private HashMap<String, Song> songMap;
    // ArrayList chứa kho nhạc phục vụ tìm kiếm tuyến tính theo từ khóa
    private List<Song> songList;
    // HashMap quản lý các Playlist trên hệ thống bằng ID - O(1)
    private HashMap<String, Playlist> playlistMap;

    public MusicLibrary() {
        songMap = new HashMap<>();
        songList = new ArrayList<>();
        playlistMap = new HashMap<>();
    }

    public void addSongToLibrary(Song song) {
        if (song != null) {
            songMap.put(song.getSongId(), song);
            songList.add(song);
        }
    }

    // Hàm hỗ trợ gán cứng ID cho playlist
    public void addPlaylistToLibrary(String playlistId, Playlist playlist) {
        if (playlist != null) {
            playlistMap.put(playlistId, playlist);
        }
    }

    // Lấy toàn bộ danh sách bài hát
    public List<Song> getAllSongs() {
        return songList;
    }

    // Lấy Playlist theo ID
    public Playlist getPlaylistById(String playlistId) {
        return playlistMap.get(playlistId);
    }

    // THUẬT TOÁN 3A: Tra cứu nhanh theo ID bằng HashMap - O(1)
    public Song getSongById(String songId) {
        return songMap.get(songId);
    }

    // THUẬT TOÁN 3B: Tìm kiếm tuyến tính kết hợp lọc chuỗi (Keyword) - O(n)
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

    // =========================================================================
    // QUY TRÌNH THUẬT TOÁN CHUẨN 6 BƯỚC: THÊM BÀI HÁT VÀO PLAYLIST (Trang 4 PDF)
    // =========================================================================
    public String addSongToPlaylist(String playlistId, String songId) {
        // Bước 1: Tiếp nhận dữ liệu đầu vào (playlistId và songId)
        System.out.println("[Yêu cầu] Thêm bài hát " + songId + " vào Playlist " + playlistId);

        // Bước 2: Kiểm tra tính hợp lệ của Playlist_ID và Song_ID trên hệ thống
        Playlist playlist = playlistMap.get(playlistId);
        Song song = songMap.get(songId);
        if (playlist == null || song == null) {
            return "Lỗi: Không tìm thấy dữ liệu (Playlist hoặc Bài hát không tồn tại trên hệ thống)";
        }

        // Bước 3: Kiểm tra trùng lặp trong Playlist (Duyệt qua DLL)
        Node current = playlist.getHead();
        while (current != null) {
            if (current.data.getSongId().equals(songId)) {
                return "Cảnh báo: Bài hát đã tồn tại trong danh sách phát";
            }
            current = current.next;
        }

        // Bước 4 + Bước 5: Chèn dữ liệu vào cuối (Append DLL) và cập nhật Metadata
        // Thao tác này được đóng gói trong phương thức addSong của đối tượng Playlist
        playlist.addSong(song);

        // Bước 6: Trả về thông báo phản hồi thành công
        return "Thông báo: Thêm bài hát thành công!";
    }
}
