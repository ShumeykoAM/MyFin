package com.bloodliviykot.tools.widget;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DialogFragment;
import android.app.Fragment;

/**
 * Created by Kot on 20.11.2016.
 */
@SuppressLint("ValidFragment")
public abstract class DialogFragmentEx<Handler extends DialogFragmentEx.I_ResultHandler<PARAM>, PARAM>
  extends DialogFragment //!!! внимание, наследники DialogFragment должны иметь конструктор без параметров
{
  public DialogFragmentEx(int R_layout)
  {
    super();
    this.R_layout = R_layout;
  }
  //Обработчик результата
  public interface I_ResultHandler<PARAM>
  {
    void resultHandler(int R_layout, PARAM result_values);
  }
  public int getR_layout()
  {
    return R_layout;
  }

  //
  private int R_layout;
  protected void handleResult(PARAM result_values)
  {
    boolean need_call_activity = true;
    try
    {
      //Пытаемся сначала вызвать обработчик фрагмента, вызвавшего данный
      Fragment target = getTargetFragment();
      if(target != null)
      {
        ((Handler)target).resultHandler(R_layout, result_values);
        need_call_activity = false;
      }
    }catch(ClassCastException e2)
    {      }
    if(need_call_activity)
      try
      {
        //Иначе пытаемся вызвать обработчик родительской активности
        Activity a = getActivity();
        ((Handler)getActivity()).resultHandler(R_layout, result_values);
      } catch(ClassCastException e1)
      {      }
  }

}
