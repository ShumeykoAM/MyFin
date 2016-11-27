package com.bloodliviykot.MyFin;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import com.bloodliviykot.tools.widget.DialogFragmentEx;

/**
 * Created by Kot on 16.11.2016.
 */
@SuppressLint("ValidFragment")
public class DDocumentParams
  extends DialogFragmentEx<DialogFragmentEx.I_ResultHandler<Bundle>, Bundle>
{
  private EditText price;

  public DDocumentParams()
  {
    super(R.layout.d_document_params);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
  {
    //getDialog().setTitle("Заголовок");
    getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
    final View v = inflater.inflate(getR_layout(), null);
    price = (EditText)v.findViewById(R.id.d_document_params_price);
    //icon = (Spinner)v.findViewById(R.id.accounts_d_new_icon);

    //Клавиатуру для конкретного view можно корректно вызвать только так
    price.post(new Runnable()
    {
      @Override
      public void run()
      {
        InputMethodManager imm = (InputMethodManager)
          price.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(price, InputMethodManager.SHOW_IMPLICIT);
        price.requestFocus();
      }
    });
    return v;
  }

}
