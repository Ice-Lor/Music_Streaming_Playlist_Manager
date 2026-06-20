<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List, model.Song, model.Playlist, model.User" %>
<%
    List<Song> songs = (List<Song>) request.getAttribute("songs");
    List<Playlist> playlists = (List<Playlist>) request.getAttribute("playlists");
    User user = (User) request.getAttribute("user");
%>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Music Streaming – Nhóm 2</title>
    <meta name="description" content="Ứng dụng quản lý playlist nhạc – Music Streaming Playlist Manager">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/style.css">
</head>
<body>
<div class="app-layout">

    <!-- TOPBAR -->
    <header class="topbar">
        <div class="logo">
            <div class="logo-icon">🎵</div>
            <span>SoundStream</span>
        </div>
        <form class="search-form" action="${pageContext.request.contextPath}/search" method="get">
            <button type="submit" title="Tìm kiếm">🔍</button>
            <input type="text" name="q" placeholder="Tìm bài hát, nghệ sĩ...">
        </form>
        <div class="user-info">
            <div class="avatar"><%= user != null ? user.getUsername().substring(0,1).toUpperCase() : "U" %></div>
            <span><%= user != null ? user.getUsername() : "Guest" %></span>
        </div>
    </header>

    <!-- SIDEBAR -->
    <nav class="sidebar">
        <div class="sidebar-section">
            <div class="sidebar-label">Menu</div>
            <a href="${pageContext.request.contextPath}/home" class="nav-item active">🏠 Trang chủ</a>
            <a href="${pageContext.request.contextPath}/search" class="nav-item">🔎 Tìm kiếm</a>
        </div>
        <div class="sidebar-section">
            <div class="sidebar-label">Playlist của bạn</div>
            <%
                if (playlists != null) {
                    String[] emojis = {"🎧","🎸","🎹","🎺","🥁"};
                    int idx = 0;
                    for (Playlist pl : playlists) {
            %>
            <a href="${pageContext.request.contextPath}/playlist?playlistId=<%= pl.getPlaylistId() %>" class="playlist-card-side">
                <div class="playlist-thumb"><%= emojis[idx % emojis.length] %></div>
                <div class="playlist-info">
                    <div class="playlist-name-side"><%= pl.getName() %></div>
                    <div class="playlist-count"><%= pl.getTotalSongs() %> bài hát</div>
                </div>
            </a>
            <% idx++; } } %>
        </div>
    </nav>

    <!-- MAIN -->
    <main class="main-content">
        <div class="section-header">
            <h1 class="section-title">Kho nhạc <span>(<%= songs != null ? songs.size() : 0 %> bài)</span></h1>
        </div>

        <div class="song-table-wrap">
            <table class="song-table">
                <thead>
                    <tr>
                        <th class="song-num">#</th>
                        <th>Tên bài hát</th>
                        <th>Nghệ sĩ</th>
                        <th>Album</th>
                        <th>Thể loại</th>
                        <th>Thời lượng</th>
                        <th>Thêm vào</th>
                    </tr>
                </thead>
                <tbody>
                <%
                    if (songs != null) {
                        int i = 1;
                        for (Song song : songs) {
                            int min = song.getDuration() / 60;
                            int sec = song.getDuration() % 60;
                %>
                <tr>
                    <td class="song-num"><%= i++ %></td>
                    <td class="song-title">🎵 <%= song.getTitle() %></td>
                    <td><%= song.getArtist() %></td>
                    <td><%= song.getAlbum() %></td>
                    <td><span class="badge"><%= song.getGenre() %></span></td>
                    <td class="duration"><%= String.format("%d:%02d", min, sec) %></td>
                    <td>
                        <%
                            if (playlists != null) {
                                for (Playlist pl : playlists) {
                        %>
                        <form method="post" action="${pageContext.request.contextPath}/playlist" style="display:inline">
                            <input type="hidden" name="action" value="add">
                            <input type="hidden" name="playlistId" value="<%= pl.getPlaylistId() %>">
                            <input type="hidden" name="songId" value="<%= song.getSongId() %>">
                            <button type="submit" class="btn-add">+ <%= pl.getName() %></button>
                        </form>
                        <% } } %>
                    </td>
                </tr>
                <% } } %>
                </tbody>
            </table>
        </div>
    </main>
</div>
</body>
</html>
