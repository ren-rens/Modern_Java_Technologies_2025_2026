package bg.sofia.uni.fmi.mjt.music.server;

import bg.sofia.uni.fmi.mjt.music.command.Command;
import bg.sofia.uni.fmi.mjt.music.command.creator.CommandCreator;
import bg.sofia.uni.fmi.mjt.music.command.executor.CommandExecutor;
import bg.sofia.uni.fmi.mjt.music.server.repository.PlaylistRepository;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MusicStreamingServer {

    public MusicStreamingServer(int port, PlaylistRepository playlistRepository) {
        this.port = port;
        // this.playlistRepository = playlistRepository;
        this.clientRequestHandler = new ClientRequestHandler();
        this.executorService = Executors.newFixedThreadPool(MAX_EXECUTOR_THREADS);
    }

    public void start() {
        try (ServerSocketChannel serverSocketChannel = ServerSocketChannel.open()) {
            selector = Selector.open();
            configureServerSocketChannel(serverSocketChannel, selector);
            this.buffer = ByteBuffer.allocate(BUFFER_SIZE);
            isServerWorking = true;

            System.out.println("Server started on port " + port);

            while (isServerWorking) {
                try {
                    int readyChannels = selector.select();
                    if (readyChannels == 0) {
                        continue;
                    }

                    processSelectedKeys();
                } catch (IOException e) {
                    System.out.println("Error occurred while processing client request: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            throw new UncheckedIOException("failed to start server", e);
        } finally {
            stop();
        }
    }

    public void stop() {
        this.isServerWorking = false;
        if (executorService != null) {
            executorService.shutdown();
        }

        if (selector != null && selector.isOpen()) {
            selector.wakeup();
        }

        if (selector != null) {
            for (SelectionKey key : selector.keys()) {
                try {
                    key.channel().close();
                } catch (IOException e) {
                    System.out.println("Error occurred with closing channel");
                }
            }
        }

        if (selector != null) {
            try {
                selector.close();
            } catch (IOException e) {
                System.out.println("Error with closing selector occurred");
            }
        }
    }

    private void configureServerSocketChannel(ServerSocketChannel channel, Selector selector) throws IOException {
        channel.bind(new InetSocketAddress(SERVER_HOST, this.port));
        channel.configureBlocking(false);
        channel.register(selector, SelectionKey.OP_ACCEPT);
    }

    private void processSelectedKeys() throws IOException {
        Iterator<SelectionKey> keyIterator = selector.selectedKeys().iterator();

        while (keyIterator.hasNext()) {
            SelectionKey key = keyIterator.next();

            if (key.isReadable()) {
                handleReadableKey(key);
            } else if (key.isAcceptable()) {
                acceptConnection(key);
            }

            keyIterator.remove();
        }
    }

    private void handleReadableKey(SelectionKey key) throws IOException {
        SocketChannel clientChannel = (SocketChannel) key.channel();
        String clientInput = getClientInput(clientChannel);

        if (clientInput == null) {
            this.clientRequestHandler.removeClient(clientChannel);
            clientChannel.close();
            return;
        }

        System.out.println("Received from: " + this.clientRequestHandler.getClientId(clientChannel) +
            ": " + clientInput);

        executorService.submit(() -> {
            try {
                processClientCommand(clientChannel, clientInput, key);
            } catch (IOException e) {
                System.out.println("Error processing command: " + e.getMessage());
            }
        });
    }

    private void processClientCommand(SocketChannel clientChannel, String clientInput, SelectionKey key)
        throws IOException {

        Command command = CommandCreator.newCommand(clientInput);
        PlaylistRepository clientRepository = this.clientRequestHandler.getRepositoryForClient(clientChannel);
        String output = CommandExecutor.execute(clientRepository, command);

        if (output.equals("disconnect")) {
            this.clientRequestHandler.removeClient(clientChannel);
            clientChannel.close();

            System.out.println("Client disconnected");
            return;
        }

        // to ensure synchronization
        synchronized (clientChannel) {
            writeClientOutput(clientChannel, output);
        }
    }

    private String getClientInput(SocketChannel clientChannel) throws IOException {
        buffer.clear();

        int readBytes = clientChannel.read(buffer);
        if (readBytes < 0) {
            clientChannel.close();
            return null;
        }

        buffer.flip();

        byte[] clientInputBytes = new byte[buffer.remaining()];
        buffer.get(clientInputBytes);

        return new String(clientInputBytes, StandardCharsets.UTF_8);
    }

    private void writeClientOutput(SocketChannel clientChannel, String output) throws IOException {
        buffer.clear();
        buffer.put(output.getBytes());
        buffer.flip();

        clientChannel.write(buffer);
    }

    private void acceptConnection(SelectionKey key) throws IOException {
        ServerSocketChannel sockChannel = (ServerSocketChannel) key.channel();
        SocketChannel accept = sockChannel.accept();

        accept.configureBlocking(false);
        accept.register(selector, SelectionKey.OP_READ);

        String clientId = this.clientRequestHandler.getClientId(accept);
        System.out.println("New client connected: " + clientId);
    }

    private final int port;
    // private final PlaylistRepository playlistRepository;
    private final ClientRequestHandler clientRequestHandler;
    private final ExecutorService executorService;

    private static final int BUFFER_SIZE = 1024;
    private static final int MAX_EXECUTOR_THREADS = 10;
    private static final String SERVER_HOST = "localhost";

    private boolean isServerWorking;
    private ByteBuffer buffer;
    private Selector selector;

}