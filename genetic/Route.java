package genetic;

import java.util.ArrayList;

import model.Product;
import model.Vehicle;
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
	private Product depot;
	private Vehicle worker;

	private double distance;
	private double cost;
	private double time;

	/**
	 * Initialize Route
	 * 
	 * @param destinationsIndex
	 *            Array of destinations index where the worker will go
	 * @param destinations
	 *            The destinations referenced
	 * @param vehicle
	 * 						The worker assigned to the route
	 * @param depot
	 * 						The origin point of the worker
	 * 
	 */
	public Route(int[] destinationsIndex, ArrayList<Product> destinations, Vehicle vehicle, Product depot) {

		this.distance = 0;
		this.cost = 0;
		this.time = 0;

		// Create route
		if(destinationsIndex != null) {
			this.route = new Product[destinationsIndex.length];
			
			for (int i = 0; i < route.length; i++) {
				this.route[i] = destinations.get(destinationsIndex[i]);
			}
		}
		else {
			this.route = null;
		}

		// Assign coordinates
		this.depot = new Product(depot);
		this.worker = new Vehicle(vehicle);
	}
	/**
	 * Initialize Blank Route
	 * 
	 * @param destinationsIndex
	 *            Array of destinations index where the worker will go
	 * @param destinations
	 *            The destinations referenced
	 * @param worker
	 * 						The worker assigned to the route
	 * @param depot
	 * 						The origin point of the worker
	 * 
	 */
	public Route(ArrayList<Product> destinations, Vehicle worker, Product depot) {

		this.distance = 0;
		this.cost = 0;
		this.time = 0;
		this.route = null;

		// Assign coordinates
		this.depot = new Product(depot);
		this.worker = new Vehicle(worker);
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
		if (this.route == null) {
			return 0;
		}

		// Loop over cities in route and calculate route distance
		double totalDistance = 0;
		
		totalDistance += this.depot.distanceFrom(this.route[0]);
		for (int i = 0; i + 1 < this.route.length; i++) {
			totalDistance += this.route[i].distanceFrom(this.route[i + 1]);
		}

		this.distance = totalDistance;

		return totalDistance;
	}

	/**
	 * Get route cost
	 * 
	 * @return cost The route's total cost
	 */
	public double getCost() {
		if (this.cost > 0) {
			return this.cost;
		}
		if (this.route == null) {
			return 0;
		}

		double totalCost = this.getDistance() * worker.getCostPerKm();

		this.cost = totalCost;

		return totalCost;
	}

	/**
	 * Get route time
	 * 
	 * @return time The route's total duration
	 */
	public double getTime() {
		if (this.time > 0) {
			return this.time;
		}
		if (this.route == null) {
			return 0;
		}

		double totalTime = this.getDistance() / worker.getSpeed();

		this.time = totalTime;

		return totalTime;
	}

	public void printRoute () {

	}
}