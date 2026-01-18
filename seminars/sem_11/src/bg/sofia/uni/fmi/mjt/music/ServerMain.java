package bg.sofia.uni.fmi.mjt.music;

import bg.sofia.uni.fmi.mjt.music.server.MusicStreamingServer;
import bg.sofia.uni.fmi.mjt.music.server.repository.InMemoryPlaylistRepository;
import bg.sofia.uni.fmi.mjt.music.server.repository.PlaylistRepository;

public class ServerMain {

    public static void main(String[] args) {
        PlaylistRepository repository = new InMemoryPlaylistRepository();
        MusicStreamingServer server = new MusicStreamingServer(SERVER_PORT, repository);

        System.out.println("Starting Music Streaming Server on port " + SERVER_PORT + "...");
        server.start();
    }

    private static final int SERVER_PORT = 8080;

}