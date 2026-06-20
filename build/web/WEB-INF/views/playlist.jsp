<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List, model.Playlist, model.Node, model.Song" %>
<%
    Playlist playlist = (Playlist) request.getAttribute("playlist");
    List<Playlist> playlists = (List<Playlist>) request.getAttribute("playlists");
    String msg = request.getParameter("msg");
    if (msg == null) msg = "";
%>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title><%= playlist != null ? playlist.getName() : "Playlist" %> – SoundStream</title>
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
            <button type="submit">🔍</button>
            <input type="text" name="q" placeholder="Tìm bài hát, nghệ sĩ...">
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
            <a href="${pageContext.request.contextPath}/search" class="nav-item">🔎 Tìm kiếm</a>
        </div>
        <div class="sidebar-section">
            <div class="sidebar-label">Playlist</div>
            <% if (playlists != null) {
                String[] emojis = {"🎧","🎸"};
                int idx = 0;
                for (Playlist pl : playlists) {
                    boolean active = playlist != null && pl.getPlaylistId().equals(playlist.getPlaylistId());
            %>
            <a href="${pageContext.request.contextPath}/playlist?playlistId=<%= pl.getPlaylistId() %>" class="playlist-card-side <%= active ? "active" : "" %>">
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
        <% if (playlist == null) { %>
        <div style="text-align:center; padding:80px; color:var(--text-muted)">
            <p style="font-size:3rem">🎶</p>
            <p>Không tìm thấy playlist.</p>
        </div>
        <% } else { %>

        <!-- Playlist Hero -->
        <div class="playlist-hero">
            <div class="playlist-cover">🎧</div>
            <div class="playlist-meta">
                <div class="playlist-type">Playlist</div>
                <div class="playlist-title"><%= playlist.getName() %></div>
                <div class="playlist-stats">
                    <span>🎵 <%= playlist.getTotalSongs() %> bài hát</span>
                    <span>·</span>
                    <%
                        int totalMin = playlist.getTotalDuration() / 60;
                        int totalSec = playlist.getTotalDuration() % 60;
                    %>
                    <span>⏱ <%= String.format("%d phút %02d giây", totalMin, totalSec) %></span>
                    <span>·</span>
                    <span>📅 <%= playlist.getCreationDate() %></span>
                </div>
            </div>
        </div>

        <!-- Thông báo kết quả thao tác -->
        <% if (!msg.isEmpty()) {
            String alertClass = "alert-success";
            if (msg.contains("Cảnh báo") || msg.contains("đã tồn tại")) alertClass = "alert-warning";
            if (msg.contains("Lỗi") || msg.contains("không tìm thấy") || msg.contains("Khong")) alertClass = "alert-error";
        %>
        <div class="alert <%= alertClass %>">
            <%= msg %>
        </div>
        <% } %>

        <!-- Actions -->
        <div class="actions">
            <!-- Nút Shuffle -->
            <form method="post" action="${pageContext.request.contextPath}/playlist">
                <input type="hidden" name="action" value="shuffle">
                <input type="hidden" name="playlistId" value="<%= playlist.getPlaylistId() %>">
                <button type="submit" class="btn-primary" id="btn-shuffle">🔀 Shuffle</button>
            </form>
            <a href="${pageContext.request.contextPath}/home" class="btn-secondary">+ Thêm bài hát</a>
        </div>

        <!-- Danh sách bài hát trong Playlist -->
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
                    </tr>
                </thead>
                <tbody>
                <%
                    Node current = playlist.getHead();
                    if (current == null) {
                %>
                <tr><td colspan="6" style="text-align:center; padding:48px; color:var(--text-muted)">
                    🎵 Playlist chưa có bài hát. <a href="${pageContext.request.contextPath}/home" style="color:var(--accent-light)">Thêm bài hát ngay!</a>
                </td></tr>
                <%
                    } else {
                        int i = 1;
                        while (current != null) {
                            Song s = current.data;
                            int min = s.getDuration() / 60;
                            int sec = s.getDuration() % 60;
                %>
                <tr>
                    <td class="song-num"><%= i++ %></td>
                    <td class="song-title">🎵 <%= s.getTitle() %></td>
                    <td><%= s.getArtist() %></td>
                    <td><%= s.getAlbum() %></td>
                    <td><span class="badge"><%= s.getGenre() %></span></td>
                    <td class="duration"><%= String.format("%d:%02d", min, sec) %></td>
                </tr>
                <% current = current.next; } } %>
                </tbody>
            </table>
        </div>
        <% } %>
    </main>
</div>
</body>
</html>
