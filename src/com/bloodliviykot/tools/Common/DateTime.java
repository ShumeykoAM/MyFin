package com.bloodliviykot.tools.Common;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.TimeZone;

/**
 * Created by Kot on 09.10.2016.
 */
public class DateTime
  extends GregorianCalendar
{

  public DateTime()
  {
    super();
  }
  public DateTime(long milliseconds)
  {
    super();
    setTimeInMillis(milliseconds);
  }
  public DateTime(int year, int month, int day)
  {
    super(year, month, day);
  }
  public DateTime(int year, int month, int day, int hour, int minute)
  {
    super(year, month, day, hour, minute);
  }
  public DateTime(int year, int month, int day, int hour, int minute, int second)
  {
    super(year, month, day, hour, minute, second);
  }

  public String getSDateTime()
  {
    DateFormat date_format = SimpleDateFormat.getDateTimeInstance();
    date_format.setTimeZone(TimeZone.getDefault());
    return date_format.format(getTime());
  }
  public String getSDate()
  {
    DateFormat date_format = SimpleDateFormat.getDateInstance();
    date_format.setTimeZone(TimeZone.getDefault());
    return date_format.format(getTime());
  }
  public String getSTime()
  {
    DateFormat date_format = SimpleDateFormat.getTimeInstance();
    date_format.setTimeZone(TimeZone.getDefault());
    return date_format.format(getTime());
  }
  @Override
  public long getTimeInMillis() //Вернет количество миллисекунд по гринвичу начиная с 01 Янв 1970г. 00:00:00
  {
    return super.getTimeInMillis();
  }

}
