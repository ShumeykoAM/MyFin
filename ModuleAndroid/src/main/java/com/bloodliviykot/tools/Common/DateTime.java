package com.bloodliviykot.tools.Common;

import android.os.Parcel;
import android.os.Parcelable;
import com.bloodliviykot.MyFin.Common;
import com.bloodliviykot.MyFin.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.TimeZone;

/**
 * Created by Kot on 09.10.2016.
 */
public class DateTime
  extends GregorianCalendar
  implements Parcelable
{
  public static final int YEAR_CORRECTOR = 1900;
  public static final long SECONDS_IN_DAY = 86400000L;

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
    String result = "";
    long normalize_cur_time = (new DateTime().getTimeInMillis()) / SECONDS_IN_DAY * SECONDS_IN_DAY;
    DateTime yesterday = new DateTime(normalize_cur_time - SECONDS_IN_DAY);
    DateTime today = new DateTime(normalize_cur_time);
    DateTime dt = new DateTime(getTimeInMillis() / SECONDS_IN_DAY * SECONDS_IN_DAY);
    if( dt.compareTo(yesterday) == 0)
      result = Common.context.getString(R.string.yesterday);
    else if(dt.compareTo(today) == 0)
      result = Common.context.getString(R.string.today);
    else
    {
      DateFormat date_format = SimpleDateFormat.getDateInstance();
      date_format.setTimeZone(TimeZone.getDefault());
      result = date_format.format(getTime());
    }
    return result;
  }
  public String getSTime()
  {
    DateFormat date_format = SimpleDateFormat.getTimeInstance(SimpleDateFormat.SHORT);
    date_format.setTimeZone(TimeZone.getDefault());
    return date_format.format(getTime());
  }
  @Override
  public long getTimeInMillis() //Вернет количество миллисекунд по гринвичу начиная с 01 Янв 1970г. 00:00:00
  {
    return super.getTimeInMillis();
  }
  public boolean isAMPMFormat()
  {
    String time = getSTime();
    return time.contains("AM") || time.contains("PM");
  }
  public int getYear()
  {
    return getTime().getYear() + YEAR_CORRECTOR;
  }
  public int getMonth()
  {
    return getTime().getMonth();
  }
  public int getDayOfMonth()
  {
    return getTime().getDate();
  }
  public int getHours()
  {
    return getTime().getHours();
  }
  public int getMinutes()
  {
    return getTime().getMinutes();
  }
  public final void setDT(int year, int month, int day, int hourOfDay, int minute)
  {
    super.set(year, month, day, hourOfDay, minute);
  }

  //Parcelable -----------------------------------------------------------------------------------------------
  public static final Parcelable.Creator CREATOR = new Parcelable.Creator()
  {
    @Override
    public DateTime createFromParcel(Parcel source)
    {
      return new DateTime(source.readLong());
    }
    @Override
    public DateTime[] newArray(int size)
    {
      return new DateTime[size];
    }
  };
  @Override
  public int describeContents()
  {
    return 0;
  }
  @Override
  public void writeToParcel(Parcel dest, int flags)
  {
    dest.writeLong(getTimeInMillis());
  }
}
