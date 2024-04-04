package market;

import market.Class.Customer;
import market.Class.Market;
import market.Class.Order;
import market.Class.Product;
import market.Enum.Gender;
import market.Enum.Holiday;
import market.exeption.CustomerNotFoundException;
import market.exeption.InvalidDateFormatException;
import market.exeption.ProductNotFoundException;
import market.exeption.QuantityIsNegativeException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class MarketProgram {
    public static void main(String[] args) {
        Market market = new Market();

        List<Customer> customers = market.getCustomers();
        List<Product> products = market.getProducts();
        Order[] orders = createOrders(customers, products, market);
        Date randomDate = generateCurrentDateOrInputData();
//        printOrders(orders);
        printTotalSpentByCustomers(market, customers);
        System.out.println("===========================================");
        try {
            int orderId = market.createOrder(new Customer(6, "Alex", 23, "+375339123344", Gender.MALE));
            System.out.println("Created new order with ID: " + orderId);
        } catch (CustomerNotFoundException e) {
            System.out.println(e.getMessage());
        } catch (InvalidDateFormatException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Генерирует объект Date, представляющий текущую дату или дату, введенную пользователем.
     * Если пользовательский ввод имеет формат yyyy-MM-dd, он создает объект Date из этого ввода.
     * Если пользовательский ввод недействителен или пуст, используется текущая дата по умолчанию.
     * @return объект Date, представляющий текущую дату или пользовательскую дату
     */
    public static Date generateCurrentDateOrInputData() {
        Scanner scanner =new Scanner(System.in);
        System.out.println("Введите дату в формате yyyy-MM-dd: ");
        String inputData = scanner.nextLine();
        if (!inputData.isEmpty()) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date date = sdf.parse(inputData);
                return date;
            } catch (ParseException e) {
                System.out.println("Invalid date format. Using current date instead.");
                return new Date();
            }
        } else {
            return new Date();
        }
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
                Date randomDate = generateCurrentDateOrInputData();
                buy(market, customers.get(i), randomProduct, quantity, randomDate);
            }
            orders[i] = order;
        }
        return orders;
    }

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
        } catch (CustomerNotFoundException | ProductNotFoundException | QuantityIsNegativeException |
                 InvalidDateFormatException e) {
            System.out.println(e.getMessage());
        }
    }
//        public static void printOrders(Order[] orders) {
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


}

