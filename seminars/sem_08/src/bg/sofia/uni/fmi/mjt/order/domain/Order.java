package bg.sofia.uni.fmi.mjt.order.domain;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public record Order(String id, LocalDate date, String product, Category category,
                    double price, int quantity, double totalSales,
                    String customerName, String customerLocation,
                    PaymentMethod paymentMethod, Status status) implements Serializable {

    @Serial
    private static final long serialVersionUID = 5221409326107450861L;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Order order = (Order) o;
        return Objects.equals(id, order.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    public static Order of(String line) {
        String[] attributes = line.split(",");

        String id = attributes[ID_INDEX];
        LocalDate date = LocalDate.parse(attributes[DATE_INDEX], DateTimeFormatter.ofPattern("dd-MM-yy"));
        String product = attributes[PRODUCT_INDEX];
        Category category = Category.valueOf(attributes[CATEGORY_INDEX].toUpperCase().replace(" ", "_"));
        double price = Double.parseDouble(attributes[PRICE_INDEX]);
        int quantity = Integer.parseInt(attributes[QUANTITY_INDEX]);
        double totalSales = Double.parseDouble(attributes[TOTAL_SALES_INDEX]);
        String customerName = attributes[CUSTOMER_NAME_INDEX];
        String customerLocation = attributes[CUSTOMER_LOCATION_INDEX];
        PaymentMethod paymentMethod = PaymentMethod.valueOf(attributes[PAYMENT_METHOD_INDEX]
            .toUpperCase().replace(" ", "_"));
        Status status = Status.valueOf(attributes[STATUS_INDEX].toUpperCase());

        return new Order(id, date, product, category, price, quantity, totalSales,
            customerName, customerLocation, paymentMethod, status);
    }

    private static final int ID_INDEX = 0;
    private static final int DATE_INDEX = 1;
    private static final int PRODUCT_INDEX = 2;
    private static final int CATEGORY_INDEX = 3;
    private static final int PRICE_INDEX = 4;
    private static final int QUANTITY_INDEX = 5;
    private static final int TOTAL_SALES_INDEX = 6;
    private static final int CUSTOMER_NAME_INDEX = 7;
    private static final int CUSTOMER_LOCATION_INDEX = 8;
    private static final int PAYMENT_METHOD_INDEX = 9;
    private static final int STATUS_INDEX = 10;

}
