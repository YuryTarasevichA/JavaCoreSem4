package market.Class;

import market.Enum.Holiday;
import market.exeption.InvalidDateFormatException;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Order {
    private static int count = 1;
    private int id;
    private Customer customer;
    private Map<Product, Integer> products;
    private Holiday holiday;
    private Date orderDate;
    private double discount;

    public void setOrderDate(Date orderDate) throws InvalidDateFormatException {
        this.orderDate = orderDate;
        this.holiday = determineHoliday(orderDate);
    }

    public Holiday determineHoliday(Date orderDate) throws InvalidDateFormatException {
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(orderDate);
            int month = calendar.get(Calendar.MONTH) + 1; // (январь - 1, февраль - 2, и т.д.)
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            if (month < 1 || month > 12 || day < 1 || day > 31) {
                throw new InvalidDateFormatException("Invalid date format: " +
                        calendar.get(Calendar.YEAR) + "-" + month + "-" + day);
            }
            if (month == 1 && calendar.get(Calendar.DAY_OF_MONTH) == 1 || calendar.get(Calendar.DAY_OF_MONTH) == 2) {
                return Holiday.NEW_YEAR; // новый год 1 и 2 января скидки
            } else if (month == 3 && calendar.get(Calendar.DAY_OF_MONTH) == 8) {
                return Holiday.WOMAN_DAY;
            } else if (month == 2 && calendar.get(Calendar.DAY_OF_MONTH) == 23) {
                return Holiday.MAN_DAY;
            }
        } catch (InvalidDateFormatException e) {
            throw e;
        }
        return Holiday.N0_HOLIDAY;
    }

    public Holiday getHoliday() {
        return holiday;
    }

    public Order(Customer customer) {
        this.id = count++;
        this.customer = customer;
        products = new HashMap<Product, Integer>();
    }

    public int getId() {
        return id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public void add(Product product, int quantity) {
        products.put(product, quantity);
    }

    @Override
    public String toString() {
        return "Order{" +
                "customer=" + customer +
                ", products=" + products +
                "}\n";
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public double calculateTotalPrice() {
        double totalPrice = 0;
        for (Map.Entry<Product, Integer> entry : products.entrySet()) {
            Product product = entry.getKey();
            int quantity = entry.getValue();
            totalPrice += product.getPrice() * quantity;
        }
        return totalPrice - (totalPrice * discount);
    }

    public Map<Product, Integer> getProducts() {
        return products;
    }

    public void setProducts(Map<Product, Integer> products) {
        this.products = products;
    }
}
