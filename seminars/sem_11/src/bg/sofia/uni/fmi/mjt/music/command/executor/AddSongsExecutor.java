package bg.sofia.uni.fmi.mjt.music.command.executor;

import bg.sofia.uni.fmi.mjt.music.command.constants.ExecutorConstants;
import bg.sofia.uni.fmi.mjt.music.command.executor.validator.ExecutorValidator;
import bg.sofia.uni.fmi.mjt.music.server.repository.PlaylistRepository;
import bg.sofia.uni.fmi.mjt.music.server.repository.exception.PlaylistNotFoundException;
import bg.sofia.uni.fmi.mjt.music.server.repository.exception.SongAlreadyExistsException;

public class AddSongsExecutor {

    public static String addSongs(PlaylistRepository playlistRepository, String[] arguments) {
        if (ExecutorValidator.validateExecutor(arguments, ExecutorConstants.ADD_SONG_ARGUMENTS_COUNT) == null) {
            return "{\"status\":\"ERROR\",\"message\":" +
                "\"Usage: add-song <playlist_name> <song_title> <artist_name> <duration>\"}";
        }

        return addSongsImpl(playlistRepository, arguments);
    }

    private static String addSongsImpl(PlaylistRepository playlistRepository, String[] arguments) {
        int duration = getDuration(arguments);
        if (duration == -1) {
            return "{\"status\":\"ERROR\",\"message\":\"Duration must be a positive integer\"}";
        }

        return getMessage(playlistRepository, arguments, duration);

    }

    private static int getDuration(String[] arguments) {
        int duration = 0;
        try {
            duration = Integer.parseInt(arguments[ExecutorConstants.THREE_IDX].trim());
        } catch (NumberFormatException e) {
            duration = -1;
        }

        return duration;
    }

    private static String getMessage(PlaylistRepository playlistRepository, String[] arguments, int duration) {
        String playlistName = arguments[ExecutorConstants.ZERO_IDX];
        String songTitle = arguments[ExecutorConstants.ONE_IDX];
        String artistName = arguments[ExecutorConstants.TWO_IDX];

        try {
            if (playlistRepository.addSong(playlistName, songTitle, artistName, duration) == null) {
                return "{\"status\":\"ERROR\",\"message\":" +
                    "\"Invalid input for duration OR song name OR artists\"}";
            }
        } catch (PlaylistNotFoundException e) {
            return String.format("{\"status\":\"ERROR\",\"message\":\"Playlist %s does not exist.\"}",
                playlistName);
        } catch (SongAlreadyExistsException e) {
            return String.format("{\"status\":\"ERROR\",\"message\":\"Song %s by %s already exists in playlist %s.\"}",
                songTitle, artistName, playlistName);
        }

        return String.format("{\"status\":\"OK\",\"message\":\"Song %s by %s added successfully.\"}",
            songTitle, artistName);
    }

}
