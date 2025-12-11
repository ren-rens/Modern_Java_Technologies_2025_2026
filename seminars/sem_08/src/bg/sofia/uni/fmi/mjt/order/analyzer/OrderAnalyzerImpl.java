package bg.sofia.uni.fmi.mjt.order.analyzer;

import bg.sofia.uni.fmi.mjt.order.domain.Category;
import bg.sofia.uni.fmi.mjt.order.domain.Order;
import bg.sofia.uni.fmi.mjt.order.domain.PaymentMethod;
import bg.sofia.uni.fmi.mjt.order.domain.Status;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class OrderAnalyzerImpl implements OrderAnalyzer {

    public OrderAnalyzerImpl(List<Order> orders) {
        this.orders = orders;
    }

    @Override
    public List<Order> allOrders() {
        if (orders == null) {
            return List.of();
        }

        return orders.stream()
            .filter(Objects::nonNull)
            .toList();
    }

    @Override
    public List<Order> ordersByCustomer(String customer) {
        if (customer == null || customer.isBlank()) {
            throw new IllegalArgumentException("No such customer in file exists");
        }

        return List.copyOf(orders
            .stream()
            .filter(Objects::nonNull)
            .filter(customerOrders -> customerOrders.customerName().equals(customer))
            .toList());
    }

    @Override
    public Map.Entry<LocalDate, Long> dateWithMostOrders() {
        if (orders == null) {
            return null;
        }

        Map<LocalDate, Long> result = orders
            .stream()
            .filter(Objects::nonNull)
            .collect(Collectors.groupingBy(Order::date, Collectors.counting()));

        return result.entrySet().stream()
            .max(Comparator.comparing(Map.Entry<LocalDate, Long>::getValue)
                .thenComparing(Map.Entry::getKey, Comparator.reverseOrder())) // earliest = smallest date
            .orElse(null);
    }

    @Override
    public List<String> topNMostOrderedProducts(int n) {
        if (n < 0) {
            throw new IllegalArgumentException("Number of orders should be >= 0");
        }

        if (n == 0) {
            return List.of();
        }

        Map<String, Long> result = orders
            .stream()
            .filter(Objects::nonNull)
            .collect(Collectors.groupingBy(Order::product, Collectors.counting()));

        return result.entrySet()
            .stream()
            .sorted(Map.Entry.<String, Long>comparingByValue(Comparator.reverseOrder())
                .thenComparing(Map.Entry::getKey))
            .map(Map.Entry::getKey)
            .limit(n)
            .toList();
    }

    @Override
    public Map<Category, Double> revenueByCategory() {
        Map<Category, Double> revenue =
            orders.stream()
                .filter(Objects::nonNull)
                .collect(Collectors.groupingBy(
                    Order::category,
                    Collectors.summingDouble(Order::totalSales)
                ));

        return Map.copyOf(revenue);
    }

    @Override
    public Set<String> suspiciousCustomers() {
        Map<String, Long> customers = orders
            .stream()
            .filter(Objects::nonNull)
            .filter(order -> order.status() == Status.CANCELLED)
            .filter(order -> order.totalSales() < SUSPICIOUS_ORDERS_TOTAL_SALES)
            .collect(Collectors.groupingBy(Order::customerName, Collectors.counting()));

        return Set.copyOf(customers.entrySet()
            .stream()
            .filter(entry -> entry.getValue() > SUSPICIOUS_ORDERS_COUNT)
            .map(Map.Entry::getKey)
            .collect(Collectors.toSet()));
    }

    @Override
    public Map<Category, PaymentMethod> mostUsedPaymentMethodForCategory() {
        if (orders == null || orders.isEmpty()) {
            return Map.of();
        }

        Map<Category, Map<PaymentMethod, Long>> grouped = findMapGroupedForMostUsedPaymentMethodForCategory();
        Map<Category, PaymentMethod> result = findResultForMostUsedPaymentMethodForCategory(grouped);

        return Map.copyOf(result);
    }

    private Map<Category, Map<PaymentMethod, Long>> findMapGroupedForMostUsedPaymentMethodForCategory() {
        return orders.stream()
            .filter(Objects::nonNull)
            .collect(Collectors.groupingBy(
                Order::category,
                Collectors.groupingBy(Order::paymentMethod, Collectors.counting())
            ));
    }

    private Map<Category, PaymentMethod> findResultForMostUsedPaymentMethodForCategory(
        Map<Category, Map<PaymentMethod, Long>> grouped) {

        Map<Category, PaymentMethod> result =
            grouped.entrySet()
                .stream()
                .collect(Collectors.toMap(
                    Map.Entry::getKey,
                    entry -> entry.getValue()
                        .entrySet()
                        .stream()
                        .max(Comparator
                            .comparing(Map.Entry<PaymentMethod, Long>::getValue)
                            .thenComparing(e -> e.getKey().name(), Comparator.reverseOrder())
                        )
                        .get()
                        .getKey()
                ));

        return result;

    }

    @Override
    public String locationWithMostOrders() {
        if (orders == null || orders.isEmpty()) {
            return null;
        }

        Map<String, Long> locations = orders.stream()
            .filter(Objects::nonNull)
            .collect(Collectors.groupingBy(
                Order::customerLocation,
                Collectors.counting()
            ));

        return locations.entrySet()
            .stream()
            .max(
                Comparator.comparingLong(Map.Entry<String, Long>::getValue)
                    .thenComparing(Comparator.comparing(Map.Entry<String, Long>::getKey).reversed())
            )
            .map(Map.Entry::getKey)
            .orElse(null);
    }

    @Override
    public Map<Category, Map<Status, Long>> groupByCategoryAndStatus() {
        if (orders == null || orders.isEmpty()) {
            return Map.of();
        }

        Map<Category, Map<Status, Long>> result = orders
            .stream()
            .filter(Objects::nonNull)
            .collect(
                Collectors.groupingBy(Order::category,
                    Collectors.groupingBy(Order::status, Collectors.counting()))
            );

        return Map.copyOf(result);
    }

    private final List<Order> orders;
    private static final int SUSPICIOUS_ORDERS_COUNT = 3;
    private static final double SUSPICIOUS_ORDERS_TOTAL_SALES = 100.0;

}
