package com.bloodliviykot.tools.Common;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Kot on 08.10.2016.
 */
public class Money
  implements Parcelable
{
  private long value;
  public Money(double value)
  {
    this.value = (long)(value * 100);
  }
  public Money(long value)
  {
    this.value = value;
  }
  public Money(String format_string)
  {
    this(Double.parseDouble(new MyDecimalFormat().clearFormat(format_string)));
  }

  public long getLongValue()
  {
    return value;
  }
  public double getDoubleValue()
  {
    return ((double)value) / 100;
  }

  @Override
  public String toString()
  {
    return new MyDecimalFormat().double_format(getDoubleValue());
  }
  public boolean equals(Money money)
  {
    return value == money.value;
  }

  //Parcelable -----------------------------------------------------------------------------------------------
  public static final Parcelable.Creator CREATOR = new Parcelable.Creator()
  {
    @Override
    public Money createFromParcel(Parcel source)
    {
      return new Money(source.readLong());
    }
    @Override
    public Money[] newArray(int size)
    {
      return new Money[size];
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
    dest.writeLong(getLongValue());
  }

}
