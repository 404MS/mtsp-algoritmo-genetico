package genetic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import model.Vehicle;
import model.Worker;

public class Individual {
	
	/**
	 * In this case, the chromosome is an array of integers
	 * It'll be a three part chromosome of length n + m + m
	 * 
	 * First part: represents each product to be sent
	 * Second part: represents each vehicle, cointains how many consecutive products it will carry
	 * Third part: represents which worker will drive each vehicle
	 * 
	 * @author ms
	 * 
	 */
	private int[] chromosome;
	private double fitness = -1;

	/**
	 * Initializes individual with specific chromosome
	 * 
	 * @param chromosome
	 *            The chromosome to give individual
	 */
	public Individual(int[] chromosome) {
		// Create individualchromosome
		this.chromosome = chromosome.clone();
	}

	/**
	 * Initializes random individual
	 * 
	 * @param numDestinations
	 *            The number of destinations
	 * @param numVehicles
	 * 						The number of vehicles
	 * @param vehicles
	 * 						Array of vehicles associated with individual
	 * @param workers
	 * 						Array of workers associated with individual
	 */
	public Individual(int numDestinations, int numVehicles, ArrayList<Vehicle> vehicles, ArrayList<Worker> workers) {
		// Create random individual
		int[] individual;
		individual = new int[numDestinations + numVehicles + numVehicles];
		
		/**
		 * First, generate random permutation of destinations
		 * Then, random valid sequence of integers with total sum n
		 * where each one is less or equal to its corresponding vehicles's capacity
		 * Finally, a random permutation of workers
		 */

		// list to represent each destination
		List <Integer> destinations = new ArrayList<Integer>();
		for(int i = 0; i < numDestinations; i++) {
			destinations.add(i);
		}
		// shuffle to get a random order
		java.util.Collections.shuffle(destinations);

		// list to represent number of ordered destinations each vehicle will visit
		List <Integer> vehicleToDestinations = new ArrayList<Integer>();
		for(int i = 0; i < numVehicles; i++) {
			vehicleToDestinations.add(0);
		}
		// get a random set of numbers that sum up to the number of destinations
		int randomVehicle;
		for(int i=0; i < numDestinations; i++){
			Random r = new Random();
			randomVehicle = r.nextInt(numVehicles);
			while(true){
				if(vehicleToDestinations.get(randomVehicle) == vehicles.get(randomVehicle).getCapacity()) {
					if(randomVehicle == numVehicles-1)	randomVehicle = -1;
					randomVehicle++;
				}
				else {
					break;
				}
			}
			vehicleToDestinations.set(randomVehicle, vehicleToDestinations.get(randomVehicle) + 1);
		}

		// list to represent each worker
		List<Integer> workersIndex = new ArrayList<Integer>();
		for(int i = 0; i < workers.size(); i++){
			workersIndex.add(i);
		}
		Collections.shuffle(workersIndex);
		// shuffle and select a random set of workers, one for each vehicle
		List<Integer> workerToVehicle = new ArrayList<Integer>();
		for(int i =0 ; i < numVehicles; i++){
			workerToVehicle.add(workersIndex.get(i));
		}
		
		// assign values to the individual's chromosome
		for (int gene = 0; gene < numDestinations; gene++) {
			individual[gene] = destinations.get(gene);
		}
		for (int gene = numDestinations, i = 0; gene < numVehicles + numDestinations; gene++, i++) {
			individual[gene] = vehicleToDestinations.get(i);
		}
		for (int gene = numDestinations + numVehicles, i = 0; gene < 2 * numVehicles + numDestinations; gene++, i++) {
			individual[gene] = workerToVehicle.get(i);
		}
		
		this.chromosome = individual;
		destinations.clear();
		vehicleToDestinations.clear();
		workersIndex.clear();
		workerToVehicle.clear();
	}

	/**
	 * Gets individual's chromosome
	 * 
	 * @return The individual's chromosome
	 */
	public int[] getChromosome() {
		return this.chromosome;
	}

	/**
	 * Gets individual's chromosome length
	 * 
	 * @return The individual's chromosome length
	 */
	public int getChromosomeLength() {
		return this.chromosome.length;
	}

	/**
	 * Set gene at offset
	 * 
	 * @param gene
	 * @param offset
	 */
	public void setGene(int offset, int gene) {
		this.chromosome[offset] = gene;
	}

	/**
	 * Get gene at offset
	 * 
	 * @param offset
	 * @return gene
	 */
	public int getGene(int offset) {
		return this.chromosome[offset];
	}

	/**
	 * Store individual's fitness
	 * 
	 * @param fitness
	 *            The individuals fitness
	 */
	public void setFitness(double fitness) {
		this.fitness = fitness;
	}

	/**
	 * Gets individual's fitness
	 * 
	 * @return The individual's fitness
	 */
	public double getFitness() {
		return this.fitness;
	}
	
	public String toString() {
		String output = "";
		for (int gene = 0; gene < this.chromosome.length; gene++) {
			output += this.chromosome[gene] + ",";
		}
		return output;
	}

	/**
	 * Search for a specific integer gene in this individual.
	 * 
	 * For instance, in a Traveling Salesman Problem where cities are encoded as
	 * integers with the range, say, 0-99, this method will check to see if the
	 * city "42" exists.
	 * 
	 * @param gene
	 * @return
	 */
	public boolean containsDestination(int gene, int n) {
		for (int i = 0; i < n; i++) {
			if (this.chromosome[i] == gene) {
				return true;
			}
		}
		return false;
	}

	public boolean containsWorker(int gene, int n, int m){
		for (int i = n+m; i < n+m+m; i++) {
			if (this.chromosome[i] == gene) {
				return true;
			}
		}
		return false;
	}
	
}
