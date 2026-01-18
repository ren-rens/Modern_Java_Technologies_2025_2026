package bg.sofia.uni.fmi.mjt.music;

import bg.sofia.uni.fmi.mjt.music.client.MusicStreamingClient;

public class ClientMain {

    public static void main(String[] args) {
        MusicStreamingClient client = new MusicStreamingClient(SERVER_PORT);
        client.start();
    }

    private static final int SERVER_PORT = 8080;

}