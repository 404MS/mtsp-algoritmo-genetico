import genetic.Population;
import genetic.Routes;

import java.util.ArrayList;
import java.util.Random;

import genetic.GeneticAlgorithm;
import model.Product;
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
	private static int maxYCoordinate = 25;
	private static int minXCoordinate = 0;
	private static int maxXCoordinate = 35;


	public static void main(String[] args) {
		
		// Create depot
		int depotX = 20, depotY = 30;
		Product depot = new Product(depotX,depotY);

		// Create workers
		ArrayList<Worker> workers = new ArrayList<Worker>();
		for (int i = 0; i < 2; i++){
			workers.add(new Worker(2, 60,3));
		}
		for (int i = 0; i < 2; i++){
			workers.add(new Worker(4, 30, 5));
		}
		int numWorkers = workers.size();

		// Create products/destinations
		int numProducts = 5;
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

		// Select first C products that can be carried by total capacity of workers
		
		// Order by deadline
		ArrayList<Product> selectedProducts = new ArrayList<Product>();
		
		// Get total capacity of workers
		int totalCapacity = 0;
		for(int i = 0; i < workers.size(); i++){
			totalCapacity += workers.get(i).getCapacity();
		}

		// Select products up to total capacity
		int numSelectedProducts = 0;
		for (int i = 0; i < totalCapacity && i < products.length; i++){
			selectedProducts.add(products[i]);
			numSelectedProducts++;
		}

		/**
		 * Begins Genetic Algorithm
		 */

		// Initial GA
		GeneticAlgorithm ga = new GeneticAlgorithm(5, 0.001, 0.9, 1, 3);

		// Initialize population
		Population population = ga.initPopulation(numSelectedProducts, numWorkers, workers);
	
		// Evaluate population
		ga.evalPopulation(population, selectedProducts, workers,depot);

		// Print population
		System.out.println("Initial population");
		for (int i = 0; i < population.size(); i++) {
			System.out.println(population.getIndividual(i));
		}

		Routes startRoute = new Routes(population.getFittest(0), selectedProducts, workers, depot);
		System.out.println("Start Cost: " + startRoute.getCost());



		// Keep track of current generation
		// int generation = 1;

		// // Start evolution loop
		// while (ga.isTerminationConditionMet(generation, maxGenerations) == false) {
		// 	// Print fittest individual from population
		// 	Route route = new Route(population.getFittest(0).getChromosome(), products, depot.getX(), depot.getY());
		// 	if(true){
		// 		System.out.println("G"+generation+" Best distance: " + route.getDistance());
		// 		System.out.printf("G"+generation+" Best chromosome:");
		// 		for(int i=0; i<population.getNumberDestinations()+population.getNumberSalesmen(); i++){
		// 			System.out.printf("%d ", population.getFittest(0).getChromosome()[i]);
		// 		}
		// 		System.out.println();
				
		// 	}
			
		// 	// Apply crossover
		// 	population = ga.crossoverPopulation(population);

		// 	// Print population
		// 	// System.out.println("Population after crossver");
		// 	// for (int i = 0; i < population.size(); i++) {
		// 	// 	int aux = i;
		// 	// 	for(int j = 0; j<population.getNumberDestinations()+population.getNumberSalesmen(); j++) {
		// 	// 		System.out.printf("%d ", population.getIndividual(aux).getChromosome()[j]);
		// 	// 	}
		// 	// 	System.out.println();
		// 	// }
		// 	// System.out.println();

		// 	// Apply mutation
		// 	//population = ga.mutatePopulation(population);

		// 	// Evaluate population
		// 	ga.evalPopulation(population, products, depot);

		// 	// Increment the current generation
		// 	generation++;
		// }
		
		// System.out.println("Stopped after " + maxGenerations + " generations.");
		// Route route = new Route(population.getFittest(0).getChromosome(), products, depot.getX(), depot.getY());
		// System.out.println("Best distance: " + route.getDistance());
		// route.printRoute();
	}
}
