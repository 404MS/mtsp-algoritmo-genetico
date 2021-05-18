package model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

public class TimeRange {
  public LocalDateTime start;
  public LocalDateTime end;

  public TimeRange(LocalDateTime start, LocalDateTime end) {
    this.start = start;
    this.end = end;
  }

  public TimeRange(TimeRange tr){
    this.start = tr.getStart();
    this.end = tr.getEnd();
  }

  public TimeRange(int startHour, LocalDate startDay, int endHour, LocalDate endDay){
    LocalTime start = LocalTime.of(startHour, 0);
    LocalTime end = LocalTime.of(endHour, 0);

    this.start = LocalDateTime.of(startDay, start);
    this.end = LocalDateTime.of(endDay, end);
  }

  public boolean isWithinRange(LocalDateTime date) {
    return !(date.isBefore(this.start) || date.isAfter(this.end));
  }

  public boolean isPastRange(LocalDateTime date) {
    return date.isAfter(this.end);
  }

  /**
   * 
   * @param date
   * @return difference in hours between date and the end of the timerange
   */
  public double hoursPastRange(LocalDateTime date) {
    long minutes = (long) ChronoUnit.MINUTES.between(this.end, date);
    long hours = minutes / 60;
    minutes = minutes % 60;
    return (double) hours + (double) minutes / 60.0;
  }

  public LocalDateTime getStart(){
    return this.start;
  }

  public LocalDateTime getEnd(){
    return this.end;
  }
}
