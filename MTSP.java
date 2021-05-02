import genetic.Population;
import genetic.Routes;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import genetic.GeneticAlgorithm;
import model.Product;
import model.TimeRange;
import model.Vehicle;
import model.Worker;

/**
 * Main, executive class for the Multiple Traveling Salesman Problem.
 * 
 * Problem defined as having m salesman and n destinations, we need to
 * search the visiting sequences such that the total cost is minimized.
 * Each city can only be visited once by one salesman. All of them start
 * from and return to the same depot. Cada uno puede visitar a lo mucho
 * P destinos.
 * 
 * @author ms
 *
 */
public class MTSP {

	public static int maxGenerations = 10000;

	// Min and Max coordinates
	private static int minYCoordinate = 0;
	private static int maxYCoordinate = 50;
	private static int minXCoordinate = 0;
	private static int maxXCoordinate = 70;

	public static void main(String[] args) {
		
		DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");

		// Cost parameters
		int overtimeBike = 8;
		int overtimeCar = 12;
		int lateDeliveryPenalty = 20; // Per hour/fraction

		// Time and shift settings
		LocalDateTime curTime = LocalDateTime.now();

		TimeRange shift = new TimeRange(22, 6);
		TimeRange breakRange = new TimeRange(18, 20);

		System.out.println("Starting time: "+dateFormat.format(curTime));

		// Random delivery times
		LocalDate from = LocalDate.of(2021, 4, 30);
		LocalDate to = LocalDate.of(2021, 5, 4);
		long days = from.until(to, ChronoUnit.DAYS);

		// Create depot
		int depotX = 20, depotY = 30;
		Product depot = new Product(depotX,depotY);

		// Create vehicles
		ArrayList<Vehicle> vehicles = new ArrayList<Vehicle>();
		for (int i = 0; i < 10; i++){
			vehicles.add(new Vehicle(4, 60,3));
		}
		for (int i = 0; i < 5; i++){
			vehicles.add(new Vehicle(25, 30, 5));
		}
		int numVehicles = vehicles.size();

		// Create workers (at least as many workers as vehicles)
		ArrayList<Worker> workers = new ArrayList<>();
		for(int i = 0 ; i < 17; i++){
			Random r = new Random();
			workers.add(new Worker(i, r.nextBoolean()));
		}

		// Create products/destinations
		int numProducts = 10;
		ArrayList<Product> products = new ArrayList<>();
		
		// Loop to create random locations
		for (int i = 0; i < numProducts; i++) {
			Random r = new Random();
			// Generate x,y position
			int xPos = r.nextInt(maxXCoordinate - minXCoordinate + 1) + minXCoordinate;
			int yPos = r.nextInt(maxYCoordinate - minYCoordinate + 1) + minYCoordinate;
			// Generate random Deadline
			long randomDays = ThreadLocalRandom.current().nextLong(days + 1);
			LocalDate randomDate = from.plusDays(randomDays);
			LocalTime randomTime = LocalTime.of(r.nextInt(24),r.nextInt(60));
			LocalDateTime deadline = LocalDateTime.of(randomDate, randomTime);
			// Add Order
			products.add(new Product(i, xPos, yPos, deadline,r.nextBoolean()));
		}

		// Select first C products that can be carried by total capacity of vehicles
		
		// Order by deadline
		Collections.sort(products);

		ArrayList<Product> selectedProducts = new ArrayList<Product>();
		
		// Get total capacity of vehicles
		int totalCapacity = 0;
		for(int i = 0; i < vehicles.size(); i++){
			totalCapacity += vehicles.get(i).getCapacity();
		}

		// Select products up to total capacity
		int numSelectedProducts = 0;
		for (int i = 0; i < totalCapacity && i < products.size(); i++){
			selectedProducts.add(products.get(i));
			numSelectedProducts++;
		}

	 	System.out.println("Number of products to send: " + numSelectedProducts);

		/**
		 * Begins Genetic Algorithm
		 */
		
	// 	final long startTime = System.currentTimeMillis();

	// 	// Initial GA
	// 	GeneticAlgorithm ga = new GeneticAlgorithm(200, 0.001, 0.8, 1, 5);

	// 	// Initialize population
	// 	Population population = ga.initPopulation(numSelectedProducts, numVehicles, vehicles);
	
	// 	// Evaluate population
	// 	ga.evalPopulation(population, selectedProducts, vehicles,depot);

	// 	Routes startRoute = new Routes(population.getFittest(0), selectedProducts, vehicles, depot);
	// 	System.out.println("Start Cost: " + startRoute.getCost());

	// 	// Print population
	// 	// System.out.println("Initial population");
	// 	// for (int i = 0; i < population.size(); i++) {
	// 	// 	System.out.println(population.getIndividual(i));
	// 	// }

	// 	// Keep track of current generation
	// 	int generation = 1;

	// 	// Start evolution loop
	// 	while (ga.isTerminationConditionMet(generation, maxGenerations) == false) {
	// 		// Print fittest individual from population
	// 		Routes routes = new Routes(population.getFittest(0), selectedProducts, vehicles, depot);
	// 		if(generation % 1000 == 0){
	// 			System.out.println("G"+generation+" Best cost: " + routes.getCost());
	// 			System.out.printf("G"+generation+" Best chromosome: ");
	// 			System.out.println(population.getFittest(0));
	// 		}
			
	// 		// Apply crossover
	// 		population = ga.crossoverPopulation(population, vehicles);

	// 		// Apply mutation
	// 		//population = ga.mutatePopulation(population);

	// 		// Evaluate population
	// 		ga.evalPopulation(population, selectedProducts, vehicles,depot);

	// 		// Increment the current generation
	// 		generation++;
	// 	}
	// 	final long endTime = System.currentTimeMillis();
	// 	System.out.println();
	// 	System.out.println("Total execution time: " + (endTime - startTime));
	// 	System.out.println();

	// 	System.out.println("Stopped after " + maxGenerations + " generations.");
	// 	Routes routes = new Routes(population.getFittest(0), selectedProducts, vehicles, depot);
	// 	System.out.println("Best cost: " + routes.getCost());
	// 	routes.printRoutes();
	 }
}
