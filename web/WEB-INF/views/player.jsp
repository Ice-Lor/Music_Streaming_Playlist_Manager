<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!-- BOTTOM PLAYER -->
<div class="bottom-player" id="bottomPlayer">
    <div class="player-left">
        <div class="player-track-icon">🎵</div>
        <div class="player-track-info">
            <div class="player-track-title" id="playerTitle">Chưa chọn bài hát</div>
            <div class="player-track-artist" id="playerArtist">Nghệ sĩ</div>
        </div>
    </div>
    <div class="player-center">
        <div class="player-controls">
            <button class="player-btn btn-prev" id="playerPrev" title="Bài trước" onclick="playPrevious()">⏮</button>
            <button class="player-btn btn-play" id="playerPlay" title="Phát/Tạm dừng">▶</button>
            <button class="player-btn btn-next" id="playerNext" title="Bài tiếp theo" onclick="playNext()">⏭</button>
        </div>
        <div class="player-progress-area">
            <span class="player-time-current" id="playerTimeCurrent">0:00</span>
            <input type="range" class="player-progress" id="playerProgress" min="0" value="0" step="1">
            <span class="player-time-total" id="playerTimeTotal">0:00</span>
        </div>
    </div>
    <div class="player-right">
        <div class="player-volume-area">
            <span>🔊</span>
            <input type="range" class="player-volume" id="playerVolume" min="0" max="1" value="0.8" step="0.05">
        </div>
    </div>
    <audio id="mainAudio" style="display: none;"></audio>
</div>

<script>
    const audio = document.getElementById('mainAudio');
    const playBtn = document.getElementById('playerPlay');
    const progress = document.getElementById('playerProgress');
    const timeCurrent = document.getElementById('playerTimeCurrent');
    const timeTotal = document.getElementById('playerTimeTotal');
    const volume = document.getElementById('playerVolume');
    
    const playerTitle = document.getElementById('playerTitle');
    const playerArtist = document.getElementById('playerArtist');

    let isPlaying = false;
    let playlistSongs = []; // Danh sách các bài hát trong trang hiện tại để Next/Prev
    let currentSongIndex = -1;

    // Quét toàn bộ bài hát trên bảng hiện tại để lưu danh sách phát
    function initPlaylistSongs() {
        playlistSongs = [];
        const rows = document.querySelectorAll('.song-table tbody tr');
        rows.forEach((row, index) => {
            const playButton = row.querySelector('.btn-play-song');
            if (playButton) {
                const url = playButton.getAttribute('data-url');
                const local = playButton.getAttribute('data-local');
                const title = playButton.getAttribute('data-title');
                const artist = playButton.getAttribute('data-artist');
                playlistSongs.push({ url, local, title, artist, index });
                
                // Gán sự kiện click cho dòng
                playButton.onclick = function() {
                    playAudioAtIndex(index);
                };
            }
        });
    }

    function playAudioAtIndex(index) {
        if (index < 0 || index >= playlistSongs.length) return;
        currentSongIndex = index;
        const song = playlistSongs[index];
        
        // Cập nhật trạng thái active trên bảng
        const rows = document.querySelectorAll('.song-table tbody tr');
        rows.forEach(r => r.classList.remove('active-row'));
        if (rows[index]) {
            rows[index].classList.add('active-row');
        }

        let isFallback = false;
        audio.src = song.url;
        playerTitle.innerText = song.title;
        playerArtist.innerText = song.artist;
        
        // Cơ chế tự động hồi phục khi có lỗi mạng/chết link
        audio.onerror = function() {
            if (!isFallback && song.local) {
                console.warn("Không tải được nhạc online, tự động phát file local dự phòng!");
                isFallback = true;
                audio.src = song.local;
                audio.play().then(() => {
                    isPlaying = true;
                    playBtn.innerText = '⏸';
                }).catch(e => console.error("Không phát được cả file local dự phòng:", e));
            }
        };
        
        audio.play().then(() => {
            isPlaying = true;
            playBtn.innerText = '⏸';
        }).catch(err => {
            console.warn("Phát nhạc online bị từ chối, thử dùng file local:", err);
            if (!isFallback && song.local) {
                isFallback = true;
                audio.src = song.local;
                audio.play().then(() => {
                    isPlaying = true;
                    playBtn.innerText = '⏸';
                }).catch(e => console.error("Lỗi khi phát file local:", e));
            }
        });
    }

    function playNext() {
        if (playlistSongs.length === 0) return;
        let nextIndex = currentSongIndex + 1;
        if (nextIndex >= playlistSongs.length) {
            nextIndex = 0; // Quay lại bài đầu tiên
        }
        playAudioAtIndex(nextIndex);
    }

    function playPrevious() {
        if (playlistSongs.length === 0) return;
        let prevIndex = currentSongIndex - 1;
        if (prevIndex < 0) {
            prevIndex = playlistSongs.length - 1; // Nhảy xuống bài cuối
        }
        playAudioAtIndex(prevIndex);
    }

    // Nút Play/Pause ở Bottom Player
    playBtn.addEventListener('click', () => {
        if (!audio.src) {
            if (playlistSongs.length > 0) {
                playAudioAtIndex(0);
            }
            return;
        }
        if (isPlaying) {
            audio.pause();
            isPlaying = false;
            playBtn.innerText = '▶';
        } else {
            audio.play();
            isPlaying = true;
            playBtn.innerText = '⏸';
        }
    });

    // Cập nhật thanh trượt & thời lượng
    audio.addEventListener('timeupdate', () => {
        if (isNaN(audio.duration)) return;
        
        const curTime = audio.currentTime;
        const totalDuration = audio.duration;
        
        progress.max = Math.floor(totalDuration);
        progress.value = Math.floor(curTime);
        
        timeCurrent.innerText = formatTime(curTime);
        timeTotal.innerText = formatTime(totalDuration);
    });

    // Tua nhạc
    progress.addEventListener('input', () => {
        audio.currentTime = progress.value;
    });

    // Thay đổi âm lượng
    volume.addEventListener('input', () => {
        audio.volume = volume.value;
    });

    function formatTime(seconds) {
        const min = Math.floor(seconds / 60);
        const sec = Math.floor(seconds % 60);
        return min + ":" + (sec < 10 ? "0" : "") + sec;
    }
    
    // Hết bài tự động chuyển sang bài tiếp theo
    audio.addEventListener('ended', () => {
        playNext();
    });

    // Khởi chạy khi DOM load xong
    document.addEventListener('DOMContentLoaded', () => {
        initPlaylistSongs();
    });
</script>

<style>
    .active-row {
        background: rgba(124, 58, 237, 0.15) !important;
        border-left: 4px solid var(--accent-light);
    }
</style>
