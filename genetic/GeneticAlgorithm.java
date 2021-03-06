package genetic;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import model.Product;
import model.TimeRange;
import model.Vehicle;
import model.Worker;

public class GeneticAlgorithm {

  private int populationSize;
  private double mutationRate;
  private double crossoverRate;
  private int elitismCount;
  protected int tournamentSize;

  public GeneticAlgorithm(int populationSize, double mutationRate, double crossoverRate, int elitismCount,
      int tournamentSize) {

    this.populationSize = populationSize;
    this.mutationRate = mutationRate;
    this.crossoverRate = crossoverRate;
    this.elitismCount = elitismCount;
    this.tournamentSize = tournamentSize;
  }

  /**
   * Initialize population
   * 
   * @param numDestinations The number of destinations
   * @param numVehicles     The number of vehicles
   * @param vehicles        Array of vehicles to check capacities and generate
   *                        valid population
   * @return population The initial population generated
   */
  public Population initPopulation(int numDestinations, int numVehicles, ArrayList<Vehicle> vehicles,
      ArrayList<Worker> workers) {
    // Initialize population
    Population population = new Population(this.populationSize, numDestinations, numVehicles, vehicles, workers);
    return population;
  }

  /**
   * Check if population has met termination condition -- this termination
   * condition is a simple one; simply check if we've exceeded the allowed number
   * of generations.
   * 
   * @param generationsCount Number of generations passed
   * @param maxGenerations   Number of generations to terminate after
   * @return boolean True if termination condition met, otherwise, false
   */
  public boolean isTerminationConditionMet(int generationsCount, int maxGenerations) {
    return (generationsCount > maxGenerations);
  }

  /**
   * Calculate individual's fitness value
   * 
   * Fitness, in this problem, is inversely proportional to the route's total
   * distance. The total distance is calculated by the Route class.
   * 
   * @param individual the individual to evaluate
   * @param products   the destinations being referenced
   * @param vehicles   the vehicles being referenced
   * @param depot      the depot (only for its coordinates)
   * @return double The fitness value for individual
   */
  public double calcFitness(Individual individual, ArrayList<Product> products, ArrayList<Vehicle> vehicles,
      ArrayList<Worker> workers, Product depot, TimeRange shift, TimeRange breakRange, LocalDateTime curTime,
      int overtimeBike, int overtimeCar, int lateDeliveryPenalty) {
    // Get fitness

    Routes routes = new Routes(individual, products, vehicles, workers, depot, shift, breakRange, curTime, overtimeBike,
        overtimeCar, lateDeliveryPenalty);

    double fitness = 1 / routes.getCost();

    // Store fitness
    individual.setFitness(fitness);

    return fitness;
  }

  /**
   * Evaluate population -- basically run calcFitness on each individual.
   * 
   * @param population the population to evaluate
   * @param products   the products being referenced
   * @param vehicles   the vehicles being referenced
   * @param depot      the point of origin for each route
   * 
   */
  public void evalPopulation(Population population, ArrayList<Product> products, ArrayList<Vehicle> vehicles,
      ArrayList<Worker> workers, Product depot, TimeRange shift, TimeRange breakRange, LocalDateTime curTime,
      int overtimeBike, int overtimeCar, int lateDeliveryPenalty) {
    double populationFitness = 0;

    // Loop over population evaluating individuals and summing population fitness

    // Linear
    for (Individual individual : population.getIndividuals()) {
      populationFitness += this.calcFitness(individual, products, vehicles, workers, depot, shift, breakRange, curTime,
          overtimeBike, overtimeCar, lateDeliveryPenalty);
    }

    // Threads
    // ExecutorService threadPool = Executors.newFixedThreadPool(population.size());
    // for(int i = 0; i < population.size(); i++){
    // threadPool.submit(new Runnable (){
    // public void run() {
    // calcFitness(population.getIndividual(i), products, vehicles, depot);
    // }
    // });
    // }

    double avgFitness = populationFitness / population.size();
    population.setPopulationFitness(avgFitness);
  }

  /**
   * Selects parent for crossover using tournament selection
   * 
   * Tournament selection was introduced in Chapter 3
   * 
   * @param population
   * 
   * @return The individual selected as a parent
   */
  public Individual selectParent(Population population) {
    // Create tournament
    Population tournament = new Population(this.tournamentSize);

    // Add random individuals to the tournament
    population.shuffle();
    for (int i = 0; i < this.tournamentSize; i++) {
      Individual tournamentIndividual = population.getIndividual(i);
      tournament.setIndividual(i, tournamentIndividual);
    }

    // Return the best
    return tournament.getFittest(0);
  }

  /**
   * Crossover operation:
   * 
   * We use ordered crossover for the first part of the chromosome so the set of
   * destinations in each individual remains the same (no repeated nodes)
   * 
   * For the second part part, we assume the array of vehicles is ordered by type
   * therefore for each type we use asexual single point crossover so there's no
   * risk of assigning more orders than a worker can carry
   * 
   * 
   * @param population
   * @param vehicles
   * @return The new population
   */
  public Population crossoverPopulation(Population population, ArrayList<Vehicle> vehicles, ArrayList<Worker> workers) {
    int n = population.getNumDestinations();
    int m = population.getNumVehicles();

    int separationPoint = 0, size = vehicles.size();
    for (int i = 0; i < size - 1
        && vehicles.get(i).getCapacity() == vehicles.get(i + 1).getCapacity(); i++, separationPoint = i) {
    }
    separationPoint++;

    // Create new population
    Population newPopulation = new Population(population.size());
    newPopulation.setNumDestinations(n);
    newPopulation.setNumVehicles(m);

    // Loop over current population by fitness
    for (int populationIndex = 0; populationIndex < population.size(); populationIndex++) {
      // Get parent1
      Individual parent1 = population.getFittest(populationIndex);

      // Apply crossover to this individual?
      if (this.crossoverRate > Math.random() && populationIndex >= this.elitismCount) {
        // Find parent2 with tournament selection
        Individual parent2 = this.selectParent(population);

        // Create blank offspring chromosome
        int offspringChromosome[] = new int[n + m + m];
        Arrays.fill(offspringChromosome, -1);

        /**
         * First part of the chromosome using ordered crossover
         */
        Individual offspring = new Individual(offspringChromosome);

        // Get subset of parent chromosomes
        Random r = new Random();
        int substrPos1 = r.nextInt(n - 0 + 1) + 0;
        int substrPos2 = r.nextInt(n - 0 + 1) + 0;

        // make the smaller the start and the larger the end
        final int startSubstr = Math.min(substrPos1, substrPos2);
        final int endSubstr = Math.max(substrPos1, substrPos2);

        // Loop and add the sub tour from parent1 to our child
        for (int i = startSubstr; i < endSubstr; i++) {
          offspring.setGene(i, parent1.getGene(i));
        }

        // Loop through parent2's city tour
        for (int i = 0; i < n; i++) {
          int parent2Gene = i + endSubstr;
          if (parent2Gene >= n) {
            parent2Gene -= n;
          }

          // If offspring doesn't have the city add it
          if (offspring.containsDestination(parent2.getGene(parent2Gene), n) == false) {
            // Loop to find a spare position in the child's tour
            for (int ii = 0; ii < n; ii++) {
              // Spare position found, add city
              if (offspring.getGene(ii) == -1) {
                offspring.setGene(ii, parent2.getGene(parent2Gene));
                break;
              }
            }
          }
        }

        /**
         * Second part of the chromosome using single point asexual crossover for each
         * type of vehicle (differentiaded with its capacity)
         * 
         * First find the point where there's the change of vehicle type
         */

        int crossPoint = (int) Math.random() * separationPoint;

        for (int i = n, j = crossPoint; i < crossPoint; i++, j++) {
          offspring.setGene(i, parent1.getGene(j));
        }

        for (int i = n + crossPoint, j = n; i < n + separationPoint; i++, j++) {
          offspring.setGene(i, parent1.getGene(j));
        }

        if (separationPoint < size) {
          crossPoint = (int) Math.random() * (m - separationPoint);

          for (int i = n + separationPoint - 1, j = crossPoint; i < crossPoint; i++, j++) {
            offspring.setGene(i, parent1.getGene(j));
          }

          for (int i = n + separationPoint - 1 + crossPoint, j = n + separationPoint - 1; i < m + n; i++, j++) {
            offspring.setGene(i, parent1.getGene(j));
          }
        }

        /**
         * Third part of the chromosome using ordered crossover
         */

        // Get subset of parent chromosomes
        int substrPosA = r.nextInt(n+m+m - (n+m) + 1) + n+m;
        int substrPosB = r.nextInt(n+m+m - (n+m) + 1) + n+m;

        // make the smaller the start and the larger the end
        final int startSubstr2 = Math.min(substrPosA, substrPosB);
        final int endSubstr2 = Math.max(substrPosA, substrPosB);

        // Loop and add the sub tour from parent1 to our child
        for (int i = startSubstr2; i < endSubstr2; i++) {
          offspring.setGene(i, parent1.getGene(i));
        }

        // Loop through parent2's workers
        for (int i = n+m; i < n+m+m; i++) {
          int parent2Gene = i;

          // If offspring doesn't have the worker add it
          if (offspring.containsWorker(parent2.getGene(parent2Gene), n, m) == false) {
            // Loop to find a spare position in the child's tour
            for (int ii = n+m; ii < n+m+m; ii++) {
              // Spare position found, add city
              if (offspring.getGene(ii) == -1) {
                offspring.setGene(ii, parent2.getGene(parent2Gene));
                break;
              }
            }
          }
        }

        // Add child
        newPopulation.setIndividual(populationIndex, offspring);
      } else {
        // Add individual to new population without applying crossover
        newPopulation.setIndividual(populationIndex, parent1);
      }
    }

    return newPopulation;
  }

  /**
   * Apply mutation to population
   * 
   * Because the traveling salesman problem must visit each city only once, this
   * form of mutation will randomly swap two genes instead of bit-flipping a gene
   * like in earlier examples.
   * 
   * @param population The population to apply mutation to
   * @return The mutated population
   */
  public Population mutatePopulation(Population population, int numProducts, int numVehicles) {
    // Initialize new population
    Population newPopulation = new Population(this.populationSize);
    newPopulation.setNumDestinations(numProducts);
    newPopulation.setNumVehicles(numVehicles);

    int n = population.getNumDestinations();
    int m = population.getNumVehicles();

    // Loop over current population by fitness
    for (int populationIndex = 0; populationIndex < population.size(); populationIndex++) {
      Individual individual = population.getFittest(populationIndex);

      // Skip mutation if this is an elite individual
      if (populationIndex >= this.elitismCount) {

        /**
         * Apply mutation to first part of the chromosome
         */

        // Loop over individual's genes
        for (int geneIndex = 0; geneIndex < n; geneIndex++) {

          // Does this gene need mutation?
          if (this.mutationRate > Math.random()) {
            // Get new gene position
            int newGenePos = (int) (Math.random() * n);
            // Get genes to swap
            int gene1 = individual.getGene(newGenePos);
            int gene2 = individual.getGene(geneIndex);
            // Swap genes
            individual.setGene(geneIndex, gene1);
            individual.setGene(newGenePos, gene2);
          }
        }

        /**
         * Apply mutation to second part of the chromosome
         */
        // Loop over individual's genes
        // for (int geneIndex = n; geneIndex < n + m; geneIndex++) {

        //   // Does this gene need mutation?
        //   if (this.mutationRate > Math.random()) {
        //     // Get new gene position
        //     int newGenePos = (int) (n + (Math.random() * (m + n)));
        //     // Get genes to swap
        //     int gene1 = individual.getGene(newGenePos);
        //     int gene2 = individual.getGene(geneIndex);
        //     // Swap genes
        //     individual.setGene(geneIndex, gene1);
        //     individual.setGene(newGenePos, gene2);
        //   }
        // }
      }

      // Add individual to population
      newPopulation.setIndividual(populationIndex, individual);
    }

    // Return mutated population
    return newPopulation;
  }

}
