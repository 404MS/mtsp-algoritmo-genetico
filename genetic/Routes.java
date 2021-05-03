package genetic;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import model.Product;
import model.TimeRange;
import model.Vehicle;
import model.Worker;

public class Routes {
  private ArrayList<Route> routes;
  private double totalDistance;
  private double totalTime;
  private double totalCost;
  
  public Routes(Individual individual, ArrayList<Product> products, ArrayList<Vehicle> vehicles, ArrayList<Worker>workers, Product depot, TimeRange shift, TimeRange breakRange, LocalDateTime curTime, int overtimeBike, int overtimeCar, int lateDeliveryPenalty){

    routes = new ArrayList<>();
    int n = products.size();
    int m = vehicles.size();

    double totalDistance = 0, totalTime = 0, totalCost = 0;

    // Loop the individual's chromosome to generate m Routes
    int chromosome[] =  individual.getChromosome();
    int l, ot;
    for (int i = n, j=0, k=0; i < n+m; i++, j++){
      l = chromosome[i+m];
      if(vehicles.get(j).getType() == 0) {
        ot = overtimeBike;
      }
      else{
        ot = overtimeCar;
      }

      if(chromosome[i] == 0) {
        routes.add(new Route(products, vehicles.get(j), workers.get(l), depot, shift, breakRange, curTime, ot, lateDeliveryPenalty));
      }
      else{
        int aux[] = new int[chromosome[i]];
        for(int x=0; x<chromosome[i]; x++){
          aux[x] = chromosome[k];
          k++;
        }
        routes.add(new Route(aux, products, vehicles.get(j), workers.get(l), depot, shift, breakRange, curTime, ot, lateDeliveryPenalty));
        
        totalDistance += routes.get(j).getDistance();
        totalTime += routes.get(j).getTime();
        totalCost += routes.get(j).getCost();
      }
    }

    this.totalDistance = totalDistance;
    this.totalTime = totalTime;
    this.totalCost = totalCost;
  }

  public double getDistance() {
    return this.totalDistance;
  }

  public double getCost() {
    return this.totalCost;
  }

  public double getTime(){
    return this.totalTime;
  }

  public void printRoutes(){
    DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
    for(int i = 0; i < routes.size(); i++){
      System.out.println("Vehicle "+i);
      System.out.println("Worker " + routes.get(i).getWorker().getId());
      System.out.println(routes.get(i));
      System.out.println("ETA: " + dateFormat.format(routes.get(i).getEndTime()));
      System.out.println();
    }
  }
}
