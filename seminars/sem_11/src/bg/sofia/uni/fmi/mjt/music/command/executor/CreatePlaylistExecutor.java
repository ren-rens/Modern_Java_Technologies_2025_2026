package bg.sofia.uni.fmi.mjt.music.command.executor;

import bg.sofia.uni.fmi.mjt.music.command.constants.ExecutorConstants;
import bg.sofia.uni.fmi.mjt.music.command.executor.validator.ExecutorValidator;
import bg.sofia.uni.fmi.mjt.music.server.repository.PlaylistRepository;
import bg.sofia.uni.fmi.mjt.music.server.repository.exception.PlaylistAlreadyExistsException;

public class CreatePlaylistExecutor {

    public static String createPlaylist(PlaylistRepository playlistRepository, String[] arguments) {
        if (ExecutorValidator.validateExecutor(arguments, ExecutorConstants.CREATE_PLAYLIST_ARGUMENTS_COUNT) == null) {
            return "{\"status\":\"ERROR\",\"message\":\"Usage: create-playlist <playlist_name>\"}";
        }

        return createPlaylistImpl(playlistRepository, arguments);
    }

    private static String createPlaylistImpl(PlaylistRepository playlistRepository, String[] arguments) {
        String playlistName = arguments[ExecutorConstants.ZERO_IDX];

        try {
            playlistRepository.createPlaylist(playlistName);
        } catch (PlaylistAlreadyExistsException e) {
            return String.format("{\"status\":\"ERROR\",\"message\":\"Playlist %s already exists.\"}",
                playlistName);
        }

        return String.format("{\"status\":\"OK\",\"message\":\"Playlist %s created successfully.\"}",
            playlistName);
    }

}
