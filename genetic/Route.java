package genetic;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

import model.Product;
import model.TimeRange;
import model.Vehicle;
import model.Worker;
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
	private double distances[];

	private Product depot;
	private Vehicle vehicle;
	private Worker worker;
	private TimeRange shift;
	private TimeRange breakRange;
	private LocalDateTime startTime;
	private LocalDateTime endTime;
	
	private int otRate;
	private int lateDeliveryPenalty;

	private double distance;
	private double cost;
	private double time;

	/**
	 * Initialize Route
	 * 
	 * @param destinationsIndex
	 *            Array of destinations index where the worker will go
	 * @param products
	 *            The destinations referenced
	 * @param vehicle
	 * 						The worker assigned to the route
	 * @param depot
	 * 						The origin point of the worker
	 * 
	 */
	public Route(int[] destinationsIndex, ArrayList<Product> products, Vehicle vehicle, Worker worker, Product depot, TimeRange shift, TimeRange breakRange, LocalDateTime curTime, int overtime, int lateDeliveryPenalty) {

		this.distance = 0;
		this.cost = 0;
		this.time = 0;

		// Create route
		if(destinationsIndex != null) {
			this.route = new Product[destinationsIndex.length];
			this.distances = new double[destinationsIndex.length];
			
			for (int i = 0; i < route.length; i++) {
				this.route[i] = products.get(destinationsIndex[i]);
				this.distances[i] = 0;
			}
		}
		else {
			this.route = null;
		}

		this.depot = new Product(depot);
		this.vehicle = new Vehicle(vehicle);
		this.worker = new Worker(worker);
		this.shift = new TimeRange(shift);
		this.breakRange = new TimeRange(breakRange);
		this.startTime = curTime;
		this.otRate = overtime;
		this.lateDeliveryPenalty = lateDeliveryPenalty;
	}
	/**
	 * Initialize Blank Route
	 * 
	 * @param products
	 *            The destinations referenced
	 * @param vehicle
	 * 						The vehicle assigned to the route
	 * @param worker
	 * 						The worker assigned to the route
	 * @param depot
	 * 						The origin point of the vehicle
	 * 
	 */
	public Route(ArrayList<Product> products, Vehicle vehicle, Worker worker, Product depot, TimeRange shift, TimeRange breakRange, LocalDateTime curTime, int overtime, int lateDeliveryPenalty) {

		this.distance = 0;
		this.cost = 0;
		this.time = 0;
		this.route = null;

		this.depot = new Product(depot);
		this.vehicle = new Vehicle(vehicle);
		this.worker = new Worker(worker);
		this.shift = new TimeRange(shift);
		this.breakRange = new TimeRange(breakRange);
		this.startTime = curTime;
		this.otRate = overtime;
		this.lateDeliveryPenalty = lateDeliveryPenalty;

		this.endTime = curTime;
	}

	public Vehicle getVehicle() {
		return this.vehicle;
	}

	public Worker getWorker() {
		return this.worker;
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
		
		this.distances[0] = this.depot.distanceFrom(this.route[0]);
		totalDistance += this.distances[0];
		for (int i = 0; i + 1 < this.route.length; i++) {
			this.distances[i+1] = this.route[i].distanceFrom(this.route[i + 1]);
			totalDistance += this.distances[i+1];
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

		// Get distance cost
		double distanceCost = this.getDistance() * vehicle.getCostPerKm();

		// Get route's end time
		// Consider break range -> can change start/end time
		int hours = (int) this.getTime();
		int minutes = (int) Math.round((this.getTime() - hours) * 60);
		this.endTime = this.startTime.plusHours(hours).plusMinutes(minutes);

		if(!worker.hadBreak() && !breakRange.isPastRange(this.startTime)){
			TimeRange endRange = new TimeRange(breakRange.getStart(), breakRange.getEnd().minusHours(1));
			if(!endRange.isPastRange(endTime)){
				this.startTime = breakRange.getEnd();
				this.endTime = this.startTime.plusHours(hours).plusMinutes(minutes);
			}	
		}

		// Get overtime cost
		double otCost = 0;
		if(!shift.isWithinRange(endTime)){
			otCost = this.otRate * shift.hoursPastRange(endTime);
		}

		// Get late delivery penalty cost
		double lateCost = 0;
		double arrivalTime, aHours, aMinutes;
		LocalDateTime aTime = this.startTime;
		for(int i=0; i < distances.length; i++){ 
			arrivalTime = distances[i]/this.vehicle.getSpeed();
			aHours = (int) arrivalTime;
			aMinutes = Math.round((arrivalTime - aHours)*60);
			aTime = aTime.plusHours((long)aHours).plusMinutes((long)aMinutes);
			if(aTime.isAfter(this.route[i].getDeadline())){
				long differenceInMin = ChronoUnit.MINUTES.between(this.route[i].getDeadline(), aTime);
				int hoursDiff = (int) Math.ceil ((differenceInMin / 60));
				lateCost += hoursDiff * lateDeliveryPenalty;
			}
		}

		this.cost = distanceCost + otCost + lateCost;

		return this.cost;
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

		double totalTime = this.getDistance() / vehicle.getSpeed();

		this.time = totalTime;

		return totalTime;
	}

	public LocalDateTime getEndTime(){
		return this.endTime;
	}

	public String toString() {
		String str = "";
		if(route == null) return "no destinations";
		for(int i = 0; i < this.route.length; i++) {
			str += "(" + this.route[i].getX() + "," + this.route[i].getY() + ")";
			if(i < this.route.length - 1) str += " -> ";
		}
		return str;
	}
}