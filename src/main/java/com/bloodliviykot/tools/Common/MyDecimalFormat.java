package com.bloodliviykot.tools.Common;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;


public class MyDecimalFormat
  extends DecimalFormat
{
  public static final char separator, decimal_point;
  private static final DecimalFormatSymbols otherSymbols;
  public static final String FORMAT;

  static
  {
    otherSymbols = new DecimalFormatSymbols();
    otherSymbols.setGroupingSeparator((char)160);
    otherSymbols.setDecimalSeparator('.');
    FORMAT = "###,###.##";
    DecimalFormat decimal_format = new DecimalFormat(FORMAT, otherSymbols);
    String template_format = decimal_format.format(1234.56);
    separator = template_format.charAt(1);
    decimal_point = template_format.charAt(5);
  }

  public MyDecimalFormat()
  {
    super(FORMAT, otherSymbols);
    setRoundingMode(RoundingMode.DOWN); //Отсекаем лишние знаки после запятой
  }

  public final String double_format(Double value)
  {
    String format = super.format(value);
    return format;
  }

  public String clearFormat(String string)
  {
    return string.replaceAll("["+Character.toString(MyDecimalFormat.separator)+"]","");
  }

}
