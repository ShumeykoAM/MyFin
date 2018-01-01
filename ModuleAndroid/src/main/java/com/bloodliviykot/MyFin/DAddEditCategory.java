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
import com.bloodliviykot.MyFin.DB.EQ;
import com.bloodliviykot.MyFin.DB.MySQLiteOpenHelper;
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
  implements View.OnClickListener, DialogFragmentEx.I_ResultHandler<Long>
{
  private Cursor cursor_unit;

  private TextView tv_title;
  private EditText et_name;
  private Button b_ok, b_cancel, b_chose_parent;
  private Long _id_parent; public static final String ID_PARENT = "_id_parent";
  private String name_category; public static final String NAME_CATEGORY = "name_category";
  
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
    (b_cancel = (Button) v.findViewById(R.id.d_add_edit_cat_cancel)).setOnClickListener(this);
    (b_chose_parent = (Button) v.findViewById(R.id.d_add_edit_cat_choose_parent)).setOnClickListener(this);
  
    Bundle params;
    if(savedInstanceState != null)
      params = savedInstanceState;
    else
    {
      params = getArguments();
      et_name.setText(params.getString(NAME_CATEGORY));
      //Надо бы курсор в et_name в конец выставить
    }
    _id_parent = params.getLong(ID_PARENT);
    setTitleText();

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
  public void onSaveInstanceState(Bundle outState)
  {
    outState.putLong(ID_PARENT, _id_parent);
    outState.putString(NAME_CATEGORY, name_category);
    super.onSaveInstanceState(outState);
  }
  private void setTitleText()
  {
    String title = "";
    try
    {
      Category parent = Category.getCategoryFromId(_id_parent);
      MySQLiteOpenHelper oh = MySQLiteOpenHelper.getMySQLiteOpenHelper();
      Cursor cursor = oh.db.rawQuery(oh.getQuery(EQ.IS_ROOT_CATEGORY), new String[]{_id_parent.toString()});
      cursor.moveToFirst();
      boolean is_root = cursor.getInt(cursor.getColumnIndex("is_root")) == 1;
      if(is_root)
      {
        if(parent.getTrend() == Transact.TREND.CREDIT)
          title = getString(R.string.add_expenditure_category);
        else if(parent.getTrend() == Transact.TREND.DEBIT)
          title = getString(R.string.add_a_profit_category);
      }
      else
        title = getString(R.string.add_subcategory_of) + " " + parent.getName() + ".";
    } catch(Entity.EntityException e)
    {  }
    tv_title.setText(title);
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
          Category parent = Category.getCategoryFromId(_id_parent);
          long _id;
          Category category = new Category(_id_parent, parent.getTrend(), et_name.getText().toString());
          if((_id = category.insert() )== -1)
            Toast.makeText(Common.context, "Такая категория уже есть", Toast.LENGTH_LONG).show();
          else
          {
            Bundle result_values = new Bundle();
            result_values.putLong("_id", _id);
            result_values.putString("name", category.getName());
            dismiss();
            handleResult(result_values);
          }
        } catch(Entity.EntityException e)
        {  }
    }
    else if(v == b_cancel)
    {
      dismiss();
    }
    else if(v == b_chose_parent)
    {
      DChooseCategory d_chose_category = new DChooseCategory();
      d_chose_category.setTargetFragment(this, R.layout.categories);
      Bundle params = new Bundle();
      
      d_chose_category.setArguments(params);
      d_chose_category.show(getFragmentManager(), null);
    }
  }
  @Override
  public void resultHandler(int R_layout, Long result_values)
  {
    if(R_layout == R.layout.categories)
    {
      _id_parent = result_values;
      setTitleText();
    }
  }
}
