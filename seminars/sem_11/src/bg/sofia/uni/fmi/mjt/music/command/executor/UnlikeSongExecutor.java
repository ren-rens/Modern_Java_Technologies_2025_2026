package bg.sofia.uni.fmi.mjt.music.command.executor;

import bg.sofia.uni.fmi.mjt.music.command.constants.ExecutorConstants;
import bg.sofia.uni.fmi.mjt.music.command.executor.validator.ExecutorValidator;
import bg.sofia.uni.fmi.mjt.music.server.repository.PlaylistRepository;
import bg.sofia.uni.fmi.mjt.music.server.repository.exception.PlaylistNotFoundException;
import bg.sofia.uni.fmi.mjt.music.server.repository.exception.SongNotFoundException;

public class UnlikeSongExecutor {

    public static String unlikeSong(PlaylistRepository playlistRepository, String[] arguments) {
        if (ExecutorValidator.validateExecutor(arguments, ExecutorConstants.UNLIKE_SONG_ARGUMENTS_COUNT) == null) {
            return "{\"status\":\"ERROR\",\"message\":" +
                "\"Usage: unlike-song <playlist_name> <song_title> <artist_name>\"}";
        }

        return unlikeSongImpl(playlistRepository, arguments);
    }

    private static String unlikeSongImpl(PlaylistRepository playlistRepository, String[] arguments) {
        String playlistName = arguments[ExecutorConstants.ZERO_IDX];
        String songName = arguments[ExecutorConstants.ONE_IDX];
        String artistName = arguments[ExecutorConstants.TWO_IDX];

        int likes;

        try {
            likes = playlistRepository.unlikeSong(playlistName, songName, artistName);
        } catch (PlaylistNotFoundException e) {
            return String.format("{\"status\":\"ERROR\",\"message\":\"Playlist %s does not exist.\"}",
                playlistName);
        } catch (SongNotFoundException e) {
            return String.format("{\"status\":\"ERROR\",\"message\":\"Song %s by %s does not exist in playlist %s.\"}",
                songName, artistName, playlistName);
        }

        return String.format("{\"status\":\"OK\",\"message\":\"Song %s by %s unliked. Likes: %d\"}",
            songName, artistName, likes);
    }

}
