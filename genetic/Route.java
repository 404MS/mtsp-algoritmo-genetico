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
	private boolean empty;

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
	public Route(int[] destinationsIndex, Product destinations[], int x, int y) {

		// Create route
		if(destinationsIndex.length <= destinations.length){
			this.route = new Product[destinationsIndex.length];
		}
		else {
			this.route = new Product[destinations.length];
		}
		
		for (int i = 0; i < route.length; i++) {
			this.route[i] = destinations[destinationsIndex[i]];
		}
		// Assign coordinates
		this.depot = new Product(x, y);
		this.empty = false;
	}

	public Route(int x, int y) {
		this.depot = new Product(x,y);
		this.empty = true;
	}

	public boolean isEmpty() {
		return this.empty;
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
		if (this.empty) {
			return 0;
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