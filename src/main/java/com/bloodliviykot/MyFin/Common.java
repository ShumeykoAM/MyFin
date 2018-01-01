package com.bloodliviykot.MyFin;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import com.bloodliviykot.tools.Common.MyDecimalFormat;

import java.lang.reflect.Method;

/**
 * Created by Kot on 24.09.2016.
 */
public class Common
{
  public static Context context = null;
  public static final MyDecimalFormat FORMAT_MONEY = new MyDecimalFormat();

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

  public static class MoneyWatcher
    implements TextWatcher
  {
    public MoneyWatcher(EditText et)
    {
      this.et = et;
    }
    private EditText et;
    private String format_text;
    private int position;
    private boolean is_second = false;
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after)
    {

    }
    @Override
    public void onTextChanged(CharSequence s,
                              int start,  //Позиция от куда начинаем
                              int before, //Количество символов которые убираем
                              int count)  //Количество символов которые вставляем
    {
      try
      {
        if(!is_second)
          format_text = format(start+count, s.toString());
      }
      catch(Exception e)
      {
        format_text = s.toString();
      }
    }
    @Override
    public void afterTextChanged(Editable s)
    {
      if(!is_second)
      {
        if(!et.getText().toString().equals(format_text))
        {
          is_second = true;
          int save_position = position;
          et.setText(format_text);
          et.setSelection(save_position);
        }
      }
      else
        is_second = false;
    }
    private String format(int selection, String string)
    {
      if(string.length() > 0)
        string = string.replaceAll("[.]", Character.toString(MyDecimalFormat.decimal_point));
      if(string.length() > 0 && string.charAt(0) == '0')
        selection--;
      if(string.length() > 0 && string.charAt(0) == MyDecimalFormat.decimal_point)
      {
        string = "0" + string;
        selection++;
      }
      String format_text = "";
      int delta = 0;
      //Сначала удалим спецсимволы и подсчитаем необходимое смещение
      for(int i=0; i<string.length(); ++i)
        if(selection>=i && string.charAt(i) == MyDecimalFormat.separator)
          delta--;
      selection += delta;
      format_text = FORMAT_MONEY.clearFormat(string);
      //Снова добавим спец символы и посчитаем необходимое смещение
      int index_point = format_text.indexOf(MyDecimalFormat.decimal_point); //Индекс где находится точка, если ее нет
      if(index_point == -1)                       //  то считаем что она в конце
        index_point = format_text.length();
      int count_apostrophe = (format_text.length() - (format_text.length() - index_point) - 1) / 3;
      int distance_apostrophe = (selection>index_point ? 0 : index_point - selection) / 3;
      selection += count_apostrophe - distance_apostrophe;
      if(format_text.length() != 0)
        format_text = FORMAT_MONEY.double_format(Double.valueOf(format_text.replaceAll(
          "["+Character.toString(MyDecimalFormat.decimal_point)+"]",".")));
      if(string.length() != 0 && string.charAt(string.length()-1) == MyDecimalFormat.decimal_point)
        format_text += MyDecimalFormat.decimal_point;
      position = selection;
      if(position >= format_text.length())
        position = format_text.length();
      if(position < 0)
        position = 0;
      return format_text;
      //http://dmilvdv.narod.ru/Translate/MISC/how_to_use_java_bigdecimal.html
    }

  }

  public static void assertNeedTest()
  {
    Throwable t = new Throwable();
    StackTraceElement trace[] = t.getStackTrace();
    // Глубина стэка должна быть больше 1-го, поскольку интересующий
    // нас элемент стэка находится под индексом 1 массива элементов
    // Элемент с индексом 0 - это текущий метод
    if (trace.length > 1)
    {
      StackTraceElement element = trace[1];
      String message = "Метод " + element.getClassName() + "." + element.getMethodName() +
        " в строке " + element.getLineNumber() + " файла " + element.getFileName() + " еще не проверен";
      Toast.makeText(Common.context, message, Toast.LENGTH_LONG).show();
    }
  }

  //Для выделения всего текста в EditText при фокусировке
  public static View.OnFocusChangeListener getOnFocusChangeListener()
  {
    return on_focus_change_listener;
  }
  private static final View.OnFocusChangeListener on_focus_change_listener = new View.OnFocusChangeListener()
  {
    @Override
    public void onFocusChange(View v, boolean hasFocus)
    {
      try
      {
        final EditText edit_text = (EditText)v;
        edit_text.post(new Runnable()
        {
          @Override
          public void run()
          {
            if(hasFocus)
              edit_text.selectAll();
            else
              edit_text.setSelection(0);
          }
        });
      } catch(Throwable e)
      {      }
    }
  };

}
