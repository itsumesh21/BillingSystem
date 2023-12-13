import java.text.SimpleDateFormat;
import java.util.Scanner;
import java.util.InputMismatchException;
import java.io.*;


class Product {
    private final int id;
    private final String pname;
    private final int qty;
    private final double price;
    private final double totalPrice;

    Product(int id, String pname, int qty, double price, double totalPrice) {
        this.id = id;
        this.pname = pname;
        this.qty = qty;
        this.price = price;
        this.totalPrice = totalPrice;
    }

    public int getId() {
        return id;
    }

    public String getPname() {
        return pname;
    }

    public int getQty() {
        return qty;
    }

    public double getPrice() {
        return price;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    // Display header for the product table
    public static void displayHeader() {
        System.out.println("-----------------------------------------------------------------------------------------------------------------------------------");
        System.out.println("Product ID \t\tName\t\tQuantity\t\tRate \t\t\t\tTotal Price");
        System.out.println("-----------------------------------------------------------------------------------------------------------------------------------");
    }

    // Display a product's details in a formatted way
    public void display() {
        System.out.printf("   %5d             %-9s      %5d               %9.2f                       %14.2f\n", id, pname, qty, price, totalPrice);
    }
}

public class ShoppingBill {
    private static final int MAX_PRODUCTS = 100; // Set the maximum number of products

    // Add a product to the productList with user input
    public static int addProduct(Product[] productList, int productCount, Scanner scan) {
        String filename = "product.txt";
                    
        try(PrintWriter file = new PrintWriter(new FileWriter(filename))) {

            System.out.println("Enter the details of the new product: ");

            // Read and validate product ID
            int id;
            while (true) {
                System.out.print("Product ID (numeric value greater than 0 and unique): ");
                if (scan.hasNextInt()) {
                    id = scan.nextInt();
                    file.write("Product ID : "+ id + "\n");
                    if (id > 0 && isProductIdUnique(id, productList, productCount)) {
                        break;
                    } else {
                        System.out.println("Invalid input for Product ID. Please enter a unique numeric value greater than 0.");
                    }
                } else {
                    System.out.println("Invalid input for Product ID. Please enter a unique numeric value greater than 0.");
                    scan.next(); // Consume the invalid input to avoid an infinite loop
                }
            }

            scan.nextLine(); // Clear the buffer

            // Read product name
            String productName;
            while (true) {
                System.out.print("Product Name: ");
                productName = scan.nextLine().trim();
                file.write("Product Name : "+productName + "\n");
                if (!productName.isEmpty() && productName.matches("[a-zA-Z\\s]+")) {
                    break;
                } else {
                    System.out.println("Invalid input for Product Name. Please enter a valid name with letters and spaces.");
                }
            }

            // Read and validate quantity
            int quantity;
            while (true) {
                System.out.print("Quantity: ");
                if (scan.hasNextInt()) {
                    quantity = scan.nextInt();
                    file.write("Product Quantity : "+quantity + "\n");
                    if (quantity >= 0) {
                        break;
                    } else {
                        System.out.println("Invalid input for Quantity. Please enter a non-negative integer.");
                    }
                } else {
                    System.out.println("Invalid input for Quantity. Please enter a non-negative integer.");
                    scan.next(); // Consume the invalid input to avoid an infinite loop
                }
            }

            scan.nextLine(); // Clear the buffer

            // Read and validate price
            double price;
            while (true) {
                System.out.print("Price (per unit): ");
                if (scan.hasNextDouble()) {
                    price = scan.nextDouble();
                    file.write("Product price : "+price + "\n");
                    if (price >= 0) {
                        break;
                    } else {
                        System.out.println("Invalid input for Price. Please enter a non-negative value.");
                    }
                } else {
                    System.out.println("Invalid input for Price. Please enter a non-negative value.");
                    scan.next(); // Consume the invalid input to avoid an infinite loop
                }
            }

            scan.nextLine(); // Clear the buffer

            // Calculate total price
            double totalPrice = price * quantity;
            file.write("Product total price : "+totalPrice);

            // Create a new Product object and add it to the productList
            Product newProduct = new Product(id, productName, quantity, price, totalPrice);
            productList[productCount++] = newProduct;

            System.out.println("Product added successfully!");
            return productCount;
        } catch (Exception e) {
            e.printStackTrace();
            return productCount;
        }
    }

    // Helper method to check if a given product ID is unique in the productList
    private static boolean isProductIdUnique(int productId, Product[] productList, int productCount) {
        for (int i = 0; i < productCount; i++) {
            if (productList[i].getId() == productId) {
                return false; // Product ID is not unique
            }
        }
        return true; // Product ID is unique
    }

    // Perform admin tasks such as adding and displaying products

    static String filename = "admin.txt";
    public static void performAdminTasks(Product[] productList, Scanner scanner) {
    int attempts = 3;
    int productCount = 0;

    while (attempts > 0) {
        System.out.print("Enter the admin password: ");
        String adminPassword = scanner.next();
        scanner.nextLine();
        try(FileWriter file = new FileWriter(filename)){
            file.write("Password : "+adminPassword);
        }
        catch(Exception e){
            System.out.println(e);
        }

        if ("1234".equals(adminPassword)) {
            System.out.println("Password is correct. Performing admin tasks...");
            int adminChoice = 0; // Initialize adminChoice to an invalid value

            do {
                System.out.println("Admin Menu:");
                System.out.println("1. Add Product");
                System.out.println("2. Display Products");
                System.out.println("3. Exit");

                System.out.print("Enter your choice: ");

                try {
                    adminChoice = scanner.nextInt();
                    scanner.nextLine(); // Consume the newline character
                    
                    switch (adminChoice) {
                        case 1:
                            productCount = addProduct(productList, productCount, scanner);
                            
                            break;
                        case 2:
                            if (productCount == 0) {
                                System.out.println("No Items added yet.");
                            } else {
                                Product.displayHeader();
                                for (int i = 0; i < productCount; i++) {
                                    productList[i].display();
                                }
                            }
                            break;
                        case 3:
                            System.out.println("Exiting Admin Menu.");
                            return;
                        default:
                            System.out.println("Invalid choice. Please enter a valid option.");
                    }
                } catch (InputMismatchException e) {
                    System.out.println("Invalid input. Please enter a numeric value.");
                    scanner.nextLine(); // Consume the invalid input
                }
            } while (adminChoice != 3);

            scanner.nextLine();
            break;
        } else {
            attempts--;

            if (attempts > 0) {
                System.out.println("Incorrect password. " + attempts + " attempts remaining. Please try again.");
            } else {
                System.out.println("Out of attempts. Exiting program.");
                return;
            }
        }
    }
}

        // Perform customer tasks including creating an invoice
    public static void performCustomerTasks(Product[] productList, Scanner scan) {
        int id = 0;
        String productName = null;
        int quantity = 0;
        double price = 0.0;
        double totalPrice = 0.0;
        double overAllPrice = 0.0;
        double cgst, sgst, subtotal = 0.0, discount = 0.0;
        char choice = '\0';

        int counter = 0;

        System.out.println("\t\t\t\t--------------------Invoice-----------------");
        System.out.println("\t\t\t\t\t " + "  " + "BML BAZAR");
        System.out.println("\t\t\t\t\tBML munjal university");
        System.out.println("\t\t\t\t\t" + "    " + "Gurugram");
        System.out.println("GSTIN: 03AWBPP8756K592" + "\t\t\t\t\t\t\tContact: (+91) 9988776655");

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        java.util.Date date = new java.util.Date();
        java.util.Calendar calendar = java.util.Calendar.getInstance();
        String[] days = new String[]{"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};

        System.out.println("Date: " + formatter.format(date) + "  " + days[calendar.get(java.util.Calendar.DAY_OF_WEEK) - 1] + "\t\t\t\t\t\t (+91) 9998887770");

        int productCount = 0;
        String filename = "customer.txt";
        try(PrintWriter file = new PrintWriter(new FileWriter(filename))) {
            // Read and validate customer name
            String customerName;
            while (true) {
                System.out.print("Enter Customer Name: ");
                customerName = scan.nextLine().trim();
                file.write("Customer name : "+customerName+ "\n");
                if (!customerName.isEmpty() && customerName.matches("[a-zA-Z\\s]+")) {
                    break;
                } else {
                    System.out.println("Invalid input for Customer Name. Please enter a valid name with letters and spaces.");
                }
            }

            do {
                System.out.println("Enter the product details: ");

                int inputId;
                while (true) {
                    System.out.print("Product ID (numeric value greater than 0 and unique): ");
                    if (scan.hasNextInt()) {
                        inputId = scan.nextInt();
                        file.write("Product ID : "+inputId+ "\n");
                        if (inputId > 0 && isProductIdUnique(inputId, productList, productCount)) {
                            id = inputId;
                            break;
                        } else {
                            System.out.println("Invalid input for Product ID. Please enter a unique numeric value greater than 0.");
                        }
                    } else {
                        System.out.println("Invalid input for Product ID. Please enter a unique numeric value greater than 0.");
                        scan.next(); // Consume the invalid input to avoid an infinite loop
                    }
                }

                scan.nextLine(); // Consume the newline character

                // Read and validate product name
                while (true) {
                    System.out.print("Product Name: ");
                    productName = scan.nextLine().trim();
                    file.write("Product Name : "+productName+ "\n");
                    if (!productName.isEmpty() && productName.matches("[a-zA-Z\\s]+")) {
                        break;
                    } else {
                        System.out.println("Invalid input for Product Name. Please enter a valid name with letters and spaces.");
                    }
                }

                int inputQuantity;
                while (true) {
                    System.out.print("Quantity: ");
                    if (scan.hasNextInt()) {
                        inputQuantity = scan.nextInt();
                        file.write("Quantity : "+inputQuantity+ "\n");
                        if (inputQuantity >= 0) {
                            quantity = inputQuantity;
                            break;
                        } else {
                            System.out.println("Invalid input for Quantity. Please enter a non-negative integer.");
                        }
                    } else {
                        System.out.println("Invalid input for Quantity. Please enter a non-negative integer.");
                        scan.next(); // Consume the invalid input to avoid an infinite loop
                    }
                }

                double inputPrice;
                while (true) {
                    System.out.print("Price (per unit): ");
                    if (scan.hasNextDouble()) {
                        inputPrice = scan.nextDouble();
                        file.write("Price : "+inputPrice+ "\n");
                        if (inputPrice >= 0) {
                            price = inputPrice;
                            break;
                        } else {
                            System.out.println("Invalid input for Price. Please enter a non-negative value.");
                        }
                    } else {
                        System.out.println("Invalid input for Price. Please enter a non-negative value.");
                        scan.next(); // Consume the invalid input to avoid an infinite loop
                    }
                }

                totalPrice = price * quantity;
                overAllPrice += totalPrice;

                productList[counter++] = new Product(id, productName, quantity, price, totalPrice);

                System.out.print("Want to add more items? (y or n): ");
                choice = scan.next().charAt(0);
                scan.nextLine(); // consume the newline character
            } while (choice == 'y' || choice == 'Y');

            Product.displayHeader();

            for (int i = 0; i < counter; i++) {
                productList[i].display();
            }

            System.out.println("\n\t\t\t\t\t\t\t\t\t\tTotal Amount (Rs.) " + overAllPrice);

            discount = overAllPrice * 2 / 100;
            System.out.println("\n\t\t\t\t\t\t\t\t\t\t    Discount (Rs.) " + discount);

            subtotal = overAllPrice - discount;
            System.out.println("\n\t\t\t\t\t\t\t\t\t\t          Subtotal " + subtotal);

            sgst = overAllPrice * 12 / 100;
            System.out.println("\n\t\t\t\t\t\t\t\t\t\t          SGST (%) " + sgst);

            cgst = overAllPrice * 12 / 100;
            System.out.println("\n\t\t\t\t\t\t\t\t\t\t          CGST (%) " + cgst);

            System.out.println("\n\t\t\t\t\t\t\t\t\t\t     Invoice Total " + (subtotal + cgst + sgst));
            System.out.println("\t\t\t\t----------------Thank You for Shopping!!-----------------");
            System.out.println("\t\t\t\t                     Visit Again");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Main method to start the program
    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            Product[] productList = new Product[MAX_PRODUCTS];

            // Display the welcome message and user options
            int userChoice;
            do {
                System.out.println("                                                 ---Welcome! Please choose an option:--- ");
                System.out.println("1. Admin");
                System.out.println("2. Customer");
                System.out.println("3. Exit");
                System.out.println();
                System.out.print("Enter choice: ");

                try {
                    userChoice = Integer.parseInt(scanner.nextLine());

                    // Handle user choice
                    switch (userChoice) {
                        case 1:
                            performAdminTasks(productList, scanner);
                            break;
                        case 2:
                            performCustomerTasks(productList, scanner);
                            break;
                        case 3:
                            System.out.println("Exiting the program. Goodbye!");
                            break;
                        default:
                            System.out.println("Invalid option. Please choose a valid option.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input. Please enter a numeric value.");
                    userChoice = -1; // Set to an invalid value to loop again
                }
            } while (userChoice != 3);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}