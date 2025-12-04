package bg.sofia.uni.fmi.mjt.order.loader;

import bg.sofia.uni.fmi.mjt.order.domain.Order;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.UncheckedIOException;
import java.util.ArrayList;
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

        List<Order> orders = new ArrayList<>();

        try (var data = new BufferedReader(reader)) {
            List<String> lines = data.lines().toList();

            if (lines.isEmpty()) {
                return orders;
            }

            // skips the first line if it looks like a header
            int startIndex = lines.get(0).startsWith("id,") ? 1 : 0;

            orders = lines.stream()
                .skip(startIndex)
                .map(Order::of)
                .toList();
        } catch (IOException | UncheckedIOException e) {
            throw new RuntimeException("Problem with reading the file occurred", e);
        }

        return orders;
    }

}