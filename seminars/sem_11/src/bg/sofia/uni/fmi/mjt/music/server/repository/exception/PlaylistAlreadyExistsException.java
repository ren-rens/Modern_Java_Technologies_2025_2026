package bg.sofia.uni.fmi.mjt.music.server.repository.exception;

public class PlaylistAlreadyExistsException extends Exception {

    public PlaylistAlreadyExistsException(String s) {
        super(s);
    }

    public PlaylistAlreadyExistsException(String s, Throwable cause) {
        super(s, cause);
    }

}
