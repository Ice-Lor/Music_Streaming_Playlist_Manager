-- Script khởi tạo cơ sở dữ liệu SQL Server cho SoundStream
CREATE DATABASE SoundStreamDB;
GO

USE SoundStreamDB;
GO

-- Bảng lưu thông tin bài hát
CREATE TABLE Songs (
    songId VARCHAR(50) PRIMARY KEY,
    title NVARCHAR(100) NOT NULL,
    artist NVARCHAR(100) NOT NULL,
    album NVARCHAR(100),
    duration INT NOT NULL, -- thời lượng tính bằng giây
    genre NVARCHAR(50),
    fileUrl VARCHAR(255) NOT NULL -- đường dẫn tệp âm thanh (ví dụ: assets/songs/song1.mp3)
);

-- Bảng lưu thông tin người dùng
CREATE TABLE Users (
    userId VARCHAR(50) PRIMARY KEY,
    username NVARCHAR(100) NOT NULL
);

-- Bảng lưu thông tin Playlist
CREATE TABLE Playlists (
    playlistId VARCHAR(50) PRIMARY KEY,
    name NVARCHAR(100) NOT NULL,
    creationDate VARCHAR(50) NOT NULL,
    userId VARCHAR(50) FOREIGN KEY REFERENCES Users(userId) ON DELETE CASCADE
);

-- Bảng trung gian Playlist_Songs lưu danh sách bài hát trong Playlist
CREATE TABLE Playlist_Songs (
    playlistId VARCHAR(50) FOREIGN KEY REFERENCES Playlists(playlistId) ON DELETE CASCADE,
    songId VARCHAR(50) FOREIGN KEY REFERENCES Songs(songId) ON DELETE CASCADE,
    PRIMARY KEY (playlistId, songId)
);

-- Chèn dữ liệu mẫu
INSERT INTO Users (userId, username) VALUES ('U01', N'Dai Ca Music');

INSERT INTO Songs (songId, title, artist, album, duration, genre, fileUrl) VALUES
('S01', 'Chung Ta Cua Tuong Lai', 'Son Tung M-TP', 'Sky Tour', 240, 'Pop', 'assets/songs/song1.mp3'),
('S02', 'Hoa Hai Duong', 'Jack', 'Nhung Bong Hoa', 210, 'Pop/Ballad', 'assets/songs/song2.mp3'),
('S03', 'Waiting For You', 'MONO', '22', 205, 'Synth-Pop', 'assets/songs/song3.mp3'),
('S04', 'Sau Loi Tu Syn', 'Suni Ha Linh', 'Single', 195, 'R&B', 'assets/songs/song4.mp3'),
('S05', 'Biet On Cuoc Doi', 'Ha Anh Tuan', 'Best Of', 230, 'Ballad', 'assets/songs/song5.mp3'),
('S06', 'Tu Nay', 'Ha Anh Tuan', 'Best Of', 220, 'Acoustic', 'assets/songs/song6.mp3');

INSERT INTO Playlists (playlistId, name, creationDate, userId) VALUES
('P01', 'Nhac Hot 2026', '07/06/2026', 'U01'),
('P02', 'Acoustic Chill', '07/06/2026', 'U01');

INSERT INTO Playlist_Songs (playlistId, songId) VALUES
('P01', 'S01'),
('P01', 'S02'),
('P01', 'S03'),
('P02', 'S05'),
('P02', 'S06');
