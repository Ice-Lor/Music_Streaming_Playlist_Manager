package model;

import java.util.ArrayList;
import java.util.Random;

/**
 * Lớp Playlist quản lý danh sách các bài hát sử dụng cấu trúc
 * Danh sách liên kết đôi (Doubly Linked List).
 */
public class Playlist {
    private String playlistId;
    private String name;
    private String creationDate;
    
    // Cấu trúc Danh sách liên kết đôi (Doubly Linked List)
    private Node head;
    private Node tail;
    
    private int totalSongs;
    private int totalDuration;

    public Playlist(String playlistId, String name, String creationDate) {
        this.playlistId = playlistId;
        this.name = name;
        this.creationDate = creationDate;
        this.head = null;
        this.tail = null;
        this.totalSongs = 0;
        this.totalDuration = 0;
    }

    /**
     * THUẬT TOÁN 1: Thêm bài hát vào cuối Playlist (Append to DLL)
     * 
     * ĐỘ PHỨC TẠP:
     * - Thời gian: O(1) do sử dụng con trỏ đuôi 'tail' trực tiếp, không duyệt danh sách.
     * - Bộ nhớ: O(1) bổ trợ (chỉ tạo 1 đối tượng Node mới).
     * 
     * QUY TRÌNH THỰC THI (DESIGN PROCESS):
     * 1. Nhận đối tượng Song đầu vào.
     * 2. Khởi tạo Node mới chứa bài hát (newNode = new Node(song)).
     * 3. Trường hợp danh sách rỗng (head == null):
     *    - Gán head = newNode, tail = newNode.
     * 4. Trường hợp danh sách đã có phần tử:
     *    - Nối liên kết tiếp theo của tail cũ vào node mới (tail.next = newNode).
     *    - Nối liên kết phía trước của node mới về tail cũ (newNode.prev = tail).
     *    - Cập nhật con trỏ tail của Playlist sang node mới (tail = newNode).
     * 5. Cập nhật các thông số quản lý (Metadata):
     *    - Tăng số lượng bài hát (totalSongs++).
     *    - Cộng dồn thời lượng bài hát mới vào tổng thời lượng (totalDuration += duration).
     */
    public boolean addSong(Song song) {
        if (song == null) return false;
        
        Node newNode = new Node(song);
        
        if (head == null) {
            head = newNode;
            tail = newNode;
        } else {
            tail.next = newNode;
            newNode.prev = tail;
            tail = newNode;
        }
        
        totalSongs++;
        totalDuration += song.getDuration();
        return true;
    }

    /**
     * THUẬT TOÁN 2: Xáo trộn Playlist (Fisher-Yates Shuffle)
     * 
     * ĐỘ PHỨC TẠP:
     * - Thời gian: O(n) tuyến tính, tối ưu hóa việc truy cập ngẫu nhiên thay vì xáo trực tiếp trên DLL.
     * - Bộ nhớ: O(n) phụ trợ để lưu trữ mảng tham chiếu tạm thời.
     * 
     * QUY TRÌNH THỰC THI (DESIGN PROCESS):
     * 1. Kiểm tra điều kiện dừng: Nếu playlist có ít hơn hoặc bằng 1 bài hát, kết thúc.
     * 2. Chuyển đổi cấu trúc:
     *    - Duyệt tuần tự qua DLL và đưa địa chỉ của các Node vào một ArrayList tạm thời (tempArray).
     *    - ArrayList giúp truy cập ngẫu nhiên các Node bằng chỉ mục (index) với chi phí O(1).
     * 3. Tiến hành thuật toán Fisher-Yates Shuffle trên tempArray:
     *    - Vòng lặp chạy từ cuối mảng (i = n - 1) giảm dần về 1.
     *    - Ở mỗi lượt, chọn ngẫu nhiên một chỉ mục j trong khoảng từ [0, i].
     *    - Tráo đổi vị trí của phần tử tại i và j trong tempArray.
     * 4. Tái lập cấu trúc liên kết đôi của DLL:
     *    - Gán head = tempArray.get(0), head.prev = null.
     *    - Duyệt từ 0 đến n-2 để nối liên kết xuôi (next) và ngược (prev) giữa các Node liền kề.
     *    - Gán tail = tempArray.get(n-1), tail.next = null.
     */
    public void shuffle() {
        if (totalSongs <= 1) return; // Không cần xáo trộn

        // Bước 1: Đưa các Node vào một ArrayList tạm thời
        ArrayList<Node> tempArray = new ArrayList<>();
        Node current = head;
        while (current != null) {
            tempArray.add(current);
            current = current.next;
        }

        // Bước 2: Tiến hành Fisher-Yates Shuffle
        int n = tempArray.size();
        Random rand = new Random();
        for (int i = n - 1; i > 0; i--) {
            int j = rand.nextInt(i + 1);
            
            // Tráo đổi vị trí
            Node temp = tempArray.get(i);
            tempArray.set(i, tempArray.get(j));
            tempArray.set(j, temp);
        }

        // Bước 3: Cài đặt lại các liên kết đôi
        head = tempArray.get(0);
        head.prev = null;
        
        for (int i = 0; i < n - 1; i++) {
            tempArray.get(i).next = tempArray.get(i + 1);
            tempArray.get(i + 1).prev = tempArray.get(i);
        }
        
        tail = tempArray.get(n - 1);
        tail.next = null;
    }

    // In danh sách phát phục vụ kiểm tra
    public void displayPlaylist() {
        System.out.println("Playlist: " + name + " (Tổng số bài: " + totalSongs + ", Tổng thời lượng: " + totalDuration + "s)");
        Node current = head;
        while (current != null) {
            System.out.print(current.data + " ");
            if (current.next != null) System.out.print("<-> ");
            current = current.next;
        }
        System.out.println();
    }

    // Getters
    public String getPlaylistId()   { return playlistId; }
    public String getName()         { return name; }
    public String getCreationDate() { return creationDate; }
    public Node   getHead()         { return head; }
    public Node   getTail()         { return tail; }
    public int    getTotalSongs()   { return totalSongs; }
    public int    getTotalDuration(){ return totalDuration; }
}
