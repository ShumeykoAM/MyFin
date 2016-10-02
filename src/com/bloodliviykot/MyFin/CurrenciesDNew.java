package com.bloodliviykot.MyFin;

import android.annotation.SuppressLint;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Kot on 02.10.2016.
 */
@SuppressLint("ValidFragment")
public class CurrenciesDNew
  extends DialogFragment //!!! внимание, наследники DialogFragment должны иметь конструктор без параметров
{
  public CurrenciesDNew()
  {

  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
  {
    getDialog().setTitle("Добавить валюту");
    //getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
    final View v = inflater.inflate(R.layout.currensies_d_new, null);

    return v;
  }




}
