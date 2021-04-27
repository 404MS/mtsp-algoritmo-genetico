package model;

import java.util.ArrayList;

/**
 * A simple abstraction of a worker. Represents an available worker in a
 * defined vehicle, with defined capacity and speed
 * 
 * @author ms
 *
 */

public class Worker {
  private ArrayList<Product> assignedOrders;
  private int capacity;
  private int speed;

	/**
	 * Initialize Worker
	 * 
	 * @param c
	 *            capacity in number of products
	 * @param s
	 *            average speed
	 */
  public Worker(int c, int s) {
    this.capacity = c;
    this.speed = s;
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
}
