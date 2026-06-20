<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List, model.Song, model.Playlist" %>
<%
    List<Song> results = (List<Song>) request.getAttribute("results");
    List<Playlist> playlists = (List<Playlist>) request.getAttribute("playlists");
    String keyword = (String) request.getAttribute("keyword");
    if (keyword == null) keyword = "";
%>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Tìm kiếm – SoundStream</title>
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
            <input type="text" name="q" placeholder="Tìm bài hát, nghệ sĩ..." value="<%= keyword %>">
        </form>
        <div class="user-info">
            <div class="avatar">U</div>
            <span>Dai Ca Music</span>
        </div>
    </header>

    <!-- SIDEBAR -->
    <nav class="sidebar">
        <div class="sidebar-section">
            <div class="sidebar-label">Menu</div>
            <a href="${pageContext.request.contextPath}/home" class="nav-item">🏠 Trang chủ</a>
            <a href="${pageContext.request.contextPath}/search" class="nav-item active">🔎 Tìm kiếm</a>
        </div>
        <div class="sidebar-section">
            <div class="sidebar-label">Playlist</div>
            <%
                if (playlists != null) {
                    String[] emojis = {"🎧","🎸"};
                    int idx = 0;
                    for (Playlist pl : playlists) {
            %>
            <a href="${pageContext.request.contextPath}/playlist?playlistId=<%= pl.getPlaylistId() %>" class="playlist-card-side">
                <div class="playlist-thumb"><%= emojis[idx++ % emojis.length] %></div>
                <div class="playlist-info">
                    <div class="playlist-name-side"><%= pl.getName() %></div>
                    <div class="playlist-count"><%= pl.getTotalSongs() %> bài</div>
                </div>
            </a>
            <% } } %>
        </div>
    </nav>

    <!-- MAIN -->
    <main class="main-content">
        <div class="search-hero">
            <h1>
                <% if (!keyword.isEmpty()) { %>
                Kết quả cho: <span style="color:var(--accent-light)">"<%= keyword %>"</span>
                <% } else { %>
                Tất cả bài hát
                <% } %>
            </h1>
            <p>Tìm kiếm tuyến tính Linear Search – Thuật toán O(n)</p>
        </div>

        <div class="result-count">
            <%= results != null ? results.size() : 0 %> bài hát được tìm thấy
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
                    if (results != null && !results.isEmpty()) {
                        int i = 1;
                        for (Song song : results) {
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
                        <% if (playlists != null) { for (Playlist pl : playlists) { %>
                        <form method="post" action="${pageContext.request.contextPath}/playlist" style="display:inline">
                            <input type="hidden" name="action" value="add">
                            <input type="hidden" name="playlistId" value="<%= pl.getPlaylistId() %>">
                            <input type="hidden" name="songId" value="<%= song.getSongId() %>">
                            <button type="submit" class="btn-add">+ <%= pl.getName() %></button>
                        </form>
                        <% } } %>
                    </td>
                </tr>
                <%
                        }
                    } else {
                %>
                <tr><td colspan="7" style="text-align:center; padding:40px; color:var(--text-muted)">
                    🔍 Không tìm thấy bài hát phù hợp
                </td></tr>
                <% } %>
                </tbody>
            </table>
        </div>
    </main>
</div>
</body>
</html>
