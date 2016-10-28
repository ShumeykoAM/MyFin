package com.bloodliviykot.MyFin;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.database.CharArrayBuffer;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.net.Uri;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.*;
import com.bloodliviykot.MyFin.DB.EQ;
import com.bloodliviykot.MyFin.DB.MySQLiteOpenHelper;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Kot on 22.10.2016.
 */
public class Categories
  extends Activity
{
  ListView categories;

  private MySQLiteOpenHelper oh;
  private SimpleCursorAdapter list_adapter;

  private class TreeCursor
    implements Cursor
  {

    class Node
    {
      public class NodeException
        extends Throwable
      {     }
      private Long _id;
      private Cursor cursor;
      private Map<Long, Node> sub_nodes = new HashMap<Long, Node>();
      public Node(Long _id, Cursor cursor)
      {
        this._id = _id;
        this.cursor = cursor;
        if(_id == null)
          for(Boolean status = cursor.moveToFirst(); status; status = cursor.moveToNext())
          {
            long id = cursor.getLong(cursor.getColumnIndex("_id"));
            sub_nodes.put(id, new Node(id, null));
          }
      }
      boolean change_expand() //Возвращает true если состояние изменилось
      {
        if(_id == null)
          return false;
        if(cursor == null)
        {
          cursor = oh.db.rawQuery(oh.getQuery(EQ.CATEGORIES), new String[]{_id.toString()});
          if(cursor.getCount() != 0)
          {
            for(Boolean status = cursor.moveToFirst(); status; status = cursor.moveToNext())
            {
              long _id = cursor.getLong(cursor.getColumnIndex("_id"));
              sub_nodes.put(_id, new Node(_id, null));
            }
            return true;
          }
          else
          {
            sub_nodes.clear();
            cursor = null;
            return false;
          }
        }
        else
        {
          cursor = null;
          return true;
        }
      }
      int getCountExpanded()
      {
        int count = 0;
        if(cursor != null)
        {
          count = cursor.getCount();
          for(Map.Entry<Long, Node> entry : sub_nodes.entrySet())
            count += entry.getValue().getCountExpanded();
        }
        return count;
      }
      Pair<Node, Cursor> getSubNode(int position) throws NodeException
      {
        Pair<Node, Cursor> result = null;
        int count_expanded = getCountExpanded();
        if(position > count_expanded-1)
          throw new NodeException();
        for(Boolean status = cursor.moveToFirst(); status; status = cursor.moveToNext())
        {
          result = new Pair<Node, Cursor>(sub_nodes.get(cursor.getLong(cursor.getColumnIndex("_id"))), cursor) ;
          if(position == 0)
            break;
          count_expanded = result.first.getCountExpanded();
          if(position <= count_expanded)
          {
            result = result.first.getSubNode(position-1);
            break;
          }
          position = position - 1 - count_expanded;
        }

        return result;
      }
    }

    Node head_node;
    boolean changeExpand(int position) //Возвращает true если состояние изменилось
    {
      boolean change_expand = false;
      Node subnode_node;
      try
      {
        if( (subnode_node = head_node.getSubNode(position).first) != null)
          change_expand = subnode_node.change_expand();
      } catch(Node.NodeException e)
      {
        e.printStackTrace();
      }
      return change_expand;
    }

    public TreeCursor(Cursor cursor)
    {
      if(cursor == null)
        throw new NullPointerException();
      head_node = new Node(null, cursor);
    }


    int position;
    Pair<Node, Cursor> current;
    @Override
    public int getCount()
    {
      return head_node.getCountExpanded();
    }

    @Override
    public int getPosition()
    {
      return head_node.cursor.getPosition();
    }

    @Override
    public boolean move(int offset)
    {
      return head_node.cursor.move(offset);
    }

    @Override
    public boolean moveToPosition(int position)
    {
      try
      {
        current = head_node.getSubNode(this.position = position);
        return true;
      } catch(Node.NodeException e)
      {
        return false;
      }
    }

    @Override
    public boolean moveToFirst()
    {
      return head_node.cursor.moveToFirst();
    }

    @Override
    public boolean moveToLast()
    {
      return head_node.cursor.moveToLast();
    }

    @Override
    public boolean moveToNext()
    {
      return head_node.cursor.moveToNext();
    }

    @Override
    public boolean moveToPrevious()
    {
      return head_node.cursor.moveToPrevious();
    }

    @Override
    public boolean isFirst()
    {
      return head_node.cursor.isFirst();
    }

    @Override
    public boolean isLast()
    {
      return head_node.cursor.isLast();
    }

    @Override
    public boolean isBeforeFirst()
    {
      return head_node.cursor.isBeforeFirst();
    }

    @Override
    public boolean isAfterLast()
    {
      return head_node.cursor.isAfterLast();
    }

    @Override
    public int getColumnIndex(String columnName)
    {
      return current.second.getColumnIndex(columnName);
    }

    @Override
    public int getColumnIndexOrThrow(String columnName) throws IllegalArgumentException
    {
      return head_node.cursor.getColumnIndexOrThrow(columnName);
    }

    @Override
    public String getColumnName(int columnIndex)
    {
      return head_node.cursor.getColumnName(columnIndex);
    }

    @Override
    public String[] getColumnNames()
    {
      return head_node.cursor.getColumnNames();
    }

    @Override
    public int getColumnCount()
    {
      return head_node.cursor.getColumnCount();
    }

    @Override
    public byte[] getBlob(int columnIndex)
    {
      return current.second.getBlob(columnIndex);
    }

    @Override
    public String getString(int columnIndex)
    {
      return current.second.getString(columnIndex);
    }

    @Override
    public void copyStringToBuffer(int columnIndex, CharArrayBuffer buffer)
    {
      current.second.copyStringToBuffer(columnIndex, buffer);
    }

    @Override
    public short getShort(int columnIndex)
    {
      return current.second.getShort(columnIndex);
    }

    @Override
    public int getInt(int columnIndex)
    {
      return current.second.getInt(columnIndex);
    }

    @Override
    public long getLong(int columnIndex)
    {
      return current.second.getLong(columnIndex);
    }

    @Override
    public float getFloat(int columnIndex)
    {
      return current.second.getFloat(columnIndex);
    }

    @Override
    public double getDouble(int columnIndex)
    {
      return current.second.getDouble(columnIndex);
    }

    @Override
    public int getType(int columnIndex)
    {
      return current.second.getType(columnIndex);
    }

    @Override
    public boolean isNull(int columnIndex)
    {
      return current.second.isNull(columnIndex);
    }

    @Override
    public void deactivate()
    {
      head_node.cursor.deactivate();
    }

    @Override
    public boolean requery()
    {
      return head_node.cursor.requery();
    }

    @Override
    public void close()
    {
      head_node.cursor.close();
    }

    @Override
    public boolean isClosed()
    {
      return head_node.cursor.isClosed();
    }

    @Override
    public void registerContentObserver(ContentObserver observer)
    {
      head_node.cursor.registerContentObserver(observer);
    }

    @Override
    public void unregisterContentObserver(ContentObserver observer)
    {
      head_node.cursor.unregisterContentObserver(observer);
    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer)
    {
      head_node.cursor.registerDataSetObserver(observer);
    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer)
    {
      head_node.cursor.unregisterDataSetObserver(observer);
    }

    @Override
    public void setNotificationUri(ContentResolver cr, Uri uri)
    {
      head_node.cursor.setNotificationUri(cr, uri);
    }

    @Override
    public boolean getWantsAllOnMoveCalls()
    {
      return head_node.cursor.getWantsAllOnMoveCalls();
    }

    @Override
    public Bundle getExtras()
    {
      return head_node.cursor.getExtras();
    }

    @Override
    public Bundle respond(Bundle extras)
    {
      return head_node.cursor.respond(extras);
    }
  }

  private TreeCursor cursor;
  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.categories);

    oh = MySQLiteOpenHelper.getMySQLiteOpenHelper();
    cursor = new TreeCursor(oh.db.rawQuery(oh.getQuery(EQ.CATEGORIES_NO_PARENT), null));
    categories = (ListView)findViewById(R.id.categories_list_view);
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

