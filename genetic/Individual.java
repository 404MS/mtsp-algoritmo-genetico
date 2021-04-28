package genetic;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Individual {
	
	/**
	 * In this case, the chromosome is an array of integers rather than a string.
	 * It'll be a two part chromosome of length n + m. First n genes 
	 * represent the visiting permutation for the salesmen. The remaining m genes 
	 * represent the number of cities for each salesman to visit.
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
	 * @param numSalesmen
	 * 						The number of salesmen
	 */
	public Individual(int numDestinations, int numSalesmen) {
		// Create random individual
		int[] individual;
		individual = new int[numDestinations + numSalesmen];
		
		/**
		 * In this case, we can no longer simply pick 0s and 1s -- we need to
		 * use every destination and salesman index available.
		 * 
		 * First, generate random permutation of destinations
		 * Then, random valid sequence of integers with total sum n
		 */

		List <Integer> destinations = new ArrayList<Integer>();
		for(int i = 0; i < numDestinations; i++) {
			destinations.add(i);
		}
		java.util.Collections.shuffle(destinations);

		List <Integer> salesmanToDestinations = new ArrayList<Integer>();
		int maxAvailable = numDestinations;
		int sumPrev = 0, aux = 0;
		for(int i = 0; i < numSalesmen; i++) {
			Random r = new Random();
			aux = r.nextInt((maxAvailable-sumPrev) - 0 + 1) + 0;
			sumPrev += aux;
			salesmanToDestinations.add(aux);
		}
		
		for (int gene = 0; gene < numDestinations; gene++) {
			individual[gene] = destinations.get(gene);
		}
		for (int gene = numDestinations, i = 0; gene < numSalesmen + numDestinations; gene++, i++) {
			individual[gene] = salesmanToDestinations.get(i);
		}
		
		this.chromosome = individual;
		destinations.clear();
		salesmanToDestinations.clear();
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


	
}
