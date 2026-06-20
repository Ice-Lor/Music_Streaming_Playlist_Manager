package controller;

import model.Song;
import model.Playlist;
import java.util.ArrayList;
import java.util.List;

/**
 * PerformanceTestRunner: Chạy các thực nghiệm đo lường hiệu năng thuật toán
 * Đo thời gian thực thi (nanoseconds) của Tra cứu O(1), Tìm kiếm O(n) và Shuffle O(n).
 */
public class PerformanceTestRunner {

    public static void main(String[] args) {
        System.out.println("==================================================================");
        System.out.println("   THỰC NGHIỆM ĐO HIỆU NĂNG THUẬT TOÁN - MUSIC PLAYLIST MANAGER   ");
        System.out.println("==================================================================");

        // Các kích thước dữ liệu cần thử nghiệm (N)
        int[] dataSizes = {1000, 10000, 100000, 500000};

        for (int N : dataSizes) {
            System.out.printf("\n--- THỬ NGHIỆM VỚI DỮ LIỆU N = %,d BÀI HÁT ---\n", N);

            // 1. Khởi tạo dữ liệu ngẫu nhiên
            MusicLibrary library = new MusicLibrary();
            // Tắt DB để đo thuần hiệu năng thuật toán cấu trúc dữ liệu
            // (MusicLibrary tự fallback sang bộ nhớ tạm khi chạy TestRunner độc lập)
            
            List<Song> testSongs = new ArrayList<>();
            for (int i = 1; i <= N; i++) {
                String id = "S" + i;
                Song s = new Song(id, "Song Title " + i, "Artist " + i, "Album " + i, 200, "Pop");
                library.addSongToLibrary(s);
                testSongs.add(s);
            }

            // Tạo playlist có N bài hát để test Shuffle
            Playlist playlist = new Playlist("P_TEST", "Test Playlist", "20/06/2026");
            for (Song s : testSongs) {
                playlist.addSong(s);
            }

            // Target song ở cuối danh sách để đo Worst-case của tìm kiếm tuyến tính
            String targetId = "S" + N;
            String searchKeyword = "Artist " + N;

            // --- THỰC NGHIỆM 1: Tra cứu HashMap O(1) ---
            long startHash = System.nanoTime();
            Song foundHash = library.getSongById(targetId);
            long endHash = System.nanoTime();
            long durationHash = endHash - startHash;

            // --- THỰC NGHIỆM 2: Tìm kiếm tuyến tính O(N) ---
            long startLinear = System.nanoTime();
            List<Song> foundLinear = library.searchSongs(searchKeyword);
            long endLinear = System.nanoTime();
            long durationLinear = endLinear - startLinear;

            // --- THỰC NGHIỆM 3: Xáo trộn Fisher-Yates Shuffle O(N) ---
            long startShuffle = System.nanoTime();
            playlist.shuffle();
            long endShuffle = System.nanoTime();
            long durationShuffle = endShuffle - startShuffle;

            // In kết quả
            System.out.printf("1. Tra cứu HashMap O(1)    : %,12d ns  (Tìm thấy: %s)\n", 
                    durationHash, (foundHash != null));
            System.out.printf("2. Tìm kiếm tuyến tính O(N): %,12d ns  (Kết quả: %d bài)\n", 
                    durationLinear, foundLinear.size());
            System.out.printf("3. Fisher-Yates Shuffle O(N): %,12d ns  (Độ dài DLL: %d)\n", 
                    durationShuffle, playlist.getTotalSongs());
        }
        System.out.println("\n==================================================================");
        System.out.println("Thử nghiệm hoàn tất! Đại ca có thể copy kết quả trên vào báo cáo.");
        System.out.println("==================================================================");
    }
}
