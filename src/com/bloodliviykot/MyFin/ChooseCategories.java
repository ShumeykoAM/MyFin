package com.bloodliviykot.MyFin;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import com.bloodliviykot.MyFin.DB.EQ;
import com.bloodliviykot.MyFin.DB.MySQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kot on 03.11.2016.
 */
public class ChooseCategories
  extends Activity
{

  private LinearLayout navigation_linear;
  private Button root;
  private Button add_new;
  private SearchView search;
  private ListView list;
  private Button tree;
  private Button ok;
  private List<Button> navigation_buttons = new ArrayList<Button>();

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
    root = (Button)navigation_linear.findViewById(R.id.arrow_button);
    add_new = (Button)findViewById(R.id.choose_categories_add_new);
    search = (SearchView)findViewById(R.id.choose_categories_search);
    list = (ListView)findViewById(R.id.choose_categories_list);
    tree = (Button)findViewById(R.id.choose_categories_tree);
    ok = (Button)findViewById(R.id.choose_categories_ok);

    oh = MySQLiteOpenHelper.getMySQLiteOpenHelper();


    navigation_buttons.add((Button)(getLayoutInflater().inflate(R.layout.arrow_button, navigation_linear, false)));
    navigation_buttons.add((Button)(getLayoutInflater().inflate(R.layout.arrow_button, navigation_linear, false)));
    navigation_buttons.add((Button)(getLayoutInflater().inflate(R.layout.arrow_button, navigation_linear, false)));
    navigation_buttons.add((Button)(getLayoutInflater().inflate(R.layout.arrow_button, navigation_linear, false)));
    navigation_buttons.add((Button)(getLayoutInflater().inflate(R.layout.arrow_button, navigation_linear, false)));
    navigation_buttons.add((Button)(getLayoutInflater().inflate(R.layout.arrow_button, navigation_linear, false)));
    Button b = (Button)(getLayoutInflater().inflate(R.layout.arrow_button, navigation_linear, false));
    navigation_buttons.add(navigation_buttons.size(),b);
    b.setText("Последний");
    b.setOnClickListener(new View.OnClickListener(){
      @Override
      public void onClick(View v)
      {
        int ff = 0;
        ff++;
      }
    });

    for(Button bb : navigation_buttons)
      navigation_linear.addView(bb);

    root.setText("Первый");
    navigation_buttons.add(0, b);

    Button navigation_last = navigation_buttons.get(navigation_buttons.size() - 1);
    LinearLayout.LayoutParams button_params = (LinearLayout.LayoutParams)navigation_last.getLayoutParams();
    button_params.rightMargin = 0;
    navigation_last.setLayoutParams(button_params);

    cursor = oh.db.rawQuery(oh.getQuery(EQ.ALL_CATEGORIES_LIKE), new String[]{"0", "%"});
    list_adapter = new ChooseItemAdapter(cursor);
    list.setAdapter(list_adapter);

    int fdfdf=0;
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
