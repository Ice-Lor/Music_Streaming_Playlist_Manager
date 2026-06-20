package servlet;

import controller.MusicLibrary;
import model.Song;
import java.io.IOException;
import java.util.List;
import javax.servlet.*;
import javax.servlet.http.*;

/**
 * SearchServlet: Xử lý tìm kiếm bài hát theo từ khóa.
 * Xử lý GET /search?q=keyword
 */
public class SearchServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        MusicLibrary library = (MusicLibrary) getServletContext().getAttribute("library");
        String keyword = req.getParameter("q");

        List<Song> results = null;
        if (keyword != null && !keyword.trim().isEmpty()) {
            results = library.searchSongs(keyword.trim());
        } else {
            results = library.getAllSongs();
        }

        req.setAttribute("results", results);
        req.setAttribute("keyword", keyword);
        req.setAttribute("playlists", ((model.User) getServletContext().getAttribute("user")).getPlaylists());
        req.getRequestDispatcher("/WEB-INF/views/search.jsp").forward(req, resp);
    }
}
