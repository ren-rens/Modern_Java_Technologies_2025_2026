package bg.sofia.uni.fmi.mjt.order.loader;

import bg.sofia.uni.fmi.mjt.order.domain.Order;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.List;

public class OrderLoaderTest {

    @Test
    void testLoadWithNullReader() {
        assertThrows(IllegalArgumentException.class, () -> OrderLoader.load(null),
            "When testing load with null reader should throw IllegalArgumentException");
    }

    @Test
    void testLoadThrowsRuntimeExceptionOnIOException() throws IOException {
        Reader failingReader = new FailingReaderStub();

        assertThrows(RuntimeException.class, () -> OrderLoader.load(failingReader),
            "When testing load and a problem with reading the file occurs should throw RuntimeException");
    }

    @Test
    void testLoadWithHeaderLine() {
        String input = "id,date,product,category,price,quantity,total,customer,location,payment,status\n" +
            "id1,2025-01-01,product1,BOOKS,10.0,1,10,customer,location,AMAZON_PAY,COMPLETED\n" +
            "id2,2025-01-02,product2,CLOTHING,20.0,2,40,customer2,location2,CREDIT_CARD,CANCELLED";

        Reader reader = new StringReader(input);
        List<Order> orders = OrderLoader.load(reader);

        assertEquals(2, orders.size(), "Should read 2 orders and skip header line");
        assertEquals("id1", orders.get(0).id());
        assertEquals("id2", orders.get(1).id());
    }


    @Test
    void testLoadWithOnlyHeaderLine() {
        String input = "id,date,product,category,price,quantity,total,customer,location,payment,status";

        Reader reader = new StringReader(input);
        List<Order> orders = OrderLoader.load(reader);

        assertEquals(0, orders.size(),
            "When testing load with only header line should return empty list");
    }

    @Test
    void testLoadWithHeaderAndSingleOrder() {
        String input = "id,date,product,category,price,quantity,total,customer,location,payment,status\n" +
            "id1,2025-01-01,product1,BOOKS,10.0,1,10,customer,location,AMAZON_PAY,COMPLETED";

        Reader reader = new StringReader(input);
        List<Order> orders = OrderLoader.load(reader);

        assertEquals("id1", orders.get(0).id(),
            "When testing load with header line should read 1 order and skip header");
    }

    @Test
    void testLoadWithEmptyReader() {
        Reader reader = new StringReader("");
        List<Order> orders = OrderLoader.load(reader);

        assertEquals(0, orders.size(),
            "When testing load with empty reader should return empty list for empty input");
    }

    @Test
    void testLoadWithValidInput() {
        String input = "id1,2025-01-01,product1,BOOKS,10.0,1,10,customer,location,AMAZON_PAY,COMPLETED\n" +
            "id2,2025-01-02,product2,CLOTHING,20.0,2,40,customer2,location2,CREDIT_CARD,CANCELLED";

        Reader reader = new StringReader(input);
        List<Order> orders = OrderLoader.load(reader);

        assertEquals(2, orders.size(), "Should read 2 orders from valid input");
        assertEquals("id1", orders.get(0).id());
        assertEquals("id2", orders.get(1).id());
    }

}