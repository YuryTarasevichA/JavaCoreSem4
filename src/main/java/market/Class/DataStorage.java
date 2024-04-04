package market.Class;

import java.util.ArrayList;
import java.util.List;

abstract class DataStorage {
    final static List<Customer> customers = new ArrayList<>();
    final static List<Product> products = new ArrayList<>();
    final static List<Order> orders = new ArrayList<>();
}
