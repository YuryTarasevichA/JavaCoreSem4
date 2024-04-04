package market.Class;

import market.Enum.Gender;
import market.Enum.Holiday;
import market.Enum.ObjectType;
import market.exeption.CustomerNotFoundException;
import market.exeption.InvalidDateFormatException;
import market.exeption.ProductNotFoundException;
import market.exeption.QuantityIsNegativeException;

import java.io.*;
import java.util.*;


public class Market {
    private List<Customer> customers;
    private List<Product> products;
    private List<Order> orders;
    final File productsF = new File("products.txt");
    final File customerF = new File("customers.txt");
    final File ordersF = new File("orders.txt");

    public Market() {
        readData(customerF);
        System.out.println(DataStorage.customers);
        readData(productsF);
        readData(ordersF);
        customers = new ArrayList<>(List.of(
                new Customer(1, "Nick", 30, "+375295556788", Gender.MALE),
                new Customer(2, "Pop", 23, "+375295556788", Gender.MALE),
                new Customer(3, "Trevor", 41, "+375295556788", Gender.MALE),
                new Customer(4,"Petr", 34, "+375295556788", Gender.MALE),
                new Customer(5,"Masha", 25, "+375295435678", Gender.FEMALE)
        ));

        products = new ArrayList<>(List.of(
                new Product(1, "Meat", 12),
                new Product(2, "Milk", 2),
                new Product(3, "Beard", 1),
                new Product(4, "Oil", 4),
                new Product(5, "Chicken", 9)
        ));
        orders = new ArrayList<>();
    }
    void loadData(File file, List list, ObjectType type) {
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String[] prod = new String[3];
            String str;
            while ((str = br.readLine()) != null) {
                prod = str.split(",");
                System.out.println(Arrays.toString(prod));
                switch (type) {
                    case PRODUCT -> {
                        list.add(new Product(Integer.parseInt(prod[0]), prod[1], Integer.parseInt(prod[2])));
                    }
                    case CUSTOMER -> {
                        Customer customer = new Customer(Integer.parseInt(prod[0]), prod[1], Integer.parseInt(prod[2]),
                                prod[3], prod[4].equals("m") ? Gender.MALE : Gender.FEMALE);
                        System.out.println(customer);
                        list.add(customer);
                    }
                    default -> System.out.println("Incorrect type");
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + e);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println(Arrays.toString(e.getStackTrace()));
        }
    }
    void readData(File file) {
        switch (file.getName()) {
            case "products.txt":
                loadData(file, DataStorage.products, ObjectType.PRODUCT);
                break;
            case "customers.txt":
                loadData(file, DataStorage.customers, ObjectType.CUSTOMER);
                break;
            case "orders.txt":
                loadData(file, DataStorage.orders, ObjectType.ORDER);
                break;
            default:
                System.out.println("Incorrect file name");
                break;
        }
    }
    void saveData(File file, List list) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file, true))) {
            switch (file.getName()) {
                case "products.txt" -> {
                    for (Object prod : list) {
                        Product product = (Product) prod;
                        bw.write(product.getID() + "," + product.getName() + "," + product.getPrice());
                        bw.newLine();
                    }
                }
                case "customers.txt" -> {
                    for (Object cust : list) {
                        Customer customer = (Customer) cust;
                        bw.write(customer.getId() + "," + customer.getName() + "," + customer.getAge() + "," +
                                customer.getPhone() + "," + (customer.getGender() == Gender.MALE ? "m" : "f"));
                        bw.newLine();
                    }
                }
                case "orders.txt" -> {
                    for (Object ord : list) {
                        Order order = (Order) ord;
                        bw.write(order.getId() + "," + order.getCustomer().getId() + "," + order.getHoliday());
                        bw.newLine();
                        
                    }
                }
                default -> System.out.println("Incorrect file name");
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    void saveAllData() {
        saveData(productsF, products);
        saveData(customerF, customers);
        saveData(ordersF, orders);
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

    /**
     * Метод создает заказ для указанного клиента и добавляет его в список заказов.
     * Устанавливает дату заказа. Применяет скидки в зависимости от праздника и пола клиента.
     *
     * @param customer клиент, для которого создается заказ
     * @return идентификатор созданного заказа
     * @throws CustomerNotFoundException если клиент не найден в списке клиентов
     */
    public int createOrder(Customer customer) throws CustomerNotFoundException, InvalidDateFormatException {
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
    /**
     * Метод для добавления продукта в заказ.
     *
     * @param orderId идентификатор заказа
     * @param product добавляемый продукт
     * @param quantity количество продукта
     * @return обновленный заказ
     * @throws ProductNotFoundException если продукт не найден
     * @throws QuantityIsNegativeException если количество продукта отрицательное или равно нулю
     */
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
