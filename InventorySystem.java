import java.util.*;

// Main Application Class
public class InventorySystem {
    private static Scanner scanner = new Scanner(System.in);
    private static UserManager userManager = new UserManager();
    private static ProductManager productManager = new ProductManager();
    private static OrderManager orderManager = new OrderManager();
    private static User currentUser = null;

    public static void main(String[] args) {
        initializeData();
        
        while (true) {
            if (currentUser == null) {
                showLoginMenu();
            } else {
                if (currentUser.isAdmin()) {
                    showAdminMenu();
                } else {
                    showCustomerMenu();
                }
            }
        }
    }

    private static void initializeData() {
        // Add default admin
        userManager.addUser(new User("admin", "admin123", true));
        
        // Add sample products
        productManager.addProduct(new Product(1, "Laptop", 999.99, 10));
        productManager.addProduct(new Product(2, "Mouse", 29.99, 50));
        productManager.addProduct(new Product(3, "Keyboard", 79.99, 30));
        productManager.addProduct(new Product(4, "Monitor", 299.99, 15));
        productManager.addProduct(new Product(5, "Headphones", 149.99, 25));
    }

    private static void showLoginMenu() {
        System.out.println("\n========== INVENTORY SYSTEM ==========");
        System.out.println("1. Login");
        System.out.println("2. Register");
        System.out.println("3. Exit");
        System.out.print("Choose option: ");
        
        int choice = getIntInput();
        
        switch (choice) {
            case 1:
                login();
                break;
            case 2:
                register();
                break;
            case 3:
                System.out.println("Goodbye!");
                System.exit(0);
            default:
                System.out.println("Invalid option!");
        }
    }

    private static void login() {
        System.out.print("Username: ");
        String username = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();
        
        currentUser = userManager.authenticate(username, password);
        if (currentUser != null) {
            System.out.println("Login successful! Welcome, " + username);
        } else {
            System.out.println("Invalid credentials!");
        }
    }

    private static void register() {
        System.out.print("Username: ");
        String username = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();
        
        if (userManager.addUser(new User(username, password, false))) {
            System.out.println("Registration successful!");
        } else {
            System.out.println("Username already exists!");
        }
    }

    private static void showAdminMenu() {
        System.out.println("\n========== ADMIN MENU ==========");
        System.out.println("1. View All Products");
        System.out.println("2. Add Product");
        System.out.println("3. Update Product");
        System.out.println("4. Delete Product");
        System.out.println("5. View All Orders");
        System.out.println("6. Logout");
        System.out.print("Choose option: ");
        
        int choice = getIntInput();
        
        switch (choice) {
            case 1:
                viewProducts();
                break;
            case 2:
                addProduct();
                break;
            case 3:
                updateProduct();
                break;
            case 4:
                deleteProduct();
                break;
            case 5:
                viewAllOrders();
                break;
            case 6:
                currentUser = null;
                System.out.println("Logged out successfully!");
                break;
            default:
                System.out.println("Invalid option!");
        }
    }

    private static void showCustomerMenu() {
        System.out.println("\n========== CUSTOMER MENU ==========");
        System.out.println("1. View Products");
        System.out.println("2. Add to Cart");
        System.out.println("3. View Cart");
        System.out.println("4. Update Cart Item");
        System.out.println("5. Remove from Cart");
        System.out.println("6. Checkout");
        System.out.println("7. View My Orders");
        System.out.println("8. Logout");
        System.out.print("Choose option: ");
        
        int choice = getIntInput();
        
        switch (choice) {
            case 1:
                viewProducts();
                break;
            case 2:
                addToCart();
                break;
            case 3:
                viewCart();
                break;
            case 4:
                updateCartItem();
                break;
            case 5:
                removeFromCart();
                break;
            case 6:
                checkout();
                break;
            case 7:
                viewMyOrders();
                break;
            case 8:
                currentUser = null;
                System.out.println("Logged out successfully!");
                break;
            default:
                System.out.println("Invalid option!");
        }
    }

    private static void viewProducts() {
        List<Product> products = productManager.getAllProducts();
        System.out.println("\n========== PRODUCTS ==========");
        for (Product p : products) {
            System.out.println(p);
        }
    }

    private static void addProduct() {
        System.out.print("Product ID: ");
        int id = getIntInput();
        System.out.print("Name: ");
        String name = scanner.nextLine();
        System.out.print("Price: ");
        double price = getDoubleInput();
        System.out.print("Stock: ");
        int stock = getIntInput();
        
        if (productManager.addProduct(new Product(id, name, price, stock))) {
            System.out.println("Product added successfully!");
        } else {
            System.out.println("Product ID already exists!");
        }
    }

    private static void updateProduct() {
        System.out.print("Product ID to update: ");
        int id = getIntInput();
        System.out.print("New Name: ");
        String name = scanner.nextLine();
        System.out.print("New Price: ");
        double price = getDoubleInput();
        System.out.print("New Stock: ");
        int stock = getIntInput();
        
        if (productManager.updateProduct(id, name, price, stock)) {
            System.out.println("Product updated successfully!");
        } else {
            System.out.println("Product not found!");
        }
    }

    private static void deleteProduct() {
        System.out.print("Product ID to delete: ");
        int id = getIntInput();
        
        if (productManager.deleteProduct(id)) {
            System.out.println("Product deleted successfully!");
        } else {
            System.out.println("Product not found!");
        }
    }

    private static void addToCart() {
        System.out.print("Product ID: ");
        int productId = getIntInput();
        System.out.print("Quantity: ");
        int quantity = getIntInput();
        
        Product product = productManager.getProduct(productId);
        if (product != null) {
            if (product.getStock() >= quantity) {
                currentUser.getCart().addItem(product, quantity);
                System.out.println("Added to cart!");
            } else {
                System.out.println("Insufficient stock! Available: " + product.getStock());
            }
        } else {
            System.out.println("Product not found!");
        }
    }

    private static void viewCart() {
        Cart cart = currentUser.getCart();
        System.out.println("\n========== YOUR CART ==========");
        cart.displayCart();
        System.out.println("Total: $" + String.format("%.2f", cart.getTotal()));
    }

    private static void updateCartItem() {
        System.out.print("Product ID to update: ");
        int productId = getIntInput();
        System.out.print("New Quantity: ");
        int quantity = getIntInput();
        
        Product product = productManager.getProduct(productId);
        if (product != null && product.getStock() >= quantity) {
            currentUser.getCart().updateItem(productId, quantity);
            System.out.println("Cart updated!");
        } else {
            System.out.println("Invalid product or insufficient stock!");
        }
    }

    private static void removeFromCart() {
        System.out.print("Product ID to remove: ");
        int productId = getIntInput();
        
        currentUser.getCart().removeItem(productId);
        System.out.println("Item removed from cart!");
    }

    private static void checkout() {
        Cart cart = currentUser.getCart();
        if (cart.isEmpty()) {
            System.out.println("Cart is empty!");
            return;
        }
        
        // Check stock availability
        for (CartItem item : cart.getItems()) {
            Product product = productManager.getProduct(item.getProduct().getId());
            if (product.getStock() < item.getQuantity()) {
                System.out.println("Insufficient stock for: " + product.getName());
                return;
            }
        }
        
        // Create order
        Order order = new Order(orderManager.getNextOrderId(), currentUser.getUsername(), cart);
        orderManager.addOrder(order);
        
        // Update stock
        for (CartItem item : cart.getItems()) {
            Product product = productManager.getProduct(item.getProduct().getId());
            product.setStock(product.getStock() - item.getQuantity());
        }
        
        cart.clear();
        System.out.println("Order placed successfully! Order ID: " + order.getOrderId());
    }

    private static void viewMyOrders() {
        List<Order> orders = orderManager.getOrdersByUser(currentUser.getUsername());
        System.out.println("\n========== MY ORDERS ==========");
        if (orders.isEmpty()) {
            System.out.println("No orders found.");
        } else {
            for (Order order : orders) {
                System.out.println(order);
            }
        }
    }

    private static void viewAllOrders() {
        List<Order> orders = orderManager.getAllOrders();
        System.out.println("\n========== ALL ORDERS ==========");
        if (orders.isEmpty()) {
            System.out.println("No orders found.");
        } else {
            for (Order order : orders) {
                System.out.println(order);
            }
        }
    }

    private static int getIntInput() {
        while (!scanner.hasNextInt()) {
            scanner.next();
            System.out.print("Invalid input. Enter a number: ");
        }
        int value = scanner.nextInt();
        scanner.nextLine();
        return value;
    }

    private static double getDoubleInput() {
        while (!scanner.hasNextDouble()) {
            scanner.next();
            System.out.print("Invalid input. Enter a number: ");
        }
        double value = scanner.nextDouble();
        scanner.nextLine();
        return value;
    }
}

// User Class
class User {
    private String username;
    private String password;
    private boolean isAdmin;
    private Cart cart;

    public User(String username, String password, boolean isAdmin) {
        this.username = username;
        this.password = password;
        this.isAdmin = isAdmin;
        this.cart = new Cart();
    }

    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public boolean isAdmin() { return isAdmin; }
    public Cart getCart() { return cart; }
}

// UserManager Class
class UserManager {
    private Map<String, User> users = new HashMap<>();

    public boolean addUser(User user) {
        if (users.containsKey(user.getUsername())) {
            return false;
        }
        users.put(user.getUsername(), user);
        return true;
    }

    public User authenticate(String username, String password) {
        User user = users.get(username);
        if (user != null && user.getPassword().equals(password)) {
            return user;
        }
        return null;
    }
}

// Product Class
class Product {
    private int id;
    private String name;
    private double price;
    private int stock;

    public Product(int id, String name, double price, int stock) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public double getPrice() { return price; }
    public int getStock() { return stock; }
    public void setName(String name) { this.name = name; }
    public void setPrice(double price) { this.price = price; }
    public void setStock(int stock) { this.stock = stock; }

    @Override
    public String toString() {
        return String.format("ID: %d | %s | $%.2f | Stock: %d", id, name, price, stock);
    }
}

// ProductManager Class
class ProductManager {
    private Map<Integer, Product> products = new HashMap<>();

    public boolean addProduct(Product product) {
        if (products.containsKey(product.getId())) {
            return false;
        }
        products.put(product.getId(), product);
        return true;
    }

    public boolean updateProduct(int id, String name, double price, int stock) {
        Product product = products.get(id);
        if (product == null) return false;
        product.setName(name);
        product.setPrice(price);
        product.setStock(stock);
        return true;
    }

    public boolean deleteProduct(int id) {
        return products.remove(id) != null;
    }

    public Product getProduct(int id) {
        return products.get(id);
    }

    public List<Product> getAllProducts() {
        return new ArrayList<>(products.values());
    }
}

// CartItem Class
class CartItem {
    private Product product;
    private int quantity;

    public CartItem(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    public Product getProduct() { return product; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public double getSubtotal() { return product.getPrice() * quantity; }

    @Override
    public String toString() {
        return String.format("%s x%d = $%.2f", product.getName(), quantity, getSubtotal());
    }
}

// Cart Class
class Cart {
    private Map<Integer, CartItem> items = new HashMap<>();

    public void addItem(Product product, int quantity) {
        if (items.containsKey(product.getId())) {
            CartItem item = items.get(product.getId());
            item.setQuantity(item.getQuantity() + quantity);
        } else {
            items.put(product.getId(), new CartItem(product, quantity));
        }
    }

    public void updateItem(int productId, int quantity) {
        if (quantity <= 0) {
            removeItem(productId);
        } else if (items.containsKey(productId)) {
            items.get(productId).setQuantity(quantity);
        }
    }

    public void removeItem(int productId) {
        items.remove(productId);
    }

    public List<CartItem> getItems() {
        return new ArrayList<>(items.values());
    }

    public double getTotal() {
        return items.values().stream().mapToDouble(CartItem::getSubtotal).sum();
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }

    public void clear() {
        items.clear();
    }

    public void displayCart() {
        if (items.isEmpty()) {
            System.out.println("Cart is empty");
        } else {
            for (CartItem item : items.values()) {
                System.out.println(item);
            }
        }
    }
}

// Order Class
class Order {
    private int orderId;
    private String username;
    private List<CartItem> items;
    private double total;
    private Date orderDate;

    public Order(int orderId, String username, Cart cart) {
        this.orderId = orderId;
        this.username = username;
        this.items = new ArrayList<>(cart.getItems());
        this.total = cart.getTotal();
        this.orderDate = new Date();
    }

    public int getOrderId() { return orderId; }
    public String getUsername() { return username; }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("\nOrder #%d | User: %s | Date: %s\n", orderId, username, orderDate));
        for (CartItem item : items) {
            sb.append("  ").append(item).append("\n");
        }
        sb.append(String.format("Total: $%.2f", total));
        return sb.toString();
    }
}

// OrderManager Class
class OrderManager {
    private Map<Integer, Order> orders = new HashMap<>();
    private int nextOrderId = 1;

    public void addOrder(Order order) {
        orders.put(order.getOrderId(), order);
    }

    public List<Order> getOrdersByUser(String username) {
        List<Order> userOrders = new ArrayList<>();
        for (Order order : orders.values()) {
            if (order.getUsername().equals(username)) {
                userOrders.add(order);
            }
        }
        return userOrders;
    }

    public List<Order> getAllOrders() {
        return new ArrayList<>(orders.values());
    }

    public int getNextOrderId() {
        return nextOrderId++;
    }
}