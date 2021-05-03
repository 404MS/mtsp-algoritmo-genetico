package model;


public class Worker {

  /**
	 * It represents an available worker instance
   * Available as in: not on break, not on route
	 */

  private int id;
  private boolean hadBreak;

  private double overtime;

  /**
   * 
   * @param id
   *          Worker id
   * @param hb
   *          Boolean, if true worker's had break this shift
   */
  public Worker(int id, boolean hb){
    this.id = id;
    this.hadBreak = hb;
    this.overtime = 0;
  }

  public Worker(Worker w){
    this.id = w.getId();
    this.hadBreak = w.hadBreak();
    this.overtime = w.getOvertime();
  }

  public void addOvertime(double e){
    this.overtime += e;
  }

  public double getOvertime(){
    return this.overtime;
  }

  public int getId(){
    return this.id;
  }

  public boolean hadBreak(){
    return this.hadBreak;
  }

}
