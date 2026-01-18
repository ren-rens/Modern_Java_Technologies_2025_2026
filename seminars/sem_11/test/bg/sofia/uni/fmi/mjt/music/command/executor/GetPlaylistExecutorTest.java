package bg.sofia.uni.fmi.mjt.music.command.executor;

import bg.sofia.uni.fmi.mjt.music.server.repository.InMemoryPlaylistRepository;
import bg.sofia.uni.fmi.mjt.music.server.repository.PlaylistRepository;
import bg.sofia.uni.fmi.mjt.music.server.repository.exception.PlaylistAlreadyExistsException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GetPlaylistExecutorTest {

    private static final String PLAYLIST_NAME = "playlist1";
    private static final String[] ARGS = new String[1];

    @BeforeAll
    static void setUp() {
        ARGS[0] = PLAYLIST_NAME;
    }

    @Test
    void testGetPlaylistWithIncorrectArgumentsSize() {
        PlaylistRepository repository = new InMemoryPlaylistRepository();

        String[] wrongArgs = new String[0];
        String result = "{\"status\":\"ERROR\",\"message\":\"Usage: get-playlist <playlist_name>\"}";;

        assertEquals(result, GetPlaylistExecutor.getPlaylist(repository, wrongArgs),
            "When testing get playlist with wrong args size" +
                "should return error message");
    }

    @Test
    void testGetPlaylistWithPlaylistNameThatDoesNotExist() {
        PlaylistRepository repository = new InMemoryPlaylistRepository();

        String result = String.format("{\"status\":\"ERROR\",\"message\":\"Playlist %s does not exist.\"}",
            PLAYLIST_NAME);

        assertEquals(result, GetPlaylistExecutor.getPlaylist(repository, ARGS),
            "When testing get playlist with playlist that has not been created" +
                "should return error message");
    }

    @Test
    void testGetPlaylistWithValidData() throws PlaylistAlreadyExistsException {
        PlaylistRepository repository = new InMemoryPlaylistRepository();
        repository.createPlaylist(PLAYLIST_NAME);

        String result = String.format("{\"status\":\"OK\",\"playlist\":{\"name\":\"%s\",\"songs\":[]}}",
            PLAYLIST_NAME);

        assertEquals(result, GetPlaylistExecutor.getPlaylist(repository, ARGS),
            "When testing get playlist with valid data" +
                "should return OK message");
    }

}
