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
import android.widget.TextView;
import android.widget.Toast;
import com.bloodliviykot.MyFin.DB.entities.Category;
import com.bloodliviykot.MyFin.DB.entities.Transact;
import com.bloodliviykot.tools.DataBase.Entity;
import com.bloodliviykot.tools.widget.DialogFragmentEx;

/**
 * Created by Kot on 26.11.2016.
 */
@SuppressLint("ValidFragment")
public class DAddEditCategory
  extends DialogFragmentEx<DialogFragmentEx.I_ResultHandler<Bundle>, Bundle>
  implements View.OnClickListener
{
  private Cursor cursor_unit;

  private TextView tv_title;
  private EditText et_name;
  private Button b_ok;
  private Long _id_parent;
  private Transact.TREND trend;

  public DAddEditCategory()
  {
    super(R.layout.d_add_edit_category);
  }
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
  {
    //getDialog().setTitle("Заголовок");
    getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
    final View v = inflater.inflate(getR_layout(), null);
    tv_title = (TextView)v.findViewById(R.id.d_add_edit_cat_title);
    et_name = (EditText)v.findViewById(R.id.d_add_edit_cat_name);
    (b_ok = (Button)v.findViewById(R.id.d_add_edit_cat_ok)).setOnClickListener(this);

    Bundle params = getArguments();
    trend = Transact.TREND.getTREND(params.getLong("trend"));
    _id_parent = params.getLong("_id_parent");
    String title;
    String name_parent = params.getString("name_parent");
    if(params.getBoolean("is_root"))
      title = "Добавить " + name_parent + " категорию.";
    else
      title = "Добавить подкатегорию в " + name_parent;
    tv_title.setText(title);

    //Клавиатуру для конкретного view можно корректно вызвать только так
    et_name.post(new Runnable()
    {
      @Override
      public void run()
      {
        InputMethodManager imm = (InputMethodManager)et_name.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(et_name, InputMethodManager.SHOW_IMPLICIT);
        et_name.requestFocus();
      }
    });
    return v;
  }

  @Override
  public void onClick(View v)
  {
    if(v == b_ok)
    {
      String name = et_name.getText().toString();
      if(name.isEmpty())
        return;
      else
        try
        {
          long _id;
          Category category = new Category(_id_parent, trend, et_name.getText().toString());
          if((_id = category.insert() )== -1)
            Toast.makeText(Common.application_context, "Такая категория уже есть", Toast.LENGTH_LONG).show();
          else
          {
            Bundle result_values = new Bundle();
            result_values.putLong("_id", _id);
            handleResult(result_values);
            dismiss();
          }
        } catch(Entity.EntityException e)
        {  }
    }
  }
}
