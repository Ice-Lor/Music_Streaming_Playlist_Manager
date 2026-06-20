import model.Song;
import model.Playlist;
import model.User;
import controller.MusicLibrary;

public class TestMain {
    public static void main(String[] args) {
        System.out.println("====== KIỂM THỬ THUẬT TOÁN THEO CHUẨN BÁO CÁO NHÓM 2 ======");
        
        // 1. Khởi tạo kho nhạc mẫu
        MusicLibrary library = new MusicLibrary();
        Song song1 = new Song("S01", "Chung Ta Cua Tuong Lai", "Son Tung M-TP", "Album 1", 240, "Pop");
        Song song2 = new Song("S02", "Hoa Hai Duong", "Jack", "Album 2", 210, "Pop/Ballad");
        Song song3 = new Song("S03", "Waiting For You", "MONO", "Album 22", 205, "Synth-Pop");
        
        library.addSongToLibrary(song1);
        library.addSongToLibrary(song2);
        library.addSongToLibrary(song3);

        // 2. Khởi tạo User và Playlist
        User user = new User("U01", "dai_ca_music");
        Playlist playlist = new Playlist("P01", "Nhac Hot 2026", "07/06/2026");
        
        // Đăng ký playlist vào thư viện và gán cho user
        library.addPlaylistToLibrary("P01", playlist);
        user.addPlaylist(playlist);

        System.out.println("\n--- THỰC THI THUẬT TOÁN THÊM BÀI HÁT (QUY TRÌNH 6 BƯỚC) ---");
        
        /* 
         * [MANUAL TRACE] TH1: Thêm bài hát S01 vào Playlist P01 đang rỗng
         * - Bước 1: Nhận input ("P01", "S01").
         * - Bước 2: Xác thực -> OK (Tồn tại playlist P01 và bài hát S01).
         * - Bước 3: Kiểm tra trùng -> current = head = null -> Không trùng.
         * - Bước 4: Chèn -> Node newNode = new Node(S01)
         *           Vì head == null -> head = newNode, tail = newNode.
         * - Bước 5: Cập nhật metadata -> totalSongs = 1, totalDuration = 240.
         * - Bước 6: Trả về thành công.
         */
        String res1 = library.addSongToPlaylist("P01", "S01");
        System.out.println("Kết quả: " + res1);
        
        /* 
         * [MANUAL TRACE] TH2: Thêm tiếp bài hát S02 vào Playlist P01 đã có S01
         * - Bước 1: Nhận input ("P01", "S02").
         * - Bước 2: Xác thực -> OK.
         * - Bước 3: Kiểm tra trùng -> current = head (S01) -> S01 != S02 -> current = next = null -> Không trùng.
         * - Bước 4: Chèn -> Node newNode = new Node(S02)
         *           Vì head != null -> tail.next (Node(S01).next) = newNode; newNode.prev = tail (Node(S01)); tail = newNode.
         *           Liên kết mới: Node(S01) <-> Node(S02).
         * - Bước 5: Cập nhật metadata -> totalSongs = 2, totalDuration = 240 + 210 = 450.
         * - Bước 6: Trả về thành công.
         */
        String res2 = library.addSongToPlaylist("P01", "S02");
        System.out.println("Kết quả: " + res2);
        
        /* 
         * [MANUAL TRACE] TH3: Thử thêm trùng lặp bài hát S01 vào Playlist P01
         * - Bước 1: Nhận input ("P01", "S01").
         * - Bước 2: Xác thực -> OK.
         * - Bước 3: Kiểm tra trùng -> current = head (S01).
         *           Kiểm tra trùng: current.data.getSongId() ("S01") == "S01" -> TRUE!
         *           Dừng thuật toán, trả về Cảnh báo bài hát đã tồn tại.
         */
        String res3 = library.addSongToPlaylist("P01", "S01");
        System.out.println("Kết quả: " + res3);

        // TH4: Thử thêm bài hát không tồn tại trên hệ thống (S99)
        String res4 = library.addSongToPlaylist("P01", "S99");
        System.out.println("Kết quả: " + res4);

        System.out.println("\n--- TRẠNG THÁI PLAYLIST SAU CÁC THAO TÁC ---");
        playlist.displayPlaylist();

        System.out.println("\n--- THỰC HIỆN THUẬT TOÁN XÁO TRỘN SHUFFLE ---");
        // Thêm S03 để playlist có 3 bài hát rồi xáo trộn
        library.addSongToPlaylist("P01", "S03");
        System.out.println("Trước khi Shuffle:");
        playlist.displayPlaylist();
        
        /*
         * [MANUAL TRACE] THUẬT TOÁN SHUFFLE PLAYLIST (P01 có S01, S02, S03)
         * - B1: Copy sang mảng -> tempArray = [Node(S01), Node(S02), Node(S03)]. n = 3.
         * - B2: Vòng lặp i từ 2 xuống 1:
         *       + Lượt i = 2: Chọn j ngẫu nhiên từ [0,2]. Giả sử j = 0.
         *         Hoán vị tempArray[2] và tempArray[0] -> tempArray = [Node(S03), Node(S02), Node(S01)].
         *       + Lượt i = 1: Chọn j ngẫu nhiên từ [0,1]. Giả sử j = 1.
         *         Hoán vị tempArray[1] và tempArray[1] -> tempArray = [Node(S03), Node(S02), Node(S01)].
         * - B3: Liên kết lại DLL:
         *       + head = tempArray[0] (Node(S03)), head.prev = null.
         *       + i = 0: Node(S03).next = Node(S02); Node(S02).prev = Node(S03).
         *       + i = 1: Node(S02).next = Node(S01); Node(S01).prev = Node(S02).
         *       + tail = tempArray[2] (Node(S01)), tail.next = null.
         * - Kết quả DLL mới: Node(S03) <-> Node(S02) <-> Node(S01).
         */
        playlist.shuffle();
        System.out.println("Sau khi Shuffle:");
        playlist.displayPlaylist();
        
        System.out.println("\n================ TEST COMPLETED SUCCESFULLY ================");
    }
}
