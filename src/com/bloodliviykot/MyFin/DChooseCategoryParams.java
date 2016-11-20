package com.bloodliviykot.MyFin;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import com.bloodliviykot.MyFin.DB.entities.Unit;
import com.bloodliviykot.tools.widget.DialogFragmentEx;

/**
 * Created by Kot on 20.11.2016.
 */
@SuppressLint("ValidFragment")
public class DChooseCategoryParams
  extends DialogFragmentEx<DialogFragmentEx.I_ResultHandler, Bundle>
  implements View.OnClickListener
{
  private EditText count;
  private Spinner unit;
  private Button ok;

  public DChooseCategoryParams(){}
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
  {
    //getDialog().setTitle("Заголовок");
    getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
    final View v = inflater.inflate(R.layout.d_choose_category_params, null);
    count = (EditText)v.findViewById(R.id.d_choose_cat_params_count);
    unit = (Spinner)v.findViewById(R.id.d_choose_cat_params_unit);
    (ok = (Button)v.findViewById(R.id.d_choose_cat_params_ok)).setOnClickListener(this);
    Cursor cursor_unit = Unit.getCursor();
    SimpleCursorAdapter adapter_unit = new SimpleCursorAdapter(v.getContext(), R.layout.d_choose_category_params_unit_item,
      cursor_unit, new String[]{"name"}, new int[]{R.id.d_choose_cat_params_unit_item});
    unit.setAdapter(adapter_unit);
    Bundle params = getArguments();
    Planned.Chooses count_unit = params.getParcelable("count_unit");
    count.setText(new Double(count_unit.count).toString());
    int position = 0;
    for(boolean cursor_status = cursor_unit.moveToFirst(); cursor_status; cursor_status = cursor_unit.moveToNext(), position++)
      if(cursor_unit.getLong(cursor_unit.getColumnIndex("_id")) == count_unit.unit.getId())
      {
        unit.setSelection(position);
        break;
      }
    //Клавиатуру для конкретного view можно корректно вызвать только так
    count.post(new Runnable()
    {
      @Override
      public void run()
      {
        InputMethodManager imm = (InputMethodManager)
          count.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(count, InputMethodManager.SHOW_IMPLICIT);
        count.requestFocus();
      }
    });
    return v;
  }

  @Override
  public void onClick(View v)
  {
    if(v == ok)
    {
      Bundle result_values = new Bundle();
      result_values.putString("Button", "Ok");
      handleResult(result_values);
      dismiss();
    }
  }


}
