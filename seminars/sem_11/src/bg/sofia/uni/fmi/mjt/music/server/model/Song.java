package bg.sofia.uni.fmi.mjt.music.server.model;

import java.util.Objects;

public record Song(String title, String artist, int duration) {

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Song song = (Song) o;
        return Objects.equals(title, song.title) && Objects.equals(artist, song.artist);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, artist);
    }

    /*
    @Override
    public String toString() {
        return "Song{" +
            "title='" + title + '\'' +
            ", artist='" + artist + '\'' +
            ", duration=" + duration +
            '}';
    }
     */
}