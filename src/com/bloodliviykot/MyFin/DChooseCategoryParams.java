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
import android.widget.*;
import com.bloodliviykot.MyFin.DB.entities.Unit;
import com.bloodliviykot.tools.DataBase.Entity;
import com.bloodliviykot.tools.widget.DialogFragmentEx;

/**
 * Created by Kot on 20.11.2016.
 */
@SuppressLint("ValidFragment")
public class DChooseCategoryParams
  extends DialogFragmentEx<DialogFragmentEx.I_ResultHandler<Bundle>, Bundle>
  implements View.OnClickListener
{
  private Cursor cursor_unit;

  private TextView name;
  private EditText count;
  private Spinner unit;
  private Button ok;
  private long _id;

  public DChooseCategoryParams()
  {
    super(R.layout.d_choose_category_params);
  }
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
  {
    //getDialog().setTitle("Заголовок");
    getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
    final View v = inflater.inflate(getR_layout(), null);
    count = (EditText)v.findViewById(R.id.d_choose_cat_params_count);
    unit = (Spinner)v.findViewById(R.id.d_choose_cat_params_unit);
    (ok = (Button)v.findViewById(R.id.d_choose_cat_params_ok)).setOnClickListener(this);
    name = (TextView)v.findViewById(R.id.d_choose_cat_params_name);
    cursor_unit = Unit.getCursor();
    SimpleCursorAdapter adapter_unit = new SimpleCursorAdapter(v.getContext(), R.layout.d_choose_category_params_unit_item,
      cursor_unit, new String[]{"name"}, new int[]{R.id.d_choose_cat_params_unit_item});
    unit.setAdapter(adapter_unit);
    Bundle params = getArguments();
    Planned.Chooses count_unit = params.getParcelable("count_unit");
    _id = count_unit._id;
    count.setText(new Double(count_unit.count).toString());
    name.setText(params.getString("name"));
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
      Planned.Chooses count_unit = null;
      try
      {
        cursor_unit.moveToPosition(unit.getSelectedItemPosition());
        count_unit = new Planned.Chooses(_id, Double.parseDouble(count.getText().toString()),
          Unit.getUnitFromId(cursor_unit.getLong(cursor_unit.getColumnIndex("_id"))));
      } catch(Entity.EntityException e)
      {     }
      result_values.putParcelable("count_unit", count_unit);
      handleResult(result_values);
      dismiss();
    }
  }


}
