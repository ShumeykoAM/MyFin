package com.bloodliviykot.MyFin;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import com.bloodliviykot.MyFin.DB.entities.Document;
import com.bloodliviykot.MyFin.DB.entities.Unit;
import com.bloodliviykot.tools.DataBase.Entity;
import com.bloodliviykot.tools.widget.DialogFragmentEx;
import com.kot.myfin.R;

/**
 * Created by Kot on 16.11.2016.
 */
@SuppressLint("ValidFragment")
public class DDocumentParams
  extends DialogFragmentEx<DialogFragmentEx.I_ResultHandler<Bundle>, Bundle>
{
  public static final String ID = "_id";
  private TextView name_category;
  private EditText price;
  private EditText of;
  private Spinner of_unit;
  private EditText amount;
  private Spinner amount_unit;
  private EditText cost;
  private Button b_ok;
  private Button b_cancel;

  private Document document;
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
    name_category = (TextView)v.findViewById(R.id.d_document_params_name_category);
    price = (EditText)v.findViewById(R.id.d_document_params_price);
    of = (EditText)v.findViewById(R.id.d_document_params_of);
    of_unit = (Spinner)v.findViewById(R.id.d_document_params_of_unit);
    amount = (EditText)v.findViewById(R.id.d_document_params_amount);
    amount_unit = (Spinner)v.findViewById(R.id.d_document_params_amount_unit);
    cost = (EditText)v.findViewById(R.id.d_document_params_cost);
    b_ok = (Button)v.findViewById(R.id.d_document_params_ok);
    b_cancel = (Button)v.findViewById(R.id.d_document_params_cancel);

    Bundle params = getArguments();
    try
    {
      document = Document.getDocumentFromId(params.getLong("_id"));
      name_category.setText(document.getCategory().getName());
      price.setText(document.getPrice() != null ? document.getPrice().toString() : null);
      of.setText(new Double(document.getOf()).toString());
  
      SimpleCursorAdapter adapter_unit = new SimpleCursorAdapter(v.getContext(), R.layout.d_choose_category_params_unit_item,
        Unit.getCursor(), new String[]{"name"}, new int[]{R.id.d_choose_cat_params_unit_item});
      of_unit.setAdapter(adapter_unit);
      
      
      
    }
    catch(Entity.EntityException e)
    {
      dismiss();
    }
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
