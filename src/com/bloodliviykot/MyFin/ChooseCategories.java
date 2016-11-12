package com.bloodliviykot.MyFin;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import com.bloodliviykot.MyFin.DB.EQ;
import com.bloodliviykot.MyFin.DB.MySQLiteOpenHelper;
import com.bloodliviykot.MyFin.DB.entities.Category;
import com.bloodliviykot.MyFin.DB.entities.Transact;
import com.bloodliviykot.tools.DataBase.Entity;
import com.bloodliviykot.tools.widget.ButtonID;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kot on 03.11.2016.
 */
public class ChooseCategories
  extends Activity
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
  private SimpleCursorAdapter list_adapter;

  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.choose_categories_list);

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

    oh = MySQLiteOpenHelper.getMySQLiteOpenHelper();

    //Установить цвет текста SearchView
    int autoCompleteTextViewID = getResources().getIdentifier("android:id/search_src_text", null, null);
    AutoCompleteTextView search_tv = (AutoCompleteTextView)search.findViewById(autoCompleteTextViewID);
    search_tv.setTextColor(getResources().getColor(R.color.grey));

    //buildScrollPath(Transact.TREND.CREDIT, null);
    buildScrollPath(Transact.TREND.CREDIT, 10L);

    cursor = oh.db.rawQuery(oh.getQuery(EQ.ALL_CATEGORIES_LIKE), new String[]{"0", "%"});
    list_adapter = new ChooseItemAdapter(cursor);
    list.setAdapter(list_adapter);

    int fdfdf=0;
  }

  private void buildScrollPath(Transact.TREND trend, Long _id_parent)
  {
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
      for(boolean cursor_status = cursor_parents.moveToFirst();
          cursor_status;
          cursor_status = cursor_parents.moveToNext())
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
      }catch(Entity.EntityException e)
      {     }
    }
    for(Button bb : navigation_buttons)
      navigation_linear.addView(bb);
    navigation_buttons.add(0, root);
    Button navigation_last = navigation_buttons.get(navigation_buttons.size() - 1);
    LinearLayout.LayoutParams button_params = (LinearLayout.LayoutParams)navigation_last.getLayoutParams();
    button_params.rightMargin = 0;
    navigation_last.setLayoutParams(button_params);
    for(Button bb : navigation_buttons)
      bb.setOnClickListener(new View.OnClickListener(){
        @Override
        public void onClick(View v)
        {
          Long _id = ((ButtonID)v).getID();
            buildScrollPath(trend, _id);
        }
      });
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

      ImageView iv_image = (ImageView)view.findViewById(R.id.choose_categories_list_item_enter);
      TextView tv_name   = (TextView)view.findViewById(R.id.choose_categories_list_item_name);
      TextView tv_count  = (TextView)view.findViewById(R.id.choose_categories_list_item_count);
      TextView tv_unit   = (TextView)view.findViewById(R.id.choose_categories_list_item_unit);
      CheckBox ch_chose  = (CheckBox)view.findViewById(R.id.choose_categories_list_item_choose);

      tv_name.setText(name);
    }

  }

}
