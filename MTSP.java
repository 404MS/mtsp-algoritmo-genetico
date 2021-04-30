import genetic.Population;
import genetic.Routes;

import java.util.ArrayList;
import java.util.Random;

import genetic.GeneticAlgorithm;
import model.Product;
import model.Vehicle;

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
	private static int maxYCoordinate = 25;
	private static int minXCoordinate = 0;
	private static int maxXCoordinate = 35;


	public static void main(String[] args) {
		
		// Create depot
		int depotX = 20, depotY = 30;
		Product depot = new Product(depotX,depotY);

		// Create vehicles
		ArrayList<Vehicle> vehicles = new ArrayList<Vehicle>();
		for (int i = 0; i < 2; i++){
			vehicles.add(new Vehicle(10, 60,3));
		}
		for (int i = 0; i < 2; i++){
			vehicles.add(new Vehicle(80, 30, 5));
		}
		int numVehicles = vehicles.size();

		// Create products/destinations
		int numProducts = 100;
		Product products[] = new Product[numProducts];
		
		// Loop to create random locations
		for (int i = 0; i < numProducts; i++) {
			Random r = new Random();
			// Generate x,y position
			int xPos = r.nextInt(maxXCoordinate - minXCoordinate + 1) + minXCoordinate;
			int yPos = r.nextInt(maxYCoordinate - minYCoordinate + 1) + minYCoordinate;
			
			// Add Order
			products[i] = new Product(xPos, yPos);
		}

		// Select first C products that can be carried by total capacity of vehicles
		
		// Order by deadline
		ArrayList<Product> selectedProducts = new ArrayList<Product>();
		
		// Get total capacity of vehicles
		int totalCapacity = 0;
		for(int i = 0; i < vehicles.size(); i++){
			totalCapacity += vehicles.get(i).getCapacity();
		}

		// Select products up to total capacity
		int numSelectedProducts = 0;
		for (int i = 0; i < totalCapacity && i < products.length; i++){
			selectedProducts.add(products[i]);
			numSelectedProducts++;
		}

		System.out.println("Number of products to send: " + numSelectedProducts);

		/**
		 * Begins Genetic Algorithm
		 */

		// Initial GA
		GeneticAlgorithm ga = new GeneticAlgorithm(200, 0.001, 0.8, 1, 5);

		// Initialize population
		Population population = ga.initPopulation(numSelectedProducts, numVehicles, vehicles);
	
		// Evaluate population
		ga.evalPopulation(population, selectedProducts, vehicles,depot);

		Routes startRoute = new Routes(population.getFittest(0), selectedProducts, vehicles, depot);
		System.out.println("Start Cost: " + startRoute.getCost());

		// Print population
		// System.out.println("Initial population");
		// for (int i = 0; i < population.size(); i++) {
		// 	System.out.println(population.getIndividual(i));
		// }

		// Keep track of current generation
		int generation = 1;

		// Start evolution loop
		while (ga.isTerminationConditionMet(generation, maxGenerations) == false) {
			// Print fittest individual from population
			Routes routes = new Routes(population.getFittest(0), selectedProducts, vehicles, depot);
			if(generation % 1000 == 0){
				System.out.println("G"+generation+" Best cost: " + routes.getCost());
				System.out.printf("G"+generation+" Best chromosome: ");
				System.out.println(population.getFittest(0));
			}
			
			// Apply crossover
			population = ga.crossoverPopulation(population, vehicles);

			// Apply mutation
			//population = ga.mutatePopulation(population);

			// Evaluate population
			ga.evalPopulation(population, selectedProducts, vehicles,depot);

			// Increment the current generation
			generation++;
		}
		
		System.out.println("Stopped after " + maxGenerations + " generations.");
		Routes routes = new Routes(population.getFittest(0), selectedProducts, vehicles, depot);
		System.out.println("Best cost: " + routes.getCost());
		routes.printRoutes();
	}
}
