package bg.sofia.uni.fmi.mjt.music.server.repository.exception;

public class SongNotFoundException extends Exception {

    public SongNotFoundException(String s) {
        super(s);
    }

    public SongNotFoundException(String s, Throwable cause) {
        super(s, cause);
    }

}
