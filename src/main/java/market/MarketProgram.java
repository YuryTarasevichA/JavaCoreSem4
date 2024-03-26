package market;

import market.Class.Customer;
import market.Class.Market;
import market.Class.Order;
import market.Class.Product;
import market.Enam.Gender;
import market.Enam.Holiday;
import market.exeption.CustomerNotFoundException;
import market.exeption.ProductNotFoundException;
import market.exeption.QuantityIsNegativeException;

import java.util.*;

public class MarketProgram {
    public static void main(String[] args) {
        Market market = new Market();
        List<Customer> customers = market.getCustomers();
        List<Product> products = market.getProducts();
        Order[] orders = createOrders(customers, products, market);
        Date randomDate = generateRandomDate();
//        printOrders(orders);
        printTotalSpentByCustomers(market, customers);
        System.out.println("===========================================");
        try {
            int orderId = market.createOrder(new Customer("Alex", 29, "7231876238", 05, Gender.MALE));
            System.out.println("Created new order with ID: " + orderId);
        } catch (CustomerNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Генерирует случайную дату в прошлом от начала 1900 года до текущей даты.
     *
     * @return случайная дата
     */
    public static Date generateRandomDate() {
        Random random = new Random();
        Calendar calendar = Calendar.getInstance();
        // Генерируем случайное количество дней от начала 1900 года до текущей даты
        int randomDays = random.nextInt((int) (System.currentTimeMillis() / (1000 * 60 * 60 * 24)));
        // Устанавливаем случайную дату
        calendar.add(Calendar.DAY_OF_YEAR, -randomDays);
        return calendar.getTime();
    }

    /**
     * Создает заказы для списка клиентов, списка продуктов и рынка.
     *
     * @param customers список клиентов
     * @param products  список продуктов
     * @param market    рынок
     * @return массив заказов, созданных для клиентов
     */
    public static Order[] createOrders(List<Customer> customers, List<Product> products, Market market) {
        Order[] orders = new Order[customers.size()];
        Random random = new Random();
        for (int i = 0; i < customers.size(); i++) {
            Order order = new Order(customers.get(i));
            for (int j = 0; j < random.nextInt(products.size() + 1); j++) {
                Product randomProduct = products.get(random.nextInt(products.size()));
                int quantity = random.nextInt(5) + 1;
                Date randomDate = generateRandomDate();
                buy(market, customers.get(i), randomProduct, quantity, randomDate);
            }
            orders[i] = order;
        }
        return orders;
    }

    //    public static void printOrders(Order[] orders) {
//        for (Order order : orders) {
//            System.out.println("Order for customer: " + order.getCustomer().getName());
//            System.out.println("Products:");
//            for (Map.Entry<Product, Integer> entry : order.getProducts().entrySet()) {
//                System.out.println(entry.getKey().getName() + " - Quantity: " + entry.getValue());
//            }
//            System.out.println();
//        }
//    }
//    public static void printOrders(List<Customer> customers) {
//        for (Customer customer : customers){
//
//        }
//    }

    /**
     * Метод выводит общую сумму, потраченную каждым клиентом на рынке.
     *
     * @param market    рынок, с которого получается общая сумма
     * @param customers список клиентов, для которых нужно вывести общую сумму потраченных средств
     */
    public static void printTotalSpentByCustomers(Market market, List<Customer> customers) {
        for (Customer customer : customers) {
            double totalSpentByCustomer = market.countPrice(customer.getId());
            System.out.println("Total spent by customer with ID " + customer.getName() + ": " + totalSpentByCustomer);
        }
    }

    /**
     * @param market    рынок, на котором происходит покупка
     * @param customer  клиент, совершающий покупку
     * @param product   продукт, который покупается
     * @param quantity  количество продукта, которое покупается
     * @param orderData дата заказа
     */
    public static void buy(Market market, Customer customer, Product product, int quantity, Date orderData) {
        try {
            Order order = new Order(customer);
            Holiday holiday = order.determineHoliday(orderData);
            if (holiday != null) {
                System.out.println("Today is holiday: " + holiday.name());
            }
            int orderId = market.createOrder(customer);
            market.addProductToOrder(orderId, product, quantity);
            System.out.println(market.getOrders());
        } catch (CustomerNotFoundException | ProductNotFoundException | QuantityIsNegativeException e) {
            System.out.println(e.getMessage());
        }
    }


}

