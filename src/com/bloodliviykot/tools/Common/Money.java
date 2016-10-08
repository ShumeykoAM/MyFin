package com.bloodliviykot.tools.Common;

/**
 * Created by Kot on 08.10.2016.
 */
public class Money
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
}
