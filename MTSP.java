import genetic.Population;
import genetic.Route;

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
	private static int maxYCoordinate = 50;
	private static int minXCoordinate = 0;
	private static int maxXCoordinate = 70;


	public static void main(String[] args) {
		
		// Create depot
		Product depot = new Product(45,30);

		// Create workers
		ArrayList<Worker> workers = new ArrayList<Worker>();
		for (int i = 0; i < 16; i++){
			workers.add(new Worker(4, 60));
		}
		for (int i = 0; i < 24; i++){
			workers.add(new Worker(25, 30));
		}

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

		// Initial GA
		GeneticAlgorithm ga = new GeneticAlgorithm(numProducts, 0.001, 0.9, 2, 5);

		// Initialize population
		Population population = ga.initPopulation(products.length, workers.size());

		// Evaluate population
		ga.evalPopulation(population, products, depot);

		Route startRoute = new Route(population.getFittest(0), products, depot.getX(), depot.getY());
		System.out.println("Start Distance: " + startRoute.getDistance());

		// Keep track of current generation
		int generation = 1;
		// Start evolution loop
		while (ga.isTerminationConditionMet(generation, maxGenerations) == false) {
			// Print fittest individual from population
			Route route = new Route(population.getFittest(0), products, depot.getX(), depot.getY());
			if(generation % 1000 == 0){
				System.out.println("G"+generation+" Best distance: " + route.getDistance());
			}
			
			// Apply crossover
			population = ga.crossoverPopulation(population);

			// Apply mutation
			population = ga.mutatePopulation(population);

			// Evaluate population
			ga.evalPopulation(population, products, depot);

			// Increment the current generation
			generation++;
		}
		
		System.out.println("Stopped after " + maxGenerations + " generations.");
		Route route = new Route(population.getFittest(0), products, depot.getX(), depot.getY());
		System.out.println("Best distance: " + route.getDistance());
		route.printRoute();
	}
}
