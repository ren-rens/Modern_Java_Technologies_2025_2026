package bg.sofia.uni.fmi.mjt.music.server.repository.exception;

public class PlaylistNotFoundException extends Exception {

    public PlaylistNotFoundException(String s) {
        super(s);
    }

    public PlaylistNotFoundException(String s, Throwable cause) {
        super(s, cause);
    }

}
