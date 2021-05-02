package genetic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;

import model.Vehicle;
import model.Worker;

public class Population {
	private Individual population[];
	private double populationFitness = -1;
	private int n;
	private int m;

	/**
	 * Initializes blank population of individuals
	 * 
	 * @param populationSize
	 *            The size of the population
	 */
	public Population(int populationSize) {
		// Initial population
		this.population = new Individual[populationSize];
	}

	/**
	 * Initializes population of individuals
	 * 
	 * @param populationSize
	 *            The size of the population
	 * @param numDestinations
	 *            The length of the first part of the chromosome
	 * @param numVehicles
	 *            The length of the second and third part of the chromosome
	 * @param vehicles
	 *            Array of vehicles to check capacity of each one to generate valid individual
	 */
	public Population(int populationSize, int numDestinations, int numVehicles, ArrayList<Vehicle> vehicles, ArrayList<Worker> workers) {
		// Initial population
		this.population = new Individual[populationSize];

		// Loop over population size
		for (int i = 0; i < populationSize; i++) {
			// Create individual
			Individual individual = new Individual(numDestinations, numVehicles, vehicles, workers);
			// Add individual to population
			this.population[i] = individual;
		}

		this.n = numDestinations;
		this.m = numVehicles;
	}

	public void setNumDestinations(int numDestinations) {
		this.n = numDestinations;
	}

	public void setNumVehicles(int numVehicles) {
		this.m = numVehicles;
	}

	/**
	 * Get individuals from the population
	 * 
	 * @return individuals Individuals in population
	 */
	public Individual[] getIndividuals() {
		return this.population;
	}

	/**
	 * Find fittest individual in the population
	 * 
	 * @param offset
	 * @return individual Fittest individual at offset
	 */
	public Individual getFittest(int offset) {
		// Order population by fitness
		Arrays.sort(this.population, new Comparator<Individual>() {
			@Override
			public int compare(Individual o1, Individual o2) {
				if (o1.getFitness() > o2.getFitness()) {
					return -1;
				} else if (o1.getFitness() < o2.getFitness()) {
					return 1;
				}
				return 0;
			}
		});

		// Return the fittest individual
		return this.population[offset];
	}

	/**
	 * Set population's fitness
	 * 
	 * @param fitness
	 *            The population's total fitness
	 */
	public void setPopulationFitness(double fitness) {
		this.populationFitness = fitness;
	}

	/**
	 * Get population's fitness
	 * 
	 * @return populationFitness The population's total fitness
	 */
	public double getPopulationFitness() {
		return this.populationFitness;
	}

	/**
	 * Get population's size
	 * 
	 * @return size The population's size
	 */
	public int size() {
		return this.population.length;
	}

	/**
	 * Set individual at offset
	 * 
	 * @param individual
	 * @param offset
	 * @return individual
	 */
	public void setIndividual(int offset, Individual individual) {

		population[offset] = new Individual(individual.getChromosome());
	}

	/**
	 * Get individual at offset
	 * 
	 * @param offset
	 * @return individual
	 */
	public Individual getIndividual(int offset) {
		return population[offset];
	}

	/**
	 * Shuffles the population in-place
	 * 
	 * @param void
	 * @return void
	 */
	public void shuffle() {
		Random rnd = new Random();
		for (int i = population.length - 1; i > 0; i--) {
			int index = rnd.nextInt(i + 1);
			Individual a = population[index];
			population[index] = population[i];
			population[i] = a;
		}
	}

	/**
	 * Get the number of salesmen
	 * 
	 * @return int
	 */
	public int getNumVehicles(){
		return this.m;
	}

	/**
	 * Get the number of destinations
	 * 
	 * @return int
	 */
	public int getNumDestinations(){
		return this.n;
	}

}