package bg.sofia.uni.fmi.mjt.music.command.executor;

import bg.sofia.uni.fmi.mjt.music.command.constants.ExecutorConstants;
import bg.sofia.uni.fmi.mjt.music.command.executor.validator.ExecutorValidator;
import bg.sofia.uni.fmi.mjt.music.server.model.Playlist;
import bg.sofia.uni.fmi.mjt.music.server.repository.PlaylistRepository;
import bg.sofia.uni.fmi.mjt.music.server.repository.exception.PlaylistNotFoundException;

public class GetPlaylistExecutor {

    public static String getPlaylist(PlaylistRepository playlistRepository, String[] arguments) {
        if (ExecutorValidator.validateExecutor(arguments, ExecutorConstants.GET_PLAYLIST_ARGUMENTS_COUNT) == null) {
            return "{\"status\":\"ERROR\",\"message\":\"Usage: get-playlist <playlist_name>\"}";

        }

        return getPlaylistImpl(playlistRepository, arguments);
    }

    private static String getPlaylistImpl(PlaylistRepository playlistRepository, String[] arguments) {
        String playlistName = arguments[ExecutorConstants.ZERO_IDX];

        try {
            Playlist playlist = playlistRepository.getPlaylist(playlistName);
            return String.format("{\"status\":\"OK\",\"playlist\":%s}", playlist.toJson());
        } catch (PlaylistNotFoundException e) {
            return String.format("{\"status\":\"ERROR\",\"message\":\"Playlist %s does not exist.\"}",
                playlistName);
        }
    }

}
