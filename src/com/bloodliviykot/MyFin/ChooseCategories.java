package com.bloodliviykot.MyFin;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.*;
import com.bloodliviykot.MyFin.DB.EQ;
import com.bloodliviykot.MyFin.DB.MySQLiteOpenHelper;
import com.bloodliviykot.MyFin.DB.entities.Category;
import com.bloodliviykot.MyFin.DB.entities.Transact;
import com.bloodliviykot.MyFin.DB.entities.Unit;
import com.bloodliviykot.tools.DataBase.Entity;
import com.bloodliviykot.tools.widget.ButtonID;
import com.bloodliviykot.tools.widget.DialogFragmentEx;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by Kot on 03.11.2016.
 */
public class ChooseCategories
  extends Activity
  implements DialogFragmentEx.I_ResultHandler<Bundle>
{
  private LinearLayout navigation_linear;
  private ButtonID root;
  private Button add_new;
  private SearchView search;
  private ListView list;
  private Button tree;
  private Button ok;
  private List<ButtonID> navigation_buttons = new ArrayList<ButtonID>();

  private MySQLiteOpenHelper oh;
  private Cursor cursor;
  private FilterProviderListener filter_provider_listener;
  private SimpleCursorAdapter list_adapter;
  private Transact.TREND trend;
  Map<Long, Pair<Double, Unit>> chooses = new TreeMap<>();
  private int result = RESULT_CANCELED;

  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.choose_categories_list);
    Bundle extras = getIntent().getExtras();
    trend = (Transact.TREND)extras.get("TREND");
    ArrayList<Planned.Chooses> arr_chooses = extras.getParcelableArrayList("chooses");
    for(Planned.Chooses choose : arr_chooses)
      chooses.put(choose._id, new Pair<>(choose.count, choose.unit));

    HorizontalScrollView horizontal_scroll = (HorizontalScrollView)findViewById(R.id.choose_categories_navigation_line);
    horizontal_scroll.post(new Runnable()
    {
      @Override
      public void run()
      {
        horizontal_scroll.fullScroll(HorizontalScrollView.FOCUS_RIGHT);
      }
    });
    navigation_linear = (LinearLayout)findViewById(R.id.choose_categories_navigation_linear);
    root = (ButtonID)navigation_linear.findViewById(R.id.arrow_button);
    add_new = (Button)findViewById(R.id.choose_categories_add_new);
    search = (SearchView)findViewById(R.id.choose_categories_search);
    list = (ListView)findViewById(R.id.choose_categories_list);
    tree = (Button)findViewById(R.id.choose_categories_tree);
    ok = (Button)findViewById(R.id.choose_categories_ok);
    ok.setOnClickListener(new View.OnClickListener()
    {
      @Override
      public void onClick(View v)
      {
        result = RESULT_OK;
        finish();
      }
    });
    oh = MySQLiteOpenHelper.getMySQLiteOpenHelper();

    //Установить цвет текста SearchView
    int autoCompleteTextViewID = getResources().getIdentifier("android:id/search_src_text", null, null);
    AutoCompleteTextView search_tv = (AutoCompleteTextView)search.findViewById(autoCompleteTextViewID);
    search_tv.setTextColor(getResources().getColor(R.color.grey));

    filter_provider_listener = new FilterProviderListener();
    filter_provider_listener.buildScrollPath(null);
    list_adapter = new ChooseItemAdapter(cursor);
    list.setAdapter(list_adapter);
    list_adapter.setFilterQueryProvider(filter_provider_listener);
    search.setOnQueryTextListener(filter_provider_listener);
  }
  @Override
  public void finish()
  {
    Intent intent = new Intent();
    if(result == RESULT_OK)
    {
      ArrayList<Planned.Chooses> arr_chooses = new ArrayList<Planned.Chooses>();
      for(Map.Entry<Long, Pair<Double, Unit>> entry : chooses.entrySet() )
        arr_chooses.add(new Planned.Chooses(entry.getKey(), entry.getValue().first, entry.getValue().second));
      intent.putParcelableArrayListExtra("chooses", arr_chooses);
    }
    setResult(result, intent); //RESULT_CANCELED
    super.finish();
  }

  private class FilterProviderListener
    implements FilterQueryProvider, SearchView.OnQueryTextListener
  {
    Long _id_parent = null;
    String constraint = "";

    public void setIdParent(Long _id_parent)
    {
      this._id_parent = _id_parent;
    }

    @Override
    public Cursor runQuery(CharSequence constraint)
    {
      this.constraint = constraint.toString();
      return getCursor();
    }

    @Override
    public boolean onQueryTextSubmit(String query)
    {
      return false;
    }

    @Override
    public boolean onQueryTextChange(String newText)
    {
      list_adapter.getFilter().filter(newText);
      return true;
    }

    private Cursor getCursor()
    {
      Cursor cursor;
      if(_id_parent == null)
        cursor = oh.db.rawQuery(oh.getQuery(EQ.ALL_CATEGORIES_LIKE), new String[]{new Long(trend.id_db).toString(), "%" + constraint.toString().toLowerCase() + "%"});
      else
        cursor = oh.db.rawQuery(oh.getQuery(EQ.SUB_CATEGORIES_LIKE), new String[]{_id_parent.toString(), "%" + constraint.toString().toLowerCase() + "%"});
      return cursor;
    }

    public void buildScrollPath(Long _id_parent)
    {
      filter_provider_listener.setIdParent(_id_parent);
      cursor = getCursor();
      if(list_adapter != null)
      {
        list_adapter.changeCursor(cursor);
        list_adapter.notifyDataSetChanged();
      }
      //Удалить дочерние
      boolean is_first = true;
      for(ButtonID bb : navigation_buttons)
      {
        if(is_first)
        {
          is_first = false;
          continue;
        }
        navigation_linear.removeView(bb);
      }
      navigation_buttons.clear();

      if(trend == Transact.TREND.DEBIT)
        root.setBackgroundResource(R.drawable.ic_arrow_all_debit);
      else if(trend == Transact.TREND.CREDIT)
        root.setBackgroundResource(R.drawable.ic_arrow_all_credit);
      ButtonID next_button;
      if(_id_parent != null)
      {
        Cursor cursor_parents = oh.db.rawQuery(oh.getQuery(EQ.ALL_PARENTS), new String[]{_id_parent.toString()});
        for(boolean cursor_status = cursor_parents.moveToFirst(); cursor_status; cursor_status = cursor_parents.moveToNext())
        {
          navigation_buttons.add(next_button = (ButtonID)(getLayoutInflater().inflate(R.layout.arrow_button, navigation_linear, false)));
          next_button.setText(cursor_parents.getString(cursor_parents.getColumnIndex("name")));
          next_button.setID(cursor_parents.getLong(cursor_parents.getColumnIndex("_id")));
        }
        try
        {
          Category category = Category.getCategoryFromId(_id_parent);
          navigation_buttons.add(next_button = (ButtonID)(getLayoutInflater().inflate(R.layout.arrow_button, navigation_linear, false)));
          next_button.setText(category.getName());
          next_button.setID(category.getId());
        } catch(Entity.EntityException e)
        {     }
      }
      for(Button bb : navigation_buttons)
        navigation_linear.addView(bb);
      navigation_buttons.add(0, root);
      for(Button bb : navigation_buttons)
      {
        LinearLayout.LayoutParams button_params = (LinearLayout.LayoutParams)bb.getLayoutParams();
        if(bb == navigation_buttons.get(navigation_buttons.size() - 1))
          button_params.rightMargin = 0;
        else
          button_params.rightMargin = -10;
        bb.setLayoutParams(button_params);
      }
      for(Button bb : navigation_buttons)
        bb.setOnClickListener(new View.OnClickListener()
        {
          @Override
          public void onClick(View v)
          {
            Long _id = ((ButtonID)v).getID();
            buildScrollPath(_id);
          }
        });
    }
  }

  private class ChooseItemAdapter
    extends SimpleCursorAdapter
  {

    public ChooseItemAdapter(Cursor c)
    {
      super(Common.application_context, R.layout.choose_categories_list_item, c, new String[]{},
        new int[]{R.id.choose_categories_list_item_enter, R.id.choose_categories_list_item_name,
                  R.id.choose_categories_list_item_count, R.id.choose_categories_list_item_unit,
                  R.id.choose_categories_list_item_choose});
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor)
    {
      String name = cursor.getString(cursor.getColumnIndex("name"));
      long _id = cursor.getLong(cursor.getColumnIndex("_id"));

      ImageView iv_image = (ImageView)view.findViewById(R.id.choose_categories_list_item_enter);
      TextView tv_name   = (TextView)view.findViewById(R.id.choose_categories_list_item_name);
      TextView tv_count  = (TextView)view.findViewById(R.id.choose_categories_list_item_count);
      TextView tv_unit   = (TextView)view.findViewById(R.id.choose_categories_list_item_unit);
      CheckBox ch_chose  = (CheckBox)view.findViewById(R.id.choose_categories_list_item_choose);

      ch_chose.setOnCheckedChangeListener(null);
      tv_name.setText(name);
      if(chooses.containsKey(_id))
      {
        Pair<Double, Unit> count_unit = chooses.get(_id);
        tv_count.setText(count_unit.first.toString());
        tv_unit.setText(count_unit.second.getName());
        ch_chose.setChecked(true);
        CountUnitParams count_unit_params = new CountUnitParams(_id, name);
        tv_count.setOnClickListener(count_unit_params);
        tv_unit.setOnClickListener(count_unit_params);
      }
      else
      {
        tv_count.setText("");
        tv_unit.setText("");
        ch_chose.setChecked(false);
        tv_count.setOnClickListener(null);
        tv_unit.setOnClickListener(null);
      }
      ch_chose.setOnCheckedChangeListener(new CheckedChangeListener(_id, tv_count, tv_unit, name));
      Cursor cursor_child = oh.db.rawQuery(oh.getQuery(EQ.EXIST_SUB_CATEGORY), new String[]{new Long(_id).toString()});
      if(cursor_child.getCount() != 0)
      {
        iv_image.setOnClickListener(new OnClickEntryTree(_id));
        iv_image.setImageResource(R.drawable.ic_enter);
      }
      else
      {
        iv_image.setOnClickListener(null);
        iv_image.setImageResource(R.drawable.ic_void);
      }
    }

    private class CheckedChangeListener
      implements CompoundButton.OnCheckedChangeListener
    {
      private long _id;
      private TextView tv_count;
      private TextView tv_unit;
      private String name;

      public CheckedChangeListener(long _id, TextView tv_count, TextView tv_unit, String name)
      {
        this._id = _id;
        this.tv_count = tv_count;
        this.tv_unit = tv_unit;
        this.name = name;
      }
      @Override
      public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
      {
        if(isChecked)
        {
          Pair<Double, Unit> count_unit = null;
          try
          {
            //Количество и единицы измерения бнрем из запланированного или последнего документа
            Cursor last = oh.db.rawQuery(oh.getQuery(EQ.PLANNED_OR_LAST_DOCUMENT), new String[]{new Long(_id).toString()} );
            double count = 1.0;
            Unit unit = Unit.getUnitFromId(1);
            if(last.moveToFirst())
            {
              count = last.getDouble(last.getColumnIndex("count"));
              unit = Unit.getUnitFromId(last.getLong(last.getColumnIndex("id_unit")));
            }
            count_unit = new Pair<>(count, unit);
          } catch(Entity.EntityException e)
          {     }
          chooses.put(_id, count_unit);
          tv_count.setText(count_unit.first.toString());
          tv_unit.setText(count_unit.second.getName());
          CountUnitParams count_unit_params = new CountUnitParams(_id, name);
          tv_count.setOnClickListener(count_unit_params);
          tv_unit.setOnClickListener(count_unit_params);
        }
        else
        {
          chooses.remove(_id);
          tv_count.setText("");
          tv_unit.setText("");
          tv_count.setOnClickListener(null);
          tv_unit.setOnClickListener(null);
        }
      }
    }

    private class OnClickEntryTree
      implements View.OnClickListener
    {
      private long _id;
      public OnClickEntryTree(long _id)
      {
        this._id = _id;
      }
      @Override
      public void onClick(View v)
      {
        filter_provider_listener.buildScrollPath(_id);
      }
    }

    private class CountUnitParams
      implements View.OnClickListener
    {
      private long _id;
      private String name;
      public CountUnitParams(long _id, String name)
      {
        this._id = _id;
        this.name = name;
      }
      @Override
      public void onClick(View v)
      {
        DChooseCategoryParams choose_category_params = new DChooseCategoryParams();
        Pair<Double, Unit> count_unit = chooses.get(_id);
        Bundle params = new Bundle();
        params.putParcelable("count_unit", new Planned.Chooses(_id, count_unit.first, count_unit.second));
        params.putString("name", name);
        choose_category_params.setArguments(params);
        choose_category_params.show(getFragmentManager(), null);
      }
    }
  }
  @Override
  public void resultHandler(Bundle result_values)
  {
    Planned.Chooses result_count_unit = result_values.getParcelable("count_unit");
    chooses.remove(result_count_unit._id);
    Pair<Double, Unit> count_unit = new Pair<>(result_count_unit.count, result_count_unit.unit);
    chooses.put(result_count_unit._id, count_unit);
    list_adapter.notifyDataSetChanged();
  }

}
