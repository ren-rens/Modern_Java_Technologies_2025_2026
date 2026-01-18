package bg.sofia.uni.fmi.mjt.music.command.executor;

import bg.sofia.uni.fmi.mjt.music.server.repository.InMemoryPlaylistRepository;
import bg.sofia.uni.fmi.mjt.music.server.repository.PlaylistRepository;
import bg.sofia.uni.fmi.mjt.music.server.repository.exception.PlaylistAlreadyExistsException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ListPlaylistsExecutorTest {

    private static final String[] ARGS = new String[0];
    private static final String PLAYLIST_NAME = "playlist1";

    @Test
    void testListPlaylistsWithIncorrectArgsSize() {
        PlaylistRepository repository = new InMemoryPlaylistRepository();

        String result = "{\"status\":\"ERROR\",\"message\":\"Usage: list-playlists\"}";

        assertEquals(result, ListPlaylistsExecutor.listPlaylists(repository, new String[1]),
            "When testing list playlists with wrong arguments size" +
                "should return error message");
    }

    @Test
    void testListPlaylistsWithValidData() throws PlaylistAlreadyExistsException {
        PlaylistRepository repository = new InMemoryPlaylistRepository();
        repository.createPlaylist(PLAYLIST_NAME);

        String result = String.format("{\"status\":\"OK\",\"playlists\":[\"%s\"]}", PLAYLIST_NAME);

        assertEquals(result, ListPlaylistsExecutor.listPlaylists(repository, ARGS),
            "When testing list playlists with valid data" +
                "should return OK message");
    }

}
