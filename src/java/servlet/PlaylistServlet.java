package servlet;

import controller.MusicLibrary;
import model.Playlist;
import model.Node;
import java.io.IOException;
import javax.servlet.*;
import javax.servlet.http.*;

/**
 * PlaylistServlet: Xử lý thêm bài hát vào Playlist và Shuffle.
 * POST /playlist?action=add&playlistId=P01&songId=S01
 * POST /playlist?action=shuffle&playlistId=P01
 * GET  /playlist?playlistId=P01
 */
public class PlaylistServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        MusicLibrary library = (MusicLibrary) getServletContext().getAttribute("library");
        String playlistId = req.getParameter("playlistId");

        Playlist playlist = library.getPlaylistById(playlistId);
        req.setAttribute("playlist", playlist);
        req.setAttribute("playlists", ((model.User) getServletContext().getAttribute("user")).getPlaylists());
        req.getRequestDispatcher("/WEB-INF/views/playlist.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        MusicLibrary library = (MusicLibrary) getServletContext().getAttribute("library");
        String action = req.getParameter("action");
        String playlistId = req.getParameter("playlistId");

        String message = "";
        if ("add".equals(action)) {
            String songId = req.getParameter("songId");
            message = library.addSongToPlaylist(playlistId, songId);
        } else if ("shuffle".equals(action)) {
            Playlist playlist = library.getPlaylistById(playlistId);
            if (playlist != null) {
                playlist.shuffle();
                message = "Thong bao: Da xao tron playlist thanh cong!";
            }
        }

        resp.sendRedirect(req.getContextPath() + "/playlist?playlistId=" + playlistId + "&msg=" + java.net.URLEncoder.encode(message, "UTF-8"));
    }
}
