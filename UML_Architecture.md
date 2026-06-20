# Thiết kế UML & Kiến trúc Hệ thống SoundStream

Chào đại ca! Dưới đây là tài liệu mô tả kiến trúc hệ thống trực quan sử dụng UML (Unified Modeling Language) để đại ca dễ dàng nộp báo cáo cho thầy.

---

## 1. Sơ đồ lớp UML (Class Diagram)
Sơ đồ này mô tả cấu trúc của các thực thể dữ liệu (Models), cấu trúc Danh sách liên kết đôi (Doubly Linked List) và các thành phần nghiệp vụ (Controllers).

```mermaid
classDiagram
    direction TB
    
    class User {
        -String userId
        -String username
        -List~Playlist~ playlists
        +getUserId() String
        +getUsername() String
        +getPlaylists() List~Playlist~
        +addPlaylist(Playlist playlist) void
    }

    class Playlist {
        -String playlistId
        -String name
        -String creationDate
        -Node head
        -Node tail
        -int totalSongs
        -int totalDuration
        +addSong(Song song) boolean
        +shuffle() void
        +displayPlaylist() void
        +getHead() Node
        +getTail() Node
        +getTotalSongs() int
    }

    class Node {
        +Song data
        +Node prev
        +Node next
    }

    class Song {
        -String songId
        -String title
        -String artist
        -String album
        -int duration
        -String genre
        -String fileUrl
        +getSongId() String
        +getTitle() String
        +getFileUrl() String
    }

    class MusicLibrary {
        -HashMap~String, Song~ songMap
        -List~Song~ songList
        -HashMap~String, Playlist~ playlistMap
        -boolean useDatabase
        +loadFromDatabase() void
        +addSongToLibrary(Song song) void
        +addPlaylistToLibrary(String playlistId, Playlist playlist) void
        +addSongToPlaylist(String playlistId, String songId) String
        +searchSongs(String keyword) List~Song~
    }

    class DBContext {
        -String HOST
        -String PORT
        -String DATABASE
        -String USER
        -String PASSWORD
        +getConnection() Connection
    }

    %% Relationships
    User "1" --> "*" Playlist : sở hữu
    Playlist "1" --> "0..1" Node : quản lý qua head/tail
    Node "1" --> "1" Song : chứa dữ liệu
    Node "1" <--> "1" Node : liên kết đôi (prev/next)
    MusicLibrary "1" --> "*" Song : quản lý bộ nhớ tạm
    MusicLibrary "1" --> "*" Playlist : quản lý bộ nhớ tạm
    MusicLibrary ..> DBContext : sử dụng kết nối
```

---

## 2. Kiến trúc thành phần (Component / Layered Architecture)
Mô tả cách phân chia tầng (N-Tier) từ Giao diện $\rightarrow$ Controller $\rightarrow$ Database.

```mermaid
graph TD
    subgraph UI_Layer [Tầng Giao Diện - Presentation]
        JSP[home.jsp / playlist.jsp / search.jsp]
        JS_Player[player.jsp - Trình phát nhạc JS]
    end

    subgraph Controller_Layer [Tầng Điều Khiển - Servlets & Core]
        Servlet[HomeServlet / PlaylistServlet / SearchServlet]
        Core[MusicLibrary.java]
        DB_Conn[DBContext.java]
    end

    subgraph Data_Layer [Tầng Lưu Trữ - Models & SQL Server]
        DLL[Cấu trúc dữ liệu Doubly Linked List]
        MSSQL[(Microsoft SQL Server DB)]
    end

    %% Interactions
    JSP -->|Yêu cầu GET/POST| Servlet
    JS_Player -->|Đọc file âm thanh| JSP
    Servlet -->|Gọi xử lý nghiệp vụ| Core
    Core -->|Kết nối cơ sở dữ liệu| DB_Conn
    DB_Conn -->|Thao tác SQL Query| MSSQL
    Core -->|Nạp dữ liệu vào bộ nhớ| DLL
```

---

## 3. Sơ đồ tuần tự: Quy trình Thêm bài hát & Đồng bộ Database (Sequence Diagram)
Mô tả luồng đi của dữ liệu từ khi đại ca bấm nút thêm bài hát trên giao diện JSP cho đến khi dữ liệu được ghi nhận vào SQL Server.

```mermaid
sequenceDiagram
    autonumber
    actor User as Đại Ca
    participant Browser as Trình duyệt (JSP)
    participant Servlet as PlaylistServlet
    participant Core as MusicLibrary (Controller)
    participant DB as SQL Server (Database)
    participant DLL as Playlist (DLL DLL)

    User->>Browser: Nhấp "+ Thêm vào Playlist"
    Browser->>Servlet: Gửi POST /playlist?action=add
    Servlet->>Core: Gọi addSongToPlaylist(playlistId, songId)
    
    Note over Core: Kiểm tra tính hợp lệ & trùng lặp
    
    Core->>DB: Thực thi INSERT INTO Playlist_Songs
    DB-->>Core: Phản hồi Thành Công (OK)
    
    Core->>DLL: Gọi playlist.addSong(song)
    Note over DLL: Tạo Node mới và nối vào đuôi (Tail)
    
    Core-->>Servlet: Trả về chuỗi thông báo thành công
    Servlet-->>Browser: Chuyển hướng (Redirect) cập nhật giao diện
    Browser-->>User: Hiển thị thông báo thành công & bài hát mới
```
