package com.bloodliviykot.MyFin;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import com.bloodliviykot.MyFin.DB.EQ;
import com.bloodliviykot.MyFin.DB.MySQLiteOpenHelper;
import com.bloodliviykot.tools.widget.DialogFragmentEx;
import com.bloodliviykot.tools.widget.TreeListView;
import com.kot.myfin.R;

/**
 * Created by Kot on 01.01.2017.
 */
public class DChooseCategory
  extends DialogFragmentEx<DialogFragmentEx.I_ResultHandler<Long>, Long>
{
  TreeListView categories;
  
  private MySQLiteOpenHelper oh;
  private SimpleCursorAdapter list_adapter;
  private TreeListView.TreeCursor cursor;
  
  public DChooseCategory()
  {
    super(R.layout.categories);
  }
  
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
  {
    getDialog().setTitle("Родительская категория");
    //getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
    final View v = inflater.inflate(getR_layout(), null);
  
    oh = MySQLiteOpenHelper.getMySQLiteOpenHelper();
  
    cursor = new TreeListView.TreeCursor(oh.db, oh.getQuery(EQ.ROOT_CATEGORIES), null,
      oh.getQuery(EQ.SUB_CATEGORIES), oh.getQuery(EQ.EXIST_SUB_CATEGORY));
    categories = (TreeListView)v.findViewById(R.id.categories_list_view);
    list_adapter = new CategoriesItemAdapter(R.layout.categories_item, cursor,
      new String[]{},
      new int[]{R.id.categories_item_name});
    list_adapter.changeCursor(cursor);
    categories.setAdapter(list_adapter);
  
    if(savedInstanceState != null)
    {
  
    }
    return v;
  }
  @Override
  public void onSaveInstanceState(Bundle outState)
  {
    //outState.putParcelable(DATE_TIME, date_time);
    
    super.onSaveInstanceState(outState);
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
    public void bindView(View view, Context context, final TreeListView.TreeCursor cursor)
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
      tv_name.setOnClickListener(new ItemClickListener(cursor.getLong(cursor.getColumnIndex("_id"))));
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
    private class ItemClickListener
      implements View.OnClickListener
    {
      private long _id;
      public ItemClickListener(long _id)
      {
        this._id = _id;
      }
      @Override
      public void onClick(View v)
      {
        DChooseCategory.this.dismiss();
        handleResult(_id);
      }
    }
  }
}