package market.Class;

import market.Enam.Gender;
import market.Enam.Holiday;
import market.exeption.CustomerNotFoundException;
import market.exeption.ProductNotFoundException;
import market.exeption.QuantityIsNegativeException;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;


public class Market {
    private List<Customer> customers;
    private List<Product> products;
    private List<Order> orders;


    public Market() {
        customers = new ArrayList<>(List.of(
                new Customer("Nick", 30, "+375295556788", 01, Gender.MALE),
                new Customer("Pop", 23, "+375295556788", 02, Gender.MALE),
                new Customer("Trevor", 41, "+375295556788", 03, Gender.MALE),
                new Customer("Petr", 34, "+375295556788", 04, Gender.MALE),
                new Customer("Masha", 25, "+375295435678", 04, Gender.FEMALE)
        ));

        products = new ArrayList<>(List.of(
                new Product("Meat", 12),
                new Product("Milk", 2),
                new Product("Beard", 1),
                new Product("Oil", 4),
                new Product("Chicken", 9)
        ));
        orders = new ArrayList<>();
    }

    /**
     * Метод для подсчета общей суммы потраченных денег по заказам определенного клиента.
     *
     * @param customerId ID клиента, чьи заказы нужно учесть при подсчете суммы
     * @return Общая сумма потраченных денег по заказам клиента
     */
    public int countPrice(int customerId) {
        int totalSpent = 0;
        for (Order order : orders) {
            if (order.getCustomer().getId() == customerId) {
                for (Map.Entry<Product, Integer> entry : order.getProducts().entrySet()) {
                    Product product = entry.getKey();
                    int quantity = entry.getValue();
                    totalSpent += product.getPrice() * quantity;
                }
            }
        }
        return totalSpent;
    }


    public int createOrder(Customer customer) throws CustomerNotFoundException {
        if (!customers.contains(customer))
            throw new CustomerNotFoundException("customer not found " + customers);
        Order order = new Order(customer);
        order.setOrderDate(new Date());
        if (order.getHoliday() == Holiday.NEW_YEAR) {
            order.setDiscount(0.10);
        } else if (order.getHoliday() == Holiday.WOMAN_DAY && customer.getGender() == Gender.FEMALE) {
            order.setDiscount(0.20);
        } else if (order.getHoliday() == Holiday.MAN_DAY && customer.getGender() == Gender.MALE) {
            order.setDiscount(0.20);
        }
        orders.add(order);
        return order.getId();
    }

    public Order addProductToOrder(int orderId, Product product, int quantity)
            throws ProductNotFoundException, QuantityIsNegativeException {
        if (!products.contains(product)) throw new ProductNotFoundException("product not found");
        if (quantity <= 0) throw new QuantityIsNegativeException("quantity of product is negative");
        Order order = orders.stream().filter(o -> o.getId() == orderId).findFirst().get();
        order.add(product, quantity);
        double totalDiscountedPrice = 0;
        for (Map.Entry<Product, Integer> entry : order.getProducts().entrySet()) {
            Product p = entry.getKey();
            int q = entry.getValue();
            double price = p.getPrice() * q;
            totalDiscountedPrice += price - (price * order.getDiscount());
        }
        order.calculateTotalPrice();

        return order;
    }

    public List<Customer> getCustomers() {
        return customers;
    }

    public List<Product> getProducts() {
        return products;
    }

    public List<Order> getOrders() {
        return orders;
    }


}
