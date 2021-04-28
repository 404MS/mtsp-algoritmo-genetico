package model;

import java.util.Date;

/**
 * A simple abstraction of a destination. This class maintains Cartesian coordinates
 * and also knows the Pythagorean theorem. Represents a single package to a customer.
 * 
 * @author ms
 *
 */
public class Product {
	private int x;
	private int y;
	private Date deadline;

	public Product (Product p){
		this.x = p.getX();
		this.y = p.getY();
		this.deadline = p.getDeadline();
	}

	public Product(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public Product(int x, int y, Date deadline) {
		this.x = x;
		this.y = y;
		this.deadline = deadline;
	}

	/**
	 * Calculate distance from another destination
	 * 
	 * Pythagorean theorem: a^2 + b^2 = c^2
	 * 
	 * @param product
	 *            The destination to calculate the distance from
	 * @return distance The distance from the given destination
	 */
	public double distanceFrom(Product product) {
		// Give difference in x,y
		double deltaXSq = Math.pow((product.getX() - this.getX()), 2);
		double deltaYSq = Math.pow((product.getY() - this.getY()), 2);

		// Calculate shortest path
		double distance = Math.sqrt(Math.abs(deltaXSq + deltaYSq));
		return distance;
	}

	public int getX() {
		return this.x;
	}

	public int getY() {
		return this.y;
	}

	public Date getDeadline() {
		return this.deadline;
	}

}
