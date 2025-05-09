import java.io.*;
import java.util.*;

public class ReportGenerator {
    static class TaskRunnable implements Runnable {
        private final String path;
        private double totalCost;
        private int totalAmount;
        private int totalDiscountSum;
        private int totalLines;
        private Product mostExpensiveProduct;
        private double highestCostAfterDiscount;

        public TaskRunnable(String path) {
            this.path = path;
            this.totalCost = 0;
            this.totalAmount = 0;
            this.totalDiscountSum = 0;
            this.totalLines = 0;
            this.highestCostAfterDiscount = 0;
            this.mostExpensiveProduct = null;
        }

        @Override
        public void run() {
            try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(",");
                    if (parts.length != 3) continue;

                    int productId = Integer.parseInt(parts[0]);
                    int amount = Integer.parseInt(parts[1]);
                    int discount = Integer.parseInt(parts[2]);

                    Product product = findProductById(productId);
                    if (product == null) continue;

                    double price = product.getPrice();
                    double discountedPrice = price * (1 - discount / 100.0);
                    double cost = discountedPrice * amount;

                    totalCost += cost;
                    totalAmount += amount;
                    totalDiscountSum += discount;
                    totalLines++;

                    if (cost > highestCostAfterDiscount) {
                        highestCostAfterDiscount = cost;
                        mostExpensiveProduct = product;
                    }
                }
            } catch (IOException e) {
                System.err.println("Error reading file: " + path);
                e.printStackTrace();
            }
        }

        public void makeReport() {
            System.out.println("Report for file: " + path);
            System.out.printf("Total cost: %.2f%n", totalCost);
            System.out.println("Total items bought: " + totalAmount);
            double avgDiscount = totalLines > 0 ? (double) totalDiscountSum / totalLines : 0;
            System.out.printf("Average discount: %.2f%%%n", avgDiscount);
            if (mostExpensiveProduct != null) {
                System.out.printf("Most expensive purchase: %s (%.2f)%n",
                        mostExpensiveProduct.getProductName(), highestCostAfterDiscount);
            } else {
                System.out.println("No valid purchases found.");
            }
            System.out.println();
        }

        private Product findProductById(int id) {
            for (Product p : productCatalog) {
                if (p != null && p.getProductID() == id) return p;
            }
            return null;
        }
    }

    static class Product {
        private int productID;
        private String productName;
        private double price;

        public Product(int productID, String productName, double price) {
            this.productID = productID;
            this.productName = productName;
            this.price = price;
        }

        public int getProductID() {
            return productID;
        }

        public String getProductName() {
            return productName;
        }

        public double getPrice() {
            return price;
        }
    }

    // Define the paths to the order detail text files
    private static final String[] ORDER_FILES = {
            "src/main/resources/2021_order_details.txt", "src/main/resources/2022_order_details.txt", "src/main/resources/2023_order_details.txt", "src/main/resources/2024_order_details.txt"
    };

    static Product[] productCatalog = new Product[9]; // assuming max 10 products

    public static void loadProducts() throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader("src/main/resources/Products.txt"))) {
            String line;
            int index = 0;
            while ((line = reader.readLine()) != null && index < productCatalog.length) {
                String[] parts = line.split(",");
                if (parts.length != 3) continue;

                int id = Integer.parseInt(parts[0]);
                String name = parts[1];
                double price = Double.parseDouble(parts[2]);

                productCatalog[index++] = new Product(id, name, price);
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        try {
            loadProducts();
        } catch (IOException e) {
            System.err.println("Failed to load product catalog.");
            return;
        }

        List<TaskRunnable> tasks = new ArrayList<>();
        List<Thread> threads = new ArrayList<>();

        for (String path : ORDER_FILES) {
            TaskRunnable task = new TaskRunnable(path);
            Thread thread = new Thread(task);
            tasks.add(task);
            threads.add(thread);
            thread.start();
        }

        for (Thread thread : threads) {
            thread.join();
        }

        for (TaskRunnable task : tasks) {
            task.makeReport();
        }
    }
}
