package bg.sofia.uni.fmi.mjt.music.server;

import bg.sofia.uni.fmi.mjt.music.server.repository.InMemoryPlaylistRepository;
import bg.sofia.uni.fmi.mjt.music.server.repository.PlaylistRepository;

import java.nio.channels.SocketChannel;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ClientRequestHandler {

    private final Map<String, PlaylistRepository> clientRepositories; // <clientId, repository>
    private final Map<SocketChannel, String> clients; // <client, clientId>

    public ClientRequestHandler() {
        this.clientRepositories = new ConcurrentHashMap<>();
        this.clients = new ConcurrentHashMap<>();
    }

    public PlaylistRepository getRepositoryForClient(String clientId) {
        return clientRepositories.computeIfAbsent(clientId,
            repository -> new InMemoryPlaylistRepository());
    }

    public PlaylistRepository getRepositoryForClient(SocketChannel channel) {
        String clientId = this.clients.computeIfAbsent(channel, client ->
            "client-" + Thread.currentThread());

        return getRepositoryForClient(clientId);
    }

    public void removeClient(SocketChannel channel) {
        String clientId = this.clients.remove(channel);
        if (clientId != null) {
            clientRepositories.remove(clientId);
        }
    }

    public String getClientId(SocketChannel channel) {
        return this.clients.get(channel);
    }

}
