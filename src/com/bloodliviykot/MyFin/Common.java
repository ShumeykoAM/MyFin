package com.bloodliviykot.MyFin;

import android.content.Context;
import android.widget.Spinner;
import com.bloodliviykot.tools.Common.MyDecimalFormat;

import java.lang.reflect.Method;

/**
 * Created by Kot on 24.09.2016.
 */
public class Common
{
  public static Context application_context = null;
  public static final MyDecimalFormat FORMAT_MONEY = new MyDecimalFormat("###.##");

  //+//Программно скрыть spinner выпадающий выпавший список
  public static void hideSpinnerDropDown(Spinner spinner)
  {
    try
    {
      Method method = Spinner.class.getDeclaredMethod("onDetachedFromWindow");
      method.setAccessible(true);
      method.invoke(spinner);
    }
    catch (Exception e)
    {   }
  }
}
