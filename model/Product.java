package model;

import java.time.LocalDateTime;

/**
 * A simple abstraction of a destination. This class maintains Cartesian coordinates
 * and also knows the Pythagorean theorem. Represents a single package to a customer.
 * 
 * @author ms
 *
 */
public class Product implements Comparable<Product>{
	private int id;
	private int x;
	private int y;
	private LocalDateTime deadline;
	private boolean isLast;

	public Product (Product p){
		this.x = p.getX();
		this.y = p.getY();
		this.deadline = p.getDeadline();
	}

	public Product(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public Product(int id, int x, int y, LocalDateTime deadline, boolean isLast) {
		this.id = id;
		this.x = x;
		this.y = y;
		this.deadline = deadline;
		this.isLast = isLast;
	}

	/**
	 * Calculate distance from another destination
	 * 
	 * Using Manhattan distance
	 * 
	 * @param product
	 *            The destination to calculate the distance from
	 * @return distance The distance from the given destination
	 */
	public double distanceFrom(Product product) {
		
		double xDif = Math.abs(this.getX() - product.getX());
		double yDif = Math.abs(this.getY() - product.getY());

		return xDif + yDif;
	}

	public boolean isLast() {
		return this.isLast;
	}

	public int getId() {
		return this.id;
	}

	public int getX() {
		return this.x;
	}

	public int getY() {
		return this.y;
	}

	public LocalDateTime getDeadline() {
		return this.deadline;
	}

	public String toString() {
		String prod = "";
		prod += this.id + ":  (" + this.x + "," + this.y + ") " + this.deadline + " - " + this.isLast;
		return prod;
	}

	@Override
	public int compareTo(Product o) {
		return this.getDeadline().compareTo(o.getDeadline());
	}

}
