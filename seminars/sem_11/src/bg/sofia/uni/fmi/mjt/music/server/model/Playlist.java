package bg.sofia.uni.fmi.mjt.music.server.model;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class Playlist {

    public Playlist(String name, Map<Song, Integer> songs) {
        this.name = name;
        this.songs = new HashMap<>(songs);
    }

    /*
    public String getName() {
        return this.name;
    }
     */

    public Map<Song, Integer> getSongs() {
        return this.songs;
    }

    public void addSong(Song song) {
        this.songs.put(song, 0);
    }

    public String toJson() {
        String songsJson = songs.entrySet().stream()
            .map(entry -> {
                Song song = entry.getKey();
                int likes = entry.getValue();
                return String.format("{\"title\":\"%s\",\"artist\":\"%s\",\"duration\":%d,\"likes\":%d}",
                    song.title(), song.artist(), song.duration(), likes);
            })
            .collect(Collectors.joining(","));

        return String.format("{\"name\":\"%s\",\"songs\":[%s]}", name, songsJson);
    }

    /*
    @Override
    public String toString() {
        return "playlist:{" +
            "name='" + name + "', songs=" + songs + '}';
    }
     */

    private final String name;
    private final Map<Song, Integer> songs;

}