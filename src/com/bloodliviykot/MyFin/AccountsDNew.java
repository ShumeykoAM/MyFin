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

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
  {
    getDialog().setTitle("Создать счет");
    //getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

    final View v = inflater.inflate(R.layout.accounts_d_new, null);

    return v;
  }
}
