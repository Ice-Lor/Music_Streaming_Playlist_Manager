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
        // Khởi tạo thư viện nhạc (tự động kết nối DB hoặc dùng bộ nhớ tạm)
        MusicLibrary library = new MusicLibrary();

        // Nếu thư viện trống (chưa có bài hát nào trong DB hoặc chế độ demo)
        if (library.getAllSongs().isEmpty()) {
            Song s1 = new Song("S01", "Chung Ta Cua Tuong Lai", "Son Tung M-TP", "Sky Tour", 240, "Pop", "assets/songs/song1.mp3");
            Song s2 = new Song("S02", "Hoa Hai Duong", "Jack", "Nhung Bong Hoa", 210, "Pop/Ballad", "assets/songs/song2.mp3");
            Song s3 = new Song("S03", "Waiting For You", "MONO", "22", 205, "Synth-Pop", "assets/songs/song3.mp3");
            Song s4 = new Song("S04", "Sau Loi Tu Syn", "Suni Ha Linh", "Single", 195, "R&B", "assets/songs/song4.mp3");
            Song s5 = new Song("S05", "Biet On Cuoc Doi", "Ha Anh Tuan", "Best Of", 230, "Ballad", "assets/songs/song5.mp3");
            Song s6 = new Song("S06", "Tu Nay", "Ha Anh Tuan", "Best Of", 220, "Acoustic", "assets/songs/song6.mp3");

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
        }

        // Tạo user và liên kết tất cả playlist hiện có
        User user = new User("U01", "Dai Ca Music");
        Playlist p1 = library.getPlaylistById("P01");
        Playlist p2 = library.getPlaylistById("P02");
        if (p1 != null) user.addPlaylist(p1);
        if (p2 != null) user.addPlaylist(p2);

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
