package bg.sofia.uni.fmi.mjt.order.analyzer;

import bg.sofia.uni.fmi.mjt.order.domain.Category;
import bg.sofia.uni.fmi.mjt.order.domain.Order;
import bg.sofia.uni.fmi.mjt.order.domain.PaymentMethod;
import bg.sofia.uni.fmi.mjt.order.domain.Status;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class OrderAnalyzerImplTest {

    private final LocalDate date = LocalDate.now();
    private final Order order = new Order("id", date, "product",
        Category.BOOKS, 10.00, 1, 10,
        "customerName", "customerLocation",
        PaymentMethod.AMAZON_PAY, Status.CANCELLED);
    private final Order order2 = new Order("id2", date, "product2",
        Category.CLOTHING, 10.00, 1, 10,
        "customerName2", "customerLocation2",
        PaymentMethod.CREDIT_CARD, Status.COMPLETED);

    // testing allOrders
    @Test
    void testAllOrdersWithNullOrders() {
        OrderAnalyzerImpl orderAnalyzer = new OrderAnalyzerImpl(null);
        assertEquals(List.of(), orderAnalyzer.allOrders(),
            "When testing allOrders with null orders should return List.of()");
    }

    @Test
    void testAllOrdersWithEmptyOrdersList() {
        List<Order> orders = new ArrayList<>();
        OrderAnalyzerImpl orderAnalyzer = new OrderAnalyzerImpl(orders);

        assertEquals(List.copyOf(orders), orderAnalyzer.allOrders(),
            "When testing allOrders with empty orders should return empty immutable array");
    }

    @Test
    void testAllOrdersWithValidOrdersListButAllNullOrders() {
        List<Order> orders = new ArrayList<>();
        orders.add(null);
        orders.add(null);
        OrderAnalyzerImpl orderAnalyzer = new OrderAnalyzerImpl(orders);

        assertEquals(List.of(), orderAnalyzer.allOrders(),
            "When testing allOrders with valid orders array but null orders should return the immutable array with no null elements");
    }

    @Test
    void testAllOrdersWithValidOrdersListButSomeNullOrders() {
        List<Order> orders = new ArrayList<>();
        orders.add(order);
        orders.add(null);

        OrderAnalyzerImpl orderAnalyzer = new OrderAnalyzerImpl(orders);

        List<Order> result = new ArrayList<>();
        result.add(order);

        assertEquals(List.copyOf(result), orderAnalyzer.allOrders(),
            "When testing allOrders with valid orders array but null orders should return the immutable array with no null elements");
    }

    @Test
    void testAllOrdersWithValidOrdersList() {
        List<Order> orders = new ArrayList<>();
        orders.add(order);

        OrderAnalyzerImpl orderAnalyzer = new OrderAnalyzerImpl(orders);

        assertEquals(List.copyOf(orders), orderAnalyzer.allOrders(),
            "When testing allOrders with valid orders array should return the immutable array of the orders");
    }

    // testing ordersByCustomer
    @Test
    void testOrdersByCustomerWithNullCustomer() {
        OrderAnalyzerImpl orderAnalyzer = new OrderAnalyzerImpl(new ArrayList<>());
        assertThrows(IllegalArgumentException.class, () -> orderAnalyzer.ordersByCustomer(null),
            "When testing ordersByCustomer with NULL customer should throw IllegalArgumentException");
    }

    @Test
    void testOrdersByCustomerWithBlankCustomer() {
        OrderAnalyzerImpl orderAnalyzer = new OrderAnalyzerImpl(new ArrayList<>());
        assertThrows(IllegalArgumentException.class, () -> orderAnalyzer.ordersByCustomer("   "),
            "When testing ordersByCustomer with blank customer should throw IllegalArgumentException");
    }

    @Test
    void testOrdersByCustomerWithNonExistentCustomer() {
        List<Order> orders = new ArrayList<>();
        orders.add(order);

        OrderAnalyzerImpl orderAnalyzer = new OrderAnalyzerImpl(orders);
        String customer = "notCustomerName";

        assertEquals(List.of(), orderAnalyzer.ordersByCustomer(customer),
            "When testing ordersByCustomer with non-existent customer should return empty list");
    }

    @Test
    void testOrdersByCustomerWithExistentCustomer() {
        List<Order> orders = new ArrayList<>();
        orders.add(order);

        OrderAnalyzerImpl orderAnalyzer = new OrderAnalyzerImpl(orders);
        String customer = "customerName";

        assertEquals(List.of(order), orderAnalyzer.ordersByCustomer(customer),
            "When testing ordersByCustomer with existent customer should return list of all orders made by the specified customer");
    }

    @Test
    void testOrdersByCustomerWithExistentCustomerAndSomNullOrders() {
        List<Order> orders = new ArrayList<>();
        orders.add(order);
        orders.add(null);

        OrderAnalyzerImpl orderAnalyzer = new OrderAnalyzerImpl(orders);
        String customer = "customerName";

        assertEquals(List.of(order), orderAnalyzer.ordersByCustomer(customer),
            "When testing ordersByCustomer with existent customer should return list of all orders made by the specified customer");
    }

    // testing dateWithMostOrders
    @Test
    void testDateWithMostOrdersWithNullOrders() {
        OrderAnalyzerImpl orderAnalyzer = new OrderAnalyzerImpl(null);

        assertNull(orderAnalyzer.dateWithMostOrders(),
            "When testing dateWithMostOrders with null orders should return null");
    }

    @Test
    void testDateWithMostOrdersWithNoOrders() {
        OrderAnalyzerImpl orderAnalyzer = new OrderAnalyzerImpl(new ArrayList<>());

        assertNull(orderAnalyzer.dateWithMostOrders(),
            "When testing dateWithMostOrders with no orders should return null");
    }

    @Test
    void testDateWithMostOrdersWithAllNullOrders() {
        List<Order> orders = new ArrayList<>();
        orders.add(null);
        orders.add(null);

        OrderAnalyzerImpl orderAnalyzer = new OrderAnalyzerImpl(orders);


        assertNull(orderAnalyzer.dateWithMostOrders(),
            "When testing dateWithMostOrders with all null orders should return null");
    }

    @Test
    void testDateWithMostOrdersValidOrdersButSomeNullOrders() {
        List<Order> orders = new ArrayList<>();
        orders.add(order);
        orders.add(null);

        OrderAnalyzerImpl orderAnalyzer = new OrderAnalyzerImpl(orders);
        Map.Entry<LocalDate, Long> expected = Map.entry(
            date, 1L
        );

        assertEquals(expected, orderAnalyzer.dateWithMostOrders(),
            "When testing dateWithMostOrders with valid orders and some null orders should return" +
                " the date with the highest number of orders and the count of the orders that are not null");
    }

    @Test
    void testDateWithMostOrdersValidOrders() {
        List<Order> orders = new ArrayList<>();

        Order order3 = new Order("id3", LocalDate.of(2020, 12, 12), "product3",
            Category.BOOKS, 10.00, 1, 10,
            "customerName", "customerLocation",
            PaymentMethod.AMAZON_PAY, Status.CANCELLED);

        orders.add(order);
        orders.add(order2);
        orders.add(order3);

        OrderAnalyzerImpl orderAnalyzer = new OrderAnalyzerImpl(orders);
        Map.Entry<LocalDate, Long> expected = Map.entry(
            date, 2L
        );

        assertEquals(expected, orderAnalyzer.dateWithMostOrders(),
            "When testing dateWithMostOrders with valid orders and different orders dates should return" +
                " the date with the highest number of orders and the count of the orders");
    }

    @Test
    void testDateWithMostOrdersValidOrderTie() {
        List<Order> orders = new ArrayList<>();

        LocalDate date2020 = LocalDate.of(2020, 12, 12);
        Order order3 = new Order("id3", date2020, "product3",
            Category.BOOKS, 10.00, 1, 10,
            "customerName", "customerLocation",
            PaymentMethod.AMAZON_PAY, Status.CANCELLED);

        orders.add(order);
        orders.add(order3);

        OrderAnalyzerImpl orderAnalyzer = new OrderAnalyzerImpl(orders);
        Map.Entry<LocalDate, Long> expected = Map.entry(
            date2020, 1L
        );

        assertEquals(expected, orderAnalyzer.dateWithMostOrders(),
            "When testing dateWithMostOrders with valid orders and different orders dates should return" +
                " the date with the highest number of orders and the count of the orders" +
                "In case of a tie, return the earliest of the dates with equal number of orders");
    }

    // test topNMostOrderedProducts
    @Test
    void testTopNMostOrderedProductsWithNegativeN() {
        OrderAnalyzerImpl orderAnalyzer = new OrderAnalyzerImpl(new ArrayList<>());
        assertThrows(IllegalArgumentException.class, () -> orderAnalyzer.topNMostOrderedProducts(-1),
            "When testing topNMostOrderedProducts with negative n should throw IllegalArgumentException");
    }

    @Test
    void testTopNMostOrderedProductsWithZeroN() {
        OrderAnalyzerImpl orderAnalyzer = new OrderAnalyzerImpl(new ArrayList<>());
        assertEquals(List.of(), orderAnalyzer.topNMostOrderedProducts(0),
            "When testing topNMostOrderedProducts with zero n should return empty immutable list");
    }

    @Test
    void testTopNMostOrderedProductsWithPositiveNOneProduct() {
        List<Order> orders = new ArrayList<>();
        orders.add(order);

        OrderAnalyzerImpl orderAnalyzer = new OrderAnalyzerImpl(orders);
        assertEquals(List.of(order.product()), orderAnalyzer.topNMostOrderedProducts(2),
            "When testing topNMostOrderedProducts with positive n should return a list of the one product name");
    }

    @Test
    void testTopNMostOrderedProductsWithPositiveNManyProducts() {
        List<Order> orders = new ArrayList<>();

        orders.add(order);
        orders.add(order);
        orders.add(order2);

        OrderAnalyzerImpl orderAnalyzer = new OrderAnalyzerImpl(orders);
        assertEquals(List.of(order.product(), order2.product()), orderAnalyzer.topNMostOrderedProducts(10),
            "When testing topNMostOrderedProducts with positive n should return a list of product names ordered by frequency");
    }

    @Test
    void testTopNMostOrderedProductsWithPositiveNManyProductsTie() {
        List<Order> orders = new ArrayList<>();

        orders.add(order);
        orders.add(order2);

        OrderAnalyzerImpl orderAnalyzer = new OrderAnalyzerImpl(orders);
        assertEquals(List.of(order.product(), order2.product()), orderAnalyzer.topNMostOrderedProducts(10),
            "When testing topNMostOrderedProducts with positive n should return a list of product names ordered by frequency" +
                "but when two products have the same number of orders, sorting them alphabetically");
    }

    // testing revenueByCategory
    @Test
    void testRevenueByCategoryWithNullOrders() {
        List<Order> orders = new ArrayList<>();
        orders.add(null);
        orders.add(null);
        orders.add(order);

        OrderAnalyzerImpl orderAnalyzer = new OrderAnalyzerImpl(orders);
        Map<Category, Double> expected = new HashMap<>(Map.of(Category.BOOKS, 10.00));

        assertEquals(expected, orderAnalyzer.revenueByCategory(),
            "When testing revenueByCategory with some null orders it should ignore them" +
                "and return a map of category and totalSales for each not null order");
    }

    @Test
    void testRevenueByCategoryWithValidOrders() {
        List<Order> orders = new ArrayList<>();

        Order order3 = new Order("id3", date, "product3",
            Category.BOOKS, 10.00, 1, 10,
            "customerName", "customerLocation",
            PaymentMethod.AMAZON_PAY, Status.CANCELLED);

        orders.add(order);
        orders.add(order2);
        orders.add(order3);

        OrderAnalyzerImpl orderAnalyzer = new OrderAnalyzerImpl(orders);
        Map<Category, Double> expected = new HashMap<>(Map.of(Category.BOOKS, 20.00, Category.CLOTHING, 10.00));

        assertEquals(expected, orderAnalyzer.revenueByCategory(),
            "When testing revenueByCategory with valid orders" +
                "should return a map of category and totalSales for each not null order");
    }

    // testing suspiciousCustomers
    @Test
    void testSuspiciousCustomersWithNullOrders() {
        List<Order> orders = new ArrayList<>();
        orders.add(order);
        orders.add(order);
        orders.add(null);
        orders.add(order);
        orders.add(order);

        OrderAnalyzer orderAnalyzer = new OrderAnalyzerImpl(orders);

        Set<String> result = new HashSet<>();
        result.add(order.customerName());

        assertEquals(result, orderAnalyzer.suspiciousCustomers(),
            "When testing suspiciousCustomers with some null orders" +
                "should ignore them and return the suspicious customers");
    }

    @Test
    void testSuspiciousCustomersWithValidOrders() {
        List<Order> orders = new ArrayList<>();

        Order order3 = new Order("id2", date, "product2",
            Category.BOOKS, 10.00, 1, 10,
            "customerName", "customerLocation",
            PaymentMethod.AMAZON_PAY, Status.CANCELLED);

        orders.add(order3);
        orders.add(order3);
        orders.add(order3);
        orders.add(order);
        orders.add(order);
        orders.add(order);

        OrderAnalyzer orderAnalyzer = new OrderAnalyzerImpl(orders);

        Set<String> result = new HashSet<>();
        result.add(order.customerName());
        result.add(order3.customerName());

        assertEquals(result, orderAnalyzer.suspiciousCustomers(),
            "When testing suspiciousCustomers with many valid suspicious orders" +
                "return the suspicious customers");
    }

    // test mostUsedPaymentMethodForCategory
    @Test
    void testMostUsedPaymentMethodForCategoryWithNullDatabase() {
        OrderAnalyzerImpl orderAnalyzer = new OrderAnalyzerImpl(null);
        assertEquals(Map.of(), orderAnalyzer.mostUsedPaymentMethodForCategory(),
            "When testing mostUsedPaymentMethodForCategory with null database should return empty map");
    }

    @Test
    void testMostUsedPaymentMethodForCategoryWithEmptyDatabase() {
        OrderAnalyzerImpl orderAnalyzer = new OrderAnalyzerImpl(new ArrayList<>());
        assertEquals(Map.of(), orderAnalyzer.mostUsedPaymentMethodForCategory(),
            "When testing mostUsedPaymentMethodForCategory with empty database should return empty map");
    }

    @Test
    void testMostUsedPaymentMethodForCategoryWithValidDatabase() {
        List<Order> orders = new ArrayList<>();
        orders.add(order);
        orders.add(order2);
        orders.add(order);

        Map<Category, PaymentMethod> result = Map.of(order.category(), order.paymentMethod(), order2.category(), order2.paymentMethod());
        OrderAnalyzer orderAnalyzer = new OrderAnalyzerImpl(orders);

        assertEquals(result, orderAnalyzer.mostUsedPaymentMethodForCategory(),
            "When testing mostUsedPaymentMethodForCategory with valid database" +
                "should return a map from category to its most frequently used payment method");
    }

    @Test
    void testMostUsedPaymentMethodForCategoryWithValidDatabaseTie() {
        List<Order> orders = new ArrayList<>();
        orders.add(order);
        orders.add(order2);

        Map<Category, PaymentMethod> result = Map.of(order.category(), order.paymentMethod(), order2.category(), order2.paymentMethod());
        OrderAnalyzer orderAnalyzer = new OrderAnalyzerImpl(orders);

        assertEquals(result, orderAnalyzer.mostUsedPaymentMethodForCategory(),
            "When testing mostUsedPaymentMethodForCategory with valid database" +
                "should return a map from category to its most frequently used payment method" +
                "in case of a tie for a category, return the PaymentMethod whose name comes first alphabetically.");
    }

    //testing locationWithMostOrders
    @Test
    void testLocationWithMostOrdersWithNullOrders() {
        OrderAnalyzer orderAnalyzer = new OrderAnalyzerImpl(null);

        assertNull(orderAnalyzer.locationWithMostOrders(),
            "When testing locationWithMostOrders with null orders should return null");
    }

    @Test
    void testLocationWithMostOrdersWithNoOrders() {
        OrderAnalyzer orderAnalyzer = new OrderAnalyzerImpl(new ArrayList<>());

        assertNull(orderAnalyzer.locationWithMostOrders(),
            "When testing locationWithMostOrders with no orders should return null");
    }

    @Test
    void testLocationWithMostOrdersWithValidOrders() {
        List<Order> orders = new ArrayList<>();
        orders.add(order);
        orders.add(order);
        orders.add(order2);

        OrderAnalyzer orderAnalyzer = new OrderAnalyzerImpl(orders);
        assertEquals(order.customerLocation(), orderAnalyzer.locationWithMostOrders(),
            "When testing locationWithMostOrders with valid orders should" +
                " return the location with the most orders");
    }

    @Test
    void testLocationWithMostOrdersWithValidOrdersTie() {
        List<Order> orders = new ArrayList<>();
        orders.add(order);
        orders.add(order2);

        OrderAnalyzer orderAnalyzer = new OrderAnalyzerImpl(orders);
        assertEquals(order.customerLocation(), orderAnalyzer.locationWithMostOrders(),
            "When testing locationWithMostOrders with valid orders should return" +
                " the location with the most orders" +
                "but if multiple locations tie, return the one that is alphabetically smallest");
    }

    // testing groupByCategoryAndStatus
    @Test
    void testGroupByCategoryAndStatusWithNullDatabase() {
        OrderAnalyzer orderAnalyzer = new OrderAnalyzerImpl(null);

        assertEquals(Map.of(), orderAnalyzer.groupByCategoryAndStatus(),
            "When testing groupByCategoryAndStatus with null database should return empty map");
    }

    @Test
    void testGroupByCategoryAndStatusWithEmptyDatabase() {
        OrderAnalyzer orderAnalyzer = new OrderAnalyzerImpl(new ArrayList<>());

        assertEquals(Map.of(), orderAnalyzer.groupByCategoryAndStatus(),
            "When testing groupByCategoryAndStatus with empty database should return empty map");
    }

    @Test
    void testGroupByCategoryAndStatus() {
        List<Order> orders = new ArrayList<>();
        orders.add(order);
        orders.add(order2);

        Map<Category, Map<Status, Long>> result = Map.of(
            order.category(), Map.of(order.status(), 1L),
            order2.category(), Map.of(order2.status(), 1L)
        );

        OrderAnalyzer orderAnalyzer = new OrderAnalyzerImpl(orders);

        assertEquals(result, orderAnalyzer.groupByCategoryAndStatus(),
            "When testing groupByCategoryAndStatus should return a map" +
                " where each category maps to another map from status to the count of orders with that status");
    }

}
