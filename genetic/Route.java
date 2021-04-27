package genetic;

import model.Product;
/**
 * The main Evaluation class for the TSP. It's pretty simple -- given an
 * Individual (ie, a chromosome) and a list of canonical cities, calculate the
 * total distance required to travel to the cities in the specified order. The
 * result returned by getDistance() is used by GeneticAlgorithm.calcFitness.
 * 
 * @author bkanber
 *
 */

public class Route {
	private Product route[];
	private double distance = 0;
	private Product depot;

	/**
	 * Initialize Route
	 * 
	 * @param individual
	 *            A GA individual
	 * @param destinations
	 *            The cities referenced
	 * @param x
	 *            X coordinate of the depot
	 * @param y
	 *            Y coordinate of the depot
	 */
	public Route(Individual individual, Product destinations[], int x, int y) {
		// Get individual's chromosome
		int chromosome[] = individual.getChromosome();
		// Create route
		this.route = new Product[destinations.length];
		for (int geneIndex = 0; geneIndex < destinations.length; geneIndex++) {
			this.route[geneIndex] = destinations[chromosome[geneIndex]];
		}
		// Assign coordinates
		this.depot = new Product(x, y);
	}

	/**
	 * Get route distance
	 * 
	 * @return distance The route's distance
	 */
	public double getDistance() {
		if (this.distance > 0) {
			return this.distance;
		}

		// Loop over cities in route and calculate route distance
		double totalDistance = 0;
		
		totalDistance += this.depot.distanceFrom(this.route[0]);
		for (int i = 0; i + 1 < this.route.length; i++) {
			totalDistance += this.route[i].distanceFrom(this.route[i + 1]);
		}

		totalDistance += this.route[this.route.length - 1].distanceFrom(this.depot);
		this.distance = totalDistance;

		return totalDistance;
	}

	public void printRoute () {
		System.out.println("Route:");
		for(int i=0; i < this.route.length; i++) {
			System.out.println("    " + this.route[i].getX() + " " + this.route[i].getY());
		}
	}
}