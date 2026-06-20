package servlet;

import controller.MusicLibrary;
import model.Song;
import model.Playlist;
import model.User;
import java.io.IOException;
import java.util.List;
import javax.servlet.*;
import javax.servlet.http.*;

/**
 * HomeServlet: Khởi tạo dữ liệu mẫu và hiển thị trang chủ.
 * Xử lý GET /home
 */
public class HomeServlet extends HttpServlet {

    @Override
    public void init() throws ServletException {
        // Khởi tạo dữ liệu mẫu và lưu vào ServletContext để dùng chung
        MusicLibrary library = new MusicLibrary();
        Song s1 = new Song("S01", "Chung Ta Cua Tuong Lai", "Son Tung M-TP", "Sky Tour", 240, "Pop");
        Song s2 = new Song("S02", "Hoa Hai Duong", "Jack", "Nhung Bong Hoa", 210, "Pop/Ballad");
        Song s3 = new Song("S03", "Waiting For You", "MONO", "22", 205, "Synth-Pop");
        Song s4 = new Song("S04", "Sau Loi Tu Syn", "Suni Ha Linh", "Single", 195, "R&B");
        Song s5 = new Song("S05", "Biet On Cuoc Doi", "Ha Anh Tuan", "Best Of", 230, "Ballad");
        Song s6 = new Song("S06", "Tu Nay", "Ha Anh Tuan", "Best Of", 220, "Acoustic");

        library.addSongToLibrary(s1);
        library.addSongToLibrary(s2);
        library.addSongToLibrary(s3);
        library.addSongToLibrary(s4);
        library.addSongToLibrary(s5);
        library.addSongToLibrary(s6);

        // Tạo Playlist mẫu
        Playlist p1 = new Playlist("P01", "Nhac Hot 2026", "07/06/2026");
        library.addPlaylistToLibrary("P01", p1);
        library.addSongToPlaylist("P01", "S01");
        library.addSongToPlaylist("P01", "S02");
        library.addSongToPlaylist("P01", "S03");

        Playlist p2 = new Playlist("P02", "Acoustic Chill", "07/06/2026");
        library.addPlaylistToLibrary("P02", p2);
        library.addSongToPlaylist("P02", "S05");
        library.addSongToPlaylist("P02", "S06");

        // Tạo user
        User user = new User("U01", "Dai Ca Music");
        user.addPlaylist(p1);
        user.addPlaylist(p2);

        // Lưu vào context
        getServletContext().setAttribute("library", library);
        getServletContext().setAttribute("user", user);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        MusicLibrary library = (MusicLibrary) getServletContext().getAttribute("library");
        User user = (User) getServletContext().getAttribute("user");

        req.setAttribute("songs", library.getAllSongs());
        req.setAttribute("playlists", user.getPlaylists());
        req.setAttribute("user", user);

        req.getRequestDispatcher("/WEB-INF/views/home.jsp").forward(req, resp);
    }
}
