package bg.sofia.uni.fmi.mjt.music.command.executor;

import bg.sofia.uni.fmi.mjt.music.command.Command;
import bg.sofia.uni.fmi.mjt.music.server.repository.InMemoryPlaylistRepository;
import bg.sofia.uni.fmi.mjt.music.server.repository.PlaylistRepository;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CommandExecutorTest {

    private final PlaylistRepository playlistRepository = new InMemoryPlaylistRepository();
    private static final String PLAYLIST_NAME = "playlist1";
    private static final String SONG_NAME = "song1";
    private static final String ARTIST_NAME = "artist1";
    private static final String DURATION = "1";
    private static final String DISCONNECT = "disconnect";
    private static final String INVALID = "invalid";

    @Test
    void testCreatePlaylistCorrectly() {
        String[] args = new String[1];
        args[0] = PLAYLIST_NAME;

        String result = String.format("{\"status\":\"OK\",\"message\":\"Playlist %s created successfully.\"}",
            PLAYLIST_NAME);

        assertEquals(result, CommandExecutor.execute(playlistRepository,
            new Command("create-playlist", args)),
            "When testing create playlist with valid one argument" +
                " should return a message saying everything ended successfully"
        );
    }

    @Test
    void testAddSongCorrectly() {
        testCreatePlaylistCorrectly();

        String[] args = new String[4];
        args[0] = PLAYLIST_NAME;
        args[1] = SONG_NAME;
        args[2] = ARTIST_NAME;
        args[3] = DURATION;

        String result = String.format("{\"status\":\"OK\",\"message\":\"Song %s by %s added successfully.\"}",
            SONG_NAME, ARTIST_NAME);

        assertEquals(result, CommandExecutor.execute(playlistRepository,
                new Command("add-song", args)),
            "When testing adding a song to a playlist with valid arguments" +
                " should return a message saying everything ended successfully"
        );
    }

    @Test
    void testListPlaylistsCorrectly() {
        testCreatePlaylistCorrectly();

        String result = String.format("{\"status\":\"OK\",\"playlists\":[\"%s\"]}", PLAYLIST_NAME);;

        assertEquals(result, CommandExecutor.execute(playlistRepository,
                new Command("list-playlists", new String[0])),
            "When testing listing all playlists with valid arguments (NONE)" +
                " should return a message saying everything ended successfully"
        );
    }

    @Test
    void testGetPlaylistsCorrectly() {
        testCreatePlaylistCorrectly();

        String[] args = new String[1];
        args[0] = PLAYLIST_NAME;

        String result = String.format("{\"status\":\"OK\",\"playlist\":{\"name\":\"%s\",\"songs\":[]}}", PLAYLIST_NAME);;

        assertEquals(result, CommandExecutor.execute(playlistRepository,
                new Command("get-playlist", args)),
            "When testing getting a playlist with valid arguments (ONE)" +
                " should return a message saying everything ended successfully"
        );
    }

    @Test
    void testLikeSongCorrectly() {
        testAddSongCorrectly();

        String[] args = new String[3];
        args[0] = PLAYLIST_NAME;
        args[1] = SONG_NAME;
        args[2] = ARTIST_NAME;

        String result = String.format("{\"status\":\"OK\",\"message\":\"Song %s by %s liked. Likes: %d\"}",
            SONG_NAME, ARTIST_NAME, 1);

        assertEquals(result, CommandExecutor.execute(playlistRepository,
                new Command("like-song", args)),
            "When testing liking a song from a playlist with valid arguments (3)" +
                " should return a message saying everything ended successfully"
        );
    }

    @Test
    void testUnlikeSongCorrectly() {
        testAddSongCorrectly();

        String[] args = new String[3];
        args[0] = PLAYLIST_NAME;
        args[1] = SONG_NAME;
        args[2] = ARTIST_NAME;

        String result = String.format("{\"status\":\"OK\",\"message\":\"Song %s by %s unliked. Likes: %d\"}",
            SONG_NAME, ARTIST_NAME, 0);

        assertEquals(result, CommandExecutor.execute(playlistRepository,
                new Command("unlike-song", args)),
            "When testing unliking a song from a playlist with valid arguments (3)" +
                " should return a message saying everything ended successfully"
        );
    }

    @Test
    void testDisconnectCorrectly() {
        String[] args = new String[1];
        args[0] = DISCONNECT;

        assertEquals(DISCONNECT, CommandExecutor.execute(playlistRepository,
                new Command(DISCONNECT, args)),
            "When testing disconnecting from the server" +
                " should return the string disconnect"
        );
    }

    @Test
    void testInvalidCommandGiven() {
        String[] args = new String[1];
        args[0] = INVALID;

        String result = "{\"status\":\"ERROR\",\"message\":\"Unknown command\"}";

        assertEquals(result, CommandExecutor.execute(playlistRepository,
                new Command(INVALID, args)),
            "When testing command executor with invalid command from the client" +
                " should return a string saying an error has occurred"
        );
    }

}
