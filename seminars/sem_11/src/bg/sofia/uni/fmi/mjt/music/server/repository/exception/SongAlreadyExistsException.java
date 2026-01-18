package bg.sofia.uni.fmi.mjt.music.server.repository.exception;

public class SongAlreadyExistsException extends Exception {

    public SongAlreadyExistsException(String s) {
        super(s);
    }

    public SongAlreadyExistsException(String s, Throwable cause) {
        super(s, cause);
    }

}
