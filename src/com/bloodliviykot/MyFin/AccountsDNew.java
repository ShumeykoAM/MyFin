package com.bloodliviykot.MyFin;

import android.annotation.SuppressLint;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Shumeiko on 26.09.2016.
 */
@SuppressLint("ValidFragment")
public class AccountsDNew
  extends DialogFragment
{
  public interface I_Resultable
  {
    void resultHandler(RESULT result);
  }
  public enum RESULT
  {
    ADDED,
    CANSEL
  }
  private I_Resultable resultable = null;
  public AccountsDNew(I_Resultable resultable)
  {
    this.resultable = resultable;
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
  {
    getDialog().setTitle("Создать счет");
    //getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
    final View v = inflater.inflate(R.layout.accounts_d_new, null);
    return v;
  }
  @Override
  public void onDestroyView()
  {
    super.onDestroyView();
    if(resultable != null)
      resultable.resultHandler(RESULT.CANSEL);
  }
}
