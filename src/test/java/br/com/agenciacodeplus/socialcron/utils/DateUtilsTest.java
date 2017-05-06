package br.com.agenciacodeplus.socialcron.utils;

import static org.junit.Assert.assertEquals;
import java.util.Calendar;
import java.util.Date;
import org.junit.Test;

public class DateUtilsTest {

  private Calendar calendar = Calendar.getInstance();
  private Date firstDate;
  private Date secondDate;
  
  @Test
  public void testSumDate() {
    // Hours sum    
    calendar.set(2017, 04, 05, 10, 0, 0);
    firstDate = calendar.getTime();
    
    calendar.set(2017, 04, 05, 11, 0, 0);
    secondDate = calendar.getTime();
    
    assertEquals(DateUtils.sumDate(firstDate, Calendar.HOUR_OF_DAY, 0), firstDate);
    assertEquals(DateUtils.sumDate(firstDate, Calendar.HOUR_OF_DAY, 1), secondDate);
    
    // Days sum
    calendar.set(2017, 04, 05, 10, 0, 0);
    firstDate = calendar.getTime();
    
    calendar.set(2017, 04, 06, 10, 0, 0);
    secondDate = calendar.getTime();
    
    assertEquals(DateUtils.sumDate(firstDate, Calendar.DAY_OF_MONTH, 0), firstDate);
    assertEquals(DateUtils.sumDate(firstDate, Calendar.DAY_OF_MONTH, 1), secondDate);
    
    // Week sum
    calendar.set(2017, 04, 05, 10, 0, 0);
    firstDate = calendar.getTime();
    
    calendar.set(2017, 04, 12, 10, 0, 0);
    secondDate = calendar.getTime();
    
    assertEquals(DateUtils.sumDate(firstDate, Calendar.WEEK_OF_MONTH, 0), firstDate);
    assertEquals(DateUtils.sumDate(firstDate, Calendar.WEEK_OF_MONTH, 1), secondDate);
    
    // Sum with negative quantity
    calendar.set(2017, 04, 05, 10, 0, 0);
    firstDate = calendar.getTime();
    
    try {
      DateUtils.sumDate(firstDate, Calendar.HOUR_OF_DAY, -1);
    } catch(Exception e) {
      assertEquals(e.getClass(), IllegalArgumentException.class);
    }
    
  }

  @Test
  public void testSubtractDate() {
    // Hours subtract   
    calendar.set(2017, 04, 05, 10, 0, 0);
    firstDate = calendar.getTime();
    
    calendar.set(2017, 04, 05, 9, 0, 0);
    secondDate = calendar.getTime();
    
    assertEquals(DateUtils.subtractDate(firstDate, Calendar.HOUR_OF_DAY, 0), firstDate);
    assertEquals(DateUtils.subtractDate(firstDate, Calendar.HOUR_OF_DAY, 1), secondDate);
    
    // Days subtract
    calendar.set(2017, 04, 05, 10, 0, 0);
    firstDate = calendar.getTime();
    
    calendar.set(2017, 04, 04, 10, 0, 0);
    secondDate = calendar.getTime();
    
    assertEquals(DateUtils.subtractDate(firstDate, Calendar.DAY_OF_MONTH, 0), firstDate);
    assertEquals(DateUtils.subtractDate(firstDate, Calendar.DAY_OF_MONTH, 1), secondDate);
    
    // Week subtract
    calendar.set(2017, 04, 05, 10, 0, 0);
    firstDate = calendar.getTime();
    
    calendar.set(2017, 03, 28, 10, 0, 0);
    secondDate = calendar.getTime();
    
    assertEquals(DateUtils.subtractDate(firstDate, Calendar.WEEK_OF_MONTH, 0), firstDate);
    assertEquals(DateUtils.subtractDate(firstDate, Calendar.WEEK_OF_MONTH, 1), secondDate);
    
    // Subtract with negative quantity
    calendar.set(2017, 04, 05, 10, 0, 0);
    firstDate = calendar.getTime();
    
    try {
      DateUtils.subtractDate(firstDate, Calendar.HOUR_OF_DAY, -1);
    } catch(Exception e) {
      assertEquals(e.getClass(), IllegalArgumentException.class);
    }
  }

  @Test
  public void testStringToDateConstant() {
    try {
      DateUtils.stringToDateConstant("");
    } catch(Exception e) {
      assertEquals(e.getClass(), IllegalArgumentException.class);
    }
    
    try {
      DateUtils.stringToDateConstant(null);
    } catch(Exception e) {
      assertEquals(e.getClass(), IllegalArgumentException.class);
    }
    
    try {
      DateUtils.stringToDateConstant("invalid string");
    } catch(Exception e) {
      assertEquals(e.getClass(), IllegalArgumentException.class);
    }
    
    assertEquals(DateUtils.stringToDateConstant("minute"), new Integer(Calendar.MINUTE));
    assertEquals(DateUtils.stringToDateConstant("Minute"), new Integer(Calendar.MINUTE));
    assertEquals(DateUtils.stringToDateConstant("MINUTE"), new Integer(Calendar.MINUTE));
    
    assertEquals(DateUtils.stringToDateConstant("hour"), new Integer(Calendar.HOUR_OF_DAY));
    assertEquals(DateUtils.stringToDateConstant("Hour"), new Integer(Calendar.HOUR_OF_DAY));
    assertEquals(DateUtils.stringToDateConstant("HOUR"), new Integer(Calendar.HOUR_OF_DAY));
   
    assertEquals(DateUtils.stringToDateConstant("day"), new Integer(Calendar.DAY_OF_MONTH));
    assertEquals(DateUtils.stringToDateConstant("Day"), new Integer(Calendar.DAY_OF_MONTH));
    assertEquals(DateUtils.stringToDateConstant("DAY"), new Integer(Calendar.DAY_OF_MONTH));
    
    assertEquals(DateUtils.stringToDateConstant("week"), new Integer(Calendar.WEEK_OF_MONTH));
    assertEquals(DateUtils.stringToDateConstant("Week"), new Integer(Calendar.WEEK_OF_MONTH));
    assertEquals(DateUtils.stringToDateConstant("WEEK"), new Integer(Calendar.WEEK_OF_MONTH));
   
    assertEquals(DateUtils.stringToDateConstant("month"), new Integer(Calendar.MONTH));
    assertEquals(DateUtils.stringToDateConstant("Month"), new Integer(Calendar.MONTH));
    assertEquals(DateUtils.stringToDateConstant("MONTH"), new Integer(Calendar.MONTH));
    
    assertEquals(DateUtils.stringToDateConstant("year"), new Integer(Calendar.YEAR));
    assertEquals(DateUtils.stringToDateConstant("Year"), new Integer(Calendar.YEAR));
    assertEquals(DateUtils.stringToDateConstant("YEAR"), new Integer(Calendar.YEAR));
    
  }
  
  @Test
  public void testConvertTimestampToDate() {
    
    try {
      DateUtils.convertTimestampToDate(-1l);
    } catch(Exception e) {
      assertEquals(e.getClass(), IllegalArgumentException.class);
    }
    
    assertEquals(DateUtils.convertTimestampToDate(0l).getTime(), 0);
    assertEquals(DateUtils.convertTimestampToDate(1l).getTime(), 1);
    assertEquals(DateUtils.convertTimestampToDate(1493942400l).getTime(), 1493942400);
  }

}
