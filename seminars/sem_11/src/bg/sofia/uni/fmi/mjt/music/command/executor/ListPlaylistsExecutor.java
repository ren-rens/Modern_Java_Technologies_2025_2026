package bg.sofia.uni.fmi.mjt.music.command.executor;

import bg.sofia.uni.fmi.mjt.music.command.constants.ExecutorConstants;
import bg.sofia.uni.fmi.mjt.music.command.executor.validator.ExecutorValidator;
import bg.sofia.uni.fmi.mjt.music.server.repository.PlaylistRepository;

import java.util.Collection;
import java.util.stream.Collectors;

public class ListPlaylistsExecutor {

    public static String listPlaylists(PlaylistRepository playlistRepository, String[] arguments) {
        if (ExecutorValidator.validateExecutor(arguments, ExecutorConstants.LIST_PLAYLISTS_ARGUMENTS_COUNT) == null) {
            return "{\"status\":\"ERROR\",\"message\":\"Usage: list-playlists\"}";
        }

        return listPlaylistsImpl(playlistRepository, arguments);
    }

    private static String listPlaylistsImpl(PlaylistRepository playlistRepository, String[] arguments) {
        Collection<String> playlists = playlistRepository.getAllPlaylists();

        String playlistsJson = playlists.stream()
            .map(name -> "\"" + name + "\"")
            .collect(Collectors.joining(","));

        return String.format("{\"status\":\"OK\",\"playlists\":[%s]}", playlistsJson);
    }

}
