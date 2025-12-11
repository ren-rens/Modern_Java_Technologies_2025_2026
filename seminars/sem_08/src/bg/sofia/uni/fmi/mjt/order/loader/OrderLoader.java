package bg.sofia.uni.fmi.mjt.order.loader;

import bg.sofia.uni.fmi.mjt.order.domain.Order;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.UncheckedIOException;
import java.util.List;

public class OrderLoader {

    /**
     * Returns a list of orders read from the source Reader.
     *
     * @param reader the Reader with orders
     * @throws IllegalArgumentException if the reader is null
     */
    public static List<Order> load(Reader reader) {
        if (reader == null) {
            throw new IllegalArgumentException("Reader is invalid: NULL");
        }

        try (var data = new BufferedReader(reader)) {
            return data.lines()
                .skip(1) // Always skip first line (header)
                .map(Order::of)
                .toList();
        } catch (IOException | UncheckedIOException e) {
            throw new RuntimeException("Problem with reading the file occurred", e);
        }
    }

}
