package model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class TimeRange {
  public LocalDateTime start;
  public LocalDateTime end;

  public TimeRange(LocalDateTime start, LocalDateTime end) {
    this.start = start;
    this.end = end;
  }

  public TimeRange(int startHour, int endHour){
    
    LocalDate curDate = LocalDate.now();
    LocalTime start = LocalTime.of(startHour, 0);
    LocalTime end = LocalTime.of(endHour, 0);

    if(startHour<=endHour){
      this.start = LocalDateTime.of(curDate, start);
      this.end = LocalDateTime.of(curDate, end);
    }
    else {
      LocalDate tomorrow = curDate.plusDays(1);
      this.start = LocalDateTime.of(curDate, start);
      this.end = LocalDateTime.of(tomorrow, end);
    }
  }

  public boolean isWithinRange(LocalDateTime date) {
    return !(date.isBefore(this.start) || date.isAfter(this.end));
  }
}
