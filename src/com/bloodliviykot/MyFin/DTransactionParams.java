package com.bloodliviykot.MyFin;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.bloodliviykot.tools.widget.DialogFragmentEx;

/**
 * Created by Shumeiko on 01.12.2016.
 */
@SuppressLint("ValidFragment")
public class DTransactionParams
  extends DialogFragmentEx<DialogFragmentEx.I_ResultHandler<Bundle>, Bundle>
{
  //private

  public DTransactionParams()
  {
    super(R.layout.d_transaction_params);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
  {
    getDialog().setTitle("Создать счет");
    //getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
    final View v = inflater.inflate(getR_layout(), null);
    /*
    icon = (Spinner)v.findViewById(R.id.accounts_d_new_icon);
    name = (EditText)v.findViewById(R.id.accounts_d_new_name);
    b_ok     = (Button)v.findViewById(R.id.accounts_d_new_ok);     b_ok.setOnClickListener(this);
    b_cancel = (Button)v.findViewById(R.id.accounts_d_new_cansel); b_cancel.setOnClickListener(this);
    */

    return v;
  }



}
