package br.com.agenciacodeplus.socialcron.utils;

import java.util.Date;
import java.util.Calendar;

public class DateUtils {

  private static int SUM = 0;
  private static int SUBTRACT = 1;

  private static Date manipulateDate(Date date, int type, Integer quantity, int operation) {
    Calendar c = Calendar.getInstance();
    c.setTime(date);

    if (operation == 0) {
      c.set(type, c.get(type) + quantity);
    } else {
      c.set(type, c.get(type) - quantity);
    }

    return c.getTime();
  }

  public static Date sumDate(Date date, int type, Integer quantity) {
    return manipulateDate(date, type, quantity, SUM);
  }

  public static Date subtractDate(Date date, int type, Integer quantity) {
    return manipulateDate(date, type, quantity, SUBTRACT);
  }

}
