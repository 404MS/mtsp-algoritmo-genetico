package genetic;

import java.util.ArrayList;

import model.Product;
import model.Worker;

public class Routes {
  private ArrayList<Route> routes;
  private double totalDistance;
  private double totalTime;
  private double totalCost;
  
  public Routes(Individual individual, ArrayList<Product> products, ArrayList<Worker> workers, Product depot){

    routes = new ArrayList<>();

    double totalDistance = 0, totalTime = 0, totalCost = 0;

    // Loop the individual's chromosome to generate m Routes
    int chromosome[] =  individual.getChromosome();

    for (int i = products.size(), j=0, k=0; i < chromosome.length; i++, j++){
        if(chromosome[i] == 0) {
            routes.add(new Route(products, workers.get(j), depot));
        }
        else{
            int aux[] = new int[chromosome[i]];
            for(int x=0; x<chromosome[i]; x++){
                aux[x] = chromosome[k];
                k++;
            }
            routes.add(new Route(aux, products, workers.get(j), depot));
            
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

  }
}
