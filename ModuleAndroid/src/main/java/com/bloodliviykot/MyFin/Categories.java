package com.bloodliviykot.MyFin;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
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

    cursor = new TreeListView.TreeCursor(oh.db, oh.getQuery(EQ.ROOT_CATEGORIES), null,
      oh.getQuery(EQ.SUB_CATEGORIES), oh.getQuery(EQ.EXIST_SUB_CATEGORY));
    categories = (TreeListView)findViewById(R.id.categories_list_view);
    list_adapter = new CategoriesItemAdapter(R.layout.categories_item, cursor,
      new String[]{},
      new int[]{R.id.categories_item_name});
    list_adapter.changeCursor(cursor);
    categories.setAdapter(list_adapter);
  }

  //Переопределим SimpleCursorAdapter что бы форматировать данные из базы нужным образом
  private class CategoriesItemAdapter
    extends TreeListView.TreeListAdapter
  {

    public CategoriesItemAdapter(int layout, TreeListView.TreeCursor cursor, String[] from, int[] to)
    {
      super(Common.context, layout, cursor, from, to);
    }

    private abstract class OnClickListenerExpand
      implements View.OnClickListener
    {
      int position;
      public OnClickListenerExpand(int position)
      {
        this.position = position;
      }
    }
    @Override
    public void bindView(View view, Context context, TreeListView.TreeCursor cursor)
    {
      //Сопоставляем
      TextView tv_name    = (TextView)view.findViewById(R.id.categories_item_name);
      ImageView iv_image  = (ImageView)view.findViewById(R.id.categories_item_image_expand);
      iv_image.setOnClickListener(new OnClickListenerExpand(cursor.getPosition())
      {
        @Override
        public void onClick(View v)
        {
          if(cursor.changeExpand(position))
            list_adapter.notifyDataSetChanged();
        }
      });
      LinearLayout ll = (LinearLayout)view;
      int padding = cursor.getDeep() * 42;
      iv_image.setPadding(padding, 0, 0, 0);
      if(cursor.isExpandable())
      {
        iv_image.setVisibility(view.VISIBLE);
        if(cursor.isExpanded())
          iv_image.setImageResource(R.drawable.ic_expanded_on);
        else
          iv_image.setImageResource(R.drawable.ic_expanded_off);
      }
      else
        iv_image.setVisibility(view.INVISIBLE);
      tv_name.setText(cursor.getString(cursor.getColumnIndex("name")));
    }

  }
}

