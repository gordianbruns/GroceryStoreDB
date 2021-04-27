package edu.depauw.csc480.projectv4;

import java.io.PrintStream;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collection;
import java.util.Scanner;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.RollbackException;
import javax.persistence.TypedQuery;

import edu.depauw.csc480.projectv4.model.OrderedProduct;
import edu.depauw.csc480.projectv4.model.Product;
import edu.depauw.csc480.projectv4.model.RestockOrder;
import edu.depauw.csc480.projectv4.model.Shift;
import edu.depauw.csc480.projectv4.model.Vendor;
import edu.depauw.csc480.projectv4.model.Worker;
import edu.depauw.csc480.projectv4.model.WorkerShift;

public class Main {
	private static final Scanner in = new Scanner(System.in);
	private static final PrintStream out = System.out;
	
	public static void main(String[] args) {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("grocerystoredb");
		EntityManager em = emf.createEntityManager();
		
		displayMenu();
		loop: while(true) {
			switch(requestString("Selection? ")) {
			case "0":	// Quit
				break loop;
			
			case "1":	// Reset
				resetTables(em);
				break;
				
			case "2":	// List products
				listProducts(em);
				break;
				
			case "3":	// List shifts --> add that you can see the number of workers assigned
				// to that shift
				listShifts(em);
				break;
				
			case "4":	// List vendors
				listVendors(em);
				break;
				
			case "5":	// List workers
				listWorkers(em);
				break;
				
			case "6":	// Add product
				addProduct(em);
				break;
				
			case "7":	// Add shift
				addShift(em);
				break;
				
			case "8":	// Add vendor
				addVendor(em);
				break;
				
			case "9":	// Add worker
				addWorker(em);
				break;
				
			case "10":	// Re-stock product
				restockProduct(em);
				break;
				
			case "11":	// Change price of product
				changePrice(em);
				break;
				
			case "12":	// Assign a worker to a shift
				addWorkerShift(em);
				break;
				
			case "13":	// display shifts of a worker
				displayWorkerShift(em);
				break;
				
			case "14":	// Create restockOrder
				addRestockOrder(em);
				break;
				
			case "15":	// display restockOrders
				displayRestockOrder(em);
				break;
				
			case "16":	// display products in a restockOrder
				displayRestockOrderProducts(em);
				break;
				
			case "17":	// remove product
				removeProduct(em);
				break;
				
			case "18":	// remove shift
				removeShift(em);
				break;
				
			case "19":	// remove vendor
				removeVendor(em);
				break;
				
			case "20":	// remove worker
				removeWorker(em);
				break;
				
			default:
				displayMenu();
				break;
			}
		}
		
		em.close();
		out.println("Done");
	}
	
	private static void displayMenu() {
		out.println("0: Quit");
		out.println("1: Reset tables");
		out.println("2: List products");
		out.println("3: List shifts");
		out.println("4: List vendors");
		out.println("5: List workers");
		out.println("6: Add product");
		out.println("7: Add shift");
		out.println("8: Add vendor");
		out.println("9: Add worker");
		out.println("10: Restock product");
		out.println("11: Change price");
		out.println("12: Assign worker to shift");
		out.println("13: Display shifts of a worker");
		out.println("14: Create a restock order");
		out.println("15: Display restock orders");
		out.println("16: Display products in a restock order");
		out.println("17: Remove product");
		out.println("18: Remove shift");
		out.println("19: Remove vendor");
		out.println("20: Remove worker");
		out.println("Press enter on an emty input to see the menu again.");
	}
	
	private static String requestString(String prompt) {
		out.print(prompt);
		out.flush();
		return in.nextLine();
	}
	
	private static int requestInt(String prompt) {
		out.print(prompt);
		out.flush();
		int result = in.nextInt();
		in.nextLine();
		return result;
	}
	
	private static double requestDouble(String prompt) {
		out.print(prompt);
		out.flush();
		double result = in.nextDouble();
		in.nextLine();
		return result;
	}
	
	private static LocalTime requestTime(String prompt) {
		out.print(prompt);
		out.flush();
		String stringResult = in.nextLine();
		LocalTime result;
		try {
			result = LocalTime.of(Integer.parseInt(stringResult.substring(0, 2)),
					Integer.parseInt(stringResult.substring(3, 5)));
		} catch (Exception e) {
			out.println("Please check your format!");
			return null;
		}
		return result;
	}
	
	private static LocalDate requestDate(String prompt) {
		out.print(prompt);
		out.flush();
		String stringResult = in.nextLine();
		LocalDate result;
		try {
			result = LocalDate.of(Integer.parseInt(stringResult.substring(0, 4)),
					Integer.parseInt(stringResult.substring(5, 7)),
					Integer.parseInt(stringResult.substring(8, 10)));
		} catch (Exception e) {
			out.println("Please check your format!");
			return null;
		}
		return result;
	}
	
	/**
	 * Delete the contents of the tables, then reinsert the sample data.
	 * The order does not matter since foreign keys are not used in the created data.
	 * 
	 * @param em
	 */
	private static void resetTables(EntityManager em) {
		// Clear the tables
		EntityTransaction tx = em.getTransaction();
		tx.begin();
		
		em.createQuery("delete from OrderedProduct").executeUpdate();
		em.createQuery("delete from Product").executeUpdate();
		em.createQuery("delete from RestockOrder").executeUpdate();
		em.createQuery("delete from Shift").executeUpdate();
		em.createQuery("delete from Vendor").executeUpdate();
		em.createQuery("delete from Worker").executeUpdate();
		em.createQuery("delete from WorkerShift").executeUpdate();
		
		tryCommit(tx);
		
		em.clear();	// flush any locally-persisted objects
		
		// Now create the sample data objects and persist them
		tx = em.getTransaction();
		tx.begin();
		
		Product skittles = new Product("skittles", "Wrigley", 1.99, 50);
		Product coke = new Product("Coke", "Coca-Cola", 1.49, 50);
		Product pringles = new Product("Pringles", "Procter & Gamble", 2.79, 30);
		
		em.persist(skittles);
		em.persist(coke);
		em.persist(pringles);
		
		Shift a04_26_2021_8 = new Shift(LocalTime.of(8, 0), LocalTime.of(10, 0),
				LocalDate.of(2021, 4, 26));
		Shift a04_26_2021_10 = new Shift(LocalTime.of(10, 0), LocalTime.of(12, 0),
				LocalDate.of(2021, 4, 26));
		Shift a04_26_2021_12 = new Shift(LocalTime.of(12, 0), LocalTime.of(14, 0),
				LocalDate.of(2021, 4, 26));
		Shift a04_26_2021_14 = new Shift(LocalTime.of(14, 0), LocalTime.of(16, 0),
				LocalDate.of(2021, 4, 26));
		Shift a04_26_2021_16 = new Shift(LocalTime.of(16, 0), LocalTime.of(18, 0),
				LocalDate.of(2021, 4, 26));
		Shift a04_26_2021_18 = new Shift(LocalTime.of(18, 0), LocalTime.of(20, 0),
				LocalDate.of(2021, 4, 26));
		Shift a04_26_2021_20 = new Shift(LocalTime.of(20, 0), LocalTime.of(22, 0),
				LocalDate.of(2021, 4, 26));
		
		em.persist(a04_26_2021_8);
		em.persist(a04_26_2021_10);
		em.persist(a04_26_2021_12);
		em.persist(a04_26_2021_14);
		em.persist(a04_26_2021_16);
		em.persist(a04_26_2021_18);
		em.persist(a04_26_2021_20);
		
		Vendor firstVendor = new Vendor("first Vendor");
		Vendor secondVendor = new Vendor("second Vendor");
		Vendor thirdVendor = new Vendor("third Vendor");
		
		em.persist(firstVendor);
		em.persist(secondVendor);
		em.persist(thirdVendor);
		
		Worker gordian = new Worker("Gordian", "Bruns", "gordian@depauw.edu",
				LocalDate.of(2000, 9, 11), 10.0);
		Worker ben = new Worker("Ben", "Chapiro", "ben@gmail.com",
				LocalDate.of(1995, 3, 12), 12.0);
		
		em.persist(gordian);
		em.persist(ben);
		
		tryCommit(tx);
				
	}
	
	private static void tryCommit(EntityTransaction tx) {
		try {
			tx.commit();
		} catch (RollbackException ex) {
			ex.printStackTrace();
			tx.rollback();
		}
	}
	
	/**
	 * Print a table of all products with their id, name, company name, price, and stock.
	 * 
	 * @param em
	 */
	private static void listProducts(EntityManager em) {
		out.printf("%-3s %-10s %-17s %-8s %-4s\n", "Id", "Name", "Company", "Price", "Stock");
		out.println("-----------------------------------------------");
		
		EntityTransaction tx = em.getTransaction();
		tx.begin();
		
		String query = "select p from Product p";
		TypedQuery<Product> q = em.createQuery(query, Product.class);
		
		for (Product product : q.getResultList()) {
			out.printf("%3d %-10s %-17s %-6.2f %4d\n", product.getPId(), product.getName(),
					product.getCompanyName(), product.getPrice(), product.getStock());
		}
		
		tryCommit(tx);
	}
	
	/**
	 * Print a table of all shifts with their id, start time, end time, date,
	 * and number of workers assigned to the shift.
	 * 
	 * @param em
	 */
	private static void listShifts(EntityManager em) {
		out.printf("%3s %-10s %-10s %-10s %-7s\n", "Id", "Start time", "End time", "Date",
				"Workers");
		out.println("--------------------------------------------");
		
		EntityTransaction tx = em.getTransaction();
		tx.begin();
		
		String query1 = "select s from Shift s";
		TypedQuery<Shift> q1 = em.createQuery(query1, Shift.class);
		
		for(Shift shift : q1.getResultList()) {
			String query2 = "select ws from WorkerShift ws where ws.shift.sId = ?1";
			TypedQuery<WorkerShift> q2 = em.createQuery(query2, WorkerShift.class);
			q2.setParameter(1, shift.getSId());
			long numOfWorkers = q2.getResultList().size();
			out.printf("%3d %-10s %-10s %-10s %7d\n", shift.getSId(), shift.getStartTime().toString(),
					shift.getEndTime().toString(), shift.getDate().toString(), numOfWorkers);
		}
		
		tryCommit(tx);
	}
	
	/**
	 * Print a table of all vendors with their id and name.
	 * 
	 * @param em
	 */
	private static void listVendors(EntityManager em) {
		out.printf("%3s %-10s\n", "Id", "Name");
		out.println("-----------------");
		
		EntityTransaction tx = em.getTransaction();
		tx.begin();
		
		String query = "select v from Vendor v";
		TypedQuery<Vendor> q = em.createQuery(query, Vendor.class);
		
		for(Vendor vendor : q.getResultList()) {
			out.printf("%3d %-10s\n", vendor.getVId(), vendor.getName());
		}
		
		tryCommit(tx);
	}
	
	/**
	 * Print a table of all workers with their id, first name, last name, email,
	 * birth date, and salary.
	 * 
	 * @param em
	 */
	private static void listWorkers(EntityManager em) {
		out.printf("%3s %-12s %-12s %-18s %-10s %-8s\n", "Id", "First name", "Last name",
				"Email", "Birth date", "Salary");
		out.println("------------------------------------------------------------------");
		
		EntityTransaction tx = em.getTransaction();
		tx.begin();
		
		String query = "select w from Worker w";
		TypedQuery<Worker> q = em.createQuery(query, Worker.class);
		
		for(Worker worker : q.getResultList()) {
			out.printf("%3d %-12s %-12s %-18s %-10s %-8.2f\n", worker.getWId(), worker.getFName(),
					worker.getLName(), worker.getEmail(), worker.getBirthDate().toString(), worker.getSalary());
		}
		
		tryCommit(tx);
	}
	
	/**
	 * Request information to add a product to the database. Note that the id
	 * is automatically generated.
	 * 
	 * @param em
	 */
	private static void addProduct(EntityManager em) {
		String name = requestString("Name? ");
		String company = requestString("Company name? ");
		double price = requestDouble("Price? ");
		if (price < 0.0) {
			out.println("The price must be greater than or equal to 0");
			return;
		}
		int stock = requestInt("Stock? ");
		if (stock < 0) {
			out.println("The stock must be greater than or equal to 0");
			return;
		}
		
		EntityTransaction tx = em.getTransaction();
		tx.begin();
		
		Product product = new Product(name, company, price, stock);
		em.persist(product);
		
		tryCommit(tx);
	}

	/**
	 * Request information to add a shift to the database. Note that the id 
	 * is automatically generated.
	 * 
	 * @param em
	 */
	private static void addShift(EntityManager em) {
		LocalTime startTime = requestTime("Start time? (format: hh:mm) ");
		// check whether the input was valid
		if (startTime == null) {
			return;
		}
		LocalTime endTime = requestTime("End time? (format: hh:mm) ");
		// check whether the input was valid
		if (endTime == null) {
			return;
		}
		LocalDate date = requestDate("Date? (format: yyyy-mm-dd) ");
		// check whether the input was valid
		if (date == null) {
			return;
		}
		
		EntityTransaction tx = em.getTransaction();
		tx.begin();
		
		Shift shift = new Shift(startTime, endTime, date);
		em.persist(shift);
		
		tryCommit(tx);
	}
	
	/**
	 * Request information to add a vendor to the database. Note that the id
	 * is automatically generated.
	 * 
	 * @param em
	 */
	private static void addVendor(EntityManager em) {
		String name = requestString("Name? ");
		
		EntityTransaction tx = em.getTransaction();
		tx.begin();
		
		Vendor vendor = new Vendor(name);
		em.persist(vendor);
		
		tryCommit(tx);
	}
	
	/**
	 * Request information to add a worker to the database. Note that the id
	 * is automatically generated.
	 * 
	 * @param em
	 */
	private static void addWorker(EntityManager em) {
		String fName = requestString("First name? ");
		String lName = requestString("Last name? ");
		String email = requestString("Email? ");
		LocalDate birthDate = requestDate("Birthdate? (format: yyyy-mm-dd) ");
		double salary = requestDouble("Salary? ");
		
		EntityTransaction tx = em.getTransaction();
		tx.begin();
		
		Worker worker = new Worker(fName, lName, email, birthDate, salary);
		em.persist(worker);
		
		tryCommit(tx);
	}
	
	/**
	 * Request a product id to re-stock that product by a given number of products.
	 * 
	 * @param em
	 */
	private static void restockProduct(EntityManager em) {
		int pId = requestInt("Product id number? ");
		int numOfProducts = requestInt("Number of added products? ");
		if (numOfProducts <= 0) {
			out.println("The number of added products must be greater than 0");
			return;
		}
		
		EntityTransaction tx = em.getTransaction();
		tx.begin();
		
		try {
			Product product = em.find(Product.class, pId);	// null if the product id does not exist
			product.getPId();	// raise an error if product is null
			product.setStock(product.getStock() + numOfProducts);
		} catch (Exception e) {
			out.println("There is no product with id " + pId);
			tx.rollback();
			return;
		}
		
		tryCommit(tx);
	}
	
	/**
	 * Request product id and a new price to set a new price for the given product. Note that
	 * the product id must exist and that the price must be greater than or equal to 0.
	 * 
	 * @param em
	 */
	private static void changePrice(EntityManager em) {
		int pId = requestInt("Product id number? ");
		double newPrice = requestDouble("New price? ");
		if (newPrice < 0) {
			out.println("The price must be greater than or equal to 0");
			return;
		}
		
		EntityTransaction tx = em.getTransaction();
		tx.begin();
		
		try {
			Product product = em.find(Product.class, pId);	// null if the product id does not exist
			product.getPId();	// raise an error if product is null
			product.setPrice(newPrice);
		} catch (Exception e) {
			out.println("There is no product with id " + pId + ".");
			tx.rollback();
			return;
		}
		
		tryCommit(tx);
	}
	
	/**
	 * Request worker id and shift id to assign a worker to a shift. Note that both ids must exist.
	 * 
	 * @param em
	 */
	private static void addWorkerShift(EntityManager em) {
		int wId = requestInt("Worker id number? ");
		int sId = requestInt("Shift id number? ");
		
		EntityTransaction tx = em.getTransaction();
		tx.begin();
		
		try {
			Worker worker = em.find(Worker.class, wId);	// null if the product id does not exist
			Shift shift = em.find(Shift.class, sId);	// null if the product id does not exist
			worker.getWId();	// raise an error if product is null
			shift.getSId();		// raise an error if product is null
			WorkerShift workerShift = new WorkerShift(worker, shift);
			em.persist(workerShift);
		} catch (Exception e) {
			out.println("Either the worker id or the shift id does not exist.");
			tx.rollback();
			return;
		}
		
		tryCommit(tx);
	}
	
	/**
	 * Request worker id and display all shifts that he or she is assigned to.
	 * 
	 * @param em
	 */
	private static void displayWorkerShift(EntityManager em) {
		int wId = requestInt("Worker id number? ");
		
		out.printf("%3s %-10s %-10s %-10s\n", "Id", "Start time", "End time", "Date");
		out.println("-------------------------------------");
		
		EntityTransaction tx = em.getTransaction();
		tx.begin();
		
		TypedQuery<WorkerShift> q;
		
		try {
			String query = "select ws from WorkerShift ws where ws.worker.wId = ?1";
			q = em.createQuery(query, WorkerShift.class);
			q.setParameter(1, wId);
		} catch (Exception e) {
			out.println("There is no worker with id " + wId + ".");
			tx.rollback();
			return;
		}
		
		for(WorkerShift workerShift : q.getResultList()) {
			Shift shift = workerShift.getShift();
			out.printf("%3d %-10s %-10s %-10s\n", shift.getSId(), shift.getStartTime(),
					shift.getEndTime(), shift.getDate());
		}
		
		tryCommit(tx);
	}
	
	/**
	 * Request a worker id and a vendor id to place a re-stock order. The worker id represents
	 * the worker who is placing the order and the vendor id represents the vendor where the
	 * order is placed. Note that both ids must exist.
	 * Once the re-stock order is created, it will request a product id and how many of that product
	 * you want to order. As long as you do not enter "no", it will keep asking you to add products
	 * to that order.
	 * 
	 * @param em
	 */
	private static void addRestockOrder(EntityManager em) {
		int wId = requestInt("Worker id of worker who is ordering? ");
		int vId = requestInt("Vendor id you would you like to use? ");
		
		EntityTransaction tx = em.getTransaction();
		tx.begin();
		
		RestockOrder restockOrder;
		
		try {
			Worker worker = em.find(Worker.class, wId);	// null if the product id does not exist
			Vendor vendor = em.find(Vendor.class, vId);	// null if the product id does not exist
			worker.getWId();	// raise an error if product is null
			vendor.getVId();	// raise an error if product is null
			restockOrder = new RestockOrder(vendor, worker);
			em.persist(restockOrder);
		} catch (Exception e) {
			out.println("Either the worker id or the vendor id does not exist.");
			tx.rollback();
			return;
		}
		
		out.println("Enter the products you would like to add.");
		
		boolean loopCondition = true;
		while(loopCondition) {
			int pId = requestInt("Product id number? ");
			Product product;
			try {
				product = em.find(Product.class, pId);	// null if the product id does not exist
				product.getPId();	// raise an error if product is null
			} catch (Exception e) {
				out.println("There is no product with this id.");
				tx.rollback();
				return;
			}
			
			int numOfProducts = requestInt("Number of products?");
			
			OrderedProduct orderedProduct = new OrderedProduct(numOfProducts, restockOrder,
					product);
			em.persist(orderedProduct);
			// if we added all products we need to set the loopCondition equal to false
			// otherwise continue requesting product id and quantity
			String stringBoolean = requestString("Do you want to add more products? (yes or no) ");
			if(stringBoolean.equalsIgnoreCase("no")) {
				loopCondition = false;
			}
		}
		
		tryCommit(tx);	
	}
	
	/**
	 * Print a table of all re-stock order with the worker who placed the order and the vendor where
	 * the order was placed.
	 * 
	 * @param em
	 */
	private static void displayRestockOrder(EntityManager em) {
		out.printf("%-3s %-21s %-10s\n", "Id", "Worker name", "Vendor name");
		out.println("----------------------------------------");
		
		EntityTransaction tx = em.getTransaction();
		tx.begin();
		
		String query = "select ro from RestockOrder ro";
		TypedQuery<RestockOrder> q = em.createQuery(query, RestockOrder.class);
		
		for(RestockOrder restockOrder : q.getResultList()) {
			Worker worker = restockOrder.getWorker();
			Vendor vendor = restockOrder.getVendor();
			out.printf("%3d %-20s %-10s\n", restockOrder.getRoId(), worker.getFName() + " " +
			worker.getLName(), vendor.getName());
		}
		
		tryCommit(tx);
	}
	
	/**
	 * Request re-stock order id to print a table with all products that are in that re-stock order.
	 * Note that the re-stock order id must exist.
	 * 
	 * @param em
	 */
	private static void displayRestockOrderProducts(EntityManager em) {
		int roId = requestInt("Re-stock order id number? ");
		
		RestockOrder restockOrder;
		try {
			restockOrder = em.find(RestockOrder.class, roId);	// null if the product id does not exist
			restockOrder.getRoId();	// raise an error if product is null
		} catch (Exception e) {
			out.println("There is no restock order with id " + roId + ".");
			return;
		}
		
		EntityTransaction tx = em.getTransaction();
		tx.begin();
		
		out.printf("%-3s %-10s %-17s %-8s %-4s %-14s\n", "Id", "Name", "Company", "Price", "Stock", "Number ordered");
		
		Collection<OrderedProduct> orderedProducts = restockOrder.getOrderedProducts();
		
		for (OrderedProduct orderedProduct : orderedProducts) {
			Product product = orderedProduct.getProduct();
			int quantity = orderedProduct.getQuantity();
			out.printf("%3d %-10s %-17s %-8s %-4s %14d\n", product.getPId(), product.getName(),
					product.getCompanyName(), product.getPrice(), product.getStock(), quantity);
		}
		
		tryCommit(tx);
	}
	
	/**
	 * Request product id and remove that product from the database. Note that the product id must
	 * exist.
	 * 
	 * @param em
	 */
	private static void removeProduct(EntityManager em) {
		int pId = requestInt("Product id number? ");
		
		EntityTransaction tx = em.getTransaction();
		tx.begin();
		
		try {
			Product product = em.find(Product.class, pId);	// null if the product id does not exist
			product.getPId();	// raise an error if product is null
			em.remove(product);
		} catch (Exception e) {
			out.println("There is no product with id " + pId + ".");
			tx.rollback();
			return;
		}
		
		tryCommit(tx);
	}
	
	/**
	 * Request shift id and remove the shift from the database. Note that the shift id must exist.
	 * 
	 * @param em
	 */
	private static void removeShift(EntityManager em) {
		int sId = requestInt("Shift id number? ");
		
		EntityTransaction tx = em.getTransaction();
		tx.begin();
		
		try {
			Shift shift = em.find(Shift.class, sId);	// null if the product id does not exist
			shift.getSId();	// raise an error if product is null
			em.remove(shift);
		} catch (Exception e) {
			out.println("There is no shift with id " + sId + ".");
			tx.rollback();
			return;
		}
		
		tryCommit(tx);
	}
	
	/**
	 * Request vendor id and remove the vendor from the database. Note that the vendor id must
	 * exist.
	 * 
	 * @param em
	 */
	private static void removeVendor(EntityManager em) {
		int vId = requestInt("Vendor id number? ");
		
		EntityTransaction tx = em.getTransaction();
		tx.begin();
		
		try {
			Vendor vendor = em.find(Vendor.class, vId);	// null if the product id does not exist
			vendor.getVId();	// raise an error if product is null
			em.remove(vendor);
		} catch (Exception e) {
			out.println("There is no vendor with id " + vId + ".");
			tx.rollback();
			return;
		}
		
		tryCommit(tx);
	}
	
	/**
	 * Request worker id and remove the worker from the database. Note that the worker id must exist.
	 * 
	 * @param em
	 */
	private static void removeWorker(EntityManager em) {
		int wId = requestInt("Worker id number? ");
		
		EntityTransaction tx = em.getTransaction();
		tx.begin();
		
		try {
			Worker worker = em.find(Worker.class, wId);	// null if the product id does not exist
			worker.getWId();	// raise an error if product is null
			em.remove(worker);
		} catch (Exception e) {
			out.println("There is no worker with id " + wId + ".");
			tx.rollback();
			return;
		}
		
		tryCommit(tx);
	}

}
