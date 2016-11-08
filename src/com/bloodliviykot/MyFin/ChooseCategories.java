package com.bloodliviykot.MyFin;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.*;
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
  private Button clear;
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
    clear = (Button)findViewById(R.id.choose_categories_clear);
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

    for(Button bb : navigation_buttons)
      navigation_linear.addView(bb);

    root.setText("Первый");
    navigation_buttons.add(0, b);

    Button navigation_last = navigation_buttons.get(navigation_buttons.size() - 1);
    LinearLayout.LayoutParams button_params = (LinearLayout.LayoutParams)navigation_last.getLayoutParams();
    button_params.rightMargin = 0;
    navigation_last.setLayoutParams(button_params);

    int fdfdf=0;
  }



}
