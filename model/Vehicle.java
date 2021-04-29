package model;

import java.util.ArrayList;

/**
 * A simple abstraction of a worker. Represents an available worker in a
 * defined vehicle, with defined capacity and speed
 * 
 * @author ms
 *
 */

public class Vehicle {
  private ArrayList<Product> assignedOrders;
  private int capacity;
  private int speed;
  private int costPerKm;

	/**
	 * Initialize Worker
	 * 
	 * @param c
	 *            capacity in number of products
	 * @param s
	 *            average speed in km/h
   * @param ck
	 *            cost per km
	 */
  public Vehicle(int c, int s, int ck) {
    this.capacity = c;
    this.speed = s;
    this.costPerKm = ck;
  }

  public Vehicle(Vehicle w){
    this.capacity = w.getCapacity();
    this.speed = w.getSpeed();
    this.costPerKm = w.getCostPerKm();
  }

  public int getCostPerKm() {
    return this.costPerKm;
  }

  public int getCapacity() {
    return this.capacity;
  }

  public int getSpeed() {
    return this.speed;
  }

  public ArrayList<Product> getAssignedOrders() {
    return this.assignedOrders;
  }

  public void assignOrders(ArrayList<Product> orders) {
    this.assignedOrders = orders;
  }

  public void assignOrder(Product order) {
    this.assignedOrders.add(order);
  }

  public void popOrder(Product order){
    if(this.assignedOrders.isEmpty()) return;
    this.assignedOrders.remove(0);
  }
}
