package bg.sofia.uni.fmi.mjt.music.command.executor;

import bg.sofia.uni.fmi.mjt.music.server.repository.InMemoryPlaylistRepository;
import bg.sofia.uni.fmi.mjt.music.server.repository.PlaylistRepository;
import bg.sofia.uni.fmi.mjt.music.server.repository.exception.PlaylistAlreadyExistsException;
import bg.sofia.uni.fmi.mjt.music.server.repository.exception.PlaylistNotFoundException;
import bg.sofia.uni.fmi.mjt.music.server.repository.exception.SongAlreadyExistsException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AddSongsExecutorTest {

    private static final String PLAYLIST_NAME = "playlist1";
    private static final String SONG_NAME = "song1";
    private static final String ARTIST_NAME = "artist1";
    private static final String DURATION = "2";

    private static final String[] ARGS = new String[4];

    @BeforeEach
    void setUp() {
        ARGS[0] = PLAYLIST_NAME;
        ARGS[1] = SONG_NAME;
        ARGS[2] = ARTIST_NAME;
        ARGS[3] = DURATION;
    }

    @Test
    void testAddSongsWithIncorrectArgsSize() {
        PlaylistRepository repository = new InMemoryPlaylistRepository();

        String[] wrongArgs = new String[0];
        String result = "{\"status\":\"ERROR\",\"message\":" +
            "\"Usage: add-song <playlist_name> <song_title> <artist_name> <duration>\"}";;

        assertEquals(result, AddSongsExecutor.addSongs(repository, wrongArgs),
            "When testing add song with wrong arguments size" +
                "should return error message");
    }

    @Test
    void testAddSongsWithIncorrectDurationNotNumber() {
        PlaylistRepository repository = new InMemoryPlaylistRepository();

        ARGS[3] = "DURATION";
        String result = "{\"status\":\"ERROR\",\"message\":\"Duration must be a positive integer\"}";

        assertEquals(result, AddSongsExecutor.addSongs(repository, ARGS),
            "When testing add song with wrong duration (non-number)" +
                "should return error message");
    }

    @Test
    void testAddSongsWithIncorrectDurationNotPositive() {
        PlaylistRepository repository = new InMemoryPlaylistRepository();

        ARGS[3] = "-1";
        String result = "{\"status\":\"ERROR\",\"message\":\"Duration must be a positive integer\"}";

        assertEquals(result, AddSongsExecutor.addSongs(repository, ARGS),
            "When testing add song with wrong duration (non-positive)" +
                "should return error message");
    }

    @Test
    void testAddSongsWithPlaylistNotFoundException() {
        PlaylistRepository repository = new InMemoryPlaylistRepository();

        String result = String.format("{\"status\":\"ERROR\",\"message\":\"Playlist %s does not exist.\"}",
            PLAYLIST_NAME);

        assertEquals(result, AddSongsExecutor.addSongs(repository, ARGS),
            "When testing add song with a playlist that has not been created yet" +
                "should return error message");
    }

    @Test
    void testAddSongsWithSongAlreadyExistsException()
        throws PlaylistAlreadyExistsException, SongAlreadyExistsException, PlaylistNotFoundException {
        PlaylistRepository repository = new InMemoryPlaylistRepository();
        repository.createPlaylist(PLAYLIST_NAME);
        repository.addSong(PLAYLIST_NAME, SONG_NAME, ARTIST_NAME, 1);

        String result = String.format("{\"status\":\"ERROR\",\"message\":\"Song %s by %s already exists in playlist %s.\"}",
            SONG_NAME, ARTIST_NAME, PLAYLIST_NAME);

        assertEquals(result, AddSongsExecutor.addSongs(repository, ARGS),
            "When testing add song with a song that has already been added to the playlist" +
                "should return error message");
    }

    @Test
    void testAddSongsWithValidData()
        throws PlaylistAlreadyExistsException {
        PlaylistRepository repository = new InMemoryPlaylistRepository();
        repository.createPlaylist(PLAYLIST_NAME);

        String result = String.format("{\"status\":\"OK\",\"message\":\"Song %s by %s added successfully.\"}",
            SONG_NAME, ARTIST_NAME);

        assertEquals(result, AddSongsExecutor.addSongs(repository, ARGS),
            "When testing add song with valid data" +
                "should return OK message");
    }

    @Test
    void testAddSongsWithNullSongs()
        throws PlaylistAlreadyExistsException {
        PlaylistRepository repository = new InMemoryPlaylistRepository();
        repository.createPlaylist(PLAYLIST_NAME);

        ARGS[1] = null;

        String result = "{\"status\":\"ERROR\",\"message\":" +
            "\"Invalid input for duration OR song name OR artists\"}";

        assertEquals(result, AddSongsExecutor.addSongs(repository, ARGS),
            "When testing add song with NULL song" +
                "should return OK message");
    }

    @Test
    void testAddSongsWithBlankSongs()
        throws PlaylistAlreadyExistsException {
        PlaylistRepository repository = new InMemoryPlaylistRepository();
        repository.createPlaylist(PLAYLIST_NAME);

        ARGS[1] = "    ";

        String result = "{\"status\":\"ERROR\",\"message\":" +
            "\"Invalid input for duration OR song name OR artists\"}";

        assertEquals(result, AddSongsExecutor.addSongs(repository, ARGS),
            "When testing add song with BLANK song" +
                "should return OK message");
    }

    @Test
    void testAddSongsWithNullArtist()
        throws PlaylistAlreadyExistsException {
        PlaylistRepository repository = new InMemoryPlaylistRepository();
        repository.createPlaylist(PLAYLIST_NAME);

        ARGS[2] = null;

        String result = "{\"status\":\"ERROR\",\"message\":" +
            "\"Invalid input for duration OR song name OR artists\"}";

        assertEquals(result, AddSongsExecutor.addSongs(repository, ARGS),
            "When testing add song with NULL artists" +
                "should return OK message");
    }

    @Test
    void testAddSongsWithBlankArtist()
        throws PlaylistAlreadyExistsException {
        PlaylistRepository repository = new InMemoryPlaylistRepository();
        repository.createPlaylist(PLAYLIST_NAME);

        ARGS[2] = "   ";

        String result = "{\"status\":\"ERROR\",\"message\":" +
            "\"Invalid input for duration OR song name OR artists\"}";

        assertEquals(result, AddSongsExecutor.addSongs(repository, ARGS),
            "When testing add song with BLANK artists" +
                "should return OK message");
    }

}
