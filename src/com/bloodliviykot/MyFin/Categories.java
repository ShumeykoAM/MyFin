package com.bloodliviykot.MyFin;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import com.bloodliviykot.MyFin.DB.EQ;
import com.bloodliviykot.MyFin.DB.MySQLiteOpenHelper;
import com.bloodliviykot.tools.widget.TreeListView;

/**
 * Created by Kot on 22.10.2016.
 */
public class Categories
  extends Activity
{
  TreeListView categories;

  private MySQLiteOpenHelper oh;
  private SimpleCursorAdapter list_adapter;

  private TreeListView.TreeCursor cursor;
  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.categories);

    oh = MySQLiteOpenHelper.getMySQLiteOpenHelper();

    cursor = new TreeListView.TreeCursor(oh.db, oh.getQuery(EQ.CATEGORIES_NO_PARENT), oh.getQuery(EQ.CATEGORIES), null);
    categories = (TreeListView)findViewById(R.id.categories_list_view);
    list_adapter = new CategoriesItemAdapter(R.layout.categories_item, cursor,
      new String[]{},
      new int[]{R.id.categories_item_name});
    list_adapter.changeCursor(cursor);
    categories.setAdapter(list_adapter);
    categories.setOnItemClickListener(new AdapterView.OnItemClickListener()
    {
      @Override
      public void onItemClick(AdapterView<?> parent, View view, int position, long id)
      {
        if(cursor.changeExpand(position))
          list_adapter.notifyDataSetChanged();
      }
    });
  }

  //Переопределим SimpleCursorAdapter что бы форматировать данные из базы нужным образом
  private class CategoriesItemAdapter
    extends SimpleCursorAdapter
  {

    public CategoriesItemAdapter(int layout, Cursor cursor, String[] from, int[] to)
    {
      super(Common.application_context, layout, cursor, from, to);

    }
    @Override
    public void bindView(View view, Context context, Cursor cursor)
    {
      //Сопоставляем
      TextView tv_name    = (TextView)view.findViewById(R.id.categories_item_name);
      LinearLayout ll = (LinearLayout)view;
      String nm = cursor.getString(cursor.getColumnIndex("name"));
      tv_name.setText(cursor.getString(cursor.getColumnIndex("name")));

    }

  }
}

