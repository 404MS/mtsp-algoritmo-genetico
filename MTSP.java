import genetic.Population;
import genetic.Routes;

import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

import genetic.GeneticAlgorithm;
import model.Product;
import model.TimeRange;
import model.Vehicle;
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
    
    DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
    /**
     * HARD RESTRICTIONS
     * At least as many workers as vehicles
     */

    // Cost parameters
    int overtimeBike = 8;
    int overtimeCar = 12;
    int lateDeliveryPenalty = 20; // Per hour/fraction

    // Time and shift settings
    LocalDateTime curTime = LocalDateTime.of(2021, 05, 04, 8, 00);

    // TimeRange shift = new TimeRange(22, LocalDate.now(), 6, LocalDate.now().plusDays(1));
    // TimeRange breakRange = new TimeRange(4, LocalDate.now().plusDays(1), 6, LocalDate.now().plusDays(1));
    TimeRange shift = new TimeRange(curTime, curTime.plusHours(8));
    TimeRange breakRange = new TimeRange(curTime.plusHours(4), curTime.plusHours(6));

    // System.out.println();
    // System.out.println("Starting time: "+dateFormat.format(curTime));
    // System.out.println();

    // Random delivery deadlines
    LocalDate from = LocalDate.of(2021, 4, 30);
    LocalDate to = LocalDate.of(2021, 5, 4);
    long days = from.until(to, ChronoUnit.DAYS);

    // Create depot
    int depotX = 45, depotY = 30;
    Product depot = new Product(depotX,depotY);

    // Create vehicles
    ArrayList<Vehicle> vehicles = new ArrayList<Vehicle>();
    for (int i = 0; i < 40; i++){
      vehicles.add(new Vehicle(4, 60, 3, 0));
    }
    for (int i = 0; i < 20; i++){
      vehicles.add(new Vehicle(25, 30, 5, 1));
    }
    int numVehicles = vehicles.size();

    // Create workers (at least as many workers as vehicles)
    ArrayList<Worker> workers = new ArrayList<>();
    for(int i = 0 ; i < 60; i++){
      Random r = new Random();
      workers.add(new Worker(i, false));
    }

    // Create products/destinations
    
    ArrayList<Product> products = new ArrayList<>();
    
    // Loop to create random locations
    // int numProducts = 100;
    // for (int i = 0; i < numProducts; i++) {
    // 	Random r = new Random();
    // 	// Generate x,y position
    // 	int xPos = r.nextInt(maxXCoordinate - minXCoordinate + 1) + minXCoordinate;
    // 	int yPos = r.nextInt(maxYCoordinate - minYCoordinate + 1) + minYCoordinate;
    // 	// Generate random Deadline
    // 	long randomDays = ThreadLocalRandom.current().nextLong(days + 1);
    // 	LocalDate randomDate = from.plusDays(randomDays);
    // 	LocalTime randomTime = LocalTime.of(r.nextInt(24),r.nextInt(60));
    // 	LocalDateTime deadline = LocalDateTime.of(randomDate, randomTime);
    // 	// Add Order
    // 	products.add(new Product(i, xPos, yPos, deadline,r.nextBoolean()));
    // }
    
    // Read input products
    try {
      File myObj = new File("input-100.prn");
      Scanner myReader = new Scanner(myObj);
      int pid=0;
      while (myReader.hasNext()) {
        int posX = myReader.nextInt();
        int posY = myReader.nextInt();
        int yyyy = myReader.nextInt();
        int mm = myReader.nextInt();
        int dd = myReader.nextInt();
        int HH = myReader.nextInt();
        int MM = myReader.nextInt();
        products.add(new Product(pid, posX, posY, LocalDateTime.of(yyyy,mm,dd,HH,MM), true));
        pid++;
      }
      myReader.close();
    } catch (FileNotFoundException e) {
      System.out.println("An error occurred.");
      e.printStackTrace();
    }

    // Select first C products that can be carried by total capacity of vehicles
    
    // Order by deadline
    Collections.sort(products);

    ArrayList<Product> selectedProducts = new ArrayList<Product>();
    
    // Get total capacity of vehicles
    int totalCapacity = 0;
    for(int i = 0; i < vehicles.size(); i++){
      totalCapacity += vehicles.get(i).getCapacity();
    }

    // Select products up to total capacity
    int numSelectedProducts = 0;
    for (int i = 0; i < totalCapacity && i < products.size(); i++){
      selectedProducts.add(products.get(i));
      numSelectedProducts++;
    }

     // System.out.println("Number of products to send: " + numSelectedProducts);

    /**
     * Begins Genetic Algorithm
     * Repeat 40 times
     */
    
     

    int c = 0;
    while(c < 1) {
      final long startTime = System.currentTimeMillis();
      // Initial GA
      GeneticAlgorithm ga = new GeneticAlgorithm(200, 0.001, 0.8, 1, 5);

      // Initialize population
      Population population = ga.initPopulation(numSelectedProducts, numVehicles, vehicles, workers);
    
      // Evaluate population
      ga.evalPopulation(population, selectedProducts, vehicles, workers, depot, shift, breakRange, curTime, overtimeBike, overtimeCar, lateDeliveryPenalty);

      // Routes startRoute = new Routes(population.getFittest(0), selectedProducts, vehicles, workers, depot, shift, breakRange, curTime, overtimeBike, overtimeCar, lateDeliveryPenalty);
      // System.out.println("Start Cost: " + startRoute.getCost());

      // Keep track of current generation
      int generation = 1;

      // Start evolution loop
      while (ga.isTerminationConditionMet(generation, maxGenerations) == false) {
        // Print fittest individual from population
        // Routes routes = new Routes(population.getFittest(0), selectedProducts, vehicles, workers, depot, shift, breakRange, curTime, overtimeBike, overtimeCar, lateDeliveryPenalty);
        // if(generation % 1000 == 0){
        //   System.out.println("G"+generation+" Best cost: " + routes.getCost());
        // }
        
        // Apply crossover
        population = ga.crossoverPopulation(population, vehicles, workers);

        // Apply mutation
        population = ga.mutatePopulation(population, selectedProducts.size(), vehicles.size());

        // Evaluate population
        ga.evalPopulation(population, selectedProducts, vehicles, workers, depot, shift, breakRange, curTime, overtimeBike, overtimeCar, lateDeliveryPenalty);

        // Increment the current generation
        generation++;
      }
      Routes routes = new Routes(population.getFittest(0), selectedProducts, vehicles, workers, depot, shift, breakRange, curTime, overtimeBike, overtimeCar, lateDeliveryPenalty);
      // System.out.println(routes.getCost());
      

      final long endTime = System.currentTimeMillis();
      
      //System.out.println(endTime - startTime);
      System.out.println("Stopped after " + maxGenerations + " generations.");
      System.out.println("Best cost: " + routes.getCost());
      System.out.println(population.getFittest(0));
      routes.printRoutes();
      c++;
    }

    
    // System.out.println();
    // System.out.println("Total execution time: " + (endTime - startTime));
    // System.out.println();


   }
}
