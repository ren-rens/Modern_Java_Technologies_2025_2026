package bg.sofia.uni.fmi.mjt.music.command.executor;

import bg.sofia.uni.fmi.mjt.music.server.repository.InMemoryPlaylistRepository;
import bg.sofia.uni.fmi.mjt.music.server.repository.PlaylistRepository;
import bg.sofia.uni.fmi.mjt.music.server.repository.exception.PlaylistAlreadyExistsException;
import bg.sofia.uni.fmi.mjt.music.server.repository.exception.PlaylistNotFoundException;
import bg.sofia.uni.fmi.mjt.music.server.repository.exception.SongAlreadyExistsException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LikeSongExecutorTest {

    private static final String PLAYLIST_NAME = "playlist1";
    private static final String SONG_NAME = "song1";
    private static final String ARTIST_NAME = "artist1";

    private static final String[] ARGS = new String[3];

    @BeforeEach
    void setUp() {
        ARGS[0] = PLAYLIST_NAME;
        ARGS[1] = SONG_NAME;
        ARGS[2] = ARTIST_NAME;
    }

    @Test
    void testLikeSongsWithIncorrectArgsSize() {
        PlaylistRepository repository = new InMemoryPlaylistRepository();

        String[] wrongArgs = new String[0];
        String result = "{\"status\":\"ERROR\",\"message\":\"Usage: like-song <playlist_name> <song_title> <artist_name>\"}";

        assertEquals(result, LikeSongExecutor.likeSong(repository, wrongArgs),
            "When testing like song with wrong arguments size" +
                "should return error message");
    }

    @Test
    void testLikeSongsWithPlaylistNotFoundException() {
        PlaylistRepository repository = new InMemoryPlaylistRepository();

        String result = String.format("{\"status\":\"ERROR\",\"message\":\"Playlist %s does not exist.\"}",
            PLAYLIST_NAME);

        assertEquals(result, LikeSongExecutor.likeSong(repository, ARGS),
            "When testing like song with a playlist that has not been created yet" +
                "should return error message");
    }

    @Test
    void testLikeSongsWithSongNotFoundException()
        throws PlaylistAlreadyExistsException {
        PlaylistRepository repository = new InMemoryPlaylistRepository();
        repository.createPlaylist(PLAYLIST_NAME);

        String result = String.format("{\"status\":\"ERROR\",\"message\":\"Song %s by %s does not exist in playlist %s.\"}",
            SONG_NAME, ARTIST_NAME, PLAYLIST_NAME);

        assertEquals(result, LikeSongExecutor.likeSong(repository, ARGS),
            "When testing like song with a song that has not been added to a playlist" +
                "should return error message");
    }

    @Test
    void testLikeSongsWithValidData()
        throws PlaylistAlreadyExistsException, SongAlreadyExistsException, PlaylistNotFoundException {
        PlaylistRepository repository = new InMemoryPlaylistRepository();
        repository.createPlaylist(PLAYLIST_NAME);
        repository.addSong(PLAYLIST_NAME, SONG_NAME, ARTIST_NAME, 1);

        String result = String.format("{\"status\":\"OK\",\"message\":\"Song %s by %s liked. Likes: %d\"}",
            SONG_NAME, ARTIST_NAME, 1);

        assertEquals(result, LikeSongExecutor.likeSong(repository, ARGS),
            "When testing like song with valid data" +
                "should return OK message");
    }

}
