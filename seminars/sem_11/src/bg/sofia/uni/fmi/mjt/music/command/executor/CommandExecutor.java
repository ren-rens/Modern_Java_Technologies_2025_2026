package bg.sofia.uni.fmi.mjt.music.command.executor;

import bg.sofia.uni.fmi.mjt.music.command.Command;
import bg.sofia.uni.fmi.mjt.music.command.constants.CommandConstants;
import bg.sofia.uni.fmi.mjt.music.server.repository.PlaylistRepository;

public class CommandExecutor {

    public static String execute(PlaylistRepository playlistRepository, Command cmd) {
        return switch (cmd.command()) {
            case CommandConstants.CREATE_PLAYLIST ->
                createPlaylist(playlistRepository, cmd.arguments());
            case CommandConstants.ADD_SONG ->
                addSongs(playlistRepository, cmd.arguments());
            case CommandConstants.LIST_PLAYLISTS ->
                listPlaylists(playlistRepository, cmd.arguments());
            case CommandConstants.GET_PLAYLIST ->
                getPlaylist(playlistRepository, cmd.arguments());
            case CommandConstants.LIKE_SONG ->
                likeSong(playlistRepository, cmd.arguments());
            case CommandConstants.UNLIKE_SONG ->
                unlikeSong(playlistRepository, cmd.arguments());
            case CommandConstants.DISCONNECT ->
                "disconnect";
            default -> "{\"status\":\"ERROR\",\"message\":\"Unknown command\"}";
        };
    }

    private static String createPlaylist(PlaylistRepository playlistRepository, String[] arguments) {
        return CreatePlaylistExecutor.createPlaylist(playlistRepository, arguments);
    }

    private static String addSongs(PlaylistRepository playlistRepository, String[] arguments) {
        return AddSongsExecutor.addSongs(playlistRepository, arguments);
    }

    private static String listPlaylists(PlaylistRepository playlistRepository, String[] arguments) {
        return ListPlaylistsExecutor.listPlaylists(playlistRepository, arguments);
    }

    private static String getPlaylist(PlaylistRepository playlistRepository, String[] arguments) {
        return GetPlaylistExecutor.getPlaylist(playlistRepository, arguments);
    }

    private static String likeSong(PlaylistRepository playlistRepository, String[] arguments) {
        return LikeSongExecutor.likeSong(playlistRepository, arguments);
    }

    private static String unlikeSong(PlaylistRepository playlistRepository, String[] arguments) {
        return UnlikeSongExecutor.unlikeSong(playlistRepository, arguments);
    }

    /*
    private static final int CREATE_PLAYLIST_ARGUMENTS_COUNT = 1;
    private static final int ADD_SONG_ARGUMENTS_COUNT = 4;
    private static final int LIST_PLAYLISTS_ARGUMENTS_COUNT = 0;
    private static final int GET_PLAYLIST_ARGUMENTS_COUNT = 1;
    private static final int LIKE_SONG_ARGUMENTS_COUNT = 3;
    private static final int UNLIKE_SONG_ARGUMENTS_COUNT = 3;

    private static final int ZERO_IDX = 0;
    private static final int ONE_IDX = 1;
    private static final int TWO_IDX = 2;
    private static final int THREE_IDX = 3;

    private static final String VALID_STRING = "valid";
     */

}