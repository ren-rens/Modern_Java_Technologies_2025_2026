package bg.sofia.uni.fmi.mjt.order.loader;

import java.io.IOException;
import java.io.Reader;

public class FailingReaderStub extends Reader {
    @Override
    public int read(char[] cbuf, int off, int len) throws IOException {
        throw new IOException("Simulated read error");
    }

    @Override
    public void close() throws IOException { }

    @Override
    public boolean ready() throws IOException {
        throw new IOException("Simulated ready error");
    }
}
