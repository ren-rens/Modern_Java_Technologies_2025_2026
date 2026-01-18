package bg.sofia.uni.fmi.mjt.music.command.executor;

import bg.sofia.uni.fmi.mjt.music.server.repository.InMemoryPlaylistRepository;
import bg.sofia.uni.fmi.mjt.music.server.repository.PlaylistRepository;
import bg.sofia.uni.fmi.mjt.music.server.repository.exception.PlaylistAlreadyExistsException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CreatePlaylistExecutorTest {

    private static final String PLAYLIST_NAME = "playlist1";

    private static final String[] ARGS = new String[1];

    @BeforeAll
    static void setUp() {
        ARGS[0] = PLAYLIST_NAME;
    }

    @Test
    void testCreatePlaylistWithIncorrectArgsSize() {
        PlaylistRepository repository = new InMemoryPlaylistRepository();
        String[] wrongArgs = new String[0];

        String result = "{\"status\":\"ERROR\",\"message\":\"Usage: create-playlist <playlist_name>\"}";

        assertEquals(result, CreatePlaylistExecutor.createPlaylist(repository, wrongArgs),
            "When trying to create a playlist with valid data" +
                "should return a message saying everything is OK");
    }

    @Test
    void testCreatePlaylistWithRepositoryThrowingPlaylistAlreadyExistsException()
        throws PlaylistAlreadyExistsException {
        // set up
        PlaylistRepository repository = new InMemoryPlaylistRepository();
        repository.createPlaylist(PLAYLIST_NAME);

        String result = String.format("{\"status\":\"ERROR\",\"message\":\"Playlist %s already exists.\"}",
            PLAYLIST_NAME);

        assertEquals(result, CreatePlaylistExecutor.createPlaylist(repository, ARGS),
            "When trying to create a playlist with a name that already exists in the system" +
                "should throw PlaylistAlreadyExistsException and hides in it an error message");
    }

    @Test
    void testCreatePlaylistWithValidData() {
        PlaylistRepository repository = new InMemoryPlaylistRepository();

        String result = String.format("{\"status\":\"OK\",\"message\":\"Playlist %s created successfully.\"}",
            PLAYLIST_NAME);

        assertEquals(result, CreatePlaylistExecutor.createPlaylist(repository, ARGS),
            "When trying to create a playlist with valid data" +
                "should return a message saying everything is OK");
    }

}