package bg.sofia.uni.fmi.mjt.music.server.repository;

import bg.sofia.uni.fmi.mjt.music.server.model.Playlist;
import bg.sofia.uni.fmi.mjt.music.server.model.Song;
import bg.sofia.uni.fmi.mjt.music.server.repository.exception.PlaylistAlreadyExistsException;
import bg.sofia.uni.fmi.mjt.music.server.repository.exception.PlaylistNotFoundException;
import bg.sofia.uni.fmi.mjt.music.server.repository.exception.SongAlreadyExistsException;
import bg.sofia.uni.fmi.mjt.music.server.repository.exception.SongNotFoundException;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class InMemoryPlaylistRepository implements PlaylistRepository {

    public InMemoryPlaylistRepository() {
        this.playlists = new HashMap<>();
    }

    @Override
    public void createPlaylist(String playlistName) throws PlaylistAlreadyExistsException {
        if (this.playlists.containsKey(playlistName)) {
            throw new PlaylistAlreadyExistsException("Playlist with the given name already exists");
        }

        Playlist playlist = new Playlist(playlistName, new HashMap<>());
        this.playlists.put(playlistName, playlist);
    }

    @Override
    public Song addSong(String playlistName, String songTitle, String artistName, int duration)
        throws PlaylistNotFoundException, SongAlreadyExistsException {
        if (!this.playlists.containsKey(playlistName)) {
            throw new PlaylistNotFoundException("Playlist with the given name to add a song in does not exist");
        }

        if (duration <= 0 ||
            songTitle == null || songTitle.isBlank() ||
            artistName == null || artistName.isBlank()) {
            return null;
        }

        Song song = new Song(songTitle, artistName, duration);
        Playlist playlist = this.playlists.get(playlistName);

        if (songIsInPlaylist(playlist.getSongs(), song) != null) {
            throw new SongAlreadyExistsException("Song with those title and artist already exists in this playlist");
        }

        playlist.addSong(song);
        return song;
    }

    private Song songIsInPlaylist(Map<Song, Integer> songs, Song song) {
        for (Map.Entry<Song, Integer> entry : songs.entrySet()) {
            Song currSong = entry.getKey();
            if (song.equals(currSong)) {
                return currSong;
            }
        }

        return null;
    }

    @Override
    public int likeSong(String playlistName, String songTitle, String artistName)
        throws PlaylistNotFoundException, SongNotFoundException {
        if (!this.playlists.containsKey(playlistName)) {
            throw new PlaylistNotFoundException("Playlist with given name does not exist");
        }

        Playlist playlist = this.playlists.get(playlistName);
        Song currSong = new Song(songTitle, artistName, 1);

        Song songInPlaylist = songIsInPlaylist(playlist.getSongs(), currSong);
        if (songInPlaylist == null) {
            throw new SongNotFoundException("Song does not exist in this playlist");
        }

        int likesForCurrSong = playlist.getSongs().get(songInPlaylist);
        playlist.getSongs().put(songInPlaylist, likesForCurrSong + 1);

        return likesForCurrSong + 1;
    }

    @Override
    public int unlikeSong(String playlistName, String songTitle, String artistName)
        throws PlaylistNotFoundException, SongNotFoundException {
        if (!this.playlists.containsKey(playlistName)) {
            throw new PlaylistNotFoundException("Playlist with given name does not exist");
        }

        Playlist playlist = this.playlists.get(playlistName);
        Song currSong = new Song(songTitle, artistName, 1);

        Song songInPlaylist = songIsInPlaylist(playlist.getSongs(), currSong);
        if (songInPlaylist == null) {
            throw new SongNotFoundException("Song does not exist in this playlist");
        }

        int likesForCurrSong = playlist.getSongs().get(songInPlaylist);
        if (likesForCurrSong == 0) {
            return likesForCurrSong;
        }

        playlist.getSongs().put(songInPlaylist, likesForCurrSong - 1);

        return likesForCurrSong - 1;
    }

    @Override
    public Collection<String> getAllPlaylists() {
        return this.playlists
            .keySet()
            .stream()
            .toList();
    }

    @Override
    public Playlist getPlaylist(String playlistName) throws PlaylistNotFoundException {
        if (!this.playlists.containsKey(playlistName)) {
            throw new PlaylistNotFoundException("Playlist with the given name does not exist");
        }

        return this.playlists.get(playlistName);
    }

    private final Map<String, Playlist> playlists;

}